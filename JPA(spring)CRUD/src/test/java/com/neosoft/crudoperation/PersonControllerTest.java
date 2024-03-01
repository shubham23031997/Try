/*package com.neosoft.crudoperation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.neosoft.crudoperation.controller.PersonController;
import com.neosoft.crudoperation.entity.Person;
import com.neosoft.crudoperation.repository.PersonRepo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class PersonControllerTest {
    private MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();//to convert object into String
    //ObjectMapper provides functionality for reading and writing JSON, either to
    // and from basic POJOs,or to and from a general-purpose JSON Tree Model ( JsonNode )
    ObjectWriter objectWriter = objectMapper.writer();
    @Mock
    private PersonRepo personRepo;
    @InjectMocks
    private PersonController personController;
    Person p1 = new Person(1, "shubham", 26, "male");
    Person p2 = new Person(2, "ganesh", 28, "male");
    Person p3 = new Person(3, "kaustubh", 28, "male");

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(personController).build();
    }

    @Test
    public void getAllPerson_Success() throws Exception {
//        Person record_1;
        List<Person> personList = new ArrayList<>(Arrays.asList(p1, p2, p3));
        Mockito.when(personRepo.findAll()).thenReturn(personList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/person/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)));
//                .andExpect(jsonPath("$[2].name", is("kaustubh")));
    }

    @Test
    public void getPersonById_success() throws Exception {
        Mockito.when(personRepo.findById(p1.getId())).thenReturn(java.util.Optional.of(p1));

        ResultMatcher expected = MockMvcResultMatchers.jsonPath("name")
                .value("shubham");
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/person/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()))
                .andExpect(expected);
        //.andExpect(jsonPath("$.name", is("kaustubh")));
    }

    @Test
    public void createRecord_Success() throws Exception {
        Person person = Person.builder()
                .id(5)
                .name("shubhamm")
                .age(24)
                .gender("male")
                .build();
        Mockito.when(personRepo.save(person)).thenReturn(person);
        String content = objectWriter.writeValueAsString(person);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/person/add")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockRequest)
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("shubhamm")));
    }

    @Test
    public void updatePerson_success() throws Exception {
        Person updateRecord = Person.builder()
                .id(1)
                .name("mali")
                .age(26)
                .gender("male")
                .build();
        Mockito.when(personRepo.findById(p1.getId())).thenReturn(java.util.Optional.ofNullable(p1));
        Mockito.when(personRepo.save(updateRecord)).thenReturn(updateRecord);
        String updatedContent = objectWriter.writeValueAsString(updateRecord);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/person/updateById/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(updatedContent);
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", notNullValue()))
                .andExpect(jsonPath("$.name", is("mali")));
    }

    @Test
    public void deleteById_success() throws Exception {

        Mockito.when(personRepo.findById(p1.getId())).thenReturn(Optional.of(p1));

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/person/deleteById/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
*/
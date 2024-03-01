package com.neosoft.crudoperation.controller;

import com.neosoft.crudoperation.entity.Person;
import com.neosoft.crudoperation.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/person")
public class PersonController {

    @Autowired
    PersonService personService;

    @PostMapping("/add")
    public Person addPerson(@RequestBody Person person) {
        return personService.createPerson(person);
    }

    @GetMapping("/{id}")
    public Person getPersonById(@PathVariable long id) {
        return personService.findPersonById(id).orElse(null);
    }

    @GetMapping("/findName/{id}")
    public String getPersonNameById(@PathVariable long id) {
        Person person = personService.findPersonById(id).orElse(null);
        return person.getName();
    }

    @GetMapping("/all")
    public List<Person> getAllPerson() {
        return personService.getAllPerson();
    }

    @PutMapping("/updateBy/{id}")
    public ResponseEntity<Person> updateUser(@PathVariable long id, @RequestBody Person person) {
        Person updatedPerson = personService.updatePerson(id, person);
        if (updatedPerson != null) {
            return new ResponseEntity<>(updatedPerson, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteById/{id}")
    public void deletePerson(@PathVariable long id) {
        Person person = personService.findPersonById(id).orElseThrow();
        personService.deletePerson(id);
    }
}
package com.neosoft.crudoperation.service.serviceimpl;

import com.neosoft.crudoperation.entity.Person;
import com.neosoft.crudoperation.repository.PersonRepo;
import com.neosoft.crudoperation.service.PersonService;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {

    private PersonRepo personRepo;

    @Override
    public List<Person> getAllPerson() {
        return personRepo.findAll();
    }

    @Override
    public Optional<Person> findPersonById(Long id) {
        Optional<Person> person = personRepo.findById(id);
        return person;
    }

    @Override
    public Person createPerson(Person product) {
        return personRepo.save(product);
    }

    @Override
    public Person updatePerson(Long id, Person person) {
        Person updateProduct = personRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("user not exist with id" + id));
        updateProduct.setId(person.getId());
        updateProduct.setName(person.getName());
        updateProduct.setAge(person.getAge());
        updateProduct.setGender(person.getGender());
        personRepo.save(updateProduct);
        return person;
    }

    @Override
    public void deletePerson(Long id) {
        personRepo.deleteById(id);
    }
}
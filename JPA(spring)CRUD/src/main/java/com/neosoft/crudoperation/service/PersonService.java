package com.neosoft.crudoperation.service;

import com.neosoft.crudoperation.entity.Person;

import java.util.List;
import java.util.Optional;

public interface PersonService {
    List<Person> getAllPerson();

    Optional<Person> findPersonById(Long id);

    Person createPerson(Person person);

    Person updatePerson(Long id, Person person);

    void deletePerson(Long id);
}


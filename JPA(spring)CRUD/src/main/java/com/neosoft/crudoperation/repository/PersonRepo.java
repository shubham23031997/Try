package com.neosoft.crudoperation.repository;

import com.neosoft.crudoperation.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository//no need to annotated with this annotation when we are extending jpaRepository
public interface PersonRepo extends JpaRepository<Person, Long> {
//it contains API for basic CRUD operations
}

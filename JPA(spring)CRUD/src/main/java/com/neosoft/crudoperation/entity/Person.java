package com.neosoft.crudoperation.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
//@ToString, @EqualsAndHashCode, @Getter, @Setter on all non-final fields, and @RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private int age;
    private String gender;

    public static Object builder() {
        // TODO Auto-generated method stub
        return null;
    }


//  @Transient
//  private int mobileNumber;
//  the @Transient annotation does not affect Java object serialization.
}

package com.contact.service;

import com.contact.entity.Contact;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactServiceImpl implements ContactService {

    List<Contact> list = List.of(
            new Contact(1L, "aadssfedbcd@gmail.com", "abccsaddasa", 1L),
            new Contact(2L, "pqrasdadwsv@gmail.com", "pqrasdsdads", 1L),
            new Contact(3L, "stasdassduv@gmail.com", "stuasdqqada", 2L),
            new Contact(4L, "jmtfhbfsddz@gmail.com", "acbasdzxcsd", 2L),
            new Contact(5L, "wxyasdasddz@gmail.com", "klmasdaddcd", 2L),
            new Contact(6L, "mnopsdasddz@gmail.com", "qwdasdaposd", 3L)
    );

    @Override
    public List<Contact> getContactsOfUser(Long userId) {
        return list.stream().filter(contact -> contact.getUserId().equals(userId)).collect(Collectors.toList());
    }
}
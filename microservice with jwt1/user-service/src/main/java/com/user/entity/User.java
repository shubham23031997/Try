package com.user.entity;

import java.util.ArrayList;
import java.util.List;

public class User {

    List<Contact> contacts = new ArrayList<>();
    private Long userId;
    private String name;
    private String phone;
    private String password;

    public User(Long userId, String name, String phone, String password, List<Contact> contacts) {
        super();
        this.userId = userId;
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.contacts = contacts;
    }

    public User(Long userId, String name, String phone, String password) {
        this.userId = userId;
        this.name = name;
        this.phone = phone;
        this.password = password;
    }

    public User() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
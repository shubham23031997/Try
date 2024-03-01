package com.user.service;

import com.user.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    List<User> list = List.of(
            new User(123L, "admin", "5324423", "admin"),
            new User(1234L, "teacher", "6342423", "teacher"),
            new User(12345L, "student", "7324323", "student")
    );

    @Override
    public User getUser(Long id) {
        return list.stream().filter(user -> user.getUserId().equals(id)).findAny().orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        // to get data from repository
        // User user=userRepo.findByName(name).orElse(null);
        // return User(user.getName(), user.getPassword(), new ArrayList<>());
        // logic to get the user from the in memory database
        User user = list.stream().filter(u -> u.getName().equals(name)).findAny().orElse(null);
        return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), new ArrayList<>());
    }
}
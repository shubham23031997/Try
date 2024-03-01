package com.user.controller;

import com.user.entity.Contact;
import com.user.entity.User;
import com.user.fiegnservice.ContactService;
import com.user.service.UserService;
import com.user.service.UserServiceImpl;
import com.user.utility.JwtRequest;
import com.user.utility.JwtResponse;
import com.user.utility.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ContactService contactService;

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private AuthenticationManager authenticationManager;

    private static final String linkofContact="https://contact-service/contact/user/" ;

    @SuppressWarnings("unchecked")
    @GetMapping("/{userId}")
    public User getUser(@PathVariable("userId") Long userId) {

        User user = this.userService.getUser(userId);
        List contacts = this.restTemplate.getForObject(linkofContact + user.getUserId(), List.class);
//      List <Contact> contacts= contactService.getContact(userId);
        user.setContacts(contacts);
        return user;
    }

    @PostMapping("/authenticate")
    public JwtResponse authenticationMethod(@RequestBody JwtRequest jwtRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(jwtRequest.getName(), jwtRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CRIDENTIALS", e);
        }
        final UserDetails userDetails = userServiceImpl.loadUserByUsername(jwtRequest.getName());
        final String token = jwtUtility.generateToken(userDetails);
        return new JwtResponse(token);
    }
}
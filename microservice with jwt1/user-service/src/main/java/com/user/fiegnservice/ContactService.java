//package com.user.fiegnservice;
//
//import com.user.entity.Contact;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//
//import java.util.List;
//
//@FeignClient(name= "CONTACT-SERVICE")//contact-service
//public interface ContactService {
//    @GetMapping("/contact/user")
//    public List<Contact> getContact(@PathVariable("id") Long id);
//}

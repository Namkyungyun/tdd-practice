package com.example.demo.restTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;


@Service
public class UserService {

    @Autowired
    private RestTemplate restTemplate;

    public User getUsers(Long id) {
        ResponseEntity resp =
                restTemplate.getForEntity("http://localhost:8080/users/" + id, User.class);

        return resp.getStatusCode() == HttpStatus.OK ? (User) resp.getBody() : null;
    }


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}

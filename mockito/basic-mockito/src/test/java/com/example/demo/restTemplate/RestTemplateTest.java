package com.example.demo.restTemplate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
public class RestTemplateTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UserService userService = new UserService();

    @Test
    public void givenMockingIsDoneByMockito_whenGetIsCalled_shouldReturnMockedObject() {
        //객체 정의
        User user = new User(1l, "test");
        String url = "http://localhost:8080/users/";

        //stubbing
        Mockito.when(restTemplate.getForEntity(url +"1", User.class))
               .thenReturn(new ResponseEntity<>(user, HttpStatus.OK));

        //호출
        ResponseEntity<User> result = restTemplate.getForEntity(url +"1", User.class);
        User user1 = userService.getUsers(1l);
        
        //Junit Verify request succeed
        Assertions.assertEquals(200, result.getStatusCodeValue());
        Assertions.assertEquals(user, user1);

    }





}

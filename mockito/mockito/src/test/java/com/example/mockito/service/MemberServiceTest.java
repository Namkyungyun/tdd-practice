package com.example.mockito.service;

import com.example.mockito.jpa.MemberEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(MemberServiceTest.class);

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private MemberServiceImpl service;

    @Test
    public void givenMockingIsDoneByMockito_whenGetIsCalled_shouldReturnMockedObject() throws Exception {
        //given
        MemberEntity member1 = new MemberEntity(1L, "email", "test");
        String url = "http://localhost:8080/members/email";

        Mockito.when(restTemplate.getForEntity(url, MemberEntity.class))
                .thenReturn(new ResponseEntity<MemberEntity>(member1, HttpStatus.OK));

        //when
        MemberEntity member = service.getEmail("email");
        ResponseEntity<MemberEntity> returnStatus
                = restTemplate.getForEntity("http://localhost:8080/members/email" , MemberEntity.class);

        //then
        Assertions.assertEquals(member1, member);
        Assertions.assertEquals(HttpStatus.OK, returnStatus.getStatusCode());

    }

}

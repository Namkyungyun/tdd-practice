package com.example.mockito.controller;

import com.example.mockito.jpa.MemberDto;
import com.example.mockito.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.h2.value.ValueJavaObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MemberControllerTest {

    @Mock
    private MemberService service;

    @InjectMocks
    private MemberController controller;

    /** api의 경우 함수 실행을 위해 method가 아닌 api가 호출되므로, api 요청을 받아
     * 전달하기 위한 별도의 객체가 MockMvc이다.
     * Spring Test에서 이 객체를 지원하고, @BeforeEach에서 초기화하면 된다.**/
    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    /** 추가로 MemberDto는 다른 코드에서도 사용되므로 private 함수로 공통화 **/
    private MemberDto requestMember() {
        final MemberDto requestMember = new MemberDto();
        requestMember.setEmail("test@test.test");
        requestMember.setName("testMember");
        return requestMember;
    }



    @DisplayName("회원 가입 성공")
    @Test
    void signUpSuccess() throws Exception {

        /** 회원가입 요청을 보내기 위해서는 MemberDto 객체 1개와
         * memberService의 isExist와 signUp에 대한 stub이 필요 **/
        // given
        final MemberDto request = requestMember();
        doReturn(false).when(service).isEmailExist(request.getEmail());
        doReturn(new MemberDto(1l,"test@email.com", "user1"))
                .when(service).saveMember(any(MemberDto.class));
                                        /** signUp 함수에 대한 매개변수로 우리가 만든 signUpDTO가 아닌
                                         * 어떠한 변수도 처리함을 뜻하는 any()가 사용됨에 주의
                                         * Spring에서 HTTP Body로 전달된 데이터는 MessageConverter에 의해 새로운 객체로 변환된다.
                                         * 그런데 이것은 요청이 오면 Spring에서 변환을 하는 것이므로,
                                         * 우리가 API로 전달되는 파라미터 SignUpDTO를 조작할 수 없다.
                                         * 그렇기 때문에 SignUpDTO 클래스의 어떠한 객체도 처리할 수 있도록 any()가 사용 **/

        /** mockMVC에 데이터와 함께 POST 요청을 보내야 한다.
         * 보내는 데이터는 객체가 아닌 Json이여야 하므로 별도의 변환이 필요한데,
         * 이 예제에서는 Gson을 활용 **/
        //when
        final ResultActions resultActions = mockMvc.perform(
                /** mockMvc의 perform에 요청에 대한 정보를 작성하여 넘겨주어야 한다.
                 * 요청 정보를 작성하기 위해서는 MockMvcRequestBuilders를 사용해야 하며
                 * 요청 메소드 종류, 내용, 파라미터 등을 설정할 수 있다. **/
                MockMvcRequestBuilders.post("/members/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
        );

        // then
        final MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();
        final String result = mvcResult.getResponse().getContentAsString();
        final String result2 =  mvcResult.getResponse().getContentType();

        assertThat(result).isNotNull();
        Assertions.assertNotNull(result);
        Assertions.assertEquals("application/json", result2);
    }



}

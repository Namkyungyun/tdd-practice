package com.example.mockito.service;

import com.example.mockito.jpa.MemberDto;
import com.example.mockito.jpa.MemberEntity;

import java.util.List;

public interface MemberService {

    List<MemberDto> getMembers();

    MemberDto getMemberByEmail(String name);

    MemberDto saveMember(MemberDto memberDto);

    String deleteMember(String email);

    Boolean isEmailExist(String email);

    MemberEntity getEmail(String email);
}

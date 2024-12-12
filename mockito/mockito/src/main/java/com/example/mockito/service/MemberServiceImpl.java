package com.example.mockito.service;

import com.example.mockito.jpa.MemberDto;
import com.example.mockito.jpa.MemberEntity;
import com.example.mockito.jpa.MemberRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class MemberServiceImpl implements MemberService{

    private MemberRepository repository;
    static final String EMP_URL_PREFIX = "http://localhost:8080/users/user";
    static final String URL_SEP = "/";

    private RestTemplate restTemplate;

    @Autowired
    public MemberServiceImpl(MemberRepository repository,
                             RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<MemberDto> getMembers() {
        List<MemberEntity> list = repository.findAll();
        List<MemberDto> result = new ArrayList<>();
        list.forEach(v -> result.add(new ModelMapper().map(v,MemberDto.class)));
        return result;
    }

    /** restTemplate 적용하기  **/
    @Override
    public MemberDto getMemberByEmail(String email) {
        MemberEntity member1 = repository.findByEmail(email);
        if(member1 == null) {
            throw new UsernameNotFoundException("User not found");

        }
        MemberDto result = new ModelMapper().map(member1, MemberDto.class);

        return result;
    }

    @Override
    public MemberDto saveMember(MemberDto memberDto) {
        MemberEntity member = new MemberEntity();
        member.setEmail(memberDto.getEmail());
        member.setName(memberDto.getName());
        MemberEntity member1 = repository.save(member);
        MemberDto result = new ModelMapper().map(member1, MemberDto.class);
        return  result;
    }

    @Override
    public String deleteMember(String email) {
        MemberEntity member = repository.findByEmail(email);
        repository.deleteById(member.getId());
        return "삭제 완료";
    }

    @Override
    public Boolean isEmailExist(String email) {
        MemberEntity member = repository.findByEmail(email);
        Boolean exist = false;
        if (member == null) {
            exist = false;
        }else {
            exist = true;
        }
        return exist;
    }
    @Override
    public MemberEntity getEmail(String email) {

        ResponseEntity<MemberEntity> resp = restTemplate.getForEntity("http://localhost:8080/members/" + email,
                MemberEntity.class);
        return resp.getStatusCode() == HttpStatus.OK ? resp.getBody() : null;
    }


}

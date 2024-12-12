package com.example.mockito.controller;

import com.example.mockito.jpa.MemberDto;
import com.example.mockito.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/members")
public class MemberController {

    private MemberService service;

    @Autowired
    public MemberController(MemberService service) {
        this.service = service;
    }

    @GetMapping("")
    public ResponseEntity<List<MemberDto>> getMembers() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getMembers());
    }

    @GetMapping("/member/{email}")
    public ResponseEntity<MemberDto> getMember(@PathVariable("email") String email) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getMemberByEmail(email));
    }

    @PostMapping("/member")
    public ResponseEntity saveMember(@RequestBody MemberDto dto) {
        Boolean memberDto = service.isEmailExist(dto.getEmail());
        String exist = "";
        if(memberDto) {
            exist = "회원 존재";
        }
        return memberDto
                ? new ResponseEntity(exist, HttpStatus.NOT_ACCEPTABLE)
                : ResponseEntity.ok(service.saveMember(dto));
    }

    @PostMapping("/member/{email}")
    public ResponseEntity<String> deleteMember(@PathVariable("email") String email) {
        Boolean member = service.isEmailExist(email);

        System.out.println(member);
        return member
                ? ResponseEntity.ok(service.deleteMember(email))
                : ResponseEntity.badRequest().body("삭제할 수 없습니다.");
    }

}

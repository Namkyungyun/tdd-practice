package com.example.mockito;

import com.example.mockito.jpa.MemberDto;
import com.example.mockito.jpa.MemberEntity;
import com.example.mockito.jpa.MemberRepository;
import com.example.mockito.service.MemberServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Profile;

import java.util.*;
import static org.mockito.Mockito.*;


@Profile("test")
@ExtendWith(MockitoExtension.class)
class MockitoApplicationTests {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberServiceImpl memberService;

    @Test
    @DisplayName("findAll")
    void testFindAll() {
        //given
        MemberEntity member1
                = new MemberEntity(1l, "test@email.com", "mock유저1");
        MemberEntity member2
                = new MemberEntity(2l, "test2@email.com", "mock유저2");

        List<MemberEntity> members = new ArrayList<>();
        members.add(member1);
        members.add(member2);

        //when
        when(memberRepository.findAll()).thenReturn(members);

        List<MemberEntity> findMembers = memberRepository.findAll();
        System.out.println("findMembers = " + findMembers);


        //then
        Assertions.assertEquals(2, findMembers.size());
        Assertions.assertEquals(member1.getName(), findMembers.get(0).getName());

        verify(memberRepository).findAll();
    }

    @Test
    @DisplayName("Exception")
    void testFindIdException() {
        when(memberRepository.findById(3l)).thenThrow(NullPointerException.class);

        try {
            memberRepository.findById(3l);
        }catch (NullPointerException ex) {
            System.out.println("에러");
        }
    }

    @Test
    @DisplayName("findById")
    void testFindById() {
        //given
        MemberEntity member1 = new MemberEntity(1l, "test@email.com", "mock유저1");

        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member1));

        //when
        Optional<MemberEntity> result = memberRepository.findById(1l);

        //then
        Assertions.assertEquals(1l, result.get().getId());
        verify(memberRepository).findById(anyLong());

    }

    @Test
    @DisplayName("save")
    void testSave() {
        //given
        MemberDto member1 = new MemberDto();
        member1.setName("test@email.com");
        member1.setName("mock유저1");

        ModelMapper mapper = new ModelMapper();
        MemberEntity member = mapper.map(member1, MemberEntity.class);

        when(memberRepository.save(any(MemberEntity.class))).thenReturn(member);

        //when
        MemberDto result = memberService.saveMember(member1);
        System.out.println(result);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals("mock유저1",result.getName());


        //verify
        verify(memberRepository).save(any(MemberEntity.class));
    }

    @Test
    void deleteById() {
        //when
        memberRepository.deleteById(1l);

        //then
        verify(memberRepository).deleteById(anyLong());
    }

    @Test
    void delete() {
        //given
        MemberEntity member1 = new MemberEntity(1l, "test@email.com", "mock유저1");

        //when
        memberRepository.delete(member1);

        //then
        verify(memberRepository).delete(any(MemberEntity.class));
    }



}

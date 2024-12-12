package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.service.impl.MemberServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MemberServiceTest {


    @Autowired
    MemberRepository memberRepository;


    private Member member() {
        final Member member = new Member();
        member.setName("testUser1");
        member.setAddress(member.getAddress());
        member.setOrders(member.getOrders());
        return member;
    }

    @Test
    @DisplayName("before_join")
    @Order(1)
    void create_member() {
        //given
        Member member = member();

        //when
        validateDuplicateMember(member);
        memberRepository.save(member);

        //then
        assertEquals("testUser1",member.getName());


    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()) {
            synchronized (Member.class) {
                if(!findMembers.isEmpty()) {
                    throw new IllegalStateException("이미 존재하는 회원입니다");
                }
            }
        }
    }
    @Test()
    @DisplayName("before_findByAll")
    @Order(2)
    void find_members() {
        //given
        Member member1 = member();
        Member member2 = new Member();
        member2.setName("testUser2");

        List<Member> members = new ArrayList<>();
        members.add(member1);
        members.add(member2);

        //when
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findAll();

        //then
        assertEquals(2, result.size());
        assertEquals("testUser2", result.get(1).getName());
    }

    @Test
    @DisplayName("before_findById")
    @Order(3)
    void find_member() {
        //given
        Member member = member();

        //when
        memberRepository.save(member);
        Member member1 = memberRepository.findOne(member.getId());

        //then
        assertEquals("testUser1", member1.getName());
    }

    @Test
    @DisplayName("after_join")
    @Order(4)
    void save_member() throws Exception {

        //given
        MemberServiceImpl memberService = new MemberServiceImpl(memberRepository);
        Member member = member();
        assertNotNull(memberService);

        //when
        Long result = memberService.join(member);
        Member member1 = memberService.findMember(result);

        //then
        assertEquals("testUser1", member1.getName());

    }

    @Test
    @DisplayName("after_증복_회원_예외")
    @Order(5)
    void save_member_exception() throws Exception {
        //given
        MemberServiceImpl memberService = new MemberServiceImpl(memberRepository);
        Member member1 = member();
        Member member2 = new Member();
        member2.setName("testUser1");

        //when
        memberService.join(member1);
        try {
            memberService.join(member2); // 예외가 발생해야함.
        }catch (IllegalStateException e) {
            return; //리턴되면 fail 이 뜨지 않음. => @Test(IllegalStateException.class)가 아닌 @Test일 때
        }

        //then
        /** void 메서드 호출 테스트 => validateDuplicateMember가 public일때만 가능 **/
//        Mockito.verify(memberService,Mockito.times(1))
//                .validateDuplicateMember(member2);
        Assertions.fail("예외가 발생해야 한다.");
    }

    @Test
    @DisplayName("after_findAll")
    @Order(5)
    void findAll_members() throws Exception {

        //given
        MemberServiceImpl memberService = new MemberServiceImpl(memberRepository);
        assertNotNull(memberService);

        Member member1 = member();
        Member member2 = new Member();
        member2.setName("testUser2");

        memberService.join(member1);
        memberService.join(member2);

        //when
        List<Member> result = memberService.findMembers();

        //then
        assertEquals(2, result.size());

    }


}
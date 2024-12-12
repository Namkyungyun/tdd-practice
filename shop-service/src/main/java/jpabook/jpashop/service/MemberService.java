package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;

import java.util.List;

public interface MemberService {

    List<Member> findMembers();

    Long join(Member member);

    Member findMember(Long id);
}

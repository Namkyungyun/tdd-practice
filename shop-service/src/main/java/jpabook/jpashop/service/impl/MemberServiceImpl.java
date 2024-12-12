package jpabook.jpashop.service.impl;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private MemberRepository memberRepository;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    
    /** 회원 가입 **/
    @Override
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    /** double checked locking => 멀티스레드에서  **/
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers =  memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()) {
            synchronized (Member.class) {
                if(!findMembers.isEmpty()) {
                    throw new IllegalStateException("이미 존재하는 회원입니다");
                }
            }
        }
    }
    
    /** 회원 전체 조회 **/
    @Override
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    @Override
    /** 회원 한건 조회 **/
    public Member findMember(Long id) {
        return memberRepository.findOne(id);
    }

}

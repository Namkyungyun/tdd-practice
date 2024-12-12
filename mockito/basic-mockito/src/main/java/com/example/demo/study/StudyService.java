package com.example.demo.study;

import com.example.demo.domain.Member;
import com.example.demo.domain.Study;
import com.example.demo.member.MemberService;

import java.util.Optional;

public class StudyService {

    private final MemberService memberService;

    private final StudyRepository repository;

    public StudyService(MemberService memberService, StudyRepository repository) {
        /** memberService와 StudyRepository가 있어야 StudyService를 사용할 수 있다.
         * 이때 memeberService와 repository가 항상 null이 아니여야하므로,
         * 자바의 assert키워드를 사용하여 이를 처리한다.
         * 만약 null이라면 assertException이 떨어진다.**/
        assert memberService != null;
        assert repository != null;
        this.memberService = memberService;
        this.repository = repository;
    }

    public Study createNewStudy(Long memberId, Study study) {
        Optional<Member> member = memberService.findById(memberId);
        if (member.isPresent()) {
            study.setOwnerId(memberId);
        } else {
            throw new IllegalArgumentException("Member doesn't exist for id: '" + memberId + "'");
        }
        Study newstudy = repository.save(study);
        memberService.notify(newstudy);
        memberService.notify(member.get());
        return newstudy;
    }

    public Study openStudy(Study study) {
        study.open();
        Study openedStudy = repository.save(study);
        memberService.notify(openedStudy);
        return openedStudy;
    }

    public void hi() {

    }
}

package com.example.demo.study;

import com.example.demo.domain.Member;
import com.example.demo.domain.Study;
import com.example.demo.domain.StudyStatus;
import com.example.demo.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class StudyServiceTest {

    /** 1. 먼저 테스트를 작성하려면 스터디서비스의 Instance를 만든다.
     *  2. 하지만 StudyService를 만들 수가 없게 되어지는데, 그 이유는 memberService와 StudyRepository가
     *     있어야 만들 수 있기 때문이다.
     */

    @Mock
    private MemberService memberService;

    @Mock
    private StudyRepository studyRepository;

    @Test
    @DisplayName("thenReturn")
    void createStudyService() {
        // 인스턴스 (생성자로)
        StudyService studyService
                = new StudyService(memberService, studyRepository);
        // 인스턴스가 만들어지는지 확인
        assertNotNull(studyService);

        //반환 받을 member객체 정의
        Member member = new Member();
        member.setId(1l);
        member.setEmail("test@email.com");

        // mock객체의 행동을 정의하는 stubbing
        Mockito.when(memberService.findById(ArgumentMatchers.any()))
                .thenReturn(Optional.of(member));

        //실제 호출
//        Optional<Member> findById = memberService.findById(3l);
//        assertEquals("test@email.com", findById.get().getEmail());
        assertEquals("test@email.com", memberService.findById(1l).get().getEmail());
        assertEquals("test@email.com", memberService.findById(2l).get().getEmail());
        assertEquals("test@email.com", memberService.findById(3l).get().getEmail());

    }

    @Test
    @DisplayName("doThrow")
    void createStudyServiceThrow() {
        // 인스턴스 (생성자로)
        StudyService studyService
                = new StudyService(memberService, studyRepository);
        // 인스턴스가 만들어지는지 확인
        assertNotNull(studyService);

        // mock객체의 행동을 정의하는 stubbing- doThrow
        Mockito.doThrow(new IllegalArgumentException()).when(memberService).validate(1l);

        assertThrows(IllegalArgumentException.class, () -> {
            memberService.validate(1l);
        });
    }
    @Test
    @DisplayName("동일매개변수,각기다른행동")
    void createStudyServiceOtherAct() {
        // 인스턴스 (생성자로)
        StudyService studyService
                = new StudyService(memberService, studyRepository);
        // 인스턴스가 만들어지는지 확인
        assertNotNull(studyService);

        //반환 받을 member객체 정의
        Member member = new Member();
        member.setId(1l);
        member.setEmail("test@email.com");

        // mock객체의 행동을 정의하는 stubbing
        Mockito.when(memberService.findById(ArgumentMatchers.any()))
                .thenReturn(Optional.of(member))   // 정상적인 member객체 리턴
                .thenThrow(new RuntimeException()) // 예외던지기
                .thenReturn(Optional.empty());     // 빈값 리턴

        Optional<Member> one = memberService.findById(1l);
        assertEquals("test@email.com", one.get().getEmail());

        assertThrows(RuntimeException.class, () -> {
           memberService.findById(2l);
        });

        assertEquals(Optional.empty(), memberService.findById(3l));
    }
    @Test
    @DisplayName("verify- notify(study)만 존재 시")
    void studyRepositorySaveAndfindById() {
        // 인스턴스 (생성자로)
        StudyService studyService
                = new StudyService(memberService, studyRepository);

        //study객체 정의
        Study study = new Study(10, "테스트");

        //member객체 정의
        Member member = new Member();
        member.setId(1l);
        member.setEmail("test@email.com");
        
        //stubbing
        Mockito.when(memberService.findById(1l)).thenReturn(Optional.of(member));
        Mockito.when(studyRepository.save(study)).thenReturn(study);
        
        //호출 및 검증
        studyService.createNewStudy(1l, study);
        assertEquals(member.getId(), study.getOwnerId());

        //memberService.notify study 호출 확인
        Mockito.verify(memberService, Mockito.times(1)).notify(study);

    }
    @Test
    @DisplayName("verify- notify(study & member)")
    void createNewStudyNotifyNoAny() {
        // 인스턴스 (생성자로)
        StudyService studyService
                = new StudyService(memberService, studyRepository);

        //study객체 정의
        Study study = new Study(10, "테스트");

        //member객체 정의
        Member member = new Member();
        member.setId(1l);
        member.setEmail("test@email.com");

        Mockito.when(memberService.findById(1l)).thenReturn(Optional.of(member));
        Mockito.when(studyRepository.save(study)).thenReturn(study);

        studyService.createNewStudy(1l, study);
        assertEquals(member.getId(), study.getOwnerId());

        //memberService.notify study 호출 확인-ArgumentMatchers 사용 불가
        Mockito.verify(memberService, Mockito.times(1)).notify(study);
        //memberService.notify member 호출 확인-ArgumentMatchers 사용 불가
        Mockito.verify(memberService, Mockito.times(1)).notify(member);
        //memberService.validate 호출되어지지않음을 확인
        Mockito.verify(memberService, Mockito.never()).validate(ArgumentMatchers.any());

    }
    @Test
    @DisplayName("verify- inOrder사용")
    void createNewStudyInOrder() {
        // 인스턴스 (생성자로)
        StudyService studyService
                = new StudyService(memberService, studyRepository);

        //study객체 정의
        Study study = new Study(10, "테스트");

        //member객체 정의
        Member member = new Member();
        member.setId(1l);
        member.setEmail("test@email.com");

        //stubbing
        Mockito.when(memberService.findById(1l)).thenReturn(Optional.of(member));
        Mockito.when(studyRepository.save(study)).thenReturn(study);

        //호출 및 검증
        studyService.createNewStudy(1l, study);
        assertEquals(member.getId(), study.getOwnerId());

        //순서 고려 시 Mockito.inOrder 사용
        InOrder inOrder = Mockito.inOrder(memberService);
        inOrder.verify(memberService).notify(member);
        inOrder.verify(memberService).notify(study);
    }

    @Test
    @DisplayName("OpenStudy")
    void openServiceTest() {
        //given
        StudyService studyService = new StudyService(memberService, studyRepository);
        assertNotNull(studyService);

        Study study = new Study(10, "테스트 BDD");
        assertNull(study.getOpenedDateTime());

        BDDMockito.given(studyRepository.save(study)).willReturn(study);

        //when
        studyService.openStudy(study);

        //then
        assertEquals(StudyStatus.OPENED, study.getStatus());
        assertNotNull(study.getOpenedDateTime());
        BDDMockito.then(memberService).should().notify(study);

    }
}

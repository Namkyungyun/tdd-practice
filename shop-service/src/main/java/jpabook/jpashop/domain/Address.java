package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable //내장형으로 만듦
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;

    // 값 타입은 변경 불가능하게 설계해야한다.
    // @Setter를 제거하고, 생성자에서 값을 모두 초기화해서 변경 불가능한 클래스를 만들어야한다.
    // JPA 스펙상 엔티티나 임베디드 타입은 자바 기본 생성자를 PUBLIC, PROTECTED로 설정해야한다.
    // PUBLIC으로 두는 것보다 PROTECTED로 두는 것이 더 안전
    // jpa가 이런 제약을 두는 이유는, jpa 구현 라이브러리가 객체를 생성할 때 리플랙션 같은 기술을 사용할 수 있도록
    // 지원해야 하기 때문이다.
    public Address() {
        
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}

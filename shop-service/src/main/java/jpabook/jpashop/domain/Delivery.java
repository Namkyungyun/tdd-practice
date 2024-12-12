package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Delivery {
    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING) //enum 타입은 해당 어노테이션을 반드시 넣어야한다.
    private DeliveryStatus status; //ordernal은 숫자이기에 중간에 다른 것이 들어갈 시 밀릴 수 있따.
                                   // 따라서 string으로 사용해야한다.
}
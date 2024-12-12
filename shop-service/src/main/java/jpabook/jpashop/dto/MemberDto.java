package jpabook.jpashop.dto;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class MemberDto {

    private Long id;
    private String name;
    private Address address;
    private List<Order> orders = new ArrayList<>();
}

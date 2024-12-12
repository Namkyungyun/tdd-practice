package jpabook.jpashop.service;

import jpabook.jpashop.domain.Order;

import java.util.List;

public interface OrderService {
    //주문
    Long order(Long memberId, Long itemId, int count);

    //취소
    void cancelOrder(Long orderId);

    //검색
    List<Order> findOrders(OrderSearch orderSearch);
}

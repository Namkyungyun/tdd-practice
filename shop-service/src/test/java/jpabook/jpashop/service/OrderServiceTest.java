package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class OrderServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;


    @Test
    @Order(1)
    @DisplayName("상품 주문")
    void order() throws Exception {
        //given
        Member member = getMember();

        Item book = getItem();

        int orderCount = 3;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        jpabook.jpashop.domain.Order order = orderRepository.findeOne(orderId);

        Assertions.assertEquals(OrderStatus.ORDER, order.getStatus(), "상품 주문시 상태");
        Assertions.assertEquals(1,order.getOrderItems().size(),"주문한 상품의 종류 수");
        Assertions.assertEquals(10000 * orderCount, order.getTotalPrice(),"총 금액");
        Assertions.assertEquals(7, book.getStockQuantity(),"주문 후 줄어든 제고 수량");


    }

    @Test
    @Order(2)
    @DisplayName("제고 수량 초과")
    void orderOverStock() throws Exception{
        //given
        Member member = getMember();
        Item item = getItem();

        int orderCount = 11;

        //then
        Assertions.assertThrows(NotEnoughStockException.class, () -> {
            orderService.order(member.getId(), item.getId(), orderCount);
        });
    }

    @Test
    @Order(3)
    @DisplayName("주문 취소")
    void cancel() throws Exception {
        //given
        Member member = getMember();
        Item item = getItem();

        int orderCount = 3;

        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        jpabook.jpashop.domain.Order order = orderRepository.findeOne(orderId);
        Assertions.assertEquals(OrderStatus.CANCEL,order.getStatus());
        Assertions.assertEquals(10,item.getStockQuantity());

    }


    private Member getMember() {
        Member member = new Member();
        member.setName("testUser1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);
        return member;
    }

    private Item getItem() {
        Item book = new Book();
        book.setName("jpa");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);
        return book;
    }


}
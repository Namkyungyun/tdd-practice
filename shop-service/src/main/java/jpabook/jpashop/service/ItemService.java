package jpabook.jpashop.service;

import jpabook.jpashop.domain.Item;

import java.util.List;

public interface ItemService {

    Long createItem(Item item);

    Item findItem(Long id);

    List<Item>findItems();
}

package jpabook.jpashop.service.impl;

import jpabook.jpashop.domain.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private ItemRepository itemRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }


    @Override
    @Transactional
    public Long createItem(Item item) {
        itemRepository.save(item);
        return item.getId();
    }

    @Override
    public Item findItem(Long id) {
        return itemRepository.findOne(id);
    }

    @Override
    public List<Item> findItems() {
        return itemRepository.findAll();
    }
}

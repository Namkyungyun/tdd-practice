package jpabook.jpashop.service;

import jpabook.jpashop.domain.Item;
import jpabook.jpashop.domain.item.Album;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.service.impl.ItemServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
class ItemServiceTest {

    @Autowired
    private ItemServiceImpl itemService;

    private Item itemAlbum() {
        final Album album = new Album();
        album.setId(1l);
        album.setName("회전목마");
        album.setArtist("artist1");
        album.setEtc("etc`");
        album.setPrice(4000);
        album.setStockQuantity(10);
        return album;
    }

    private Item itemBook() {
        final Book book = new Book();
        book.setName("java");
        book.setAuthor("author1");
        book.setIsbn("b");
        book.setPrice(5000);
        book.setStockQuantity(10);
        return book;
    }

    @Test
    @DisplayName("before_create_item")
    @Order(1)
    void create_item(@Mock ItemRepository itemRepository) {
        //given
        Item album = itemAlbum();
        Item book = itemBook();

        //when
        itemRepository.save(album);
        itemRepository.save(book);

        //then
        Mockito.verify(itemRepository).save(album);


    }

    @Test
    @DisplayName("before_find_one")
    @Order(2)
    void find_one_item(@Mock ItemRepository itemRepository) {
        //given
        Item album = itemAlbum();
        Item book = itemBook();

        //when
        itemRepository.save(album);
        itemRepository.save(book);
        
        Mockito.when(itemRepository.findOne(Mockito.anyLong())).thenReturn(album);

        //then
        Item result = itemRepository.findOne(Mockito.anyLong());
        assertEquals("회전목마", result.getName());
    }

    @Test
    @DisplayName("before_find_all")
    @Order(3)
    void find_all_item(@Mock ItemRepository itemRepository) {
        //given
        Item album = itemAlbum();
        Item book = itemBook();

        List<Item> items = new ArrayList<>();
        items.add(album);
        items.add(book);

        //when
        itemRepository.save(album);
        itemRepository.save(book);

        Mockito.when(itemRepository.findAll()).thenReturn(items);

        //then
        assertEquals(2, itemRepository.findAll().size());
    }

    @Test
    @DisplayName("after_find_one")
    @Order(4)
    void find_item_after() {
        //given
        Item album = itemAlbum();

        //when
        Long getId = itemService.createItem(album);
        assertNotNull(getId);

        //then
        Item result = itemService.findItem(getId);
        assertEquals("회전목마", result.getName());


    }

    @Test
    @DisplayName("after_find_all")
    @Order(5)
    void create_item_after() {
        //given
        Item album = itemAlbum();
        Item book = itemBook();

        //when
        itemService.createItem(album);
        itemService.createItem(book);

        //then
        assertEquals(2, itemService.findItems().size());
    }

}
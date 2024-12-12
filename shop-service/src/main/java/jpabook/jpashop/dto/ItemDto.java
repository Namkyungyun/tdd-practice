package jpabook.jpashop.dto;

import jpabook.jpashop.domain.Category;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class ItemDto {

    private Long id;
    private String name;
    private int price;
    private int stockQuantity;
    private String author;
    private String isbn;





}

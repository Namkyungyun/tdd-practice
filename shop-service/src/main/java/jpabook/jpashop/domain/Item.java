package jpabook.jpashop.domain;

import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) //상속관계이기 때문에 상속관계 전략을 지정해야한다. 싱글테이블 전략으로 지정하기
@DiscriminatorColumn(name = "dtype") //구분될때의
@Getter @Setter // 구현체를 만들 것이기에, 추상클래스로
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    //상속관계 매핑을 해야함. 
    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    /**  비즈니스 로직
     *   stockQuantity를 가지고 있는 쪽에 비즈니스 로직을 만드는 것이 좋다.
     *   응집력에 좋기에
     * **/
    // stock 증가가
   public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    // stock 절감
    public void removeStock(int quantity) {
       int restStock = this.stockQuantity - quantity;
       if(restStock < 0) {
           throw new NotEnoughStockException("need more stock!");
       }
       this.stockQuantity = restStock;
    }

}

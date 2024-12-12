package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> items = new ArrayList<>();
    // collection vs collection으로 다대다 관계가 가능하지만 관계형 db는 collection vs collection이 불가능하기 때문에
    // 일대다 , 다대일로 풀어낼 수 있는 중간 테이블이 필요하다 (= category_item)


    //셀프 양방향 관계 걸기
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    // 연관관계 편의 메서드
    public void addChildCategory(Category child) {
        // 카테고리 셀프지만, 양방향이어야한디. 부모, 자식 모두 들어가야함
        this.child.add(child); //컬렉션에 들어가고
        child.setParent(this); // 자식에서도 부모가 누구인지 this로 명시
    }
}

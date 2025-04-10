package kr.ollsy.item.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kr.ollsy.category.domain.Category;
import kr.ollsy.global.entity.DateEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Table(name = "items")
public class Item extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "items_id")
    Long id;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    String description;

    @Column(nullable = false)
    int price;

    @Column(nullable = false)
    int stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categories_id")
    private Category category;

    public Item(String name, String description, int price, int stock, Category category) {
        validate(name, description, price, stock);
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }

    public void updateItem(String name, String description, int price, int stock, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }

    private void validate(String name, String description, int price, int stock) {
        validateNotNull(name, description, price, stock);
        validateNotBlank(name, description);
    }

    private void validateNotNull(String name, String description, int price, int stock) {
        if (name == null || description == null || (Integer) price == null || (Integer) stock == null) {
            throw new IllegalArgumentException("제품 이름, 제품 설명, 가격을 입력해 주세요.");
        }
    }

    private void validateNotBlank(String name, String description) {
        if (name.isBlank() || description.isBlank()) {
            throw new IllegalArgumentException("제품 이름, 제품 설명은 비어있을 수 없습니다");
        }
    }

    public void removeStock(int quantity) {
        if (this.stock < quantity) {
            throw new IllegalArgumentException("재고가 부족합니다!");
        }
        this.stock -= quantity;
    }
}
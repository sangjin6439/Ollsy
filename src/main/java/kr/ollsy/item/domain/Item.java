package kr.ollsy.item.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
public class Item {

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

    public Item(String name, String description, int price) {
        validate(name, description, price);
        this.name = name;
        this.description = description;
        this.price = price;
    }

    private void validate(String name, String description, int price) {
        validateNotNull(name, description, price);
        validateNotBlank(name, description);
    }

    private void validateNotNull(String name, String description, int price) {
        if (name == null || description == null ||  (Integer)price == null) {
            throw new IllegalArgumentException("제품 이름, 제품 설명, 가격을 입력해 주세요.");
        }
    }

    private void validateNotBlank(String name, String description) {
        if (name.isBlank() || description.isBlank()) {
            throw new IllegalArgumentException("제품 이름, 제품 설명은 비어있을 수 없습니다");
        }
    }

}
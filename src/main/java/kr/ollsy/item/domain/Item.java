package kr.ollsy.item.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import kr.ollsy.category.domain.Category;
import kr.ollsy.global.entity.DateEntity;
import kr.ollsy.global.exception.CustomException;
import kr.ollsy.global.exception.GlobalExceptionCode;
import kr.ollsy.itemImage.domain.ItemImage;
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
    @Column(name = "item_id")
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
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemImage> images = new ArrayList<>();

    public Item(String name, String description, int price, int stock, Category category) {
        validate(name, description, price, stock);
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
            throw new CustomException(GlobalExceptionCode.ITEM_VALID_NOT_NULL);
        }
    }

    private void validateNotBlank(String name, String description) {
        if (name.isBlank() || description.isBlank()) {
            throw new CustomException(GlobalExceptionCode.ITEM_VALID_NOT_BLANK);
        }
    }

    public void updateItem(String name, String description, int price, int stock, Category category, List<ItemImage> itemImageList) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
        updateItemImages(itemImageList);
    }

    private void updateItemImages(List<ItemImage> itemImages) {
        this.images.clear();
        if (itemImages != null) {
            for(ItemImage itemImage : itemImages){
                itemImage.setItem(this);
                this.images.add(itemImage);
            }
        }
    }

    public void validateQuantity(int quantity){
        if(quantity<=0){
            throw new CustomException(GlobalExceptionCode.ITEM_INVALID_QUANTITY);
        }
    }

    public void removeStock(int quantity) {
        if (this.stock < quantity) {
            throw new CustomException(GlobalExceptionCode.ITEM_NOT_ENOUGH_STOCK);
        }
        this.stock -= quantity;
    }

    public void addStock(int quantity) {
        this.stock += quantity;
    }

    public void addImage(List<ItemImage> itemImages) {
        this.images = itemImages;
        for (ItemImage itemImage : itemImages) {
            itemImage.setItem(this);
        }
    }
}
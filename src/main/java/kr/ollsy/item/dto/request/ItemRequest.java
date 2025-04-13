package kr.ollsy.item.dto.request;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.ollsy.category.domain.Category;
import kr.ollsy.item.domain.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ItemRequest {

    @NotNull
    @Length(min = 2, max = 20)
    private String name;
    @NotNull
    private String description;

    private int price;

    private int stock;

    private Long categoryId;

}
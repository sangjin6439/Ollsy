package kr.ollsy.item.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import kr.ollsy.category.domain.Category;
import kr.ollsy.category.repository.CategoryRepository;
import kr.ollsy.item.domain.Item;
import kr.ollsy.item.dto.request.ItemRequest;
import kr.ollsy.item.dto.response.ItemListResponse;
import kr.ollsy.item.dto.response.ItemResponse;
import kr.ollsy.item.repository.ItemRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public ItemResponse createItem(ItemRequest itemRequest) {
        validCategoryIdIsNull(itemRequest.getCategoryId());
        Category category = findCategory(itemRequest.getCategoryId());
        Item item = itemRequest.toItem(itemRequest.getName(), itemRequest.getDescription(), itemRequest.getPrice(), itemRequest.getStock(), category);
        itemRepository.save(item);
        return ItemResponse.of(item.getId(), item.getName(), item.getDescription(), item.getPrice(), item.getStock(), item.getCategory().getName());
    }

    private void validCategoryIdIsNull(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("카테고리를 설정해 주세요");
        }
    }

    private Category findCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 다시 확인해 주세요"));
    }

    @Transactional(readOnly = true)
    public ItemResponse findItem(Long id) {
        Item item = findItemById(id);
        return ItemResponse.of(item.getId(), item.getName(), item.getDescription(), item.getPrice(), item.getStock(), item.getCategory().getName());
    }

    private Item findItemById(Long id) {
        return itemRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("제품을 찾을 수 없습니다."));
    }

    @Transactional(readOnly = true)
    public List<ItemListResponse> findItems() {
        List<Item> itemList = itemRepository.findAll();
        List<ItemListResponse> itemListResponses = createItemListResponse(itemList);
        return itemListResponses;
    }

    private List<ItemListResponse> createItemListResponse(List<Item> itemList) {
        return itemList.stream()
                .map(item -> ItemListResponse.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .description(item.getDescription())
                        .price(item.getPrice())
                        .stock(item.getStock())
                        .categoryName(item.getCategory().getName())
                        .build())
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ItemListResponse> findItemsByCategory(Long id) {
        List<Item> itemList = itemRepository.findItemsByCategoryId(id);
        List<ItemListResponse> itemListResponseList = createItemListResponse(itemList);
        return itemListResponseList;
    }

    @Transactional
    public ItemResponse updateItem(Long id, ItemRequest itemRequest) {
        Item item = findItemById(id);
        validCategoryIdIsNull(itemRequest.getCategoryId());
        Category category = findCategory(itemRequest.getCategoryId());

        item.updateItem(itemRequest.getName(), itemRequest.getDescription(), itemRequest.getPrice(), itemRequest.getStock(), category);

        return ItemResponse.of(item.getId(), item.getName(), item.getDescription(), item.getPrice(), item.getStock(), item.getCategory().getName());
    }

    @Transactional
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }
}
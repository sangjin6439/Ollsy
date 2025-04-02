package kr.ollsy.item.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

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

    @Transactional
    public ItemResponse createItem(ItemRequest itemRequest) {
        Item item = itemRequest.toItem(itemRequest.getName(), itemRequest.getDescription(), itemRequest.getPrice(), itemRequest.getStock());
        itemRepository.save(item);
        return ItemResponse.of(item.getId(), item.getName(), item.getDescription(), item.getPrice(), item.getStock());
    }

    @Transactional(readOnly = true)
    public ItemResponse findItem(Long id) {
        Item item = findItemById(id);
        return ItemResponse.of(item.getId(),item.getName(), item.getDescription(), item.getPrice(), item.getStock());
    }

    private Item findItemById(Long id){
        return itemRepository.findById(id).orElseThrow(()->new IllegalArgumentException("제품을 찾을 수 없습니다."));
    }

    @Transactional(readOnly = true)
    public List<ItemListResponse> findItems() {
        List<Item> itemList = itemRepository.findAll();
        List<ItemListResponse> itemListResponses = itemList.stream()
                .map(item -> ItemListResponse.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .description(item.getDescription())
                        .price(item.getPrice())
                        .stock(item.getStock())
                        .build())
                .toList();
        return itemListResponses;
    }

    @Transactional
    public ItemResponse updateItem(Long id, ItemRequest itemRequest) {
        Item item = findItemById(id);
        item.updateItem(itemRequest.getName(), itemRequest.getDescription(), itemRequest.getPrice(), itemRequest.getStock());

        return ItemResponse.of(item.getId(),item.getName(), item.getDescription(), item.getPrice(), item.getStock());
    }

    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }
}
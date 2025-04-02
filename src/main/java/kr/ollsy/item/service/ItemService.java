package kr.ollsy.item.service;

import org.springframework.stereotype.Service;

import kr.ollsy.item.domain.Item;
import kr.ollsy.item.dto.request.ItemRequest;
import kr.ollsy.item.dto.response.ItemResponse;
import kr.ollsy.item.repository.ItemRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemResponse createItem(ItemRequest itemRequest) {
        Item item = itemRequest.toItem(itemRequest.getName(), itemRequest.getDescription(), itemRequest.getPrice());
        itemRepository.save(item);
        return ItemResponse.of(item.getId(), item.getName(), item.getDescription(), item.getPrice());
    }
}
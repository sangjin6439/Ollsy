package kr.ollsy.item.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;
import kr.ollsy.item.dto.request.ItemRequest;
import kr.ollsy.item.dto.response.ItemListResponse;
import kr.ollsy.item.dto.response.ItemResponse;
import kr.ollsy.item.service.ItemService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemResponse> createItem(
            @RequestBody @Valid ItemRequest itemRequest
    ) {
        ItemResponse itemResponse = itemService.createItem(itemRequest);
        return ResponseEntity.created(URI.create("api/v1/item/"+ itemResponse.getId()
        )).body(itemResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> findItem(
            @PathVariable("id") Long id
    ){
        return ResponseEntity.ok(itemService.findItem(id));
    }

    @GetMapping
    public ResponseEntity<List<ItemListResponse>> findItems(
    ){
        return ResponseEntity.ok(itemService.findItems());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ItemResponse> updateItem(
            @PathVariable("id") Long id,
            @RequestBody ItemRequest itemRequest
    ){
        return ResponseEntity.ok(itemService.updateItem(id, itemRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(
            @PathVariable("id") Long id
    ){
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}

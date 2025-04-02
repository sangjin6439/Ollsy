package kr.ollsy.item.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import jakarta.validation.Valid;
import kr.ollsy.item.dto.request.ItemRequest;
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

}

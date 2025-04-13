package kr.ollsy.itemImage.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.ollsy.itemImage.dto.request.ItemImageRequest;
import kr.ollsy.itemImage.service.ItemImageService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/itemImages")
public class ItemImageController {
    private final ItemImageService itemImageService;

    @PostMapping
    public ResponseEntity<String> uploadItemImage(@ModelAttribute ItemImageRequest itemImageRequest) {
        return ResponseEntity.ok(itemImageService.uploadItemImage(itemImageRequest));
    }
}

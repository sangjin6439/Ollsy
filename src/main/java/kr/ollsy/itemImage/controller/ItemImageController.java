package kr.ollsy.itemImage.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import kr.ollsy.itemImage.dto.reponse.ItemImageResponse;
import kr.ollsy.itemImage.service.ItemImageService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/itemImages")
public class ItemImageController {
    private final ItemImageService itemImageService;

    @PostMapping
    public ResponseEntity<ItemImageResponse> uploadItemImage(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(itemImageService.uploadItemImage(file));
    }
}

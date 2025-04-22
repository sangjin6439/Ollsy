package kr.ollsy.itemImage.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.ollsy.itemImage.dto.reponse.ItemImageResponse;
import kr.ollsy.itemImage.service.ItemImageService;
import lombok.RequiredArgsConstructor;

@Tag(name = "ItemImage")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/itemImage")
public class ItemImageController {
    private final ItemImageService itemImageService;

    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "이미지 정보 저장", description = "이미지 정보를 S3와 DB에 저장합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 정보 확인 완료", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "이미지 정보가 잘못 입력됐습니다", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<ItemImageResponse> uploadItemImage(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(itemImageService.uploadItemImage(file));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "이미지 삭제", description = "이미지를 S3와 DB에서 삭제합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이미지 삭제 완료", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "이미지를 찾을 수 없습니다", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<Void> deleteItemImage(@PathVariable("id") Long id) {
        itemImageService.deleteItemImage(id);
        return ResponseEntity.noContent().build();
    }
}

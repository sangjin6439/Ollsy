package kr.ollsy.item.controller;


import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.ollsy.item.dto.request.ItemRequest;
import kr.ollsy.item.dto.request.ItemSearchRequest;
import kr.ollsy.item.dto.response.ItemListResponse;
import kr.ollsy.item.dto.response.ItemResponse;
import kr.ollsy.item.service.ItemService;
import lombok.RequiredArgsConstructor;

@Tag(name = "Item")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/item")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    @Operation(summary = "아이템 생성", description = "아이템을 생성합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아이템 생성 완료", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "올바른 아이템 정보를 입력해 주세요", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<ItemResponse> createItem(
            @RequestBody @Valid ItemRequest itemRequest
    ) {
        ItemResponse itemResponse = itemService.createItem(itemRequest);
        return ResponseEntity.created(URI.create("api/v1/item/" + itemResponse.id()
        )).body(itemResponse);
    }

    @GetMapping("/{id}")
    @Operation(summary = "아이템 조회", description = "아이템을 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아이템 조회 완료", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "조회하는 아이템이 없습니다.", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<ItemResponse> findItem(
            @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(itemService.findItem(id));
    }

    @GetMapping
    @Operation(summary = "모든 아이템 조회", description = "모든 아이템을 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모든 아이템 조회 완료", content = {@Content(mediaType = "application/json")}),
    })
    @PageableAsQueryParam
    public ResponseEntity<Page<ItemListResponse>> findItems(
            @Parameter(hidden = true)
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable
            ){
        return ResponseEntity.ok(itemService.findItems(pageable));
    }

    @GetMapping("/new")
    @Operation(summary = "신상품 순으로 조회", description = "신상품 순으로 아이템을 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "신상품 순으로 아이템 조회 완료", content = {@Content(mediaType = "application/json")}),
    })
    @PageableAsQueryParam
    public ResponseEntity<Page<ItemListResponse>> findItemsByCreated(
            @Parameter(hidden = true)
            @PageableDefault(size = 10, sort = "createAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(itemService.findItemsByCreated(pageable));
    }

    @GetMapping("/category/{id}")
    @Operation(summary = "카테고리 별 아이템 조회", description = "카테고리 별 아이템을 조회합니다. URL의 쿼리 파라미터로 includeSub=true을 붙이면 하위 카테고리까지 조회되고, 기본은 false입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카테고리 별 아이템을 조회 완료", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없습니다", content = @Content(mediaType = "application/json")),
    })
    @PageableAsQueryParam
    public ResponseEntity<Page<ItemListResponse>> findItemsByCategory(
            @Parameter(hidden = true)
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable,
            @PathVariable("id") Long id,
            @RequestParam(value = "includeSub", required = false, defaultValue = "false") boolean includeSub

    ) {
        return ResponseEntity.ok(itemService.findItemsByCategory(id, includeSub, pageable));
    }

    @GetMapping("/search")
    @Operation(summary = "여러 조건에 따른 아이템 조회", description = "제품에 포함되는 이름, 카테고리 별, 최소 최대 가격으로 아이템 조회.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "여러 조건에 따른 아이템을 조회 완료", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "검색 조건을 확인해 주세요", content = @Content(mediaType = "application/json")),
    })
    @PageableAsQueryParam
    public ResponseEntity<Page<ItemListResponse>> searchItems(
            @Parameter(hidden = true)
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable,
            @RequestBody ItemSearchRequest itemSearchRequest
            ){
        return ResponseEntity.ok(itemService.searchItems(itemSearchRequest, pageable));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "아이템 정보 변경", description = "아이템의 정보를 변경합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아이템 정보 변경 완료", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "아이템을 찾을 수 없습니다", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<ItemResponse> updateItem(
            @PathVariable("id") Long id,
            @RequestBody @Valid ItemRequest itemRequest
    ) {
        return ResponseEntity.ok(itemService.updateItem(id, itemRequest));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "아이템 정보 삭제", description = "아이템 정보를 삭제합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아이템 정보 삭제 완료", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "아이템을 찾을 수 없습니다", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<Void> deleteItem(
            @PathVariable("id") Long id
    ) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
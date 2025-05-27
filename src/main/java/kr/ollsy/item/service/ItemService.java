package kr.ollsy.item.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import kr.ollsy.category.domain.Category;
import kr.ollsy.category.repository.CategoryRepository;
import kr.ollsy.global.exception.CustomException;
import kr.ollsy.global.exception.GlobalExceptionCode;
import kr.ollsy.item.domain.Item;
import kr.ollsy.item.dto.request.ItemRequest;
import kr.ollsy.item.dto.request.ItemSearchRequest;
import kr.ollsy.item.dto.response.ItemListResponse;
import kr.ollsy.item.dto.response.ItemResponse;
import kr.ollsy.item.repository.ItemRepository;
import kr.ollsy.itemImage.domain.ItemImage;
import kr.ollsy.itemImage.repository.ItemImageRepository;
import kr.ollsy.itemImage.service.ItemImageService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final ItemImageRepository itemImageRepository;
    private final ItemImageService itemImageService;

    @Transactional
    public ItemResponse createItem(ItemRequest itemRequest) {
        validCategoryIdIsNull(itemRequest.categoryId());
        Category category = findCategory(itemRequest.categoryId());
        List<ItemImage> itemImageList = getUploadImages(itemRequest.itemImageId());
        Item item = Item.builder()
                .name(itemRequest.name())
                .description(itemRequest.description())
                .price(itemRequest.price())
                .stock(itemRequest.stock())
                .category(category)
                .images(itemImageList)
                .build();

        item.addImage(itemImageList);
        itemRepository.save(item);

        List<String> urlList = getUrlList(itemImageList);

        return ItemResponse.of(item.getId(), item.getName(), item.getDescription(), item.getPrice(), item.getStock(), item.getCategory().getName(), urlList);
    }

    private void validCategoryIdIsNull(Long id) {
        if (id == null) {
            throw new CustomException(GlobalExceptionCode.CATEGORY_NOT_FOUND);
        }
    }

    private Category findCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(GlobalExceptionCode.CATEGORY_NOT_FOUND));
    }

    private List<ItemImage> getUploadImages(List<Long> itemImageIds) {
        return itemImageIds.stream()
                .map(imageId -> itemImageRepository.findById(imageId)
                        .orElseThrow(() -> new CustomException(GlobalExceptionCode.ITEM_IMAGE_NOT_FOUND)))
                .collect(Collectors.toList());
    }

    private List<String> getUrlList(List<ItemImage> itemImageList) {
        return itemImageList.stream()
                .map(ItemImage::getUrl)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ItemResponse findItem(Long id) {
        Item item = findItemById(id);
        List<String> urlList = getUrlList(item.getImages());
        return ItemResponse.of(item.getId(), item.getName(), item.getDescription(), item.getPrice(), item.getStock(), item.getCategory().getName(), urlList);
    }

    private Item findItemById(Long id) {
        return itemRepository.findById(id).orElseThrow(() -> new CustomException(GlobalExceptionCode.ITEM_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Page<ItemListResponse> findItems(Pageable pageable) {
        Page<Item> itemPage = itemRepository.findAll(pageable);
        return itemPage.map(this::toItemListResponse);
    }
    //페이징 시 사용하는 메서드
    private ItemListResponse toItemListResponse(Item item){
        return ItemListResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .price(item.getPrice())
                .stock(item.getStock())
                .categoryName(item.getCategory().getName())
                .itemImageUrl(getUrlList(item.getImages()))
                .build();
    }

    //페이징 안 하는 메서드에 사용
    private List<ItemListResponse> createItemListResponse(List<Item> itemList) {
        return itemList.stream()
                .map(item -> ItemListResponse.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .description(item.getDescription())
                        .price(item.getPrice())
                        .stock(item.getStock())
                        .categoryName(item.getCategory().getName())
                        .itemImageUrl(getUrlList(item.getImages()))
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<ItemListResponse> findItemsByCreated(Pageable pageable) {
        Page<Item> itemPage = itemRepository.findAllOrderByCreateAtDesc(pageable);
        return itemPage.map(this::toItemListResponse);
    }

    @Transactional(readOnly = true)
    public Page<ItemListResponse> findItemsByCategory(Long id, boolean includeSub, Pageable pageable) {
        Page<Item> itemPage;
        if (includeSub) {
            Category category = findCategory(id);
            List<Long> categoryIdList = getAllCategoryIdList(category);
            itemPage = itemRepository.findAllByCategoryIdIn(categoryIdList, pageable);
        } else {
            itemPage = itemRepository.findItemsByCategoryId(id, pageable);
        }
        return itemPage.map(this::toItemListResponse);
    }

    private List<Long> getAllCategoryIdList(Category category) {
        List<Long> idList = new ArrayList<>();
        collectCategoryIdList(category, idList);
        return idList;
    }

    private void collectCategoryIdList(Category category, List<Long> idList) {
        idList.add(category.getId());
        for (Category child : category.getChildren()) {
            collectCategoryIdList(child, idList);
        }
    }

    @Transactional(readOnly = true)
    public Page<ItemListResponse> searchItems(ItemSearchRequest itemSearchRequest, Pageable pageable) {
        Page<Item> itemPage = itemRepository.searchItems(
                itemSearchRequest.name(),
                itemSearchRequest.categoryId(),
                itemSearchRequest.maxPrice(),
                itemSearchRequest.minPrice(),
                pageable
        );
        return itemPage.map(this::toItemListResponse);
    }

    @Transactional
    public ItemResponse updateItem(Long id, ItemRequest itemRequest) {
        Item item = findItemById(id);
        validCategoryIdIsNull(itemRequest.categoryId());

        Category category = findCategory(itemRequest.categoryId());
        List<ItemImage> newImageList = getUploadImages(itemRequest.itemImageId());
        List<ItemImage> oldImageList = new ArrayList<>(item.getImages());

        item.updateItem(itemRequest.name(), itemRequest.description(), itemRequest.price(), itemRequest.stock(), category, newImageList);

        List<ItemImage> imagesToDelete = getImagesToDelete(oldImageList,newImageList);

        deleteImagesFromS3(imagesToDelete);

        List<String> urlList = getUrlList(item.getImages());
        return ItemResponse.of(item.getId(), item.getName(), item.getDescription(), item.getPrice(), item.getStock(), item.getCategory().getName(), urlList);
    }

    private List<ItemImage> getImagesToDelete(List<ItemImage> oldImages, List<ItemImage> newImages) {
        return oldImages.stream()
                .filter(old -> newImages.stream().noneMatch(newImg -> newImg.getId().equals(old.getId())))
                .collect(Collectors.toList());
    }

    private void deleteImagesFromS3(List<ItemImage> imagesToDelete) {
        for (ItemImage image : imagesToDelete) {
            itemImageService.deleteItemImageInS3(image.getUrl());
        }
    }
    @Transactional
    public void deleteItem(Long id) {
        Item item = findItemById(id);

        deleteImagesFromS3(item.getImages());

        itemRepository.deleteById(id);
    }
}
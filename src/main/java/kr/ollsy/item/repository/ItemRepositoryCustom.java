package kr.ollsy.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kr.ollsy.item.domain.Item;

public interface ItemRepositoryCustom {

    Page<Item> findAllOrderByCreateAtDesc(Pageable pageable);

    Page<Item> searchItems(String name, Long categoryId, Integer maxPrice, Integer minPrice, Pageable pageable);
}
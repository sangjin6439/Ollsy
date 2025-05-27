package kr.ollsy.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

import javax.swing.ListModel;

import kr.ollsy.item.domain.Item;
import kr.ollsy.item.dto.response.ItemListResponse;

public interface ItemRepositoryCustom {

    Page<Item> findAllOrderByCreateAtDesc(Pageable pageable);

    Page<Item> searchItems(String name, Long categoryId, Integer maxPrice, Integer minPrice, Pageable pageable);
}
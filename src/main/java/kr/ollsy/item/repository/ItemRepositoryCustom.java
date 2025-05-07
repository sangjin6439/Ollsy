package kr.ollsy.item.repository;

import java.util.List;

import javax.swing.ListModel;

import kr.ollsy.item.domain.Item;

public interface ItemRepositoryCustom {
    List<Item> findAllOrderByCreateAtDesc();

    List<Item> searchItems(String name, Long categoryId,Integer maxPrice, Integer minPrice);
}
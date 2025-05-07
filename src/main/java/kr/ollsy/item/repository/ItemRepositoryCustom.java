package kr.ollsy.item.repository;

import java.util.List;

import kr.ollsy.item.domain.Item;

public interface ItemRepositoryCustom {
    List<Item> findAllOrderByCreateAtDesc();
}

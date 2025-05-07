package kr.ollsy.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import kr.ollsy.item.domain.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item,Long>, ItemRepositoryCustom {
    Page<Item> findItemsByCategoryId(Long id, Pageable pageable);
    Page<Item> findAllByCategoryIdIn(List<Long> categoryIdList, Pageable pageable);
}

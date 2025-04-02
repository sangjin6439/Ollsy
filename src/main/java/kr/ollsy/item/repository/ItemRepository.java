package kr.ollsy.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.ollsy.item.domain.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item,Long> {
}

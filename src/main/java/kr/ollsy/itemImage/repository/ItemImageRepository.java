package kr.ollsy.itemImage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.ollsy.itemImage.domain.ItemImage;

@Repository
public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {
}

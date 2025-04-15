package kr.ollsy.itemImage.repository;

import org.hibernate.sql.Delete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kr.ollsy.itemImage.domain.ItemImage;

@Repository
public interface ItemImageRepository extends JpaRepository<ItemImage,Long> {
}

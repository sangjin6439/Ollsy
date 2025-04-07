package kr.ollsy.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.ollsy.category.domain.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}

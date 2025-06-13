package kr.ollsy.item.repository;

import static kr.ollsy.item.domain.QItem.item;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import kr.ollsy.item.domain.Item;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Item> findAllOrderByCreateAtDesc(Pageable pageable) {
        List<Item> itemList = jpaQueryFactory
                .selectFrom(item)
                .orderBy(item.createAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .select(item.count())
                .from(item)
                .fetchOne();

        return new PageImpl<>(itemList, pageable, total);
    }

    @Override
    public Page<Item> searchItems(String name, Long categoryId, Integer maxPrice, Integer minPrice, Pageable pageable) {
        List<Item> itemList = jpaQueryFactory
                .selectFrom(item)
                .where(
                        nameContains(name),
                        categoryEq(categoryId),
                        priceLoe(maxPrice),
                        priceGoe(minPrice)
                )
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .select(item.count())
                .from(item)
                .fetchOne();

        return new PageImpl<>(itemList, pageable, total);
    }

    private BooleanExpression nameContains(String name) {
        if (name == null || name.isBlank()) {
            return null;
        }
        return item.name.contains(name);
    }

    private BooleanExpression categoryEq(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        return item.category.id.eq(categoryId);
    }

    private BooleanExpression priceLoe(Integer price) {
        if (price == null) {
            return null;
        }
        return item.price.loe(price);
    }

    private BooleanExpression priceGoe(Integer price) {
        if (price == null) {
            return null;
        }
        return item.price.goe(price);
    }
}
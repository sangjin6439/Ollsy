package kr.ollsy.item.repository;

import static kr.ollsy.item.domain.QItem.*;

import com.querydsl.jpa.impl.JPAQueryFactory;

import org.springframework.stereotype.Repository;

import java.util.List;

import kr.ollsy.item.domain.Item;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Item> findAllOrderByCreateAtDesc() {
        return jpaQueryFactory.selectFrom(item)
                .orderBy(item.createAt.desc())
                .fetch();
    }
}

package kr.ollsy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import kr.ollsy.item.domain.Item;
import kr.ollsy.item.dto.response.ItemListResponse;
import kr.ollsy.item.repository.ItemRepository;
import kr.ollsy.item.service.ItemService;

@SpringBootTest
class OllsyApplicationTests {
    @Autowired
    private ItemRepository itemRepository;

    @Test
    @DisplayName("Fetch전략 Eager일 N+1 발생 테스트")
    void nPlusOneTest() {
        long start = System.nanoTime();

        System.out.println("----------- Item 전체 조회 -----------");
        List<Item> items = itemRepository.findAll();
        System.out.println("----------- Item 전체 조회 완료[쿼리 1개 발생하남??] -----------");

        long end = System.nanoTime();  // 시간 측정 종료
        double elapsedMs = (end - start) / 1_000_000.0;  // 밀리초로 변환

        System.out.println("⏱ 소요 시간: " + String.format("%.2f", elapsedMs) + " ms");
    }

    @Test
    @Transactional
    @DisplayName("Fetch전략 Lazy일 때 N+1 발생 테스트")
    void fetchLazyNPlusOneTest() {
        long start = System.nanoTime();

        System.out.println("----------- Item 전체 조회 -----------");
        List<Item> items = itemRepository.findAll();
        System.out.println("----------- Item 전체 조회 완료 -----------");

        System.out.println("----------- Item에 연관된 Category 조회 -----------");
        for (Item item : items) {
            String name = item.getCategory().getName();
        }
        System.out.println("----------- Item에 연관된 Category 조회 완료-----------");

        long end = System.nanoTime();  // 시간 측정 종료
        double elapsedMs = (end - start) / 1_000_000.0;  // 밀리초로 변환

        System.out.println("⏱ 소요 시간: " + String.format("%.2f", elapsedMs) + " ms");
    }

    @Test
    @Transactional
    @DisplayName("카테고리별 페이징 조회 쿼리 테스트")
    void testFindItemsByCategoryId() {
        long start = System.nanoTime();

        Long categoryId = 3L;
        Pageable pageable = PageRequest.of(0, 10);

        System.out.println("------ Item 페이징 조회 시작 ------");
        Page<Item> page = itemRepository.findItemsByCategoryId(categoryId, pageable);
        List<Item> items = page.getContent();
        System.out.println("------ Item 페이징 조회 완료 ------");

        System.out.println("------ 카테고리 접근 시 Lazy 여부 확인 ------");
        for (Item item : items) {
            System.out.println("카테고리 이름: " + item.getCategory().getName());
        }
        System.out.println("------ 카테고리 접근 완료 ------");

        long end = System.nanoTime();  // 시간 측정 종료
        double elapsedMs = (end - start) / 1_000_000.0;  // 밀리초로 변환

        System.out.println("⏱ 소요 시간: " +String.format("%.2f", elapsedMs) + " ms");
    }

    @Test
    @DisplayName("신상품 조회 성능 측정 테스트 - repository")
    void itemOrderByCreateAtNPlusOneTest() {
        long start = System.nanoTime();
        Page<Item> items = itemRepository.findAllOrderByCreateAtDesc(PageRequest.of(0, 10));
        long end = System.nanoTime();

        System.out.println("⏱ 소요 시간(ms): " + String.format("%.2f", (end - start) / 1_000_000.0));
    }

    @Autowired
    private ItemService itemService;

    @Test
    @DisplayName("신상품 조회 성능 측정 테스트 - service")
    void findItemsByCreatedNPlusOneTest() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createAt").ascending());

        long start = System.nanoTime();

        Page<ItemListResponse> result = itemService.findItemsByCreated(pageable);

        long end = System.nanoTime();
        double elapsedMs = (end - start) / 1_000_000.0;

        System.out.println("⏱ 소요 시간(ms): " + String.format("%.2f", elapsedMs));
    }
}


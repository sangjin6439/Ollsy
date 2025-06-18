package kr.ollsy.order.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import kr.ollsy.global.exception.CustomException;
import kr.ollsy.global.exception.GlobalExceptionCode;
import kr.ollsy.global.util.NicknameGenerator;
import kr.ollsy.item.domain.Item;
import kr.ollsy.item.repository.ItemRepository;
import kr.ollsy.order.dto.request.OrderRequest;
import kr.ollsy.order.repository.OrderRepository;
import kr.ollsy.order_item.dto.request.OrderItemRequest;
import kr.ollsy.user.domain.Role;
import kr.ollsy.user.domain.User;
import kr.ollsy.user.repository.UserRepository;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private List<User> testUserList = new ArrayList<>();
    private final static int REQUEST_NUM = 100;

    @BeforeEach
    void setUser() {
        IntStream.range(0, REQUEST_NUM).forEach(i -> {
            User user = User.builder()
                    .name("user" + i)
                    .nickname(NicknameGenerator.generateNickname())
                    .email("user" + i)
                    .role(Role.USER)
                    .provider("naver")
                    .providerId("user" + i)
                    .build();
            testUserList.add(user);
        });
        userRepository.saveAll(testUserList);
    }

//    @DisplayName("createOrder 메서드에서 동시성이 일어나는 지 테스트")
//    @Test
//    public void CreateOrderConcurrencyTest() {
//
//        ExecutorService executorService = Executors.newFixedThreadPool(32); // 멀티 스레드 환경 구현
//        CountDownLatch latch = new CountDownLatch(REQUEST_NUM); //총 100건의 요청
//
//        for (int i = 0; i < REQUEST_NUM; i++) {
//            int userIndex = i;
//            executorService.submit(() -> {
//                try {
//                    String currentProviderId = testUserList.get(userIndex).getProviderId();
//                    orderService.createOrder(currentProviderId, new OrderRequest(Collections.singletonList(new OrderItemRequest(1L, 2))));
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//        Item itemStock = itemRepository.findById(1L).orElseThrow();
//        assertThat(itemStock.getStock()).isEqualTo(0);
//    }

    @DisplayName("비관락을 사용하여 createOrder 메서드 동시성 제어")
    @Test
    public void CreateOrderConcurrencyTestWithPessimistic() throws InterruptedException { // 메서드명 변경
        // 이 테스트는 Item.id 1, 초기 재고가 100개
        // 각 요청은 Item 1L에 대해 quantity 2를 주문
        // 따라서 50개 요청이 성공하고, 51번째 요청부터는 재고 부족 예외가 발생

        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(REQUEST_NUM);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0); // 실패 카운트 추가

        for (int i = 0; i < REQUEST_NUM; i++) {
            final int userIndex = i;
            String currentProviderId = testUserList.get(userIndex).getProviderId();

            executorService.submit(() -> {
                try {
                    orderService.createOrder(
                            currentProviderId,
                            new OrderRequest(Collections.singletonList(new OrderItemRequest(1L, 2))) // Item ID 1L, quantity 2
                    );
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    System.out.println("재고 없음 '" + GlobalExceptionCode.ITEM_NOT_ENOUGH_STOCK.getErrorCode() + "'");
                    failCount.incrementAndGet();
                }
                finally {
                    latch.countDown();
                }
            });
        }
        latch.await(); // 모든 작업이 동기화되도록 함. 필수!

        Item finalItem = itemRepository.findById(1L).orElseThrow();

        System.out.println("--- 동시성 테스트 결과 (비관적 락 적용, 재고 100, 요청당 2개) ---");
        System.out.println("초기 재고: 100");
        System.out.println("총 주문 시도 요청 수: " + REQUEST_NUM);
        System.out.println("각 요청당 주문 수량: 2");
        System.out.println("성공한 주문 수: " + successCount.get());
        System.out.println("재고 부족으로 실패한 주문 수: " + failCount.get());
        System.out.println("최종 재고: " + finalItem.getStock());

        assertThat(finalItem.getStock()).isEqualTo(0);
    }
}
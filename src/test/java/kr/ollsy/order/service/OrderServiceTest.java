package kr.ollsy.order.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

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

    @Autowired
    private OrderRepository orderRepository;

    private List<User> testUserList = new ArrayList<>();
    private final static int REQUEST_NUM = 100;

    @BeforeEach
    void setUser() {
        IntStream.range(0, REQUEST_NUM).forEach(i -> {
            User user = User.builder()
                    .name("user"+i)
                    .nickname(NicknameGenerator.generateNickname())
                    .email("user"+i)
                    .role(Role.USER)
                    .provider("naver")
                    .providerId("user" + i)
                    .build();
            testUserList.add(user);
        });
        userRepository.saveAll(testUserList);
    }

    @Test
    public void 동시에_100건_요청() {

        ExecutorService executorService = Executors.newFixedThreadPool(32); // 멀티 스레드 환경 구현
        CountDownLatch latch = new CountDownLatch(REQUEST_NUM); //총 100건의 요청

        for (int i = 0; i < REQUEST_NUM; i++) {
            int userIndex = i;
            executorService.submit(() -> {
                try {
                    String currentProviderId = testUserList.get(userIndex).getProviderId();
                    orderService.createOrder(currentProviderId, new OrderRequest(Collections.singletonList(new OrderItemRequest(1L, 2))));
                } finally {
                    latch.countDown();
                }
            });
        }
        Item itemStock = itemRepository.findById(1L).orElseThrow();
        assertEquals(0, itemStock.getStock());
    }
}
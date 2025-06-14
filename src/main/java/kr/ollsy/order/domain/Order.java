package kr.ollsy.order.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import kr.ollsy.global.entity.DateEntity;
import kr.ollsy.order_item.domain.OrderItem;
import kr.ollsy.user.domain.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@Table(name = "orders")
public class Order extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_Id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) //OrderItem과 생명주기가 같음
    private List<OrderItem> orderItems = new ArrayList<>();

    private int totalPrice;

    public void setUser(User user) {
        this.user = user;
        user.getOrders().add(this);
    }

    //편의 메서드
    public void addOrderItem(OrderItem orderItem) {
        orderItem.setOrder(this);
    }
}
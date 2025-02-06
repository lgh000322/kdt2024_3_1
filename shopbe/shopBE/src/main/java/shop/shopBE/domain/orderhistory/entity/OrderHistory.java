package shop.shopBE.domain.orderhistory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.shopBE.domain.destination.entity.Destination;
import shop.shopBE.domain.member.entity.Member;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class OrderHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 상품의 총 가격
    private int orderPrice;

    // 총 주문한 상품의 수
    private int orderCount;

    // 주문 날짜
    private LocalDateTime createdAt;

    
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE) //caseCade: orderHistory삭제시 destinaion도 자동삭제
    @JoinColumn(name = "destination_id")
    private Destination destination;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //삭제시 배송지 ID 값을 Null 로 설정
    public void removeDestination() {
        this.destination = null;
    }
}

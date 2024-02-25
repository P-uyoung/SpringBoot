package jpabook.jpashop.repository;

import jpabook.jpashop.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderSearch {

    // 검색 조건 객체로써, 사용자의 입력이나 특정 비즈니스 로직에 의해 결정되는 검색 조건을 관리하는 객체이다. 동적쿼리에 사용될 것임.

    private String memberName;
    private OrderStatus orderStatus;  // 주문 상태 [ORDER, CANCEL]
}

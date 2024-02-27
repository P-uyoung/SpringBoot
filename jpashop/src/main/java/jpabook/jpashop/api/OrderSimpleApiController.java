package jpabook.jpashop.api;


import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/*
*  주문 + 배송정보 + 회원을 조회하는 API
* xToOne (ManyToOne, OneToOne) 관계 최적화
* Order
* Order -> Member
* Order -> Delivery
*/

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    @GetMapping("api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
//        return all;
        for (Order order : all) {
            order.getMember().getName(); // Lazy 강제 초기화
            order.getDelivery().getAddress(); // Lazy 강제 초기화
        }
        return all;
    }

    @GetMapping("api/v2/simple-orders")
    public Result ordersV2() {
        // ORDER 2개
        // N +1 -> 1 + 회원 N + 배송 N = 5번
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        List<SimpleOrderDto> collect = all.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return new Result(collect);

    }

    // Fetch Join을 쓰자!
    @GetMapping("api/v3/simple-orders")
    public Result ordersV3() {
        List<Order> all = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> collect = all.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
        return new Result(collect);

    }

    // DTO 로 바로 받아오자!
    @GetMapping("api/v4/simple-orders")
    public Result ordersV4() {
        List<OrderSimpleQueryDto> orderDtos = orderSimpleQueryRepository.findOrderDtos();
        return new Result(orderDtos);

    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();  // LAZY 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();  // LAZY 초기화
        }
    }
}

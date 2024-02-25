package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookForm {

    // 상품의 공통 속성
    private Long id;  // 상품 변경을 위해 필요
    private String name;
    private int price;
    private int stockQuantity;

    // Book 장르의 속성
    private String author;
    private String isbn;
}

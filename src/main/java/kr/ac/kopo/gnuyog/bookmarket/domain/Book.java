package kr.ac.kopo.gnuyog.bookmarket.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Setter
@Getter

public class Book
{
    private String bookId; // 도서 id
    private String name; // 도서 제목
    private BigDecimal unitPrice; // 단가
    private String author; // 저자
    private String description; // 설명
    private String publisher; // 출판사
    private String category; // 분류
    private long unitsInStock; // 재고 수량
    private String releaseDate; // 출판일
    private String condition; // 신규도서 or 중고도서 or 전자책(도서 상태)
}

// @Data, @Getter, @Setter를 넣으면 Book 클래스의 모든 멤버 변수의 Setter()와 Getter()메서드가 추가됨

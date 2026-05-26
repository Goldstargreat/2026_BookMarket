package kr.ac.kopo.gnuyog.bookmarket.domain;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
@Setter
@Getter

public class Book
{
    @Pattern(regexp = "isbn[0-9a-zA-Z]+")
    private String bookId; // 도서 id
    @Size(min = 4, max =50)
    private String name; // 도서 제목
    @Min(value = 0)
    @Digits(integer = 8, fraction = 2)
    @NotNull
    private BigDecimal unitPrice; // 단가
    private String author; // 저자
    private String description; // 설명
    private String publisher; // 출판사
    private String category; // 분류
    private long unitsInStock; // 재고 수량
    private String releaseDate; // 출판일
    private String condition; // 신규도서 or 중고도서 or 전자책(도서 상태)
    private String fileName; // 도서 이미지 파일 이름
    private MultipartFile bookImage; // 도서 이미지 파일 객체
}

// @Data, @Getter, @Setter를 넣으면 Book 클래스의 모든 멤버 변수의 Setter()와 Getter()메서드가 추가됨

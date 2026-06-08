package kr.ac.kopo.gnuyog.bookmarket.domain;

import jakarta.validation.constraints.*;
import kr.ac.kopo.gnuyog.bookmarket.validator.BookId;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
@Setter
@Getter

public class Book
    //  책을 정의함. 책에 대한 정보를 담는 틀.
{
    @BookId // 중복 체크 어노테이션
    @Pattern(regexp = "isbn[0-9a-zA-Z] + ", message = "{Pattern.book.bookId}")
    // 오류 메시지는 messages.properties 파일에서 가져온다
    private String bookId; // 도서 id

    @Size(min = 4, max = 50, message = "{Size.book.name}")
    // 책 제목은 4자 이상 50자 이하여야 함. 위반 시 오류 메시지 표시.
    private String name; // 도서 제목

    @Min(value = 0, message = "{Min.book.unitPrice}")
    // 가격은 0 이상이어야 해 (음수 불가)
    @Digits(integer = 8, fraction = 2, message = "{Digits.book.unitPirce}")
    // 정수 8자리, 소수 2자리까지 허용
    @NotNull(message = "{NotNull.book.unitPrice}")
    private BigDecimal unitPrice; // 단가

    private String author; // 저자
    private String description; // 설명
    private String publisher; // 출판사
    private String category; // 분류
    private long unitsInStock; // 재고 수량
    private String releaseDate; // 출판일
    private String condition; // 신규도서 or 중고도서 or 전자책(도서 상태)
    // 저자, 설명, 출판사, 분류, 재고량, 출판일, 상태. 검증 어노테이션이 없으므로 아무 값이나 들어와도 됨.

    private String fileName; // 업로드된 도서 이미지 파일의 이름 저장
    private MultipartFile bookImage; // 실제로 업로드 된 도서 이미지 파일 객체를 담는다. 서버에 저장한다.
}

// @Data, @Getter, @Setter를 넣으면 Book 클래스의 모든 멤버 변수의 Setter()와 Getter()메서드가 추가됨

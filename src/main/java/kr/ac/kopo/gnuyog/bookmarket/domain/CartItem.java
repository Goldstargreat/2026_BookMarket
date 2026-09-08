package kr.ac.kopo.gnuyog.bookmarket.domain;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
public class CartItem
{
    private Book book; // 도서 정보
    private int quantity; // 수량
    private BigDecimal totalPrice; // 단위 가격 * 수량

    public CartItem(Book book)
    {
        this.book = book;
        quantity = 1;
        totalPrice = book.getUnitPrice();
    }
    public void setBook(Book book)
    {
        this.book = book;
        updateTotalPrice();
    }
    // → book 필드의 setter를 직접 재정의(원래 @Data가 자동 생성해줄 것을 오버라이드함).
    // 책을 변경하면 updateTotalPrice()를 호출해서 소계 금액.도 다시 계산해줍니다.
    // (Lombok이 자동 생성하는 단순 setter와 달리, 추가 동작이 필요해서 직접 작성한 것)

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
        updateTotalPrice();
    }
    public void updateTotalPrice()
    {
        totalPrice = book.getUnitPrice().multiply(new BigDecimal(quantity));
    }
    // → 소계 재계산 메서드.
    // book.getUnitPrice() → 책의 단가(BigDecimal)
    // .multiply(new BigDecimal(quantity)) → 단가에 수량을 곱함 (BigDecimal은 * 연산자를 못 쓰므로 multiply() 메서드 사용)
    // 결과를 totalPrice에 저장
}


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

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
        updateTotalPrice();
    }
    public void updateTotalPrice()
    {
        totalPrice = book.getUnitPrice().multiply(new BigDecimal(quantity));
    }
}


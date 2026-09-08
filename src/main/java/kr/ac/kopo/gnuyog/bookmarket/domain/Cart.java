package kr.ac.kopo.gnuyog.bookmarket.domain;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
@ToString
// → toString()을 다시 생성해줍니다
// (사실 @Data에 이미 포함되어 있어서 중복이지만, 명시적으로 표시한 것으로 보입니다).
public class Cart
{
    private String cartId;
    private Map<String, CartItem> cartItems;
    // → 장바구니에 담긴 상품들을 저장하는 맵.
    // 키는 String(보통 상품/도서의 ID), 값은 CartItem(그 상품의 정보+수량+합계).
    private BigDecimal grandTotal;

    public Cart()
    {
        cartItems = new HashMap<String, CartItem>();
        // cartItems를 빈 HashMap으로 초기화 (담긴 상품이 없는 상태로 시작)
        grandTotal = new BigDecimal(0);
    }

    public Cart(String cartId)
    {   this();
        // this(); → 위에 있는 기본 생성자를 먼저 호출해서 cartItems와 grandTotal을 초기화
        this.cartId = cartId;
        // this.cartId = cartId; → 전달받은 cartId를 필드에 저장
    }
}

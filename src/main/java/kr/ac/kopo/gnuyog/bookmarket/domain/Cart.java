package kr.ac.kopo.gnuyog.bookmarket.domain;
// Cart는 장바구니 전체를 의미해요
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
@ToString
public class Cart
{
    private String cartId; // 장바구니 번호(세션 ID를 사용)
    private Map<String, CartItem> cartItems; // 담긴 책들 키: 책ID, 값: CartItem
    private BigDecimal grandTotal;  // 총액

    // 왜 Map일까요? 같은 책을 또 담을 때 "이미 있는지" 바로 확인해야 하는데,
    // 책ID를 키로 쓰면 containsKey로 한 번에 확인됩니다.

    public Cart()
    {
        cartItems = new HashMap<String, CartItem>();
        grandTotal = new BigDecimal(0);
    }

    public Cart(String cartId)
    {
        this();
        // "같은 클래스의 다른 생성자를 호출한다"는 뜻입니다. 여기서는 위의 Cart()가 실행됩니다
        // 그리하면 cartItems와 grandTotal 초기화가 먼저 끝납니다
        this.cartId = cartId;
        // 여기서 this.cartId는 객체가 가진 필드, 오른쪽 cartId는 매개변수입니다.
        // 이름이 같아서 구분하려고 this.를 붙입니다.
        //결과적으로 전달받은 값을 필드에 저장합니다.
    }

    public void updateGrandTotal()
    {
        grandTotal = new BigDecimal(0);
        for (CartItem item: cartItems.values())
        {
            grandTotal = grandTotal.add(item.getTotalPrice());
        }
    }

    public void addCartItem(CartItem item)
    {
        String bookId = item.getBook().getBookId();

        if(cartItems.containsKey(bookId)) // 이미 담은 책이면
        {
            CartItem cartItem = cartItems.get(bookId);
            cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity()); // 수량만 증가
            cartItems.put(bookId, cartItem);
        } else
        {
            cartItems.put(bookId, item); // 새 책이면 추가
        }

        updateGrandTotal(); // 총액 다시 계산
    }

    public void removeCartItem(CartItem item)
    {
        String bookId = item.getBook().getBookId();
        cartItems.remove(bookId);
        updateGrandTotal();
    } // removeCartItem은 Map에서 책ID로 지우고 총액을 다시 계산합니다.
}
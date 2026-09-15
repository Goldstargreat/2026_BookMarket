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
    // Map은 "열쇠(key) - 값(value)" 짝을 저장하는 자료구조예요. 여기서는
    // key = 책의 ID (String)
    // value = CartItem (그 책을 몇 권 담았는지 등의 정보)
    private BigDecimal grandTotal;

    public Cart()
    {
        cartItems = new HashMap<String, CartItem>();
        grandTotal = new BigDecimal(0);
    }
    // 기본 생성자. new Cart()로 객체를 만들면 자동으로 실행됩니다. 빈 Map과 0원짜리 합계로 시작하겠다는 뜻.

    public Cart(String cartId)
    {   this();
        // this(); → 위에 있는 기본 생성자를 먼저 호출해서 cartItems와 grandTotal을 초기화
        this.cartId = cartId;
        // this.cartId = cartId; → 전달받은 cartId를 필드에 저장
    }

    public void addCartItem(CartItem item)
    {
        String bookId = item.getBook().getBookId();
        if(cartItems.containsKey(bookId))
        {
            CartItem cartItem = cartItems.get(bookId);
            cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
            cartItems.put(bookId, cartItem);
        } else
        {
            cartItems.put(bookId, item);
        }
        updateGrandTotal(); // 수량이 바뀌었으니 합계 금액도 다시 계산해줍니다.
    }
    public void removeCartitem(CartItem item) {
        String bookId = item.getBook().getBookId();
        cartItems.remove(bookId);
        updateGrandTotal();
    }

    public void updateGrandTotal()
    {
        grandTotal = new BigDecimal(0);
        for(CartItem item: cartItems.values())
        {
            grandTotal = grandTotal.add(item.getTotalPrice());
        }
    }
    // 합계를 0부터 다시 계산합니다. cartItems.values()는 Map 안에 있는 값(CartItem)들만 순서대로 꺼내는 것 (key는 무시).
    // 각 CartItem의 총 가격(getTotalPrice() — 아마 단가×수량)을 하나씩 더해서 grandTotal을 완성합니다.

}


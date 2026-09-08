package kr.ac.kopo.gnuyog.bookmarket.repository;

import kr.ac.kopo.gnuyog.bookmarket.domain.Cart;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
// 이 클래스를 스프링이 관리하는 **빈(Bean)**으로 등록합니다.
// 스프링이 이 클래스를 자동으로 객체로 만들어서(싱글톤으로) 필요한 곳에 주입(DI)해줄 수 있게 됩니다.
public class CartRepositoryImpl implements CartRepository
    // CartRepositoryImpl 클래스가 위에서 본 CartRepository 인터페이스를 구현한다고 선언.
        // 즉 create, read 메서드를 반드시 실제로 작성해야 합니다.
{
    private Map<String, Cart> listofCarts;
    // 필드(멤버 변수) 선언. String(cartId)을 키로, Cart 객체를 값으로 저장하는 Map입니다.
    // 즉 메모리 상에 장바구니들을 저장하는 저장소 역할. (DB 대신 임시로 메모리에 저장하는 방식)

    public CartRepositoryImpl()
    {
        listofCarts = new HashMap<String, Cart>();
    }
    // 생성자. 이 클래스의 객체가 만들어질 때(스프링이 빈을 생성할 때)
    // listofCarts를 비어있는 HashMap으로 초기화합니다.

    @Override
    public Cart create(Cart cart)
    // 인터페이스의 create 메서드를 실제로 구현. @Override는 "부모(인터페이스)의 메서드를 재정의(구현)한다"는 표시로,
    // 오타나 시그니처 불일치를 컴파일러가 잡아주게 해줍니다.
    {
        if(listofCarts.keySet().contains(cart.getCartId()))
        {
            throw new IllegalArgumentException(String.format("장바구니를 새로 생성할 수 없습니다. 장바구니 %s id가 존재합니다.", cart.getCartId()));
        }
        // listofCarts의 모든 키(=cartId들)를 꺼내서, 지금 넣으려는 cart의 cartId가 이미 존재하는지 확인합니다.
        // 이미 존재하면 → 같은 id로 새 장바구니를 또 만들 수 없으니 IllegalArgumentException(잘못된 인자 예외)을 던집니다.
        // 메시지는 한글로 "장바구니 %s id가 존재합니다"처럼 String.format으로 cartId를 끼워 넣어 출력합니다.
        listofCarts.put(cart.getCartId(), cart);
        return cart;
        // 문제없으면 Map에 cartId를 키로, cart 객체를 값으로 저장(put)하고, 저장한 cart를 그대로 반환합니다.
    }
    @Override
    public Cart read(String cartId)
    {
        return listofCarts.get(cartId);
    }
    // read 메서드 구현. Map에서 cartId에 해당하는 Cart를 찾아 반환. 없으면 null을 반환합니다
    // (예외 처리는 없음 — 나중에 개선 포인트가 될 수 있어요).
}

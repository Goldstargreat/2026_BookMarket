package kr.ac.kopo.gnuyog.bookmarket.service;

import kr.ac.kopo.gnuyog.bookmarket.domain.Cart;
import kr.ac.kopo.gnuyog.bookmarket.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
// → 스프링에게 "이 클래스는 서비스 계층의 컴포넌트다"라고 알려주고,
// 스프링 컨테이너가 이 클래스의 객체를 자동으로 생성·관리하게 합니다.
public class CartServiceImpl implements CartService
{
    @Autowired
    private CartRepository cartRepository;
    // → 스프링이 CartRepository 타입의 빈을 찾아서 자동으로 cartRepository 필드에 주입(연결)해줍니다.
    // 개발자가 직접 new로 객체를 생성하지 않아도 됩니다.

    @Override
    public Cart create(Cart cart)
    {
        return cartRepository.create(cart);
    }
    // CartService 인터페이스의 create 메서드를 재정의(@Override)합니다. 전달받은 cart 객체를
    // cartRepository의 create 메서드에 넘겨 실제 저장 처리를 위임하고, 그 결과(저장된 Cart)를 그대로 반환합니다.

    @Override
    public Cart read(String cartId)
    {
        return cartRepository.read(cartId);
    }
    // read 메서드를 재정의합니다.
    // cartId(문자열, 예: 세션 ID)를 받아 cartRepository의 read 메서드에 조회를 위임하고,
    // 찾은 Cart 객체를 반환합니다.
}

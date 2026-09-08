package kr.ac.kopo.gnuyog.bookmarket.repository;

import kr.ac.kopo.gnuyog.bookmarket.domain.Cart;

public interface CartRepository
{
    Cart create(Cart cart);
    // Cart 객체를 받아서 새로 저장하고, 저장된 Cart를 반환하는 메서드 선언. 구현부(중괄호 {})가 없죠
    // — 인터페이스라서 실제 구현은 이걸 implements하는 클래스가 담당합니다.
    Cart read(String cartId);
    // cartId(문자열)를 받아서 해당 장바구니를 찾아 반환하는 메서드 선언.
    //즉 이 인터페이스는 "장바구니 저장소는 최소한 create와 read 기능을 가져야 한다"는 규칙만 정의합니다.
}

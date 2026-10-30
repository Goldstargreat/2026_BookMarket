package kr.ac.kopo.gnuyog.bookmarket.repository;

import kr.ac.kopo.gnuyog.bookmarket.domain.Book;
import kr.ac.kopo.gnuyog.bookmarket.domain.Cart;
import kr.ac.kopo.gnuyog.bookmarket.domain.CartItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

// [수정] 예전에는 HashMap(메모리)에 저장해서 서버를 끄면 장바구니가 사라졌다.
//        → BookRepositoryImpl처럼 JdbcTemplate으로 MySQL DB에 저장한다.
//        테이블은 resources/schema.sql에서 만든다.
//          cart      : 장바구니 1개 = 1행 (cart_id, grand_total)
//          cart_item : 장바구니에 담긴 책 1권 = 1행 (cart_id, book_id, quantity)
@Repository
public class CartRepositoryImpl implements CartRepository
{
    private final JdbcTemplate template;
    private final BookRepository bookRepository;   // 담긴 책의 정보(제목, 가격 등)를 가져올 때 사용

    public CartRepositoryImpl(JdbcTemplate template, BookRepository bookRepository)
    {
        this.template = template;
        this.bookRepository = bookRepository;
    }

    // 그 번호의 장바구니가 DB에 있는지 확인한다
    private boolean exists(String cartId)
    {
        Integer count = template.queryForObject(
                "select count(*) from cart where cart_id = ?", Integer.class, cartId);
        return count != null && count > 0;
    }

    @Override
    public Cart create(Cart cart)
    {
        if (exists(cart.getCartId()))
        {
            throw new IllegalArgumentException(String.format("장바구니를 새로 생성할 수 없습니다. 장바구니 %s id가 이미 존재합니다.", cart.getCartId()));
        }

        template.update("insert into cart (cart_id, grand_total) values (?, ?)",
                cart.getCartId(), cart.getGrandTotal());

        // 새 장바구니에 처음부터 책이 들어 있다면 그것도 저장한다
        insertCartItems(cart.getCartId(), cart);

        return cart;
    }

    @Override
    public Cart read(String cartId)
    {
        if (!exists(cartId))
        {
            return null;   // 예전처럼 장바구니가 없으면 null을 돌려준다
        }

        Cart cart = new Cart(cartId);

        // 담긴 책들을 한 행씩 읽어서 CartItem으로 만든다
        // queryForList는 결과를 [컬럼이름 → 값] 모양의 Map 목록으로 돌려준다
        List<Map<String, Object>> rows = template.queryForList(
                "select book_id, quantity from cart_item where cart_id = ?", cartId);

        for (Map<String, Object> row : rows)
        {
            String bookId = (String) row.get("book_id");
            int quantity = ((Number) row.get("quantity")).intValue();

            Book book = bookRepository.getBookById(bookId);   // 책 정보는 book 테이블에서 가져온다

            CartItem item = new CartItem(book);   // 수량 1, 소계 = 단가로 만들어진다
            item.setQuantity(quantity);           // 실제 수량으로 바꾸면 소계도 다시 계산된다

            cart.getCartItems().put(bookId, item);
        }

        cart.updateGrandTotal();   // 총액 다시 계산

        return cart;
    }

    @Override
    @Transactional   // 지우고 다시 넣는 도중에 실패하면 전부 원래대로 되돌린다
    public void update(String cartId, Cart cart)
    {
        // [수정] cart.getCartId() → cartId (저장할 때 쓰는 키와 같은 값으로 확인)
        if (!exists(cartId))
        {
            throw new IllegalArgumentException(String.format("장바구니 목록을 갱신할 수 없습니다. 장바구니 %s id가 존재하지 않습니다.", cartId));
        }

        // 가장 쉬운 방법: 예전 항목을 모두 지우고 지금 항목을 다시 넣는다
        template.update("delete from cart_item where cart_id = ?", cartId);
        insertCartItems(cartId, cart);

        template.update("update cart set grand_total = ? where cart_id = ?",
                cart.getGrandTotal(), cartId);
    }

    @Override
    public void delete(String cartId)
    {
        if (!exists(cartId))
        {
            throw new IllegalArgumentException(String.format("장바구니 목록 삭제 불가능. 이유: 해당 장바구니 id(%s)가 존재하지 않음", cartId));
        }

        // cart_item은 schema.sql의 ON DELETE CASCADE 설정 때문에 함께 지워진다
        template.update("delete from cart where cart_id = ?", cartId);
    }

    // 장바구니에 담긴 책들을 cart_item 테이블에 한 줄씩 넣는다
    private void insertCartItems(String cartId, Cart cart)
    {
        for (CartItem item : cart.getCartItems().values())
        {
            template.update("insert into cart_item (cart_id, book_id, quantity) values (?, ?, ?)",
                    cartId, item.getBook().getBookId(), item.getQuantity());
        }
    }
}

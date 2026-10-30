-- 장바구니 테이블 (서버가 시작할 때 실행된다. 이미 있으면 그대로 둔다)

-- 장바구니 1개 = 1행
CREATE TABLE IF NOT EXISTS cart (
    cart_id     VARCHAR(100) PRIMARY KEY,   -- 장바구니 번호(세션 ID)
    grand_total DECIMAL(12, 2)              -- 총액
);

-- 장바구니에 담긴 책 1권 = 1행
CREATE TABLE IF NOT EXISTS cart_item (
    cart_id  VARCHAR(100),                  -- 어느 장바구니의 항목인지
    book_id  VARCHAR(50),                   -- 담은 책 번호
    quantity INT,                           -- 수량
    PRIMARY KEY (cart_id, book_id),         -- 한 장바구니에 같은 책은 한 줄만
    FOREIGN KEY (cart_id) REFERENCES cart (cart_id) ON DELETE CASCADE  -- 장바구니를 지우면 항목도 함께 지워진다
);

function addToCart(bookId) {
    if(confirm("장바구니에 해당 도서를 추가하시겠습니까?"))
    {
        document.addForm.action = "/BookMarket/cart/book/" + bookId;
        document.addForm.submit();
    }
}

function removeFromCart(bookId)
{
    if (confirm("장바구니에서 해당 도서를 삭제하시겠습니까?") == true)
    {
        document.removeForm.action = "/BookMarket/cart/book/" + bookId;  // /cart/remove/ → /cart/book/ 로 수정
        document.removeForm.submit();
        setTimeout('location.reload()', 10);
    }
}
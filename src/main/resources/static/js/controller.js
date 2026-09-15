function addToCart(bookId) {
    if(confirm("장바구니에 해당 도서를 추가하시겠습니까?"))
    {
        document.addForm.action = "/BookMarket/cart/book/" + bookId;
        document.addForm.submit();
    }
}

function removeFromCart(bookId)
{
    document.removeForm.action = "/BookMarket/cart/book/" + bookId;
    document.removeForm.submit();
    setTimeout('location.reload()', 10);
}
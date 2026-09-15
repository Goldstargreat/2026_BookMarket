function addToCart(bookId)
{
    if (confirm("장바구니에 해당 도서를 추가하시겠습니까?") == true)
    {  // 브라우저에 확인/취소 창(confirm)을 띄웁니다. 사용자가 [확인]을 누르면 true가 되어 안쪽 코드가 실행됩니다.
        // [취소]를 누르면 실행되지 않습니다.
        document.addForm.action = "/BookMarket/cart/book/" + bookId;
        // 웹페이지의 addForm이라는 이름의 폼(form)이 전송될 주소(action)를 설정합니다.
        // 클릭한 책의 번호(bookId)를 주소에 붙여서 서버로 보냅니다
        document.addForm.submit();
        setTimeout('location.reload()', 10);
    }
}

function removeFromCart(bookId)
{
    if (confirm("장바구니에서 해당 도서를 삭제하시겠습니까?") == true)
    {
        document.removeForm.action = "/BookMarket/cart/remove/" + bookId;
        // 삭제는 add와 다른 주소(/cart/remove/...)로 보내야 서버에서 구분할 수 있습니다.
        document.removeForm.submit();
        setTimeout('location.reload()', 10);
    }
}
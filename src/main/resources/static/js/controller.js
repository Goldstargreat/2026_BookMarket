function addToCart(bookId)
{
    if (confirm("장바구니에 해당 도서를 추가하시겠습니까?"))
    {
        document.addForm.action = "/BookMarket/cart/book/" + bookId;
        document.addForm.submit();
    }
}

function removeFromCart(bookId)
{
    if (confirm("장바구니에서 해당 도서를 삭제하시겠습니까?"))
    {
        fetch("/BookMarket/cart/book/" + bookId, { method: "DELETE" })
            .then(function (response)
            {
                if (response.ok)
                {
                    location.reload();
                } else
                {
                    alert("삭제에 실패했습니다. (상태 코드: " + response.status + ")");
                }
            })
            .catch(function (error)
            {
                alert("삭제 중 오류가 발생했습니다.");
                console.error(error);
            });
    }
}
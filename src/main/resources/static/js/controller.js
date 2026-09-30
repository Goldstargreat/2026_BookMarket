function addToCart(bookId)
{
    if (confirm("장바구니에 해당 도서를 추가하시겠습니까?"))
    {
        fetch("/BookMarket/cart/book/" + bookId, { method: "PUT" })
            .then(function (response)
            {
                if (response.ok)
                {
                    alert("장바구니에 도서가 추가되었습니다.");
                    location.href = "/BookMarket/cart";
                } else
                {
                    alert("추가에 실패했습니다. (상태 코드: " + response.status + ")");
                }
            })
            .catch(function (error)
            {
                alert("추가 중 오류가 발생했습니다.");
                console.error(error);
            });
    }
}

function removeFromCart(bookId, cartId)
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

function clearCart(cartId)
{
    if (confirm("장바구니에서 모든 도서를 삭제하시겠습니까?"))
    {
        fetch("/BookMarket/cart/" + cartId, { method: "DELETE" })
            .then(function (response)
            {
                if (response.ok)
                {
                    location.reload();
                } else
                {
                    alert("전체 삭제에 실패했습니다. (상태 코드: " + response.status + ")");
                }
            })
            .catch(function (error)
            {
                alert("전체 삭제 중 오류가 발생했습니다.");
                console.error(error);
            });
    }
}
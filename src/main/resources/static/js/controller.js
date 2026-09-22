function addToCart(bookId)
{
    if (confirm("장바구니에 해당 도서를 추가하시겠습니까?"))
    {
        document.addForm.action = "/BookMarket/cart/book/" + bookId;
        document.addForm.submit();
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

// 기존 코드는 document.clearForm.submit()으로 폼을 POST 제출하고,
// 숨겨진 _method=delete 값으로 DELETE 요청인 "척" 흉내내는 방식이었습니다.
// 이 방식은 스프링의 HiddenHttpMethodFilter가 등록되어 있어야만 동작하는데,
// Spring Boot 3.x는 이 필터가 기본적으로 꺼져 있어서 실제로는 405(허용되지 않은 메서드) 에러가 나고 있었습니다.
// 그런데도 setTimeout으로 무조건 location.reload()가 실행되니
// "삭제가 안 됐는데도 화면만 새로고침되는" 것처럼 보였던 것입니다.
//
// removeFromCart와 동일하게 fetch로 DELETE 요청을 직접 보내도록 수정합니다.
// 이 방식은 필터 설정에 의존하지 않으므로 더 안전합니다.
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
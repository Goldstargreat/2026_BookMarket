package kr.ac.kopo.gnuyog.bookmarket.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// 예외가 발생하면 상태 코드 404를 응답
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CategoryException extends RuntimeException
{
    public CategoryException()
    {
        // [수정] 필드에 따로 저장하던 것을 부모(RuntimeException)에 넘김
        //        → exception.getMessage()로 꺼낼 수 있음
        super("요청한 도서 카테고리를 찾을 수 없습니다.");
    }
}
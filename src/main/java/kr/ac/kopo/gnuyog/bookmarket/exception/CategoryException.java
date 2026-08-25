package kr.ac.kopo.gnuyog.bookmarket.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// 예외가 발생하면 상태 코드 404와 오류 메시지를 출력하도록 설정
@ResponseStatus(value = HttpStatus.NOT_FOUND)
// 이 클래스(CategoryException)로 인해 예외가 발생하면,
// Spring MVC가 자동으로 HTTP 404 (Not Found) 상태 코드를 응답에 담아 보내준다.
// 즉, 컨트롤러(Controller)에서 throw new CategoryException()을 하면,
// 별도의 예외 처리 코드 없이도 클라이언트는 404 응답을 받게 된다.
public class CategoryException extends RuntimeException
{ // RuntimeException을 상속받으므로 이 예외는 unchecked exception(비검사 예외) 입니다.
    // 즉, 메서드 시그니처에 throws CategoryException을 명시하지 않아도 되고,
    // 컴파일러가 강제로 try-catch를 요구하지도 않습니다.
    private String errorMessage;
    public CategoryException()
    {
        super();
        this.errorMessage= "요청한 도서 카테고리를 찾을 수 없습니다.";
    }
}




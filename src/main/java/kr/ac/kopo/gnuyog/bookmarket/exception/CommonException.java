package kr.ac.kopo.gnuyog.bookmarket.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice // 모든 컨트롤러에 공통 적용되는 설정
public class CommonException
{
    @ExceptionHandler(value = {RuntimeException.class})
    public ModelAndView handleError(HttpServletRequest request, Exception exception) throws Exception
    {
        // @ResponseStatus가 붙은 예외(CategoryException 등)는 처리하지 않고 다시 던짐
        if (AnnotatedElementUtils.findMergedAnnotation(exception.getClass(), ResponseStatus.class) != null)
            throw exception;
        // 공통 에러 페이지 구성
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", exception);
        mav.addObject("url", request.getRequestURL());
        mav.setViewName("errorCommon");
        return mav;
        // exception: 발생한 예외 객체를 뷰에 전달합니다.
        // url: 예외가 발생한 요청 URL을 전달합니다.
        // errorCommon: 보여줄 뷰 이름입니다. Thymeleaf라면 templates/errorCommon.html이 렌더링됩니다.

    }
}

// @ResponseStatus 예외 = "나는 특별한 에러야"라는 이름표가 붙은 예외

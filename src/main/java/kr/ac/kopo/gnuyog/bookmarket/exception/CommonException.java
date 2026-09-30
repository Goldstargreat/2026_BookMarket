package kr.ac.kopo.gnuyog.bookmarket.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class CommonException
{
    @ExceptionHandler(value = {RuntimeException.class})
    public ModelAndView handleError(HttpServletRequest request, Exception exception) throws Exception
    {
        // @ResponseStatus가 붙은 예외(CategoryException 등)는 처리하지 않고 다시 던짐
        if (AnnotatedElementUtils.findMergedAnnotation(exception.getClass(), ResponseStatus.class) != null)
            throw exception;

        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", exception);
        mav.addObject("url", request.getRequestURL());
        mav.setViewName("errorCommon");
        return mav;
    }
}

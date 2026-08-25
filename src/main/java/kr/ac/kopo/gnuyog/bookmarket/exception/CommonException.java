package kr.ac.kopo.gnuyog.bookmarket.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class CommonException
{
    @ExceptionHandler(value = {RuntimeException.class})
    public ModelAndView handleError(HttpServletRequest request, Exception exception)
    {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", exception);
        mav.addObject("url", request.getRequestURL());
        mav.setViewName("errorCommon");
        return mav;
    }
}

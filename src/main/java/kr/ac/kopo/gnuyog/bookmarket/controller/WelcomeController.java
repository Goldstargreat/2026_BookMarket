package kr.ac.kopo.gnuyog.bookmarket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // 이 클래스는 웹 요청을 받는 컨트롤러이다.
public class WelcomeController
{
    @GetMapping("/home") // 특정 주소로 들어온 GET 요청을 메서드에 연결하는 어노테이션
    public String welcomeMethod()
    {
        return "welcome";
    }
    // 문자열 "welcome"을 반환하면, 스프링(Thymeleaf)이 templates/welcome.html 파일을 찾아 화면으로 보여줍니다.
    // 반환값 = 보여줄 HTML 파일 이름(확장자 제외) 입니다.
}

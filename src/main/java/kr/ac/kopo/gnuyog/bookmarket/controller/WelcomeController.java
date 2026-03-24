package kr.ac.kopo.gnuyog.bookmarket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {
    @GetMapping("/home")
    public String welcomeMethod(){
        return "welcome";
    }
}
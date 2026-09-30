package kr.ac.kopo.gnuyog.bookmarket.config;

import kr.ac.kopo.gnuyog.bookmarket.validator.BookValidator;
import kr.ac.kopo.gnuyog.bookmarket.validator.UnitsInstockValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidationConfig
{
     @Autowired
     UnitsInstockValidator unitsInstockValidator;

     @Bean
     public BookValidator bookIdValidator()
     {
         BookValidator bookValidator = new BookValidator();
         bookValidator.springValidators.add(unitsInstockValidator);
         return bookValidator;
     }
 }

 // BookValidator(못 본 파일, 추정)는 여러 검사기를 모아 한꺼번에 실행하는
// 검사기 묶음입니다.
// 여기서 UnitsInstockValidator(재고 검사)를 묶음에 추가해서
// 하나의 Bean으로 등록합니다. BookController가 @Autowired로 받는 것이 이 Bean입니다.


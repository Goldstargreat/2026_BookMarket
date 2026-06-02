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


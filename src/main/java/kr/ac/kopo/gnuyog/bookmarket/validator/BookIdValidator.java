package kr.ac.kopo.gnuyog.bookmarket.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kr.ac.kopo.gnuyog.bookmarket.domain.Book;
import kr.ac.kopo.gnuyog.bookmarket.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;

public class BookIdValidator implements ConstraintValidator<BookId, String>
{
    @Autowired
    private BookService bookService;

    @Override
    public void initialize(BookId constraintAnnotation)
    {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String bookId, ConstraintValidatorContext constraintValidatorContext)
    {
        // 도서 id(isbn)가 중복되는 지 체크하려고 한다. 중복되면 false 리턴
        // book 참조 변수: bookId가 중복되면 Book 객체 참조값 반환, bookId가 존재하지 않으면 null 반환
        Book book;
        try {
            book = bookService.getBookById(bookId);
       } catch (RuntimeException e)
        {
           return true;
       } // 중복되는 경우
       if(book != null)
       {
           return false;
       }
        return true;
    }
}

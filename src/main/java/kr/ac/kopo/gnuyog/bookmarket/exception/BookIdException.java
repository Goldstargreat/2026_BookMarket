package kr.ac.kopo.gnuyog.bookmarket.exception;

import lombok.Data;

@Data
@SuppressWarnings("serial") // serialVersionUID 관련 경고를 숨깁니다.
public class BookIdException extends RuntimeException
{
    private String bookId;
    public BookIdException(String bookId)
    {
        this.bookId = bookId;
    }
}

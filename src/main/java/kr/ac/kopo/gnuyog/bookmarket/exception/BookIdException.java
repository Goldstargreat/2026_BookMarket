package kr.ac.kopo.gnuyog.bookmarket.exception;

import lombok.Data;

@Data
@SuppressWarnings("serial")
public class BookIdException extends RuntimeException
{
    private String bookId;
    public BookIdException(String bookId)
    {
        this.bookId = bookId;
    }
}

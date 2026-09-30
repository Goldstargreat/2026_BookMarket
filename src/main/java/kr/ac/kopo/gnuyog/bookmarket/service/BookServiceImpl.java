package kr.ac.kopo.gnuyog.bookmarket.service;

import kr.ac.kopo.gnuyog.bookmarket.domain.Book;
import kr.ac.kopo.gnuyog.bookmarket.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service // 서비스 계층 Bean이라는 표시(처리 로직 계층)
public class BookServiceImpl implements BookService
{
    @Autowired
    // 지금 Service는 Repository에 그대로 넘기기만 합니다.
    // 그래도 계층을 두는 이유는, 나중에 "재고 확인", "할인 계산" 같은
    // 업무 규칙이 생기면 Service에 넣으면 되기 때문입니다.
    private BookRepository bookRepository;

    @Override
    public List<Book> getAllBookList() {
        return bookRepository.getAllBookList();
    }

    @Override
    public Book getBookById(String bookId) {
        Book book = bookRepository.getBookById(bookId);
        return book;
    }

    @Override
    public List<Book> getBookListByCategory(String category) {
        List<Book> booksByCategory = bookRepository.getBookListByCategory(category);
        return booksByCategory;
    }

    @Override
    public Set<Book> getBookListByFilter(Map<String, List<String>> filter) {
        Set<Book> booksByFilter = bookRepository.getBookListByFilter(filter);
        return booksByFilter;
    }

    @Override
    public void setNewBook(Book book) {
        bookRepository.setNewBook(book);
    }


}
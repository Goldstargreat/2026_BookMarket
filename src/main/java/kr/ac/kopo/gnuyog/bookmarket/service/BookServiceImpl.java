package kr.ac.kopo.gnuyog.bookmarket.service;

import kr.ac.kopo.gnuyog.bookmarket.domain.Book;
import kr.ac.kopo.gnuyog.bookmarket.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;
    public List<Book> getAllBookList() {
        return bookRepository.getAllBookList();
    }
}

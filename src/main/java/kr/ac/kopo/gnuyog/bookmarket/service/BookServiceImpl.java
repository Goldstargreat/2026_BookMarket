package kr.ac.kopo.gnuyog.bookmarket.service;

import kr.ac.kopo.gnuyog.bookmarket.domain.Book;
import kr.ac.kopo.gnuyog.bookmarket.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<Book> getAllBookList()
    {
        return bookRepository.getAllBookList();
    }

    @Override
    public Book getBookById(String bookId)
    {
        Book book = bookRepository.getBookById(bookId);
        return  book;
    }


    @Override
    public List<Book> getBookListBycategory(String category)
    {
        List<Book> bookByCategory = bookRepository.getBookListByCategory(category);
        return bookByCategory;
    }

    @Override
    public Set<Book> getBookListByFilter(Map<String, List<String>> filter)
    {
        List<Book> bookByFilter = bookRepository.getBookListByFilter(filter);
        return bookByFilter;
    }

}

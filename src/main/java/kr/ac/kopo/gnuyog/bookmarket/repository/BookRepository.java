package kr.ac.kopo.gnuyog.bookmarket.repository;

import kr.ac.kopo.gnuyog.bookmarket.domain.Book;
import java.util.List;

public interface BookRepository {
  List<Book> getAllBookList();
}

package kr.ac.kopo.gnuyog.bookmarket.repository;

import kr.ac.kopo.gnuyog.bookmarket.domain.Book;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BookRepository
{
  List<Book> getAllBookList(); // 모든 책 목록 반환
  Book getBookById(String bookId); // 특정 ID의 책 1권 조회
  List<Book> getBookListByCategory(String category); // "IT", "소설" 등의 카테고리로 필터링
  Set<Book> getBookListByFilter(Map<String, List<String>> filter); // 여러 조건으로 검색(고급 검색 키- 값 구조)
  void setNewBook(Book book);
}

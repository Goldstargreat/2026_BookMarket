package kr.ac.kopo.gnuyog.bookmarket.repository;
// "책에 대한 데이터 저장/조회 담당" 계층

import kr.ac.kopo.gnuyog.bookmarket.domain.Book;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BookRepository
{
  List<Book> getAllBookList(); // 모든 책 목록 반환하는 메서드 선언
  Book getBookById(String bookId); // 특정 ID의 책 1권 조회하는 메서드 선언
  List<Book> getBookListByCategory(String category); // "IT", "소설" 등의 카테고리로 필터링
  Set<Book> getBookListByFilter(Map<String, List<String>> filter); // 여러 조건으로 검색(고급 검색 키- 값 구조)
  void setNewBook(Book book); // 새 책을 추가하는 메서드 선언. 반환값 없음.
}

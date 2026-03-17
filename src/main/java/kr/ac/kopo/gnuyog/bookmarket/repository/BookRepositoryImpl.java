package kr.ac.kopo.gnuyog.bookmarket.repository;

import kr.ac.kopo.gnuyog.bookmarket.domain.Book;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
    public class BookRepositoryImpl implements BookRepository{
    private List<Book> listofBooks = new ArrayList<Book>();

    public BookRepositoryImpl() {
        Book book1 = new Book();
        book1.setBookId("ISBN1234");
        book1.setName("그리고 아무도 없었다");
        book1.setUnitPrice(new BigDecimal(25000));
        book1.setAuthor("아가사 크리스티");
        book1.setDescription("고립된 섬에 초대받은 10명의 사람들이 동요 '열 꼬마 인디언' 가사에 맞춰 차례대로 살해당하는 클래식 미스터리");
        book1.setPublisher("황금가지");
        book1.setCategory("소설");
        book1.setUnitsInStock(1000);
        book1.setReleaseDate("2014 / 11 / 25");

        Book book2 = new Book();
        book2.setBookId("ISBN5678");
        book2.setName("혼자 공부하는 C언어");
        book2.setUnitPrice(new BigDecimal(23400));
        book2.setAuthor("서현우");
        book2.setDescription("1:1 과외하듯 배우는 프로그래밍 자습서");
        book2.setPublisher("한빛미디어");
        book2.setCategory("it");
        book2.setUnitsInStock(1000);
        book2.setReleaseDate("2023 / 05 / 18");

        Book book3 = new Book();
        book3.setBookId("ISBN9791162245651");
        book3.setName("혼자 공부하는 파이선");
        book3.setUnitPrice(new BigDecimal(19800));
        book3.setAuthor("윤인성");
        book3.setDescription("1:1 과외하듯 배우는 파이선 자습서");
        book3.setPublisher("한빛미디어");
        book3.setCategory("it");
        book3.setUnitsInStock(1000);
        book3.setReleaseDate("2022 / 06 / 01");

        listofBooks.add(book1);
        listofBooks.add(book2);
        listofBooks.add(book3);
}

    @Override
        public List<Book> getAllBookList(){
            return listofBooks;
        }
    }


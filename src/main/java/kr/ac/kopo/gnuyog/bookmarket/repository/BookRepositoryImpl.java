package kr.ac.kopo.gnuyog.bookmarket.repository;

import kr.ac.kopo.gnuyog.bookmarket.domain.Book;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;

@Repository // 스프링이 이 클래스를 데이터 접근 객체로 인식한다
    public class BookRepositoryImpl implements BookRepository{
    private List<Book> listOfBooks = new ArrayList<Book>();

    // 생성자(데이터 초기화) 책을 만들어서 추가함
    public BookRepositoryImpl() {
        Book book1 = new Book();
        book1.setBookId("ISBN1234");
        book1.setName("그리고 아무도 없었다");
        book1.setUnitPrice(new BigDecimal(10800));
        book1.setAuthor("아가사 크리스티");
        book1.setDescription("전 세계 미스터리의 역사를 재창조한 추리 소설의 여왕,\n" +
                "아가사 크리스티를 대표하는 작품만을 모은 에디터스 초이스. 고립된 섬에 초대받은 10명의 사람들이 " +
                "동요 '열 꼬마 인디언' 가사에 맞춰 차례대로 살해당하는 클래식 미스터리");
        book1.setPublisher("황금가지");
        book1.setCategory("소설");
        book1.setUnitsInStock(1000);
        book1.setReleaseDate("2014 / 11 / 25");

        Book book2 = new Book();
        book2.setBookId("ISBN5678");
        book2.setName("혼자 공부하는 C언어");
        book2.setUnitPrice(new BigDecimal(23400));
        book2.setAuthor("서현우");
        book2.setDescription("1:1 과외하듯 배우는 프로그래밍 자습서. 이 책은 C 언어의 핵심 내용을 7단계에 걸쳐 " +
                "반복 학습하면서 자연스럽게 머릿속에 기억되도록 구성했다. " +
                "모든 절에서 [핵심 키워드]와 [시작하기 전에]를 통해 각 절의 주제에 대한 대표 개념을 워밍업한 후, " +
                "본격적인 C 언어 핵심 이론과 실습을 거쳐 마무리에서는 [핵심 포인트]와 [확인 문제]로 한번에 복습한다. " +
                "‘혼자 공부할 수 있는’ 커리큘럼을 그대로 믿고 끝까지 따라가다 보면 " +
                "프로그래밍 공부가 난생 처음인 C 언어 입문자도 무리 없이 책을 끝까지 마칠 수 있다!");
        book2.setPublisher("한빛미디어");
        book2.setCategory("IT교육교재");
        book2.setUnitsInStock(1000);
        book2.setReleaseDate("2023 / 05 / 18");

        Book book3 = new Book();
        book3.setBookId("ISBN9791162245651");
        book3.setName("혼자 공부하는 파이선");
        book3.setUnitPrice(new BigDecimal(19800));
        book3.setAuthor("윤인성");
        book3.setDescription("1:1 과외하듯 배우는 파이선 자습서. 이 책은 파이선의 핵심 내용을 7단계에 걸쳐 반복 학습하면서 " +
                "자연스럽게 머릿속에 기억되도록 구성했습니다. [핵심 키워드]와 [시작하기 전에]를 통해 각 절의 주제에 대한 " +
                "대표 개념을 워밍업한 후, 본격적으로 파이썬 핵심 이론을 배우고 실습합니다. " +
                "마무리에서는 [핵심 포인트]와 [확인 문제]로 한번에 복습합니다. ‘혼자 공부할 수 있는’ 커리큘럼을 그대로 믿고 " +
                "끝까지 따라가다 보면 프로그래밍 공부가 난생 처음인 파이썬 입문자도 무리 없이 책을 끝까지 마칠 수 있습니다!");
        book3.setPublisher("한빛미디어");
        book3.setCategory("IT교육교재");
        book3.setUnitsInStock(1000);
        book3.setReleaseDate("2022 / 06 / 01");

        listOfBooks.add(book1);
        listOfBooks.add(book2);
        listOfBooks.add(book3);
}

    @Override
        public List<Book> getAllBookList()
    {
            return listOfBooks;
    } // 전체 조회

    @Override
    public Book getBookById(String bookId){ // id로 조회하기
        Book book = null;
        for(Book searchBook: listOfBooks){
            if(searchBook != null && searchBook.getBookId() != null && searchBook.getBookId().equals(bookId))
            {
                book = searchBook;
                break;
            }
        }
            if(book == null) // 없으면 예외 처리
            {
                throw new IllegalArgumentException("도서ID가 " + "bookId" + "인 도서는 찾을 수 없습니다!");
            }
            return book;
        }

    @Override
    public List<Book> getBookListByCategory(String category)
    {
        List<Book> bookByCategory = new ArrayList<Book>();
        for(Book searchBook : listOfBooks)
        {
            if(category.equalsIgnoreCase(searchBook.getCategory())) // 대소문자 무시
            {
                bookByCategory.add(searchBook);
            }
        }
        return bookByCategory;
    }

    @Override
    public Set<Book> getBookListByFilter(Map<String, List<String>> filter) // 필터 검색
    {
        Set<Book> booksByPublisher = new HashSet<Book>();
        Set<Book> booksByCategory = new HashSet<Book>();
        Set<String> booksByFilter = filter.keySet();
        if(booksByFilter.contains("publisher"))
        {
            for(String publisherName : filter.get("publisher"))
            {
                for(Book searchBook : listOfBooks)
                {
                    if(publisherName.equalsIgnoreCase(searchBook.getPublisher()))
                    {
                        booksByPublisher.add(searchBook);
                    }
                }
            }
        }
        if(booksByFilter.contains("category"))
        {
            for(String category : filter.get("category"))
            {
                List<Book> list = getBookListByCategory(category);
                booksByCategory.addAll(list);
            }
        }
        booksByCategory.retainAll(booksByPublisher);
        return booksByCategory;
    }

    @Override
    public void setNewBook(Book book) {
        listOfBooks.add(book);
    }
}


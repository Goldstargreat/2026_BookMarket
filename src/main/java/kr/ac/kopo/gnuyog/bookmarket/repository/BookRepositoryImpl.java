package kr.ac.kopo.gnuyog.bookmarket.repository;

import kr.ac.kopo.gnuyog.bookmarket.domain.Book;
import kr.ac.kopo.gnuyog.bookmarket.exception.BookIdException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class BookRepositoryImpl implements BookRepository
{
    private final JdbcTemplate template;

    public BookRepositoryImpl(JdbcTemplate template)
    {
        this.template = template;
    }

    // DB의 한 행(row)을 Book 객체로 바꿔주는 매퍼
    private final RowMapper<Book> mapper = (rs, i) -> {
        Book b = new Book();
        b.setBookId(rs.getString("book_id"));
        b.setName(rs.getString("name"));
        b.setUnitPrice(rs.getBigDecimal("unit_price"));
        b.setAuthor(rs.getString("author"));
        b.setDescription(rs.getString("description"));
        b.setPublisher(rs.getString("publisher"));
        b.setCategory(rs.getString("category"));
        b.setUnitsInStock(rs.getLong("units_in_stock"));
        b.setReleaseDate(rs.getString("release_date"));
        b.setCondition(rs.getString("book_condition"));
        b.setFileName(rs.getString("file_name"));
        return b;
    };

    @Override
    public List<Book> getAllBookList()
    {
        return template.query("select * from book", mapper);
    }

    @Override
    public Book getBookById(String bookId)
    {
        List<Book> list = template.query("select * from book where book_id = ?", mapper, bookId);
        if (list.isEmpty())
        {
            throw new BookIdException(bookId);
        }
        return list.get(0);
    }

    @Override
    public List<Book> getBookListByCategory(String category)
    {
        return template.query("select * from book where lower(category) = lower(?)", mapper, category);
    }

    @Override
    public Set<Book> getBookListByFilter(Map<String, List<String>> filter)
    {
        Set<Book> booksByPublisher = new HashSet<Book>();
        Set<Book> booksByCategory = new HashSet<Book>();

        if (filter.containsKey("publisher"))
        {
            for (String publisherName : filter.get("publisher"))
            {
                booksByPublisher.addAll(template.query(
                        "select * from book where lower(publisher) = lower(?)", mapper, publisherName));
            }
        }
        else
        {
            // [수정] 출판사 조건이 없으면 전체 책을 넣어 둔다 (조건 없음 = 제한 없음)
            booksByPublisher.addAll(getAllBookList());
        }

        if (filter.containsKey("category"))
        {
            for (String category : filter.get("category"))
            {
                booksByCategory.addAll(getBookListByCategory(category));
            }
        }
        else
        {
            // [수정] 분야 조건이 없으면 전체 책을 넣어 둔다
            booksByCategory.addAll(getAllBookList());
        }

        booksByCategory.retainAll(booksByPublisher); // 교집합
        return booksByCategory;
    }

    @Override
    public void setNewBook(Book book)
    {
        template.update(
                "insert into book (book_id, name, unit_price, author, description, publisher, category, units_in_stock, release_date, book_condition, file_name) values (?,?,?,?,?,?,?,?,?,?,?)",
                book.getBookId(), book.getName(), book.getUnitPrice(), book.getAuthor(),
                book.getDescription(), book.getPublisher(), book.getCategory(),
                book.getUnitsInStock(), book.getReleaseDate(), book.getCondition(), book.getFileName());
    }

    // [추가] DB의 재고 수량을 quantity만큼 줄인다
    // where 조건에 "재고 >= 주문 수량"을 넣어서, 두 사람이 동시에 주문해도 재고가 음수가 되지 않는다
    // update는 바뀐 행의 개수를 돌려준다 → 1이면 성공, 0이면 재고 부족(또는 없는 책)
    @Override
    public int decreaseUnitsInStock(String bookId, long quantity)
    {
        return template.update(
                "update book set units_in_stock = units_in_stock - ? where book_id = ? and units_in_stock >= ?",
                quantity, bookId, quantity);
    }
}
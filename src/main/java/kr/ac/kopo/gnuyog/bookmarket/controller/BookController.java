package kr.ac.kopo.gnuyog.bookmarket.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kr.ac.kopo.gnuyog.bookmarket.domain.Book;
import kr.ac.kopo.gnuyog.bookmarket.exception.BookIdException;
import kr.ac.kopo.gnuyog.bookmarket.exception.CategoryException;
import kr.ac.kopo.gnuyog.bookmarket.service.BookService;
import kr.ac.kopo.gnuyog.bookmarket.validator.BookValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller // 요청을 받는 곳 반환한 문자열은 화면(템플릿) 이름으로 해석됨
@RequestMapping("/books") // 이 클래스의 공통 주소
public class BookController
{
    @Autowired
    private BookService bookService;
    // 도서 관련 비즈니스 로직(DB 조회 등)을 처리하는 서비스 객체를 주입받음.

//    @Autowired
//    private UnitsInStockValidator unitsInStockValidator;

    @Autowired
    private BookValidator bookValidator;

    @Value("${file.uploadDir}")
    String fileDir;
    // @Value("${file.uploadDir}")는 application.properties의 값을 변수에 넣어줍니다.

    @RequestMapping(method = RequestMethod.GET)
    public String requestBookList(Model model)
    {
        // (1) 도서 목록: GET /books
        List<Book> listOfBooks = bookService.getAllBookList();
        model.addAttribute("bookList", listOfBooks);
        return "books";
        // 서비스에서 목록을 받아 "bookList"라는 이름으로 Model에 담고,
        // books.html에서 ${bookList}로 꺼내 씁니다.
    }

    // (2) 도서 상세: GET /books/book?id=isbn1001
    @GetMapping("/book")
    public String requestBookById(@RequestParam("id") String bookId, Model model)
    {
        // @RequestParam("id")로 ID를 받아 조회하고
        // "book"이라는 이름으로 담아 book.html을 보여줍니다.
        Book book = bookService.getBookById(bookId);
        model.addAttribute("book", book);
        return "book";
    }

    // (3) 카테고리별: GET /books/{category}
    @GetMapping("/{category}")
    public String requestBooksByCategory(
            @PathVariable("category") String bookCategory, Model model)
    {
        List<Book> booksByCategory = bookService.getBookListByCategory(bookCategory);
        if (booksByCategory == null || booksByCategory.isEmpty())
        {
            throw new CategoryException();
        }
        model.addAttribute("bookList", booksByCategory);
        return "books";
    }

    // (4)필터: GET /books/filter/{bookFilter}
    @GetMapping("/filter/{bookFilter}")
    public String requestBooksByFilter(@MatrixVariable(pathVar = "bookFilter") Map<String, List<String>> bookFilter, Model model)
    { // @MatrixVariable은 /books/filter/publisher=한빛미디어;category=IT교육교재
        // 같은 주소의 ;로 구분된 값을 Map으로 받습니다.
        Set<Book> booksByFilter = bookService.getBookListByFilter(bookFilter);
        model.addAttribute("bookList", booksByFilter);
        return "books";
    }

    // (5) 도서 등록 화면: GET /books/add
    //빈 Book 객체를 "book"으로 담아 addBook.html을 보여줍니다. 폼과 객체를 연결하기 위한 것입니다.
    @GetMapping("/add")
    public String requestAddBookForm(Model model)
    {
        model.addAttribute("book", new Book());
        return "addBook";
    }

    // 도서 등록 처리: POST /books/add
    @PostMapping("/add")
    public String submitAddNewBook(@Valid @ModelAttribute Book book, BindingResult bindingResult)
    {
        if (bindingResult.hasErrors())
            return "addBook";

        MultipartFile bookImage = book.getBookImage();

        // 1. 먼저 이미지가 있는지 확인한다 (null이거나 비어 있으면 건너뜀)
        if (bookImage != null && !bookImage.isEmpty())
        {
            // 2. 이미지가 있다고 확인된 후에만 파일 이름을 꺼낸다
            String saveName = bookImage.getOriginalFilename();
            File saveFile = new File(fileDir, saveName);

            try {
                // 3. 실제 파일 저장
                bookImage.transferTo(saveFile);
            } catch (IOException e)
            {
                throw new RuntimeException("이미지가 업로드 되지 않았습니다.");
            }

            // 4. 저장에 성공한 뒤에 파일 이름을 Book에 기록한다
            book.setFileName(saveName);
        }

        bookService.setNewBook(book);
        return "redirect:/books";
    }

    @ModelAttribute
    public void addAddtributes(Model model)
    {
        model.addAttribute("addTitle", "신규 도서 등록");
    } // @ModelAttribute가 메서드에 붙으면 이 컨트롤러의 모든 요청 처리 전에 자동 실행됩니다.
    // 그래서 어느 화면이든 ${addTitle}("신규 도서 등록")을 쓸 수 있습니다.

    // (8) 이미지 다운로드: GET /books/download?file=...
    @GetMapping("/download")
    public void downloadBookImage(@RequestParam("file") String paramKey, HttpServletResponse response)
    {
        File imgFile = new File(fileDir + paramKey);

        response.setContentType("application/download");
        response.setContentLength((int)imgFile.length());
        response.setHeader("Content-Disposition", "attachment;filename=\"" + paramKey + "\"");

        try {
            OutputStream out = response.getOutputStream();
            FileInputStream fileIn = new FileInputStream(imgFile);
            FileCopyUtils.copy(fileIn, out);
            fileIn.close();
            out.close();
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    @InitBinder
    public void initBinder(WebDataBinder binder)
    {
        binder.setValidator(bookValidator);
        // 이 컨트롤러가 검증할 때 쓸 검사기를 bookValidator로 바꿔줍니다.
        // 이 검사기(추정)는 ValidationConfig에서 UnitsInstockValidator를 추가해서 만든 것입니다.
    }

    @GetMapping("/all")
    public ModelAndView requestAllBooks()
    {
        ModelAndView modelAndView = new ModelAndView();
        // Model 대신 ModelAndView를 씁니다. 데이터(addObject)와 화면 이름(setViewName)을 한 객체에 담아 반환하는 방식입니다.
        List<Book> list = bookService.getAllBookList();
        modelAndView.addObject("bookList", list);
        modelAndView.setViewName("books");
        return modelAndView;
    }
    @ExceptionHandler(value = {BookIdException.class})
    public ModelAndView handleError(HttpServletRequest req, BookIdException exception){
        ModelAndView mav = new ModelAndView();
        mav.addObject("invalidBookId", exception.getBookId());
        mav.addObject("exception", exception);
        mav.addObject("url",req.getRequestURL() + "?" + req.getQueryString());
        mav.setViewName("errorBookId");
        return mav;
    }
}
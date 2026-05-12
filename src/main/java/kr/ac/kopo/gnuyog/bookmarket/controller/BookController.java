package kr.ac.kopo.gnuyog.bookmarket.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import kr.ac.kopo.gnuyog.bookmarket.domain.Book;
import kr.ac.kopo.gnuyog.bookmarket.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/books")
public class BookController
{

    @Autowired
    private BookService bookService;

    @Value("${file.uploadDir}")
    String fileDir;

    @RequestMapping(method = RequestMethod.GET)
    public String requestBookList(Model model)
    {
        List<Book> list = bookService.getAllBookList();
        model.addAttribute("bookList", list);
        return "books";
    }

    @GetMapping("/book")
    public String requestBookbyId(@RequestParam("id") String bookId, Model model)
    {
        Book book = bookService.getBookById(bookId);
        model.addAttribute("book", book);
        return "book";
    }

    @GetMapping("/{category}")
    public String requestBookByCategory(@PathVariable("category") String category, Model model)
    {
        List<Book> booksByCategory = bookService.getBookListBycategory(category);
        model.addAttribute("bookList", booksByCategory);
        return "books";
    }

    @GetMapping("/filter/{bookFilter}")
    public String requestBooksByFilter
            (
            @MatrixVariable(pathVar = "bookFilter") Map<String, List<String>> bookFilter,
            Model model)
    {

        Set<Book> booksByFilter = bookService.getBookListByFilter(bookFilter);
        model.addAttribute("bookList", booksByFilter);

        return "books";
    }

    @GetMapping("/add")
    public String requestAddBookForm() {
        return "addBook";
    }

    @ModelAttribute
    public void addAttributes(Model model)
    {
        model.addAttribute("addTitle", "신규 도서 등록");
    }

    @PostMapping("/add")
    public String submitAddNewBook(@ModelAttribute Book book)
    {
        MultipartFile bookImage = book.getBookImage();

        if (bookImage != null && !bookImage.isEmpty())
        {

            String saveName = bookImage.getOriginalFilename();
            File saveFile = new File(fileDir, saveName);

            try {
                bookImage.transferTo(saveFile);
                book.setFileName(saveName);
            } catch (IOException e)
            {
                throw new RuntimeException("이미지가 업로드 되지 않았습니다.");
            }
        }

        bookService.setNewBook(book);
        return "redirect:/books";
    }

    @GetMapping("/all")
    public ModelAndView requestAllbooks()
    {
        ModelAndView modelAndView = new ModelAndView();

        List<Book> list = bookService.getAllBookList();

        modelAndView.addObject("bookList", list);
        modelAndView.setViewName("books");

        return modelAndView;
    }
}
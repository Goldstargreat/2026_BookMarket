package kr.ac.kopo.gnuyog.bookmarket.controller;

import org.springframework.ui.Model;
import kr.ac.kopo.gnuyog.bookmarket.domain.Book;
import kr.ac.kopo.gnuyog.bookmarket.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService bookService;
    @RequestMapping(method = RequestMethod.GET)
    public String requestBookList(Model model){
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

    @GetMapping("all")
    public ModelAndView requestAllbooks()
    {
      ModelAndView modelAndView = new ModelAndView();
      List<Book> list = bookService.getAllBookList();
      modelAndView.addObject("bookList", list);
      modelAndView.setViewName("books");
      return modelAndView;
    }

    @GetMapping("/{category}")
    public String requestBookByCategory(@PathVariable("category") String category, Model model)
    {
        List<Book> booksByCategory = bookService.getBookListBycategory(category);
        model.addAttribute("bookList", booksByCategory);
        return "books";
    }
    @GetMapping("/filter/{bookFilter}")
    public String requestBooksByFilter(
            @MatrixVariable(pathVar = "bookFilter") Map<String, List<String>> bookFilter,
            Model model)
    {
        Set<Book> booksByFilter = bookService.getBookListByFilter(bookFilter);
        model.addAttribute("bookList", booksByFilter);
        return "books";
    }
}

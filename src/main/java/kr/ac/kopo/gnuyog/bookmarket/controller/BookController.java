package kr.ac.kopo.gnuyog.bookmarket.controller;

import org.springframework.ui.Model;
import kr.ac.kopo.gnuyog.bookmarket.domain.Book;
import kr.ac.kopo.gnuyog.bookmarket.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class BookController {
    @Autowired
    private BookService bookService;
    @RequestMapping(value = "/books", method = RequestMethod.GET)
    public String requestBookList(Model model){
        List<Book> list = bookService.getAllBookList();
        model.addAttribute("bookList", list);
        return "books";
    }
}

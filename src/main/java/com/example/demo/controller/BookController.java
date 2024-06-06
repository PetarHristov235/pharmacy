package com.example.demo.controller;


import com.example.demo.db.entity.BookEntity;
import com.example.demo.db.repository.BookRepository;
import com.example.demo.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final BookRepository bookRepository;
    private List<BookEntity> currentBooks;

    @GetMapping("/")
    public String index(Model model) {
        currentBooks = bookService.findAllBooks();
        model.addAttribute("books", currentBooks);
        return "index";
    }


    @GetMapping("/books/sort")
    public String sortBooksList(@RequestParam String sortBy, Model model) {
        if (currentBooks == null) {
            currentBooks = bookService.findAllBooks();
        }
        currentBooks = bookService.sortBooks(currentBooks, sortBy);
        model.addAttribute("books", currentBooks);
        return "index";
    }

    @GetMapping("/books/filter")
    public String filterBooksList(@RequestParam String filterBy, String filterText, Model model) {
        if (currentBooks == null) {
            currentBooks = bookService.findAllBooks();
        }

        currentBooks = bookService.filterBooks(currentBooks, filterBy, filterText);
        model.addAttribute("books", currentBooks);
        return "index";
    }

    @GetMapping("/random")
    public ModelAndView randomBook() {
        BookEntity randomBook = bookService.getRandomBook();

        if (randomBook != null) {
            return new ModelAndView("redirect:/books/" + randomBook.getId());
        } else {
            return new ModelAndView("redirect:/");
        }
    }

    @GetMapping("/books/{id}")
    public ModelAndView bookDetails(@PathVariable("id") Long id) {
        BookEntity book = bookService.getBookById(id);

        ModelAndView modelAndView = new ModelAndView("bookDetails");

        modelAndView.addObject("book", book);

        return modelAndView;
    }

    @GetMapping("/bookStock")
    public ModelAndView listOrders(Model model) {
        ModelAndView modelAndView = new ModelAndView("booksStock");

        List<BookEntity> allBooks = bookService.findAllBooks();
        modelAndView.addObject("books", allBooks);
        return modelAndView;
    }

    @GetMapping("/addBook")
    public ModelAndView addBook() {
        BookEntity book = new BookEntity();
        ModelAndView modelAndView = new ModelAndView("addBook");
        modelAndView.addObject("book", book);
        return modelAndView;
    }

    @PostMapping("/saveBook")
    public String addBook(@ModelAttribute("book") BookEntity book, @RequestParam("image") MultipartFile image) {
        try {
                if (!image.isEmpty()) {
                    book.setCover(image.getBytes());
                }
            bookService.saveBook(book);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/";
    }

    @GetMapping("/editBook/{id}")
    public ModelAndView editBookForm(@PathVariable(value = "id") long id) {
        BookEntity book = bookService.getBookById(id);
        ModelAndView modelAndView = new ModelAndView("editBook");
        modelAndView.addObject("book", book);
        return modelAndView;
    }

    @PostMapping("/editBook")
    public String editBook(@ModelAttribute BookEntity book,
                           @RequestParam("image") MultipartFile image) {
        try {
            BookEntity existingBook = bookService.getBookById(book.getId());
            if (!image.isEmpty()) {
                existingBook.setCover(image.getBytes());
            }

            existingBook.setBookName(book.getBookName());
            existingBook.setAuthor(book.getAuthor());
            existingBook.setGenre(book.getGenre());
            existingBook.setBookDetails(book.getBookDetails());
            existingBook.setStockCount(book.getStockCount());

            bookService.saveBook(existingBook);
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/editBook/" + book.getId() + "?error";
        }
        return "redirect:/books/" + book.getId();
    }

    @GetMapping(value="/deleteBook/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBookById(id);
        return "redirect:/";
    }

}

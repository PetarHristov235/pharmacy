package com.example.demo.service;

import com.example.demo.db.entity.BookEntity;

import java.util.List;

public interface BookService {
    List<BookEntity> findAllBooks();
    BookEntity getRandomBook();
    BookEntity getBookById(Long id);
    BookEntity saveBook(BookEntity book);
    void deleteBookById(Long id);
    List<BookEntity> filterBooks(List<BookEntity> booksList, String filterBy, String filterText);
    List<BookEntity> sortBooks(List<BookEntity> booksList, String sortBy);
    void decreaseBookStockCount(BookEntity book);
}
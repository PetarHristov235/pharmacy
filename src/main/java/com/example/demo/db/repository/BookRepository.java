package com.example.demo.db.repository;

import com.example.demo.db.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Book;
import java.util.List;
import java.util.Optional;


@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {

    @Query("""
            SELECT b FROM BookEntity b
            WHERE b.bookName = :bookName""")
    BookEntity findBookByBookName(@Param("bookName") String bookName);

    @Query("""
            SELECT b FROM BookEntity b
            WHERE b.genre = :genre""")
    List<BookEntity> findBooksByGenre(@Param("genre") String bookName);

    @Query("""
            DELETE FROM BookEntity b
            WHERE b.bookName = :bookName""")
    void deleteBookByBookName(@Param("bookName") String bookName);

    @Query("""
            SELECT b FROM BookEntity b
            WHERE b.author = :author""")
    List<BookEntity> searchBookByAuthor(@Param("author") String author);

    @Query("""
            SELECT b FROM BookEntity b
            WHERE b.id = :id""")
    Optional<BookEntity> findById(Long id);
}

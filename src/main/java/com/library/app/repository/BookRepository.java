package com.library.app.repository;

import com.library.app.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book , Long> {

    // we have inhereated all CRUD-related methods

    // find book by title

    List<Book> findByTitleContainingIgnoreCase(String title);

    // find by book and author

    List<Book> findByTitleAndAuthorContainingIgnoreCase(String title , String author);

    // perform search by using criteriaBuilder and criteriaQuery



}

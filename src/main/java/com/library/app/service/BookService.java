package com.library.app.service;

import com.library.app.dto.BookDTO;
import com.library.app.entity.Book;

import java.util.List;

public interface BookService {

    BookDTO addBook(BookDTO book);

    List<BookDTO> getAllBooks();

    BookDTO getBookById(Long bookId);

    BookDTO updateBook(BookDTO bookDTO);

    void deleteBook(Long bookId);

    List<BookDTO> findBooksByTitle(String title);


    List<BookDTO> findBooksByTitleAndAuthor(String title , String author);

    List<BookDTO> findBooksByCriteria(String title ,String author ,String isbn ,String barcodeNumber);


}

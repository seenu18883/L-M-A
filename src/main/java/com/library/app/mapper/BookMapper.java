package com.library.app.mapper;

import com.library.app.dto.BookDTO;
import com.library.app.entity.Book;

public class BookMapper {


    public static BookDTO mapToBookDTO(Book book) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId()); // install lombok
        bookDTO.setTitle(book.getTitle());
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setIsbn(book.getIsbn());
        bookDTO.setPublisher(book.getPublisher());
        bookDTO.setYearOfPublication(book.getYearOfPublication());
        bookDTO.setPlaceOfPublication(book.getPlaceOfPublication());
        bookDTO.setNoOfAvailableCopies(book.getNoOfAvailableCopies());
        bookDTO.setBarcodeNumber(book.getBarcodeNumber());
        return bookDTO;
    }

    public static Book mapToBookEntity(BookDTO bookDTO) {
        Book book = new Book();
        book.setId(bookDTO.getId());
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setIsbn(bookDTO.getIsbn());
        book.setPublisher(bookDTO.getPublisher());
        book.setYearOfPublication(bookDTO.getYearOfPublication());
        book.setPlaceOfPublication(bookDTO.getPlaceOfPublication());
        book.setNoOfAvailableCopies(bookDTO.getNoOfAvailableCopies());
        book.setBarcodeNumber(bookDTO.getBarcodeNumber());
        return book;
    }
}

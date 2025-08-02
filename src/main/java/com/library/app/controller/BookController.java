package com.library.app.controller;

import com.library.app.dto.BookDTO;
import com.library.app.entity.Book;
import com.library.app.service.BookService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/books")
@AllArgsConstructor
public class BookController {

    // Logger
    private static  final Logger logger = LoggerFactory.getLogger(BookController.class);

    private BookService bookService;


    /*
    * CRUD = Create ,Read ,Update, Delete
    * C = POST request
    * R = GET  request
    * U = PUT or PATCH request
    * D = DELETE request
    * */



    @PostMapping("addBook")
    // URL:  http://localhost:8080/api/books/addBook

    public ResponseEntity<BookDTO> addBook(@RequestBody BookDTO bookDTO){

        //Logger

         logger.info("Adding a Book....");
        //end

       BookDTO saveBookDTO =   bookService.addBook(bookDTO);
       logger.info("Saved the BookDTO: {}",saveBookDTO);

        return new ResponseEntity<>(saveBookDTO, HttpStatus.CREATED);
    }


    @GetMapping("listAll")

    // URL: http://localhost:8080/api/books/listAll

    public ResponseEntity<List<BookDTO>>getAllBooks(){

      List<BookDTO> allBooks = bookService.getAllBooks();

      return  new ResponseEntity<>(allBooks,HttpStatus.OK);
    }

    @GetMapping("{id}")
// e.g  URL : http://localhost:8080/api/books/1


    public ResponseEntity<BookDTO> getBookById( @PathVariable("id") Long bookId){

       BookDTO bookDTO =   bookService.getBookById(bookId);

       return  new ResponseEntity<>(bookDTO ,HttpStatus.OK);
    }

    @PatchMapping("updateBook/{id}")

    // e.g URL: http://localhost:8080/api/books/updateBook/1

    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id , @RequestBody BookDTO bookDTO){

       bookDTO.setId(id);
       BookDTO updatedBook = bookService.updateBook(bookDTO);

       return  new ResponseEntity<>(updatedBook , HttpStatus.OK);
    }


    @DeleteMapping("deleteBook/{id}")
    // http://localhost:8080/api/books/deleteBook/3
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return new ResponseEntity<>("Book successfully deleted", HttpStatus.OK);
    }

    @GetMapping("search-title")

    //e.g URL: http://localhost:8080/api/books/search-title?title=Lord of the Rings

    public ResponseEntity<List<BookDTO>> searchBooksMyTitle(@RequestParam String title){

       List<BookDTO> books = bookService.findBooksByTitle(title);

       return  new ResponseEntity<>(books , HttpStatus.OK);
    }


    @GetMapping("search-title-author")

    //e.g URL: http://localhost:8080/api/books/search-title-author&title=lord?author=tolk

    public ResponseEntity<List<BookDTO>> searchBooksMyTitleAndAuthor(@RequestParam String title,
                                                             @RequestParam String author){

        List<BookDTO> books = bookService.findBooksByTitleAndAuthor(title ,author);

        return  new ResponseEntity<>(books , HttpStatus.OK);
    }

    @GetMapping("search")

    //e.g URL : http://localhost:8080/api/books/search?title=lord&author=tolk

    public ResponseEntity<List<BookDTO>> searchBooks(

             @RequestParam(required = false) String title,
             @RequestParam(required = false) String author,
             @RequestParam(required = false) String isbn,
             @RequestParam(required = false) String barcodeNumber)
    {

        List<BookDTO> books = bookService.findBooksByCriteria(title,author,isbn,barcodeNumber);

        return  new ResponseEntity<>(books,HttpStatus.OK);
    }






}

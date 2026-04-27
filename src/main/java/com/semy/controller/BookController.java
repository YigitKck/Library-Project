package com.semy.controller;
import com.semy.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import com.semy.entities.Book;
import java.util.List;

@Component
@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookRepository bookRepository;

    @PostMapping
    public Book createBook(@RequestBody Book book){
        return bookRepository.save(book);
    }

    @GetMapping
    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id){
        return bookRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book bookDetails){
        Book book = bookRepository.findById(id).orElse(null);
        if(book != null){
            book.setId(bookDetails.getId());
            return bookRepository.save(book);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    // ID alarak kitabı silmek için
    public void deleteBook(@PathVariable Long id){
        bookRepository.deleteById(id);
    }

    @DeleteMapping("/{title}")
    // Kitap ismi alarak kitabı silmek için
    public void deleteBook(@PathVariable String title){
        Book book = bookRepository.findByTitle(title).orElse(null);
        bookRepository.deleteById(book.getId());
    }
}

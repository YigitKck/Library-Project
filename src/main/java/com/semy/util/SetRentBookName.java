package com.semy.util;
import com.semy.entities.Book;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SetRentBookName {
    private static SetRentBookName instance;
    private Book selectedBook;

    private SetRentBookName() {}

    public static SetRentBookName getInstance() {
        if (instance == null) {
            instance = new SetRentBookName();
        }
        return instance;
    }

}

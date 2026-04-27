package com.semy.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "kitap")
public class Book {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "kitap_adi", nullable=false)
    private String title;
    @Column(name = "yazar", nullable=true)
    private String author;
    @Column(name = "teslim_tarihi", nullable=true)
    // returnDate LocalDate nesnesi olarak dönüyor (string e çevirilecek)
    private LocalDate returnDate;
    @Column(name = "kiralik_mi")
    private boolean isRented=false;
}

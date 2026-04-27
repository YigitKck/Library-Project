package com.semy.service;
import com.semy.entities.Book;
import lombok.Data;
import org.json.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Service
public class BookServices {
    private static RestTemplate restTemplate = new RestTemplate();

    // Google books api dan veri çekme (ana ekrandaki kitap arama yerinden kitap ismi alınıyor)
    public static Book searchBook(String query) {
        String url = "https://www.googleapis.com/books/v1/volumes?q=" + query;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            try {
            JSONObject json = new JSONObject(response.getBody());
            JSONArray items = json.getJSONArray("items");
            JSONObject volumeInfo = items.getJSONObject(0).getJSONObject("volumeInfo");

            Book book = new Book();
            book.setTitle(volumeInfo.getString("title"));
            book.setAuthor(volumeInfo.getJSONArray("authors").join(", "));
            return book;
            } catch (Exception e) {
            throw new RuntimeException("Kitap bulunamadı", e);
        }
    }
}
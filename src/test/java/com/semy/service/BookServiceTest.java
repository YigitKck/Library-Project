package com.semy.service;

import com.semy.entities.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookServiceTest {

    private RestTemplate restTemplateMock;

    @BeforeEach
    void setup() {
        BookServicesTestHelper.setStaticRestTemplate();
        restTemplateMock = BookServicesTestHelper.restTemplateMock;
    }

    @Test
    void testSearchBook_returnsBookWithTitleAndAuthor() {
        String fakeJsonResponse = """
            {
              "items": [
                {
                  "volumeInfo": {
                    "title": "Test Kitabı",
                    "authors": ["Yazar A", "Yazar B"]
                  }
                }
              ]
            }
        """;

        when(restTemplateMock.getForEntity(anyString(), eq(String.class)))
                .thenReturn(ResponseEntity.ok(fakeJsonResponse));

        Book result = BookServices.searchBook("Test");

        assertNotNull(result);
        assertEquals("Test Kitabı", result.getTitle());
        assertTrue(result.getAuthor().contains("Yazar A"));
    }
}
 class BookServicesTestHelper {
    public static RestTemplate restTemplateMock;

    public static void setStaticRestTemplate() {
        try {
            restTemplateMock = org.mockito.Mockito.mock(RestTemplate.class);
            Field field = BookServices.class.getDeclaredField("restTemplate");
            field.setAccessible(true);
            field.set(null, restTemplateMock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

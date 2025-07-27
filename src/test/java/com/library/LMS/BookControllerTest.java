package com.library.LMS;

import com.library.common.entity.Book;
import com.library.common.repository.BookRepository;
import com.library.nomember.controller.PublicBookController;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.*;

import java.util.*;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.given;

@WebMvcTest(PublicBookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookRepository bookRepository;

    @Test
    void whenGetAllBooks_thenStatus200AndJsonArray() throws Exception {
        Book b = new Book();
        b.setTitle("Spring in Action");
        b.setAuthor("Craig Walls");


        // Mocking the bookRepository to return a list containing the book
        given(bookRepository.findAll()).willReturn(Collections.singletonList(b));

        mockMvc.perform(get("/api/public/books")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("Spring in Action"))
                .andExpect(jsonPath("$[0].author").value("Craig Walls"));
    }

    @Test
    void whenGetBookById_thenStatus200AndJson() throws Exception {
        Book b = new Book();
        b.setTitle("Spring Boot Up & Running");
        b.setAuthor("Mark Heckler");
        given(bookRepository.findById(anyLong())).willReturn(Optional.of(b));

        mockMvc.perform(get("/api/public/books/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Spring Boot Up & Running"))
                .andExpect(jsonPath("$.author").value("Mark Heckler"));
    }

    @Test
    void whenGetBookById_notFound_thenStatus404() throws Exception {
        given(bookRepository.findById(anyLong()))
                .willThrow(new NoSuchElementException("Book not found"));

        mockMvc.perform(get("/api/public/books/99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}

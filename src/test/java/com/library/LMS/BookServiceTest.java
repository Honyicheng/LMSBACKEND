package com.library.LMS;

import com.library.common.entity.Book;
import com.library.common.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks

    private Book sampleBook;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleBook = new Book();
        sampleBook.setId(1L);
        sampleBook.setTitle("JUnit in Action");
        sampleBook.setAuthor("Craig Walls");
    }

    @Test
    void whenGetAllBooks_thenReturnList() {
        // Arrange
        List<Book> books = Arrays.asList(sampleBook);
        when(bookRepository.findAll()).thenReturn(books);

        // Act
        List<Book> result = bookRepository.findAll();

        // Assert
        assertThat(result).isNotEmpty()
                .hasSize(1)
                .first()
                .extracting(Book::getTitle)
                .isEqualTo("JUnit in Action");
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void whenGetBookById_found_thenReturnBook() {
        // Arrange
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));

        // Act
        Optional<Book> result = bookRepository.findById(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.get().getAuthor()).isEqualTo("Craig Walls");
        verify(bookRepository).findById(1L);
    }

    @Test
    void whenGetBookById_notFound_thenThrow() {
        // Arrange
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> bookRepository.findById(99L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Book not found");
        verify(bookRepository).findById(99L);
    }
}



package com.aivle.bookapp.controller;

import com.aivle.bookapp.domain.Book;
import com.aivle.bookapp.service.BookService;
import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

	private final BookService bookService;

	@GetMapping
	public ResponseEntity<List<Book>> getAllBooks() {
		return ResponseEntity.ok(bookService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Book> getBook(@PathVariable Long id) {
		return bookService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	// Day 2: POST, PUT, DELETE 엔드포인트 구현 예정
	// 생성
	@PostMapping
	public ResponseEntity<Book> createook(@Valid @RequestBody Book book) {
		Book savedBook = bookService.create(book);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Book> updateBook(
			@PathVariable Long id,
			@RequestBody Book request
	) {
		return bookService.update(id, request)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
		boolean deleted = bookService.delete(id);

		if (!deleted) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.noContent().build();
	}
}

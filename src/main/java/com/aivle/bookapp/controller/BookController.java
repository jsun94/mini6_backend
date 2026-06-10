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
	public ResponseEntity<List<Book>> getAllBooks(
			@RequestParam(required = false) String memberName
	) {
		return ResponseEntity.ok(bookService.findAll(memberName));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Book> getBook(@PathVariable Long id) {
		return ResponseEntity.ok(bookService.findById(id));
	}

	// 생성
	@PostMapping
	public ResponseEntity<Book> createook(
			@RequestParam String memberName,
			@Valid @RequestBody Book book
	) {
		Book savedBook = bookService.create(book, memberName);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
	}

	// 수정
	@PatchMapping("/{id}")
	public ResponseEntity<Book> updateBook(
			@PathVariable Long id,
			@RequestBody Book request
	) {
		Book updatedBook = bookService.update(id, request);
		return ResponseEntity.ok(updatedBook);
	}

	// 표지 이미지 URL 저장
	@PatchMapping("/{id}/cover")
	public ResponseEntity<Book> updateCover(
			@PathVariable Long id,
			@RequestBody Book request
	) {
		Book updatedBook = bookService.updateCover(id, request);
		return ResponseEntity.ok(updatedBook);
	}

	// 삭제
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
		boolean deleted = bookService.delete(id);

		if (!deleted) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.noContent().build();
	}
}

package com.aivle.bookapp.service;

import com.aivle.bookapp.domain.Book;
import com.aivle.bookapp.domain.Member;
import com.aivle.bookapp.exception.BookNotFoundException;
import com.aivle.bookapp.repository.BookRepository;
import java.util.List;
import java.util.Optional;

import com.aivle.bookapp.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {

	private final BookRepository bookRepository;
	private final MemberRepository memberRepository;

	// 전체 조회
	public List<Book> findAll() {
		return bookRepository.findAll();
	}

	// 상세 조회
	public Book findById(Long id) {
		return bookRepository.findById(id)
				.orElseThrow(() -> new BookNotFoundException(id));
	}

	// 생성
	@Transactional
	public Book create(Book book) {
		Member member = memberRepository.findById("한봄")
				.orElseGet(() -> {
					Member newMember = new Member();
					newMember.setMemberName("한봄");
					return memberRepository.save(newMember);
				});
		book.setMember(member);
		return bookRepository.save(book);
	}

	// 수정
	@Transactional
	public Book update(Long id, Book request) {
		Book book = findById(id);

		if (request.getTitle() != null) {
			if (request.getTitle().isBlank()) {
				throw new IllegalArgumentException("도서 제목은 공백일 수 없습니다.");
			}
			book.setTitle(request.getTitle());
		}

		if (request.getDescription() != null) {
			if (request.getDescription().isBlank()) {
				throw new IllegalArgumentException("도서 내용은 공백일 수 없습니다.");
			}
			book.setDescription(request.getDescription());
		}

		if (request.getCoverImageUrl() != null) {
			book.setCoverImageUrl(request.getCoverImageUrl());
		}

		if (request.getFavorite() != null) {
			book.setFavorite(request.getFavorite());
		}

		return book;
	}

	// 삭제
	@Transactional
	public boolean delete(Long id) {
		if (!bookRepository.existsById(id)) {
			return false;
		}

		bookRepository.deleteById(id);
		return true;
	}
}

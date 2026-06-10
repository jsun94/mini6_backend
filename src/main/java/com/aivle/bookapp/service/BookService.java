package com.aivle.bookapp.service;

import com.aivle.bookapp.domain.Book;
import com.aivle.bookapp.domain.Member;
import com.aivle.bookapp.exception.BookNotFoundException;
import com.aivle.bookapp.repository.BookRepository;
import java.util.List;

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
	public List<Book> findAll(String memberName) {
		if (memberName == null || memberName.isBlank()) {
			return bookRepository.findAll();
		}

		Member member = memberRepository.findById(memberName)
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

		return bookRepository.findByMember(member);
	}

	// 상세 조회
	public Book findById(Long id) {
		return bookRepository.findById(id)
				.orElseThrow(() -> new BookNotFoundException(id));
	}

	// 생성
	@Transactional
	public Book create(Book book, String memberName) {

		if (memberName == null || memberName.isBlank()) {
			throw new IllegalArgumentException("로그인한 사용자 정보가 없습니다.");
		}

		Member member = memberRepository.findById(memberName)
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

		// 중복 방지
		if (bookRepository.existsByTitleAndMember(book.getTitle(), member)) {
			throw new IllegalArgumentException("이미 등록된 도서입니다.");
		}

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

	// 표지 이미지 URL 저장
	@Transactional
	public Book updateCover(Long id, Book request) {

		Book book = findById(id);

		if (request.getCoverImageUrl() == null || request.getCoverImageUrl().isBlank()) {
			throw new IllegalArgumentException("표지 이미지 URL이 존재하지 않습니다.");
		}

		book.setCoverImageUrl(request.getCoverImageUrl());

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

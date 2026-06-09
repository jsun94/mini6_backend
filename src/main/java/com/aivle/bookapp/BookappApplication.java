package com.aivle.bookapp;

import com.aivle.bookapp.domain.Book;
import com.aivle.bookapp.domain.Member;
import com.aivle.bookapp.repository.BookRepository;
import com.aivle.bookapp.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookappApplication {

	@Bean
	CommandLineRunner init(BookRepository bookRepository, MemberRepository memberRepository) {
		return args -> {
			// 회원 생성
			Member member = Member.builder()
					.memberName("한봄")
					.build();

			memberRepository.save(member);

			// 책 생성
			Book book1 = Book.builder()
					.member(member)
					.title("작가의 산책")
					.description("비 오는 날 산책을 통해 잊고 있던 꿈을 다시 떠올리는 이야기")
					.favorite(false)
					.build();

			Book book2 = Book.builder()
					.member(member)
					.title("AI와 함께 쓰는 소설")
					.description("생성형 AI와 인간이 함께 만들어가는 미래의 창작 이야기")
					.favorite(true)
					.build();

			bookRepository.save(book1);
			bookRepository.save(book2);

			System.out.println("더미 데이터 저장 완료");
		};
	}

	public static void main(String[] args) {

		SpringApplication.run(BookappApplication.class, args);
	}

}

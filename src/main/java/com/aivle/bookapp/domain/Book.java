package com.aivle.bookapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// 여러 권의 책은 한 명의 사용자에게 속함
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEMBER_NAME")
	private Member member;

	@NotBlank(message = "도서 제목은 필수입니다.")
	@Column(name = "BOOK_NAME", columnDefinition = "TEXT", nullable = false)
	private String title;

	@NotBlank(message = "도서 내용은 필수입니다.")
	@Column(name = "BOOK_DESCRIPTION", columnDefinition = "TEXT", nullable = false)
	private String description;

	@Column(name = "BOOK_COVERIMAGEURL", columnDefinition = "TEXT")
	private String coverImageUrl;

	@Column(name = "BOOK_FAVORITE")
	private Boolean favorite = false;

	@Column(name = "BOOK_CREATEDAT")
	private LocalDateTime createdAt;

	@Column(name = "BOOK_UPDATEDAT")
	private LocalDateTime updatedAt;

	@PrePersist
	void onCreate() {
		LocalDateTime now = LocalDateTime.now();
		createdAt = now;
		updatedAt = now;

		if (favorite == null) {
			favorite = false;
		}
	}

	@PreUpdate
	void onUpdate()
	{
		updatedAt = LocalDateTime.now();
	}
}

package com.aivle.bookapp.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TBL_MEMBER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

	@Id
	@Column(name = "MEMBER_NAME", nullable = false, length = 255)
	private String memberName;

	@Column(name = "MEMBER_EMAIL", length = 255)
	private String memberEmail;

	@Column(name = "MEMBER_PASSWORD", nullable = false, length = 255)
	private String memberPassword;
}

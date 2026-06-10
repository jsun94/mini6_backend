package com.aivle.bookapp.controller;

import com.aivle.bookapp.domain.Member;
import com.aivle.bookapp.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<Member> signup(@RequestBody Member request) {

        if (request.getMemberName() == null || request.getMemberName().isBlank()) {
            throw new IllegalArgumentException("사용자 이름은 필수입니다.");
        }

        if (request.getMemberPassword() == null || request.getMemberPassword().isBlank()) {
            throw new IllegalArgumentException("비밀번호는 필수입니다.");
        }

        if (memberRepository.existsById(request.getMemberName())) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        }

        Member member = new Member();
        member.setMemberName(request.getMemberName());
        member.setMemberEmail(request.getMemberEmail());
        member.setMemberPassword(request.getMemberPassword());

        Member savedMember = memberRepository.save(member);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedMember);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Member request) {

        if (request.getMemberName() == null || request.getMemberName().isBlank()) {
            throw new IllegalArgumentException("사용자 이름은 필수입니다.");
        }

        if (request.getMemberPassword() == null || request.getMemberPassword().isBlank()) {
            throw new IllegalArgumentException("비밀번호는 필수입니다.");
        }

        Member member = memberRepository.findById(request.getMemberName())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (!member.getMemberPassword().equals(request.getMemberPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return ResponseEntity.ok(Map.of(
                "memberName", member.getMemberName(),
                "memberEmail", member.getMemberEmail() == null ? "" : member.getMemberEmail(),
                "message", "로그인 성공"
        ));
    }
}

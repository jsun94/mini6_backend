# AI Book Management System - Backend

Spring Boot 기반 도서 관리 REST API 서버입니다.  

사용자는 회원가입 및 로그인을 통해 자신만의 서재를 관리할 수 있으며, 도서 등록·조회·수정·삭제(CRUD) 기능과 AI 표지 저장 기능을 제공합니다.

---

## 주요 기능

### 1. 회원 관리
#### 회원가입
- 사용자 등록
- 사용자명 중복 검사
- 필수값 검증
#### 로그인
- 사용자 인증
- 비밀번호 검증

### 2. 도서 관리 (CRUD)
#### 도서 등록
- 도서 생성
- 사용자별 도서 저장
#### 도서 목록 조회
- 로그인한 사용자 기준 조회
#### 도서 상세 조회
- 도서 단건 조회
#### 도서 수정
- 제목 및 내용 수정
- 즐겨찾기 수정
#### 도서 삭제
- 도서 삭제

### 3. AI 표지 저장
Frontend에서 생성한 AI 표지를 저장합니다.
#### 기능
- Data URL 저장
- coverImageUrl 업데이트
- coverType 자동 변경
#### 표지 타입
| 값 | 설명 |
|--------|------|
| `NONE` | 표지 없음 |
| `EXTERNAL` | 도서 표지 |
| `AI` | AI 생성 표지 |

---

## 기술 스택
### Backend
- Java 17
- Spring Boot 3
- Spring MVC
- Spring Data JPA
- Lombok
### Database
- H2 Database
### Build Tool
- Gradle

---

## ERD

```
TBL_MEMBER (사용자) 1 ──── N books (책)
```

### TBL_MEMBER (사용자)

| 컬럼명 | 타입 | 키 | 논리명 |
|--------|------|-----|--------|
| `MEMBER_NAME` | `VARCHAR(255)` | PK | 이름 |
| `MEMBER_EMAIL` | `VARCHAR(255)` | | 아이디 |
| `MEMBER_PASSWORD` | `VARCHAR(255)` | | 비밀번호 |

- Entity: `src/main/java/com/aivle/bookapp/domain/Member.java`

### BOOKS (책)

| 컬럼명 | 타입 | 키 | 논리명 |
|--------|------|-----|--------|
| `id` | `BIGINT` | PK | (API용 JPA 자동 생성) |
| `BOOK_MEMBER_NAME` | `NUMBER` | FK | 사용자 ID |
| `BOOK_NAME` | `TEXT` | | 책 제목 |
| `BOOK_DESCRIPTION` | `TEXT` | | 책 내용 |
| `BOOK_COVERIMAGEURL` | `TEXT` | | 표지 |
| `BOOK_COVERTYPE` | `TEXT` | | 표지 타입 |
| `BOOK_FAVORITE` | `BOOLEAN` | | 즐겨찾기 |
| `BOOK_CREATEDAT` | `TIMESTAMP` | | 생성일 |
| `BOOK_UPDATEDAT` | `TIMESTAMP` | | 수정일 |

- Entity: `src/main/java/com/aivle/bookapp/domain/Book.java`
- `BOOK_MEMBER_ID` → `TBL_MEMBER` (1:N 관계)
- 사용자별 도서 관리 구현
- `BOOK_CREATEDAT`, `BOOK_UPDATEDAT`는 저장·수정 시 자동 설정

---

## Mission 1~4. 설계 및 기본 CRUD 구현

### 1. Domain

| Entity | 테이블 | 설명 |
|--------|--------|------|
| `Member` | `TBL_MEMBER` | 사용자 정보 |
| `Book` | `books` | 사용자별 책 정보 (`BOOK_MEMBER_ID`로 Member와 N:1) |

### 2. Repository

| 클래스 | 위치 |
|--------|------|
| `MemberRepository` | `repository/MemberRepository.java` |
| `BookRepository` | `repository/BookRepository.java` |

- `MemberRepository` : `JpaRepository<Member, String>` (PK: `memberName`)
- `BookRepository` : `JpaRepository<Book, Long>`

### 3. Service

| 클래스 | 위치 | 역할 |
|--------|------|------|
| `MemberService` | `service/MemberService.java` | 회원가입, 로그인, 회원 조회 로직 |
| `BookService` | `service/BookService.java` | 도서 조회, 등록, 수정, 삭제, 표지 저장 로직 |

#### MemberService 주요 기능

- 회원가입 처리
- 로그인 검증
- 사용자명 기준 회원 조회
- 중복 회원 검증
- 비밀번호 검증

#### BookService 주요 기능

- 로그인 사용자 기준 도서 목록 조회
- 도서 상세 조회
- 도서 등록
- 도서 부분 수정
- 도서 삭제
- 즐겨찾기 수정
- AI 표지 URL 저장
- 표지 타입 관리
- 도서 중복 등록 검증
- 도서 미존재 예외 처리

### 4. Controller

| 클래스 | 위치 | Base URL | 역할 |
|--------|------|-------------|------|
| `MemberController` | `controller/MemberController.java` | `/api/members` | 회원가입 및 로그인 API 제공 |
| `BookController` | `controller/BookController.java` | `/api/books` | 도서 CRUD 및 표지 저장 API 제공 |

#### Member API

| Method | Path | 설명 |
|--------|------|------|
| `POST` | `/api/members/signup` | 회원가입 |
| `POST` | `/api/members/login` | 로그인 |

#### Book API

| Method | Path | 설명 |
|--------|------|------|
| `GET` | `/api/books?memberName={memberName}` | 로그인 사용자 기준 도서 목록 조회 |
| `GET` | `/api/books/{id}` | 도서 상세 조회 |
| `POST` | `/api/books?memberName={memberName}` | 도서 등록 |
| `PATCH` | `/api/books/{id}` | 도서 부분 수정 |
| `DELETE` | `/api/books/{id}` | 도서 삭제 |
| `PATCH` | `/api/books/{id}/cover` | AI 표지 URL 저장 |

### 5. Integration

#### WebConfig (CORS)

- 위치: `src/main/java/com/aivle/bookapp/config/WebConfig.java`
- 프론트엔드 연동을 위해 `localhost:3000`, `localhost:5173` Origin 허용

#### application.yml

- H2 인메모리 DB (`jdbc:h2:mem:bookdb`)
- H2 Console: `http://localhost:8080/h2-console`
- JPA `ddl-auto: update`

---

## Mission 5~6. 예외 처리 및 트랜잭션 적용

### 1. Exception
| 클래스 | 위치 |
|--------|------|
| `BookNotFoundException` | `exception/BookNotFoundException.java` |
| `GlobalExceptionHandler` | `exception/GlobalExceptionHandler.java` |

#### BookNotFoundException
도서 조회 및 수정/삭제 시 존재하지 않는 도서에 접근하는 경우 발생
```
throw new BookNotFoundException(id);
```

### 2. Transaction
Service 계층에 트랜잭션을 적용하여 데이터 정합성을 보장하였습니다.

#### 조회
```
@Transactional(readOnly = true)
```
- 전체 조회
- 상세 조회

#### 등록 / 수정 / 삭제
```
@Transactional
```
- 도서 등록
- 도서 수정
- 도서 삭제
- AI 표지 저장

### 3. Global Exception Handling
모든 Controller에서 발생하는 예외를 일관된 형태로 응답하도록 구성하였습니다.

| 예외 | 응답 |
|--------|------|
| `BookNotFoundException` | 404 Not Found |
| `MethodArgumentNotValidException` | 400 Bad Request |
| `IllegalArgumentException` | 400 Bad Request |
| `Exception` | 500 Internal Server Error |

---

## Mission 7. AI 표지 저장 기능 구현

Frontend에서 OpenAI API를 직접 호출하여 생성한 AI 표지를 Backend에 저장할 수 있도록 기능을 확장하였습니다.

### 1. Book Entity 확장

| 필드 | 설명 |
|--------|------|
| `coverImageUrl` | 표지 이미지 URL |
| `coverType` | 표지 타입 |

### 2. BookService 확장
#### 추가 메서드
```
updateCover(Long id, Book request)
```
- AI 표지 URL 저장
- coverType 자동 변경
- 수정일 갱신

### 3. API 추가

| Method | Path | 설명 |
|--------|------|------|
| `PATCH` | `/api/books/{id}/cover` | AI 표지 URL 저장 |

### 4. 저장 흐름

```
React
↓
OpenAI API 호출
↓
이미지 생성
↓
coverImageUrl 생성
↓
PATCH /api/books/{id}/cover
↓
Spring Boot
↓
H2 Database 저장
```

---

## 추가 구현 기능

### 1. 사용자별 서재 관리
- Member Entity 생성
- 회원가입 API
- 로그인 API
- Member ↔ Book (1:N) 관계 설정
- 사용자별 도서 조회
- 사용자별 도서 등록

### 2. 도서 중복 등록 방지
동일 사용자가 같은 제목의 도서를 중복 등록하지 못하도록 검증 로직을 추가하였습니다.
```
if (bookRepository.existsByTitleAndMember(book.getTitle(), member)) {
	throw new IllegalArgumentException("이미 등록된 도서입니다.");
}
```

### 3. AI 표지 / 도서 표지 구분
| coverType | 표시 |
|--------|------|
| AI | AI 표지 |
| EXTERNAL | 도서 표지 |
| NONE | 표지 없음 |

### 4. 베스트셀러 도서 내 서재 추가
Frontend의 베스트셀러 페이지와 연동하여 도서를 바로 내 서재에 저장할 수 있도록 구현하였습니다.
#### 저장 시 
```
베스트셀러
↓
내 서재에 추가
↓
Book 생성
↓
coverType = EXTERNAL
```
#### 이후 AI 표지 생성 시
```
EXTERNAL
↓
AI 생성
↓
coverType = AI
```
자동 변경되도록 구현하였습니다.

---

## 프로젝트 구조

```
src/main/java/com/aivle/bookapp

├── config
│   └── WebConfig
│
├── controller
│   ├── BookController
│   └── MemberController
│
├── domain
│   ├── Book
│   └── Member
│
├── exception
│   ├── BookNotFoundException
│   └── GlobalExceptionHandler
│
├── repository
│   ├── BookRepository
│   └── MemberRepository
│
└── service
│  └── BookService
│  └── MemberService
│
└── BookappApplication
```

---

## 실행 방법

```bash
./gradlew bootRun
```

#### H2 Console 접속 정보:
- JDBC URL: `jdbc:h2:mem:bookdb`
- Username: `sa`
- Password: (비어 있음)

---
## R&R
| 이름 / 역할 | 담당 업무 |
| :--- | :--- |
| **김정수** (PM) | - ERD / API 정의서<br>- README.md 작성<br>- 발표 자료 준비<br>- 통합 이슈 추적 |
| **이지윤** (백엔드 개발) | - Book Entity 작성<br>- BookRepository<br>- H2 콘솔 확인<br>- Lombok 4종 적용 |
| **박태희** (백엔드 개발) | - BookService 클래스<br>- 비즈니스 로직<br>- BookNotFoundException<br>- @Transactional |
| **김민지** (백엔드 개발) | - BookController<br>- 5종 CRUD 엔드포인트<br>- @Valid + @NotBlank<br>- Postman 테스트 |
| **장현지** (통합 / 예외 처리) | - WebConfig (CORS)<br>- GlobalExceptionHandler<br>- 풀스택 디버깅<br>- 트러블슈팅 정리 |
| **윤준석** (AI / Frontend 연동) | - Frontend 코드 분석<br>- fetch URL 변경 / 1차 연동<br>- OpenAI 표지 흐름<br>- E2E 시연 준비 |

---
## 트러블슈팅
| 문제 (Issue) | 원인 (Cause) | 해결 방법 (Solution) |
| :--- | :--- | :--- |
| **book entity 작성하면서 즐겨찾기 기능에 false가 기본값으로 넣어지지 않고 null로만 채워짐** | `@AllArgsConstructor`가 파라미터로 받은 값을 그대로 쓰기 때문에 `favorite=false` 기본값이 무시됨 | `@AllArgsConstructor` 주석 처리<br><br>*[관련 코드]*<br>`@Column(name = "BOOK_FAVORITE", columnDefinition = "BOOLEAN DEFAULT FALSE")`<br>`private Boolean favorite = false;` |
| **React 화면이 흰 화면으로 출력됨**<br><br><img width="1054" height="125" alt="Image" src="https://github.com/user-attachments/assets/ff688845-3a37-4c8a-97ca-8d634a971142" /><br>(Uncaught TypeError: Cannot read properties of undefined) | Frontend가 기대하는 필드명과 Backend 필드명이 달랐음 | Backend 필드명을 Frontend 필드명과 통일 시킴 |
| **백엔드 서버를 재시작했을 때, 프론트엔드 웹 화면에서 새롭게 등록하고 저장한 책 정보가 모두 사라지고 항상 초기의 샘플 데이터만 남아있는 문제** | 기존 H2 데이터베이스 설정이 `jdbc:h2:mem:bookdb`인 인메모리(In-Memory) 모드로 지정되어 있어, 서버 프로세스(내장 톰캣)가 종료되는 순간 RAM에 저장되어 있던 모든 데이터가 증발함 | `application.yml` 파일에서 `datasource.url` 경로를 인메모리 모드(`mem:`)에서 로컬 파일 저장 경로인 `jdbc:h2:file:./bookdb;AUTO_SERVER=TRUE` 모드로 전환하여 서버가 꺼져도 디스크에 데이터가 보존되게 함. |
| **DELETE API는 정상 동작했으나 삭제 후 목록에서 즉시 사라지지 않음** | [Frontend] React State 갱신 로직에서 id 타입 불일치 발생. Backend의 id는 Number이나 Frontend에서 String으로 비교 | `setBooks(prev => prev.filter(b => b.id !== String(id)))`에서<br>`setBooks(prev => prev.filter(b => b.id !== id))`로 변경 |
| **json-server → Spring Boot 전환 시 API 연결 실패** | 기본 Vite Proxy 설정이 json-server용으로 작성되어 있었음<br>`rewrite: (path) => path.replace(/^\/api/, '')` | 위 코드 주석 처리 |
| **베스트셀러에서 가져온 책에 AI 표지를 새로 생성했음에도 불구하고 여전히 “도서 표지”로 표시되는 문제** | AI 표지 저장 시 `coverImageUrl`만 갱신되고, 표지 유형을 나타내는 `coverType`이 “AI”로 변경되지 않음. Frontend에서 `/cover` 전용 API 응답값을 사용하지 않고 이미지 URL 문자열만 사용하고 있었음. | Backend `updateCover()`에서 `coverType`을 “AI”로 변경하고, Frontend `generateCover()`가 `updateBookCover()`의 응답 Book 객체를 반환하도록 수정<br><br>*[관련 코드]*<br>`if (bookId) { return await updateBookCover(bookId, imageSrc) }` |
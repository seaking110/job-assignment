#  Spring Boot JWT 인증 예제

Spring Security와 JWT(Json Web Token)를 활용한 **간단한 인증 및 권한 부여 시스템**입니다.  
기본적으로 **회원가입**, **로그인**, **관리자(admin) 권한 부여** 기능을 포함하고 있습니다.

---

## 배포 주소

- **Swagger UI:** [http://3.39.233.7:8080/swagger](http://3.39.233.7:8080/swagger)
- **API Base URL:** `http://3.39.233.7:8080`

---

## 기능 요약

| 기능 | 설명 |
|------|------|
| 회원가입 | 일반 사용자 계정 생성 |
| 로그인 | JWT 토큰 발급 |
| 관리자 권한 부여 | ADMIN 계정이 다른 사용자에게 ADMIN 권한을 부여 |
| 전체 유저 조회 | 권한이 부여되었는지 확인 가능 |

---

## API 명세서
- `POST /signup` : 회원가입
- `POST /login` : 로그인 및 토큰 발급
- `PATCH /admin/users/{userId}/roles` : 관리자 권한 부여
- `GET /users` : 전체 사용자 조회
  
## 실행 방법

### 1. Swagger UI 접속
- 주소: [http://3.39.233.7:8080/swagger](http://3.39.233.7:8080/swagger)

### 2. 회원가입
- `/signup` 엔드포인트에서 사용자 계정을 생성합니다.

### 3. 로그인
- `/login` 엔드포인트에서 로그인 시, JWT 토큰이 응답됩니다.

### ✅ 관리자 계정 정보
- `username`: `admin`
- `password`: `1234`

### 4. 토큰 인증 설정
- 로그인 후 발급받은 토큰에서 `Bearer` 부분은 **제외**하고,
- Swagger 우측 상단의 `Authorize` 버튼 클릭 후 붙여넣습니다.

### 5. 관리자 권한 부여
- `/admin/users/{userId}/roles` PATCH 요청을 통해 다른 사용자에게 권한 부여
- 정상적으로 작동하면 `ROLE_ADMIN`으로 업데이트됩니다.

### 6. 전체 사용자 목록 확인
- `/users` GET 요청을 통해 모든 유저의 권한 상태 확인 가능



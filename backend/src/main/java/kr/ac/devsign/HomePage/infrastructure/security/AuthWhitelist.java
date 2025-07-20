package kr.ac.devsign.HomePage.infrastructure.security;

public class AuthWhitelist {
    public static final String[] PATHS = {
            "/public/**",
            "/auth/**",
            "/posts",           // 게시글 목록 조회
            "/posts/*",         // 게시글 단건 조회
            "/users/*/profile", // 사용자 프로필 조회
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/suggestions",
            "/actuator/**",         // 헬스체크
            "/health",              // 헬스체크
            "/favicon.ico"          // 파비콘
    };
}

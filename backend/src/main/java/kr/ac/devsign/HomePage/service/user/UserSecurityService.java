package kr.ac.devsign.HomePage.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 사용자 보안 관련 도메인 서비스
 * - 비밀번호 정책 검증, 로그인 실패 처리, IP 차단 등
 */
@Service
@RequiredArgsConstructor
public class UserSecurityService {

    /**
     * 비밀번호 정책 검증
     * - 8자 이상, 영문/숫자/특수문자 포함
     */
    public boolean isValidPassword(String password) {
        return password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$");
    }
}

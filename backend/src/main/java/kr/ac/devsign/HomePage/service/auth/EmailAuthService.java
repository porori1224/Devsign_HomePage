package kr.ac.devsign.HomePage.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailAuthService {
    private final StringRedisTemplate redisTemplate;

    public boolean isAlreadyVerified(String email) {
//        String verified = redisTemplate.opsForValue().get("verified:email:" + email);
        // log.info("[🔍 이메일 인증 상태 확인] email={}, Redis 값: {}", email, verified);
//        return Boolean.TRUE.toString().equals(verified);
        return true;
    }

    public void clearVerification(String email) {
//        redisTemplate.delete("verified:email:" + email);
        // log.info("[🧹 이메일 인증 상태 삭제] email={}", email);
    }
}

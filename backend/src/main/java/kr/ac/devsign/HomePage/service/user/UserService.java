package kr.ac.devsign.HomePage.service.user;

import kr.ac.devsign.HomePage.domain.entity.log.AuditRegisterLog;
import kr.ac.devsign.HomePage.domain.entity.user.User;
import kr.ac.devsign.HomePage.domain.repository.UserRepository;
import kr.ac.devsign.HomePage.domain.repository.log.AuditRegisterLogRepository;
import kr.ac.devsign.HomePage.dto.user.UserRegisterRequestDto;
import kr.ac.devsign.HomePage.infrastructure.exception.*;
import kr.ac.devsign.HomePage.infrastructure.security.EncryptionUtil;
import kr.ac.devsign.HomePage.service.auth.EmailAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 사용자 관련 비즈니스 로직 처리 서비스
 * - 회원가입, 프로필 수정, 비밀번호 재설정, 로그인 실패 처리 등
 * - 보안 및 운영 관점의 상세 예외처리 포함
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final AuditRegisterLogRepository auditRegisterLogRepository;
    private final UserRepository userRepository;
    private final EncryptionUtil encryptionUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserSecurityService userSecurityService;
    private final EmailAuthService emailAuthService;

    /**
     * 회원가입 처리
     */
    public Long register(UserRegisterRequestDto dto, String ipAddress) {
        try {
            if (userRepository.existsByUserId(dto.getUserId())) {
                auditRegisterLogRepository.save(AuditRegisterLog.failure(dto.getEmail(), ipAddress, "중복된 아이디"));
                throw new DuplicateUserIdException("이미 사용 중인 아이디입니다.");
            }
            if (userRepository.existsByEmail(dto.getEmail())) {
                auditRegisterLogRepository.save(AuditRegisterLog.failure(dto.getEmail(), ipAddress, "중복된 이메일"));
                throw new DuplicateEmailException("이미 사용 중인 이메일입니다.");
            }
            // 핸드폰번호 중복 검사
            String phoneHash = encryptionUtil.hashPhone(dto.getPhone());
            if (userRepository.existsByPhoneHash(phoneHash)) {
                auditRegisterLogRepository.save(AuditRegisterLog.failure(dto.getEmail(), ipAddress, "중복된 핸드폰번호"));
                throw new IllegalArgumentException("이미 사용 중인 핸드폰번호입니다.");
            }
            if (!dto.getPassword().equals(dto.getConfirmPassword())) {
                auditRegisterLogRepository.save(AuditRegisterLog.failure(dto.getEmail(), ipAddress, "비밀번호 불일치"));
                throw new PasswordMismatchException("비밀번호가 일치하지 않습니다.");
            }
            if (!userSecurityService.isValidPassword(dto.getPassword())) {
                auditRegisterLogRepository.save(AuditRegisterLog.failure(dto.getEmail(), ipAddress, "비밀번호 정책 위반"));
                throw new IllegalArgumentException("비밀번호는 8자 이상이며, 영문/숫자/특수문자를 포함해야 합니다.");
            }
            if (!isValidPhoneNumber(dto.getPhone())) {
                auditRegisterLogRepository.save(AuditRegisterLog.failure(dto.getEmail(), ipAddress, "잘못된 전화번호 형식"));
                throw new IllegalArgumentException("올바른 전화번호 형식이 아닙니다. (예: 010-1234-5678)");
            }
            if (userRepository.existsByDiscordId(dto.getDiscordId())) {
                auditRegisterLogRepository.save(AuditRegisterLog.failure(dto.getEmail(), ipAddress, "중복된 디스코드 사용자명"));
                throw new DuplicateDiscordIdException("이미 존재하는 디스코드 사용자명입니다.");
            }

            User user = User.builder()
                    .userId(dto.getUserId())
                    .password(passwordEncoder.encode(dto.getPassword()))
                    .name(dto.getName())
                    .birth(dto.getBirth())
                    .studentId(dto.getStudentId())
                    .department(dto.getDepartment())
                    .phone(encryptionUtil.encrypt(dto.getPhone())) // 전화번호 암호화
                    .phoneHash(phoneHash) // 전화번호 해시값
                    .email(dto.getEmail())
                    .currentAddress(dto.getCurrentAddress())    // ❗암호화(?)
                    .addressDetail(dto.getAddressDetail())      // ❗암호화(?)
                    .discordId(dto.getDiscordId())
                    .interests(dto.getInterests())
                    .licenses(dto.getLicenses())
                    .bio(dto.getBio())
                    .agreedToPrivacyPolicy(dto.isAgreedToPrivacyPolicy())
                    .role(User.Role.USER)
                    .status(User.UserStatus.SUSPENDED)
                    .emailVerified(false)
                    .loginFailCount(0)
                    .build();
            userRepository.save(user);

            // 회원가입 완료 후 인증 상태 삭제
            emailAuthService.clearVerification(dto.getEmail());

            auditRegisterLogRepository.save(AuditRegisterLog.success(dto.getEmail(), ipAddress));
            return user.getId();
        } catch (RuntimeException e) {
            if (!(e instanceof DuplicateEmailException || e instanceof DuplicateUserIdException
                    || e instanceof PasswordMismatchException || e instanceof IllegalArgumentException
                    || e instanceof DuplicateDiscordIdException)) {
                // 오류 메시지 길이 제한 (500자)
                String errorMessage = e.getMessage();
                if (errorMessage != null && errorMessage.length() > 450) {
                    errorMessage = errorMessage.substring(0, 450) + "...";
                }
                auditRegisterLogRepository.save(
                        AuditRegisterLog.failure(dto.getEmail(), ipAddress, "기타 오류: " + errorMessage));
            }
            throw e;
        }
    }

    /**
     * ID로 사용자 조회
     */
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));
    }

    /**
     * 전화번호 형식 검증
     */
    public boolean isValidPhoneNumber(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }

        // 한국 휴대폰 번호 형식 검증
        // 010-1234-5678, 011-123-4567, 016-123-4567, 017-123-4567, 018-123-4567, 019-123-4567 형식
        String phonePattern = "^01[0-9]-\\d{3,4}-\\d{4}$";
        return phone.matches(phonePattern);
    }
}

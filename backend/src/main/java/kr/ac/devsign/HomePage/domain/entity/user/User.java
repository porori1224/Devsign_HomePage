package kr.ac.devsign.HomePage.domain.entity.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true) // 기존 객체를 기반으로 새 객체를 복사-수정하여 생성할 수 있도록 toBuilder() 메서드를 생성
public class User implements UserDetails {

    /**
     * 권한 목록(간부진, 관리자, 동아리원)
     */
    public enum Role {
        ADMIN,      // 어드민
        MANAGER,    // 관리자
        USER        // 동아리원
    }

    /**
     * 사용자 상태 열거형
     */
    public enum UserStatus {
        ACTIVE,     // ✅ 정상 활동 중인 사용자
        BANNED,     // ⛔ 규칙 위반 등으로 영구 정지된 사용자
        SUSPENDED,  // ⏸ 일시 정지 상태 (예: 일정 기간 활동 제한 또는 입부 승락 전)
        WITHDRAWN   // 🚪 자발적으로 동아리 탈퇴한 사용자이거나 졸업한 사람
    }

    /**
     * 기본키 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 아이디
     */
    @Column(nullable = false)
    private String userId;

    /**
     * 비밀번호 (BCrypt 암호화 저장)
     */
    @Column(nullable = false)
    private String password;

    /**
     * 이름
     */
    @Column(nullable = false)
    private String name;

    /**
     * 생년월일
     */
    @Column(nullable = false)
    private String birth;

    /**
     * 학번
     */
    @Column(nullable = false)
    private String studentId;

    /**
     * 학과
     */
    @Column(nullable = false)
    private String department;

    /**
     * 전화번호 (암호화 저장)
     */
    @Column(nullable = false)
    private String phone;

    /** 전화번호 해시값 (중복 검사용) */
    @Column(nullable = false, unique = true)
    private String phoneHash;

    /**
     * 이메일
     */
    @Column(nullable = false)
    private String email;

    /**
     * 현 거주지
     */
    @Column
    private String currentAddress;

    /**
     * 상세주소
     */
    @Column
    private String addressDetail;

    /**
     * 디스코드 사용자명
     */
    @Column(nullable = false)
    private String discordId;

    /**
     * 관심사
     */
    @Column
    private String interests;

    /**
     * 자격증
     */
    @Column
    private String licenses;

    /**
     * 자기소개
     */
    @Column
    private String bio;

    /**
     * 정보 약관 & 처리방침 동의
     */
    @Column(nullable = false)
    private Boolean agreedToPrivacyPolicy;

    /**
     * 사용자 권한 (USER 또는 ADMIN)
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    /**
     * 사용자 계정 상태 (정상, 밴, 정지, 탈퇴)
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;

    /**
     * 마지막 로그인 시각
     */
    @Column
    private LocalDateTime lastLoginAt;

    /**
     * 로그인 실패 횟수
     */
    @Column(nullable = false)
    @Builder.Default
    private int loginFailCount = 0;

    /**
     * 계정 잠금 여부
     */
    @Column(nullable = false)
    @Builder.Default
    private boolean accountLocked = false;

    /**
     * 마지막 로그인 실패 시각
     */
    @Column
    private LocalDateTime lastFailedLogin;

    /**
     * 이메일 인증 여부
     */
    @Column(nullable = false)
    @Builder.Default
    private boolean emailVerified = false;

    /**
     * 계정 밴 사유
     */
    @Column
    private String banReason;

    /**
     * 탈퇴 사유
     */
    @Column(length = 255)
    private String withdrawalReason;

    /**
     * 탈퇴 일시
     */
    @Column
    private LocalDateTime withdrawnAt;

    public LocalDateTime getWithdrawnAt() {
        return withdrawnAt;
    }

    /**
     * 계정 생성 시각
     */
    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    // ========================== 도메인 메서드 ==========================

    // ======================== 🧑‍💼 관리자 기능 ========================

    /** 관리자에 의한 계정 잠금 */
    public void lockAccount() {
        this.accountLocked = true;
    }

    /** 계정 잠금 해제 및 실패 횟수 초기화 */
    public void unlockAccount() {
        this.accountLocked = false;
        this.loginFailCount = 0;
        this.lastFailedLogin = null;
    }

    /** 계정 상태 변경 메서드 */
    public void changeStatus(UserStatus newStatus) {
        this.status = newStatus;
    }

    /** 계정 밴 처리 */
    public void ban(String reason) {
        this.status = UserStatus.BANNED;
        this.banReason = reason;
    }

    /** 계정 밴 해제 */
    public void unban() {
        this.status = UserStatus.ACTIVE;
        this.banReason = null;
    }

    // ======================== 🙋 일반 유저 기능 ========================

    /** 전화번호 업데이트 (암호화하여 저장) */
    public void updatePhone(String newPhone) {
        this.phone = newPhone; // 서비스 레이어에서 암호화된 값을 전달받음
    }

    /** 암호화된 전화번호 직접 설정 (내부 사용) */
    public void setEncryptedPhone(String encryptedPhone) {
        this.phone = encryptedPhone;
    }

    /** 이메일 인증 완료 처리 */
    public void verifyEmail() {
        this.emailVerified = true;
    }

    /** 비밀번호 변경 */
    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    /** 로그인 성공 처리 */
    public void loginSuccess() {
        this.lastLoginAt = LocalDateTime.now();
        this.loginFailCount = 0;
        this.accountLocked = false;
        this.lastFailedLogin = null;
    }

    /** 로그인 실패 처리 */
    public void loginFail() {
        this.loginFailCount++;
        this.lastFailedLogin = LocalDateTime.now();
        // 계정 잠금은 AuthService에서 별도로 처리
    }

    /** 회원 탈퇴 처리 */
    public void withdraw(String reason) {
        this.status = UserStatus.WITHDRAWN;
        this.withdrawalReason = reason;
        this.withdrawnAt = LocalDateTime.now();
        this.email = "withdrawn_" + this.id + "@byeolnight.local";
    }

    /** 개인정보 완전 삭제 (5년 경과 후) */
    public void completelyRemovePersonalInfo() {
        this.email = "deleted_" + this.id + "@removed.local";
        this.phone = "DELETED";
        this.withdrawalReason = "5년 경과로 인한 자동 삭제";
    }

    // ======================== 🔐 Spring Security 구현부 ========================

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status != UserStatus.BANNED && status != UserStatus.SUSPENDED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status == UserStatus.ACTIVE;
    }

    // ======================== ⚖ equals & hashCode ========================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
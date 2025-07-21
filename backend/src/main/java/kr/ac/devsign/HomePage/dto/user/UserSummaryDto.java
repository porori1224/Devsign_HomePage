package kr.ac.devsign.HomePage.dto.user;

import kr.ac.devsign.HomePage.domain.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 관리자용 사용자 요약 정보 DTO
 */
@Getter
@AllArgsConstructor
public class UserSummaryDto {
    private Long id;                // 식별자
    private String userId;          // 아이디
    private String name;            // 이름
    private String studentId;       // 학번
    private String email;           // 이메일
    private String role;            // 권한
    private User.UserStatus status; // 상태
    private boolean accountLocked;  // 계정 잠금 여부

    public static UserSummaryDto from(User user) {
        return new UserSummaryDto(
                user.getId(),
                user.getUserId(),
                user.getName(),
                user.getStudentId(),
                user.getEmail(),
                user.getRole().name(),
                user.getStatus(),
                user.isAccountLocked()
        );
    }
}

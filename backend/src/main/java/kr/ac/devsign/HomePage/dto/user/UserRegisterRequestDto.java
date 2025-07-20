package kr.ac.devsign.HomePage.dto.user;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterRequestDto {

    @NotBlank
    private String userId;                    // 아이디
    @NotBlank
    private String password;                  // 비밀번호
    @NotBlank
    private String confirmPassword;           // 비밀번호 확인용
    @NotBlank
    private String name;                      // 이름
    @NotBlank
    private String birth;                     // 생년월일
    @NotBlank
    private String studentId;                 // 학번
    @NotBlank
    private String department;                // 학과
    @NotBlank
    private String phone;                     // 전화번호
    @Email
    @NotBlank
    private String email;                     // 이메일

    private String currentAddress;            // 현거주지

    private String addressDetail;             // 상세주소
    @NotBlank
    private String discordId;                 // 디스코드 사용자명

    private String interests;                 // 관심분야

    private String licenses;                  // 자격증

    private String bio;                       // 자기소개
    @AssertTrue(message = "개인정보 처리방침에 동의해야 합니다.")
    private boolean agreedToPrivacyPolicy;     // 정보 약관&처리방침 동의
}

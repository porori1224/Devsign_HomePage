package kr.ac.devsign.HomePage.controller.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kr.ac.devsign.HomePage.domain.entity.user.User;
import kr.ac.devsign.HomePage.dto.user.UserRegisterRequestDto;
import kr.ac.devsign.HomePage.dto.user.UserSummaryDto;
import kr.ac.devsign.HomePage.infrastructure.common.CommonResponse;
import kr.ac.devsign.HomePage.infrastructure.util.IpUtil;
import kr.ac.devsign.HomePage.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
@Tag(name = "🔑 인증 API", description = "로그인, 회원가입, 토큰 관리 등 인증 관련 API")
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "회원가입", description = "새로운 사용자 계정을 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<CommonResponse<UserSummaryDto>> register(@RequestBody @Valid UserRegisterRequestDto dto, HttpServletRequest request) {
        try {
            String ip = IpUtil.getClientIp(request);
            Long userId = userService.register(dto, ip);

            // 회원가입 완료 인증서 발급
            User newUser = userService.findById(userId);
            UserSummaryDto summary = UserSummaryDto.from(newUser);

            return ResponseEntity.ok(CommonResponse.success(summary,"회원가입이 완료되었습니다."));
        } catch (Exception e) {
            log.error("회원가입 처리 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(CommonResponse.fail(e.getMessage()));
        }
    }
}

package kr.ac.devsign.HomePage.domain.entity.log;

import jakarta.persistence.*;
import kr.ac.devsign.HomePage.domain.entity.common.BaseTimeEntity;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AuditRegisterLog extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String ipAddress;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Result result;

    @Column(columnDefinition = "TEXT")
    private String failureReason;

    public enum Result {
        SUCCESS, FAILURE
    }

    public static AuditRegisterLog success(String email, String ipAddress) {
        return AuditRegisterLog.builder()
                .email(email)
                .ipAddress(ipAddress)
                .result(Result.SUCCESS)
                .build();
    }

    public static AuditRegisterLog failure(String email, String ipAddress, String reason) {
        return AuditRegisterLog.builder()
                .email(email)
                .ipAddress(ipAddress)
                .result(Result.FAILURE)
                .failureReason(reason)
                .build();
    }
}

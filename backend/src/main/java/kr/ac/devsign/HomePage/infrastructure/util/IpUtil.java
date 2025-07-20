package kr.ac.devsign.HomePage.infrastructure.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IpUtil {
    private static final String[] IP_HEADERS = {
            "X-Client-IP",           // 프론트에서 직접 넘기는 IP (우선순위 최상)
            "X-Forwarded-For",       // 프록시 체인을 통해 전달된 IP
            "X-Real-IP",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    };

    /**
     * HttpServletRequest에서 실제 클라이언트 IP 주소를 추출
     */
    public static String getClientIp(HttpServletRequest request) {
        for (String header : IP_HEADERS) {
            String ip = request.getHeader(header);
            if (isValidIp(ip)) {
                // X-Forwarded-For: "client1, proxy1, proxy2" → 첫 번째 IP 추출
                if (ip.contains(",")) {
                    ip = ip.split(",")[0].trim();
                }
                log.debug("IP 추출 성공 - 헤더: {}, IP: {}", header, ip);
                return ip;
            }
        }

        // fallback
        String fallbackIp = request.getRemoteAddr();
        if (isValidIp(fallbackIp)) {
            log.debug("헤더에서 못 찾음, request.getRemoteAddr() 사용 - IP: {}", fallbackIp);
            return fallbackIp;
        }

        log.debug("유효한 IP를 찾지 못함, unknown 반환");
        return "unknown";
    }

    /**
     * IP 주소가 유효한지 검증
     */
    private static boolean isValidIp(String ip) {
        if (ip == null || ip.isEmpty()) return false;

        String normalized = ip.trim().toLowerCase();
        return !normalized.equals("unknown")
                && !normalized.equals("localhost")
                && !normalized.equals("127.0.0.1")
                && !normalized.equals("0:0:0:0:0:0:0:1")
                && !normalized.equals("::1");
    }
}

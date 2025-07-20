package kr.ac.devsign.HomePage.domain.repository.log;

import kr.ac.devsign.HomePage.domain.entity.log.AuditRegisterLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRegisterLogRepository extends JpaRepository<AuditRegisterLog, Long> {
}

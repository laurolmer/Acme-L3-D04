
package acme.testing.auditor.auditRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.entities.audit.Audit;
import acme.entities.auditRecord.AuditRecord;
import acme.framework.repositories.AbstractRepository;

public interface AuditorAuditRecordTestRepository extends AbstractRepository {

	@Query("SELECT A FROM Audit A WHERE A.auditor.userAccount.username = :username")
	Collection<Audit> findAllAuditsByAuditorUsername(String username);

	@Query("SELECT AU FROM AuditRecord AU WHERE AU.audit.auditor.userAccount.username = :username")
	Collection<AuditRecord> findAllAuditRecordsByAuditorUsername(String username);

}

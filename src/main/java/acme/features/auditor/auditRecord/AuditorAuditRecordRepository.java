
package acme.features.auditor.auditRecord;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.audit.Audit;
import acme.entities.auditRecord.AuditRecord;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AuditorAuditRecordRepository extends AbstractRepository {

	@Query("select AR from AuditRecord AR where AR.audit.id = :aid")
	List<AuditRecord> findAuditRecordsByAuditId(int aid);

	@Query("select A from Audit A where A.id = :aid")
	Audit findAuditById(int aid);

	@Query("select AR from AuditRecord AR where AR.id = :arid")
	AuditRecord findAuditRecordById(int arid);

	@Query("select A from Audit A where A.draftMode = false")
	List<Audit> findAllPublishedAudits();
}

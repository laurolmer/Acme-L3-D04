
package acme.testing.auditor.audit;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.entities.audit.Audit;
import acme.framework.repositories.AbstractRepository;

public interface AuditorAuditTestRepository extends AbstractRepository {

	@Query("SELECT A FROM Audit A WHERE A.auditor.userAccount.username = :username")
	Collection<Audit> findAllAuditsByAuditorUsername(String username);

}

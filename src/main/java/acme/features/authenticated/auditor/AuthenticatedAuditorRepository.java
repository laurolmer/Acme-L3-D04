
package acme.features.authenticated.auditor;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.framework.components.accounts.UserAccount;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Auditor;

@Repository
public interface AuthenticatedAuditorRepository extends AbstractRepository {

	@Query("select UA from UserAccount UA where UA.id = :id")
	UserAccount findOneUserAccountById(int id);

	@Query("select A from Auditor A where A.userAccount.id = :id")
	Auditor findOneAuditorByUserAccountId(int id);

}

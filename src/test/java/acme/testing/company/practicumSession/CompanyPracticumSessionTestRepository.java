
package acme.testing.company.practicumSession;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.practicum.Practicum;
import acme.entities.practicumSession.PracticumSession;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface CompanyPracticumSessionTestRepository extends AbstractRepository {

	@Query("select p from Practicum p where p.company.userAccount.username = ?1")
	Collection<Practicum> findManyPracticumsByCompanyUsername(String username);

	// Método que permita obtener las sesiones de una práctica
	@Query("select sp from PracticumSession sp where sp.practicum.id = ?1 and sp.additional = true")
	Collection<PracticumSession> findManyPracticumSessionsAdditionalByPracticumId(int id);

	@Query("select sp from PracticumSession sp where sp.practicum.company.userAccount.username = ?1 and sp.practicum.draftMode = true")
	Collection<PracticumSession> findManyPracticumSessionsByCompanyUsernameInDraftMode(String username);

	@Query("select sp from PracticumSession sp where sp.practicum.company.userAccount.username = ?1 and sp.practicum.draftMode = false")
	Collection<PracticumSession> findManyPracticumSessionsByCompanyUsernameInFinalMode(String username);

	@Query("select sp from PracticumSession sp where sp.practicum.company.userAccount.username = ?1")
	Collection<PracticumSession> findManyPracticumSessionsByCompanyUsername(String username);

	@Query("select p from Practicum p where p.company.userAccount.username = ?1 and p.draftMode = true")
	Collection<Practicum> findManyPracticumsByCompanyUsernameInDraftMode(String username);

	@Query("select p from Practicum p where p.company.userAccount.username = ?1 and p.draftMode = false")
	Collection<Practicum> findManyPracticumsByCompanyUsernameInFinalMode(String username);
}

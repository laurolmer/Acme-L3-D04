
package acme.features.company.practicumSession;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.practicum.Practicum;
import acme.entities.practicumSession.PracticumSession;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface CompanyPracticumSessionRepository extends AbstractRepository {

	@Query("select p from Practicum p where p.id = ?1")
	Practicum findOnePracticumById(int practicumId);

	@Query("select s from PracticumSession s where s.id = ?1")
	PracticumSession findOnePracticumSessionById(int PracticumSessionId);

	@Query("select s from PracticumSession s where s.practicum.id = ?1")
	Collection<PracticumSession> findManyPracticumSessionsByPracticumId(int practicumId);

	@Query("select sp.practicum from PracticumSession sp where sp.id = ?1")
	Practicum findOnePracticumByPracticumSessionId(int PracticumSessionId);

	@Query("select sp from PracticumSession sp where sp.practicum.id != ?1 and sp.confirmed = false")
	Collection<PracticumSession> findManyPracticumSessionsByExtraAvailableAndPracticumId(int id);
}

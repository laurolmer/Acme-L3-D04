
package acme.testing.student.enrolment;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.entities.enrolment.Enrolment;
import acme.framework.repositories.AbstractRepository;

public interface StudentEnrolmentTestRepository extends AbstractRepository {

	@Query("SELECT e FROM Enrolment e WHERE e.student.userAccount.username = :username")
	Collection<Enrolment> findAllEnrolmentsByStudentUsername(String username);

}

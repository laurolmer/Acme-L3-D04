
package acme.features.company.practicum;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.course.Course;
import acme.entities.practicum.Practicum;
import acme.entities.practicumSession.PracticumSession;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Company;

@Repository
public interface CompanyPracticumRepository extends AbstractRepository {

	@Query("select t from Practicum t where t.course.id = :id")
	Collection<Practicum> findPracticumByCourse(int id);

	@Query("select t from Practicum t where t.company.id = :id")
	Collection<Practicum> findPracticumByCompanyId(int id);

	@Query("select t from Practicum t where t.id = :id")
	Practicum findPracticumById(int id);

	@Query("select c from Course c")
	Collection<Course> findAllCourses();

	@Query("select c from Course c where c.draftMode = false")
	Collection<Course> findNotInDraftCourses();

	@Query("select t from Practicum t where t.draftMode = false")
	Collection<Practicum> findNotInDraftPracticum();

	@Query("select a from Company a where a.id = :id")
	Company findCompanyById(int id);

	@Query("select c from Course c where c.id = :id")
	Course findCourseById(int id);

	@Query("select t from Practicum t where t.code = :code")
	Collection<Practicum> findAllPracticumByCode(String code);

	@Query("select t from Practicum t")
	Collection<Practicum> findAllPracticum();

	@Query("select t from Practicum t where t.code = :code")
	Practicum findAPracticumByCode(String code);

	@Query("select ps from PracticumSession ps where ps.practicum.id = :id")
	Collection<PracticumSession> findSessionsByPracticumId(int id);
}

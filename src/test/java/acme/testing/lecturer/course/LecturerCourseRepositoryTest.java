
package acme.testing.lecturer.course;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.entities.course.Course;
import acme.entities.lecture.Lecture;
import acme.framework.repositories.AbstractRepository;

public interface LecturerCourseRepositoryTest extends AbstractRepository {

	@Query("select l from Lecture l where l.lecturer.userAccount.username = :username")
	Collection<Lecture> findLecturesByLecturerUsername(String username);

	@Query("select c from Course c where c.lecturer.userAccount.username = :username")
	Collection<Course> findCoursesByLecturerUsername(String username);
}

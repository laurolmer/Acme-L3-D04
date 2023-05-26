
package acme.testing.lecturer.courseLecture;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.entities.course.Course;
import acme.entities.course.CourseLecture;
import acme.entities.lecture.Lecture;
import acme.framework.repositories.AbstractRepository;

public interface LecturerCourseLectureRepositoryTest extends AbstractRepository {

	@Query("select cl from CourseLecture cl where cl.course.lecturer.userAccount.username = :username")
	Collection<CourseLecture> findCourseLecturesByLecturerUsername(String username);

	@Query("select cl.course from CourseLecture cl where cl.course.lecturer.userAccount.username = :username")
	Collection<Course> findCoursesByLecturerUsername(String username);

	@Query("select cl.lecture from CourseLecture cl where cl.course.lecturer.userAccount.username = :username")
	Collection<Lecture> findLecturesByLecturerUsername(String username);
}

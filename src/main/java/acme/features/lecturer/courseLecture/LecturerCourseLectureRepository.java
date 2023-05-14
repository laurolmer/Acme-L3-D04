
package acme.features.lecturer.courseLecture;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.course.Course;
import acme.entities.course.CourseLecture;
import acme.entities.lecture.Lecture;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Lecturer;

@Repository
public interface LecturerCourseLectureRepository extends AbstractRepository {

	@Query("select cl from CourseLecture cl where cl.id = :id")
	CourseLecture findOneCourseLectureById(int id);

	@Query("select cl from CourseLecture cl where cl.lecture.id = :lectureId and cl.course.id = :courseId")
	CourseLecture findOneCourseLectureByLectureIdAndCourseId(int lectureId, int courseId);

	@Query("select l from Lecturer l where l.id = :id")
	Lecturer findOneLecturerById(int id);

	@Query("select l from Lecture l where l.id = :id")
	Lecture findOneLectureById(int id);

	@Query("select c from Course c where c.id = :id")
	Course findOneCourseById(int id);

	@Query("select cl from CourseLecture cl")
	Collection<CourseLecture> findAllCourseLecture();

	@Query("select cl from CourseLecture cl where cl.course.id = :id")
	Collection<CourseLecture> findCourseLecturesByCourseId(int id);

	@Query("select cl.lecture from CourseLecture cl where cl.course.id = :id")
	Collection<Lecture> findLecturesByCourseId(int id);

	@Query("select l from Lecture l where l.draftMode = false and l.lecturer.id = :id")
	Collection<Lecture> findPublishedLecturesByLecturerId(int id);

	@Query("select c from Course c where c.draftMode = true and c.lecturer.id = :id")
	Collection<Course> findNotPublishedCoursesByLecturerId(int id);

	@Query("select c.draftMode from Course c where c.id = :id")
	boolean isCourseInDraftModeByCourseId(int id);

	@Query("select l.draftMode from Lecture l where l.id = :id")
	boolean isLectureInDraftModeByCourseId(int id);

}

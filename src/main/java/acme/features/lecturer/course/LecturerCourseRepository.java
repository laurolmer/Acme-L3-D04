
package acme.features.lecturer.course;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.audit.Audit;
import acme.entities.course.Course;
import acme.entities.course.CourseLecture;
import acme.entities.course.CourseType;
import acme.entities.enrolment.Enrolment;
import acme.entities.lecture.Lecture;
import acme.entities.lecture.LectureType;
import acme.entities.practicum.Practicum;
import acme.entities.tutorial.Tutorial;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Lecturer;

@Repository
public interface LecturerCourseRepository extends AbstractRepository {

	@Query("select l from Lecturer l where l.id = :id")
	Lecturer findOneLecturerById(int id);

	@Query("select c from Course c where c.lecturer.userAccount.id = :id")
	Collection<Course> findCoursesByLecturerId(int id);

	@Query("select c from Course c where c.draftMode = false")
	Collection<Course> findAllCourses();

	@Query("select c from Course c where c.id = :id")
	Course findOneCourseById(int id);

	@Query("select c from Course c where c.code = :code")
	Course findOneCourseByCode(String code);

	@Query("select cl from CourseLecture cl where cl.course.id = :courseId")
	Collection<CourseLecture> findCourseLecturesByCourseId(int courseId);

	@Query("select l from Lecture l inner join CourseLecture cl on l = cl.lecture inner join Course c on cl.course = c where c.id = :id")
	Collection<Lecture> findLecturesByCourseId(int id);

	@Query("select count(cl) > 0 from CourseLecture cl where cl.course.id = :courseId")
	boolean hasACourseLecturesByCourseId(int courseId);

	@Query("select count(cl) > 0 from CourseLecture cl where cl.course.id = :courseId and cl.lecture.draftMode = true")
	boolean hasACourseLecturesNotPublishedByCourseId(int courseId);

	@Query("select count(cl) > 0 from CourseLecture cl where cl.course.id = :courseId and cl.lecture.lectureType = acme.entities.lecture.LectureType.HANDS_ON")
	boolean hasACourseHandsOnLecturesByCourseId(int courseId);

	@Query("select count(cl) from CourseLecture cl where cl.course.id = :courseId and cl.lecture.lectureType = :type")
	Integer numLecturesFromCourseByType(int courseId, LectureType type);

	@Query("select e from Enrolment e where e.course.id = :courseId")
	Collection<Enrolment> findEnrolmentsByCourseId(int courseId);

	@Query("select t from Tutorial t where t.course.id = :courseId")
	Collection<Tutorial> findTutorialsByCourseId(int courseId);

	@Query("select p from Practicum p where p.course.id = :courseId")
	Collection<Practicum> findPracticumsByCourseId(int courseId);

	@Query("select a from Audit a where a.course.id = :courseId")
	Collection<Audit> findAuditsByCourseId(int courseId);

	default CourseType calculateCourseTypeById(final int id) {
		final int handsOnLectures = this.numLecturesFromCourseByType(id, LectureType.HANDS_ON);
		final int theoreticalLectures = this.numLecturesFromCourseByType(id, LectureType.THEORETICAL);
		if (handsOnLectures > theoreticalLectures)
			return CourseType.HANDS_ON;
		else
			return CourseType.THEORY_COURSE;
	}
}


package acme.features.lecturer.lecturerDashboard;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.framework.repositories.AbstractRepository;
import acme.roles.Lecturer;

@Repository
public interface LecturerDashboardRepository extends AbstractRepository {

	@Query("select l from Lecturer l where l.id = : lecturerId")
	Lecturer findOneLecturerById(int lecturerId);

	// LECTURES

	@Query("select count(l) from Lecture l where l.lecturer.id = :lecturerId")
	Integer countLecturesByLecturerId(int lecturerId);

	@Query("select count(l) from Lecture l where l.lecturer.id = :lecturerId and l.lectureType = acme.entities.lecture.LectureType.HANDS_ON")
	Integer countHandsOnLecturesByLecturerId(int lecturerId);

	@Query("select avg(abs(time_to_sec(timediff(l.endPeriod,l.startPeriod))/3600.0)) from Lecture l where l.lecturer.id = :lecturerId")
	Double avgLectureTimeByLecturerId(int lecturerId);

	@Query("select max(abs(time_to_sec(timediff(l.endPeriod,l.startPeriod))/3600.0)) from Lecture l where l.lecturer.id = :lecturerId")
	Double maxLectureTimeByLecturerId(int lecturerId);

	@Query("select min(abs(time_to_sec(timediff(l.endPeriod,l.startPeriod))/3600.0)) from Lecture l where l.lecturer.id = :lecturerId")
	Double minLectureTimeByLecturerId(int lecturerId);

	@Query("select stddev(abs(time_to_sec(timediff(l.endPeriod,l.startPeriod))/3600.0)) from Lecture l where l.lecturer.id = :lecturerId")
	Double deviationLectureTimeByLecturerId(int lecturerId);

	// COURSES

	@Query("select count(c) from Course c where c.lecturer.id = :lecturerId")
	Integer countCoursesByLecturerId(int lecturerId);

	@Query("select sum(abs(time_to_sec(timediff(cl.lecture.endPeriod,cl.lecture.startPeriod))/3600.0)) from CourseLecture cl where cl.course.lecturer.id = :lecturerId group by cl.course.id")
	Collection<Double> getCoursesTimeByLecturerId(int lecturerId);

	public static Double avgCourseTimeByLecturerId(final Collection<Double> coursesTimes) {
		if (!coursesTimes.isEmpty())
			return coursesTimes.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
		return 0.0;
	}

	public static Double maxCourseTimeByLecturerId(final Collection<Double> coursesTimes) {
		if (!coursesTimes.isEmpty())
			return coursesTimes.stream().mapToDouble(Double::doubleValue).max().getAsDouble();
		return 0.0;
	}

	public static Double minCourseTimeByLecturerId(final Collection<Double> coursesTimes) {
		if (!coursesTimes.isEmpty())
			return coursesTimes.stream().mapToDouble(Double::doubleValue).min().getAsDouble();
		return 0.0;
	}

	public static Double deviationCourseTimeByLecturerId(final Collection<Double> coursesTimes) {
		if (!coursesTimes.isEmpty()) {
			final Double avg = LecturerDashboardRepository.avgCourseTimeByLecturerId(coursesTimes);
			final Double squareSum = coursesTimes.stream().mapToDouble(t -> Math.pow(t - avg, 2)).sum();
			return Math.sqrt(squareSum / coursesTimes.size());
		}
		return 0.0;
	}

}

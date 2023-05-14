
package acme.features.lecturer.lecturerDashboard;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.form.LecturerDashboard;
import acme.form.Statistic;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerDashboardShowService extends AbstractService<Lecturer, LecturerDashboard> {

	@Autowired
	protected LecturerDashboardRepository repository;


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int lecturerId;
		lecturerId = super.getRequest().getPrincipal().getActiveRoleId();

		LecturerDashboard dashboard;
		final Statistic lectureTime;
		final Statistic courseTime;
		final Integer totalNumTheoryLectures;

		final Integer countLectures;
		final Double averageLectureTime;
		final Double maxLectureTime;
		final Double minLectureTime;
		final Double deviationLectureTime;

		Collection<Double> coursesTimes;
		final Integer countCourses;
		final Double averageCourseTime;
		final Double maxCourseTime;
		final Double minCourseTime;
		final Double deviationCourseTime;

		// Lectures ------------------------------------------------------------------------

		totalNumTheoryLectures = this.repository.countHandsOnLecturesByLecturerId(lecturerId);

		countLectures = this.repository.countLecturesByLecturerId(lecturerId);
		averageLectureTime = this.repository.avgLectureTimeByLecturerId(lecturerId);
		maxLectureTime = this.repository.maxLectureTimeByLecturerId(lecturerId);
		minLectureTime = this.repository.minLectureTimeByLecturerId(lecturerId);
		deviationLectureTime = this.repository.deviationLectureTimeByLecturerId(lecturerId);

		lectureTime = new Statistic();
		lectureTime.setCount(countLectures);
		lectureTime.setAverage(averageLectureTime);
		lectureTime.setMaximum(maxLectureTime);
		lectureTime.setMinimum(minLectureTime);
		lectureTime.setDeviation(deviationLectureTime);

		// Courses -------------------------------------------------------------------------

		coursesTimes = this.repository.getCoursesTimeByLecturerId(lecturerId);

		countCourses = this.repository.countCoursesByLecturerId(lecturerId);
		averageCourseTime = LecturerDashboardRepository.avgCourseTimeByLecturerId(coursesTimes);
		maxCourseTime = LecturerDashboardRepository.maxCourseTimeByLecturerId(coursesTimes);
		minCourseTime = LecturerDashboardRepository.minCourseTimeByLecturerId(coursesTimes);
		deviationCourseTime = LecturerDashboardRepository.deviationCourseTimeByLecturerId(coursesTimes);

		courseTime = new Statistic();
		courseTime.setCount(countCourses);
		courseTime.setAverage(averageCourseTime);
		courseTime.setMaximum(maxCourseTime);
		courseTime.setMinimum(minCourseTime);
		courseTime.setDeviation(deviationCourseTime);

		// Dashboard -----------------------------------------------------------------------

		dashboard = new LecturerDashboard();
		dashboard.setTotalNumTheoryLectures(totalNumTheoryLectures);
		dashboard.setLectureTime(lectureTime);
		dashboard.setCourseTime(courseTime);

		super.getBuffer().setData(dashboard);
	}

	@Override
	public void unbind(final LecturerDashboard object) {
		Tuple tuple;
		Integer totalNumHandsOnLectures;

		tuple = super.unbind(object, "totalNumTheoryLectures");

		totalNumHandsOnLectures = Math.abs(object.getTotalNumTheoryLectures() - object.getLectureTime().getCount());
		tuple.put("totalNumHandsOnLectures", totalNumHandsOnLectures);

		tuple.put("countLectures", object.getLectureTime().getCount());
		tuple.put("avgLectureTime", object.getLectureTime().getAverage());
		tuple.put("maxLectureTime", object.getLectureTime().getMaximum());
		tuple.put("minLectureTime", object.getLectureTime().getMinimum());
		tuple.put("devLectureTime", object.getLectureTime().getDeviation());

		tuple.put("countCourses", object.getCourseTime().getCount());
		tuple.put("avgCourseTime", object.getCourseTime().getAverage());
		tuple.put("maxCourseTime", object.getCourseTime().getMaximum());
		tuple.put("minCourseTime", object.getCourseTime().getMinimum());
		tuple.put("devCourseTime", object.getCourseTime().getDeviation());

		super.getResponse().setData(tuple);
	}

}

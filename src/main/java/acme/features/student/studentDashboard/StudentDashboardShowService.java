/*
 * StudentDashboardShowService.java
 *
 * Copyright (C) 2012-2023 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.student.studentDashboard;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.form.Statistic;
import acme.form.StudentDashboard;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class StudentDashboardShowService extends AbstractService<Student, StudentDashboard> {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected StudentDashboardRepository repository;


	// AbstractService Interface ----------------------------------------------
	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		boolean status;
		final Student student;
		Principal principal;
		int userAccountId;
		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		student = this.repository.findStudentByUserAccountId(userAccountId);
		status = student != null && principal.hasRole(Student.class);
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		final int studentId;
		final StudentDashboard studentDashboard;
		final Principal principal;
		int userAccountId;
		final Student student;

		final Statistic activityLength;
		final Double averageActivityLength;
		final Double deviationActivityLength;
		final Double minimumActivityLength;
		final Double maximumActivityLength;
		final int countActivity;

		final Statistic courseLength;
		final Double averageCourseLength;
		final Double deviationCourseLength;
		final Double minimumCourseLength;
		final Double maximumCourseLength;
		final int countCourse;

		final Map<String, Integer> activitiesByActivityType;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		student = this.repository.findStudentByUserAccountId(userAccountId);
		studentId = student.getId();

		averageActivityLength = this.repository.findAverageActivityLength(studentId) == null ? 0 : this.repository.findAverageActivityLength(studentId);
		deviationActivityLength = this.repository.findDeviationActivityLength(studentId) == null ? 0 : this.repository.findDeviationActivityLength(studentId);
		minimumActivityLength = this.repository.findMinimumActivityLength(studentId) == null ? 0 : this.repository.findMinimumActivityLength(studentId);
		maximumActivityLength = this.repository.findMaximumActivityLength(studentId) == null ? 0 : this.repository.findMaximumActivityLength(studentId);
		countActivity = this.repository.findCountActivity(studentId);
		activityLength = new Statistic(countActivity, averageActivityLength, minimumActivityLength, maximumActivityLength, deviationActivityLength);

		averageCourseLength = this.repository.averageTimeCoursesByStudentId(studentId) == null ? 0 : this.repository.averageTimeCoursesByStudentId(studentId);
		deviationCourseLength = this.repository.deviationTimeCoursesByStudentId(studentId, averageCourseLength) == null ? 0 : this.repository.deviationTimeCoursesByStudentId(studentId, averageCourseLength);
		minimumCourseLength = this.repository.minimumTimeCoursesOfStudentId(studentId) == null ? 0 : this.repository.minimumTimeCoursesOfStudentId(studentId);
		maximumCourseLength = this.repository.maximumTimeCoursesOfStudentId(studentId) == null ? 0 : this.repository.maximumTimeCoursesOfStudentId(studentId);
		countCourse = this.repository.findCountEnrolment(studentId);
		courseLength = new Statistic(countCourse, averageCourseLength, minimumCourseLength, maximumCourseLength, deviationCourseLength);

		activitiesByActivityType = this.repository.numberOfActivitiesByActivityType(studentId);

		studentDashboard = new StudentDashboard();
		studentDashboard.setTotalActivitiesByActivityType(activitiesByActivityType);
		studentDashboard.setActivityTime(activityLength);
		studentDashboard.setCourseTime(courseLength);
		super.getBuffer().setData(studentDashboard);
	}

	@Override
	public void unbind(final StudentDashboard studentDashboard) {
		Tuple tuple;
		tuple = super.unbind(studentDashboard, "totalActivitiesByActivityType", "courseTime", "activityTime");
		super.getResponse().setData(tuple);
	}
}

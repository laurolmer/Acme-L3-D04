/*
 * StudentEnrolmentUpdateService.java
 *
 * Copyright (C) 2012-2023 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.student.enrolment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.course.Course;
import acme.entities.enrolment.Enrolment;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class StudentEnrolmentUpdateService extends AbstractService<Student, Enrolment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected StudentEnrolmentRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		boolean status;
		status = super.getRequest().hasData("id", int.class);
		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		int enrolmentId;
		final int id;
		Enrolment enrolment;
		id = super.getRequest().getPrincipal().getAccountId();
		enrolmentId = super.getRequest().getData("id", int.class);
		enrolment = this.repository.findEnrolmentById(enrolmentId);
		status = enrolment.isDraftMode() && enrolment.getStudent().getUserAccount().getId() == id;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Enrolment object;
		int enrolmentId;
		enrolmentId = super.getRequest().getData("id", int.class);
		object = this.repository.findEnrolmentById(enrolmentId);
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Enrolment object) {
		assert object != null;
		int studentId;
		Student student;
		int courseId;
		Course course;
		studentId = super.getRequest().getPrincipal().getActiveRoleId();
		student = this.repository.findStudentById(studentId);
		courseId = super.getRequest().getData("course", int.class);
		course = this.repository.findCourseById(courseId);
		super.bind(object, "code", "motivation", "goals");
		object.setStudent(student);
		object.setCourse(course);
	}

	@Override
	public void validate(final Enrolment object) {
		assert object != null;
		Collection<String> codes;
		if (!super.getBuffer().getErrors().hasErrors("code")) {
			codes = this.repository.findAllCodesFromEnrolments();
			super.state(!codes.contains(object.getCode()), "code", "student.enrolment.form.error.code");
		}
	}

	@Override
	public void perform(final Enrolment object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final Enrolment object) {
		assert object != null;
		SelectChoices choices;
		Collection<Course> courses;
		Tuple tuple;
		courses = this.repository.findNotInDraftCourses();
		choices = SelectChoices.from(courses, "code", object.getCourse());
		tuple = super.unbind(object, "code", "motivation", "goals", "course");
		tuple.put("draftMode", object.isDraftMode());
		tuple.put("course", choices.getSelected().getKey());
		tuple.put("courses", choices);
		super.getResponse().setData(tuple);
	}

}

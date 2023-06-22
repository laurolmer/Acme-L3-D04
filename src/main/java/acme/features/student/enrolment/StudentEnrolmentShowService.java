/*
 * StudentEnrolmentShowService.java
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

import acme.entities.activity.Activity;
import acme.entities.course.Course;
import acme.entities.enrolment.Enrolment;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class StudentEnrolmentShowService extends AbstractService<Student, Enrolment> {

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
		status = enrolment.getStudent().getUserAccount().getId() == id;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Enrolment enrolment;
		int id;
		id = super.getRequest().getData("id", int.class);
		enrolment = this.repository.findEnrolmentById(id);
		super.getResponse().setGlobal("draftMode", enrolment.isDraftMode());
		super.getBuffer().setData(enrolment);

	}

	@Override
	public void unbind(final Enrolment object) {
		assert object != null;
		Tuple tuple;
		Double estimatedTotalTime;
		Collection<Course> courses;
		SelectChoices choices;
		final Collection<Activity> activities;
		activities = this.repository.findManyActivitiesByEnrolmentId(object.getId());
		estimatedTotalTime = object.workTime(activities);
		courses = this.repository.findNotInDraftCourses();
		choices = SelectChoices.from(courses, "code", object.getCourse());
		tuple = super.unbind(object, "code", "motivation", "goals", "holderName", "lowerNibble", "draftMode");
		tuple.put("estimatedTotalTime", estimatedTotalTime);
		tuple.put("course", choices.getSelected().getKey());
		tuple.put("courses", choices);
		super.getResponse().setData(tuple);
		tuple.put("objectId", object.getId());
	}
}

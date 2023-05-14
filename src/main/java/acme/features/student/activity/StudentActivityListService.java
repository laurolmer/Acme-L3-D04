/*
 * AuthenticatedConsumerCreateService.java
 *
 * Copyright (C) 2012-2023 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.student.activity;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.activity.Activity;
import acme.entities.enrolment.Enrolment;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class StudentActivityListService extends AbstractService<Student, Activity> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected StudentActivityRepository repository;

	// AbstractService<Authenticated, Consumer> ---------------------------


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("enrolmentId", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {

		boolean status;
		int enrolmentId;
		Enrolment enrolment;
		Student student;
		enrolmentId = super.getRequest().getData("enrolmentId", int.class);
		enrolment = this.repository.findEnrolmentById(enrolmentId);
		student = enrolment == null ? null : enrolment.getStudent();
		status = enrolment != null || super.getRequest().getPrincipal().hasRole(student);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Activity> activities;
		Enrolment enrolment;
		final int enrolmentId = super.getRequest().getData("enrolmentId", int.class);
		activities = this.repository.findAllActivitiesByEnrolment(enrolmentId);
		enrolment = this.repository.findEnrolmentById(enrolmentId);
		super.getResponse().setGlobal("draftMode", enrolment.isDraftMode());
		super.getResponse().setGlobal("enrolmentId", enrolmentId);
		super.getBuffer().setData(activities);
	}

	@Override
	public void unbind(final Activity object) {
		assert object != null;
		Tuple tuple;
		tuple = super.unbind(object, "title", "activityType");
		super.getResponse().setData(tuple);
	}

}

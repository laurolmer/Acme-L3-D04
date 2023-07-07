
/*
 * StudentActivityShowService.java
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.activity.Activity;
import acme.entities.activity.ActivityType;
import acme.entities.enrolment.Enrolment;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class StudentActivityShowService extends AbstractService<Student, Activity> {

	//Internal state ---------------------------------------------------------

	@Autowired
	protected StudentActivityRepository repository;

	//AbstractService<Student, Activity> ---------------------------


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		Activity activity;
		Enrolment enrolment;
		int id;
		int enrolmentId;
		id = super.getRequest().getPrincipal().getAccountId();
		enrolmentId = super.getRequest().getData("id", int.class);
		activity = this.repository.findActivityById(enrolmentId);
		enrolment = activity.getEnrolment();
		status = enrolment.getStudent().getUserAccount().getId() == id && !enrolment.isDraftMode();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Enrolment enrolment;
		Activity activity;
		final int id = super.getRequest().getData("id", int.class);
		activity = this.repository.findActivityById(id);
		enrolment = activity.getEnrolment();
		super.getResponse().setGlobal("draftMode", enrolment.isDraftMode());
		super.getBuffer().setData(activity);
	}

	@Override
	public void unbind(final Activity object) {
		assert object != null;
		Tuple tuple;
		final SelectChoices choices = SelectChoices.from(ActivityType.class, object.getActivityType());
		tuple = super.unbind(object, "title", "abstractActivity", "activityType", "startPeriod", "endPeriod", "link");
		tuple.put("choicesActivityType", choices);

		super.getResponse().setData(tuple);
	}

}

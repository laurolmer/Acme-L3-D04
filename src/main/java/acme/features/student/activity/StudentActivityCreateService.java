
/*
 * StudentActivityCreateService.java
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

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.activity.Activity;
import acme.entities.activity.ActivityType;
import acme.entities.enrolment.Enrolment;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class StudentActivityCreateService extends AbstractService<Student, Activity> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected StudentActivityRepository repository;

	// AbstractService<Student, Activity> ---------------------------


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
		final int id;
		Enrolment enrolment;
		id = super.getRequest().getPrincipal().getAccountId();
		enrolmentId = super.getRequest().getData("enrolmentId", int.class);
		enrolment = this.repository.findEnrolmentById(enrolmentId);
		status = enrolment != null && enrolment.getStudent().getUserAccount().getId() == id;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		final Activity activity = new Activity();

		final int enrolmentId = super.getRequest().getData("enrolmentId", int.class);
		final Enrolment enrolment = this.repository.findEnrolmentById(enrolmentId);

		activity.setEnrolment(enrolment);

		super.getBuffer().setData(activity);
	}
	@Override
	public void bind(final Activity object) {
		assert object != null;

		super.bind(object, "title", "abstractActivity", "activityType", "startPeriod", "endPeriod", "link");
	}

	@Override
	public void validate(final Activity object) {
		assert object != null;
		final Date maxValue = new Date("2100/12/31 23:59");
		final Date minValue = new Date("2000/01/01 00:00");
		if (!super.getBuffer().getErrors().hasErrors("startPeriod") && !super.getBuffer().getErrors().hasErrors("endPeriod")) {
			final boolean validPeriod = MomentHelper.isAfter(object.getEndPeriod(), object.getStartPeriod());
			super.state(validPeriod, "endPeriod", "student.activity.form.error.validPeriod");
		}
		if (!super.getBuffer().getErrors().hasErrors("endPeriod"))
			super.state(!MomentHelper.isAfter(object.getEndPeriod(), maxValue), "endPeriod", "student.activity.error.end-reached-max-value");

		if (!super.getBuffer().getErrors().hasErrors("startPeriod"))
			super.state(MomentHelper.isAfter(object.getStartPeriod(), minValue), "startPeriod", "student.activity.error.start-didnot-reach-min-value");
	}

	@Override
	public void perform(final Activity object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Activity object) {
		assert object != null;
		Tuple tuple;

		final SelectChoices choices = SelectChoices.from(ActivityType.class, object.getActivityType());

		tuple = super.unbind(object, "title", "abstractActivity", "activityType", "startPeriod", "endPeriod", "link");
		tuple.put("choicesActivityType", choices);

		final int enrolmentId = super.getRequest().getData("enrolmentId", int.class);
		super.getResponse().setGlobal("enrolmentId", enrolmentId);

		super.getResponse().setData(tuple);
	}

}

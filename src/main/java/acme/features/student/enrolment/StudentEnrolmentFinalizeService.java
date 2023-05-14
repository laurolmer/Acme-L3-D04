/*
 * StudentEnrolmentFinalizeService.java
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.course.Course;
import acme.entities.enrolment.Enrolment;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class StudentEnrolmentFinalizeService extends AbstractService<Student, Enrolment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected StudentEnrolmentRepository repository;

	// AbstractService<Student, Enrolment> ---------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void check() {
		super.getResponse().setChecked(true);
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
		super.bind(object, "holderName", "lowerNibble");

	}

	@Override
	public void validate(final Enrolment object) {
		assert object != null;

		final String creditCard;
		final String cvc;
		final String expiryDate;

		creditCard = super.getRequest().getData("creditCard", String.class);
		if (!creditCard.matches("^\\d{4}\\/\\d{4}\\/\\d{4}\\/\\d{4}$"))
			super.state(false, "creditCard", "student.enrolment.form.error.card");

		if (!super.getBuffer().getErrors().hasErrors("holderName"))
			super.state(!object.getHolderName().isEmpty(), "holderName", "student.enrolment.form.error.holder");

		cvc = super.getRequest().getData("cvc", String.class);
		if (!cvc.matches("^\\d{3}$"))
			super.state(false, "cvc", "student.enrolment.form.error.cvc");

		expiryDate = super.getRequest().getData("expiryDate", String.class);
		final DateFormat format = new SimpleDateFormat("MM/yy");
		try {
			final Date date = format.parse(expiryDate);
			final int month = Integer.parseInt(expiryDate.split("/")[0]);
			if (month < 1 || month > 12)
				super.state(false, "expiryDate", "student.enrolment.form.error.expiryDate.month");
			if (MomentHelper.isBefore(date, MomentHelper.getCurrentMoment()))
				super.state(false, "expiryDate", "student.enrolment.form.error.expiryDate.before");
		} catch (final ParseException e) {
			super.state(false, "expiryDate", "student.enrolment.form.error.expiryDate.pattern");
		}
	}

	@Override
	public void perform(final Enrolment object) {
		assert object != null;
		String creditCard;
		object.setDraftMode(false);
		creditCard = super.getRequest().getData("creditCard", String.class);
		object.setLowerNibble(creditCard.substring(creditCard.length() - 4));

		this.repository.save(object);

	}

	@Override
	public void unbind(final Enrolment object) {
		assert object != null;
		final String creditCard = "";
		final String cvc = "";
		final String expiryDate = "";
		SelectChoices choices;
		Collection<Course> courses;
		Tuple tuple;
		courses = this.repository.findNotInDraftCourses();
		choices = SelectChoices.from(courses, "code", object.getCourse());
		tuple = super.unbind(object, "code", "motivation", "goals", "course", "holderName");
		tuple.put("draftMode", object.isDraftMode());
		tuple.put("creditCard", creditCard);
		tuple.put("cvc", cvc);
		tuple.put("expiryDate", expiryDate);
		tuple.put("course", choices.getSelected().getKey());
		tuple.put("courses", choices);
		super.getResponse().setData(tuple);
	}
}

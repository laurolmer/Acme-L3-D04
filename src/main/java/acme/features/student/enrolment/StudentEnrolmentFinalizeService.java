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
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

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

		String creditCard;
		final String cvc;
		final String expiryDate;
		int enrolmentId;
		boolean isSingleEnrollmentAllowed = false;

		creditCard = super.getRequest().getData("creditCard", String.class);
		creditCard = creditCard.replaceAll("\\D", "");
		if (!StudentEnrolmentFinalizeService.validateCreditCard(creditCard) || !creditCard.matches("\\d+") || creditCard.isEmpty())
			super.state(false, "creditCard", "student.enrolment.form.error.card");

		if (!super.getBuffer().getErrors().hasErrors("holderName"))
			super.state(!object.getHolderName().isEmpty(), "holderName", "student.enrolment.form.error.holder");

		cvc = super.getRequest().getData("cvc", String.class);
		if (cvc.length() < 1 || cvc.length() > 3 || !cvc.matches("\\d+"))
			super.state(false, "cvc", "student.enrolment.form.error.cvc");

		expiryDate = super.getRequest().getData("expiryDate", String.class);
		final Locale local = super.getRequest().getLocale();
		final String localString = local.equals(Locale.ENGLISH) ? "yy/MM" : "MM/yy";
		final DateFormat formate = new SimpleDateFormat(localString);
		try {
			final Date date = formate.parse(expiryDate);
			final Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			final int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			calendar.set(Calendar.DAY_OF_MONTH, lastDayOfMonth);
			final Date lastDayDate = calendar.getTime();
			final int i = local.equals(Locale.ENGLISH) ? 1 : 0;
			final int month = Integer.parseInt(expiryDate.split("/")[i]);
			if (month < 1 || month > 12)
				super.state(false, "expiryDate", "student.enrolment.form.error.expiryDate.month");
			if (MomentHelper.isBefore(lastDayDate, MomentHelper.getCurrentMoment()))
				super.state(false, "expiryDate", "student.enrolment.form.error.expiryDate.before");
		} catch (final ParseException e) {
			super.state(false, "expiryDate", "student.enrolment.form.error.expiryDate.pattern");
		}
		enrolmentId = super.getRequest().getData("id", int.class);
		final Course course = this.repository.findEnrolmentById(enrolmentId).getCourse();
		final Collection<Enrolment> enrolmentsCourse = this.repository.findEnrolmentsByCourseId(course.getId());
		for (final Enrolment enrolment : enrolmentsCourse)
			if (!enrolment.isDraftMode()) {
				isSingleEnrollmentAllowed = true;
				break;
			}
		if (isSingleEnrollmentAllowed)
			super.state(false, "*", "student.enrolment.form.error.enrolmentAllowed");
	}

	@Override
	public void perform(final Enrolment object) {
		assert object != null;
		String creditCard;
		object.setDraftMode(false);
		creditCard = super.getRequest().getData("creditCard", String.class);
		creditCard = creditCard.replaceAll("\\D", "");
		object.setLowerNibble(creditCard.substring(creditCard.length() - 4));

		this.repository.save(object);

	}

	@Override
	public void unbind(final Enrolment object) {
		assert object != null;
		final String creditCard = super.getRequest().getData("creditCard", String.class);
		final String cvc = super.getRequest().getData("cvc", String.class);
		final String expiryDate = super.getRequest().getData("expiryDate", String.class);
		final String estimatedTotalTime = super.getRequest().getData("estimatedTotalTime", String.class);
		SelectChoices choices;
		Collection<Course> courses;
		Tuple tuple;
		courses = this.repository.findNotInDraftCourses();
		choices = SelectChoices.from(courses, "code", object.getCourse());
		tuple = super.unbind(object, "code", "motivation", "goals", "course", "holderName");
		tuple.put("estimatedTotalTime", estimatedTotalTime);
		tuple.put("draftMode", object.isDraftMode());
		tuple.put("creditCard", creditCard);
		tuple.put("cvc", cvc);
		tuple.put("expiryDate", expiryDate);
		tuple.put("course", choices.getSelected().getKey());
		tuple.put("courses", choices);
		super.getResponse().setData(tuple);
	}

	//Metodos auxiliares ----------------------------------------------------
	public static boolean validateCreditCard(final String creditCardNumber) {
		// Eliminar espacios en blanco y caracteres no numéricos

		int sum = 0;
		boolean alternate = false;

		// Iterar sobre los dígitos de derecha a izquierda
		for (int i = creditCardNumber.length() - 1; i >= 0; i--) {
			int digit = Integer.parseInt(creditCardNumber.substring(i, i + 1));

			if (alternate) {
				digit *= 2;
				if (digit > 9)
					digit = digit % 10 + 1;
			}

			sum += digit;
			alternate = !alternate;
		}

		// La suma total debe ser divisible por 10 para que el número sea válido
		return sum % 10 == 0;
	}
}

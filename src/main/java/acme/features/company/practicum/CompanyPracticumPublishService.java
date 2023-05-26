
package acme.features.company.practicum;

import java.time.Duration;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.course.Course;
import acme.entities.practicum.Practicum;
import acme.entities.practicumSession.PracticumSession;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanyPracticumPublishService extends AbstractService<Company, Practicum> {

	// Internal state --------------------------------------------------------
	@Autowired
	protected CompanyPracticumRepository repository;


	// AbstractService interface ---------------------------------------------
	@Override
	public void check() {
		boolean status;
		status = super.getRequest().hasData("id", int.class);
		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		int PracticumId;
		Practicum Practicum;
		Company Company;

		PracticumId = super.getRequest().getData("id", int.class);
		Practicum = this.repository.findPracticumById(PracticumId);
		Company = Practicum == null ? null : Practicum.getCompany();

		status = Practicum != null && !Practicum.getDraftMode() || super.getRequest().getPrincipal().hasRole(Company);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Practicum object;
		int id;
		id = super.getRequest().getData("id", int.class);
		object = this.repository.findPracticumById(id);
		object.setDraftMode(false);
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Practicum object) {
		assert object != null;
		int CompanyId;
		Company Company;
		int courseId;
		Course course;
		CompanyId = super.getRequest().getPrincipal().getActiveRoleId();
		Company = this.repository.findCompanyById(CompanyId);
		courseId = super.getRequest().getData("course", int.class);
		course = this.repository.findCourseById(courseId);
		super.bind(object, "code", "title", "abstractPracticum", "goals", "draftMode");
		object.setCompany(Company);
		object.setCourse(course);
	}

	@Override
	public void validate(final Practicum object) {
		assert object != null;
		int id;
		final Practicum otherPracticum;
		boolean hasSession;

		// El código de un Practicum debe ser único.
		if (!super.getBuffer().getErrors().hasErrors("code")) {
			id = super.getRequest().getData("id", int.class);
			otherPracticum = this.repository.findAPracticumByCode(object.getCode());
			super.state(otherPracticum == null || otherPracticum.getCode().equals(object.getCode()) && otherPracticum.getId() == object.getId(), "code", "Company.Practicum.form.error.code-uniqueness");

			hasSession = !this.repository.findSessionsByPracticumId(id).isEmpty();
			super.state(hasSession, "code", "company.practicum.form.error.praticum-without-session");
		}

		// Únicamente se deberá de comprobar que el tiempo estimado es correcto cuando se va a publicar.
		if (!super.getBuffer().getErrors().hasErrors("estimatedTimeInHours")) {
			Collection<PracticumSession> sessions;
			final double estimatedTimeInHours;
			double totalHours;
			boolean moreThan90Percent;
			boolean lessThan110Percent;

			sessions = this.repository.findSessionsByPracticumId(object.getId());

			final Double estimatedTotalTime = object.computeEstimatedTotalTime(sessions);
			totalHours = sessions.stream().mapToDouble(session -> {
				Date start;
				Date end;
				Duration duration;

				start = session.getStart();
				end = session.getEnd();
				duration = MomentHelper.computeDuration(start, end);

				return duration.toHours();
			}).sum();

			moreThan90Percent = totalHours >= estimatedTotalTime * 0.9;
			lessThan110Percent = totalHours <= estimatedTotalTime * 1.1;

			super.state(moreThan90Percent && lessThan110Percent, "estimatedTotalTime", "company.practicum.form.error.not-in-range");
		}
	}

	@Override
	public void perform(final Practicum object) {
		assert object != null;
		object.setDraftMode(false);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Practicum object) {
		assert object != null;
		SelectChoices choices;
		Collection<Course> courses;
		Tuple tuple;
		courses = this.repository.findNotInDraftCourses();
		choices = SelectChoices.from(courses, "code", object.getCourse());
		tuple = super.unbind(object, "code", "title", "abstractPracticum", "goals", "draftMode");
		tuple.put("draftMode", object.getDraftMode());
		tuple.put("course", choices.getSelected().getKey());
		tuple.put("courses", choices);
		super.getResponse().setData(tuple);
	}

}

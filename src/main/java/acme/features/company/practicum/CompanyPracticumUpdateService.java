
package acme.features.company.practicum;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.course.Course;
import acme.entities.practicum.Practicum;
import acme.features.authenticated.company.AuthenticatedCompanyRepository;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanyPracticumUpdateService extends AbstractService<Company, Practicum> {

	// Internal state --------------------------------------------------------
	@Autowired
	protected CompanyPracticumRepository		repository;

	@Autowired
	protected AuthenticatedCompanyRepository	repositoryCompany;


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

		status = Practicum != null && Practicum.getDraftMode() == false || super.getRequest().getPrincipal().hasRole(Company);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Practicum object;
		int id;
		id = super.getRequest().getData("id", int.class);
		object = this.repository.findPracticumById(id);

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
		final Practicum oldPracticum;
		final boolean areCodesEqual;
		// El código de un Practicum debe ser único.
		if (!super.getBuffer().getErrors().hasErrors("code")) {
			id = super.getRequest().getData("id", int.class);
			//oldPracticum = this.repository.findAllPracticums().stream().filter(t -> t.getId() == id).findFirst().orElse(null);
			otherPracticum = this.repository.findAPracticumByCode(object.getCode());
			//areCodesEqual = oldPracticum.getCode().equals(object.getCode());
			super.state(otherPracticum == null || otherPracticum.getCode().equals(object.getCode()) && otherPracticum.getId() == object.getId(), "code", "Company.Practicum.form.error.code-uniqueness");
		}
	}

	@Override
	public void perform(final Practicum object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final Practicum object) {
		assert object != null;
		SelectChoices choices;
		Collection<Course> courses;
		Tuple tuple;
		courses = this.repository.findNotInDraftCourses();
		choices = SelectChoices.from(courses, "title", object.getCourse());
		tuple = super.unbind(object, "code", "title", "abstractPracticum", "goals", "draftMode");
		tuple.put("course", choices.getSelected().getKey());
		tuple.put("courses", choices);
		tuple.put("draftMode", object.getDraftMode());
		super.getResponse().setData(tuple);

	}
}

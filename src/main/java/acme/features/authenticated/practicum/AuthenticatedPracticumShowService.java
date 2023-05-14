
package acme.features.authenticated.practicum;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.course.Course;
import acme.entities.practicum.Practicum;
import acme.framework.components.accounts.Authenticated;
import acme.framework.components.accounts.Principal;
import acme.framework.components.accounts.UserAccount;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;

@Service
public class AuthenticatedPracticumShowService extends AbstractService<Authenticated, Practicum> {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected AuthenticatedPracticumRepository repository;

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
		int practicumId;
		Practicum practicum;
		Principal principal;
		int userAccountId;
		UserAccount userAccount;

		practicumId = super.getRequest().getData("id", int.class);
		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		userAccount = this.repository.findOneUserAccountById(userAccountId);
		practicum = this.repository.findOnePracticumById(practicumId);
		status = !practicum.getDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int practicumId;
		Practicum practicum;

		practicumId = super.getRequest().getData("id", int.class);
		practicum = this.repository.findOnePracticumById(practicumId);
		super.getBuffer().setData(practicum);
	}

	@Override
	public void unbind(final Practicum practicum) {
		assert practicum != null;

		Collection<Course> courses;
		SelectChoices choices;
		Tuple tuple;

		courses = this.repository.findAllCourses();
		choices = SelectChoices.from(courses, "title", practicum.getCourse());
		tuple = super.unbind(practicum, "code", "title", "abstractPracticum", "goals", "draftMode");
		tuple.put("course", choices);
		tuple.put("courses", courses);
		tuple.put("nameCompany", practicum.getCompany().getName());

		super.getResponse().setData(tuple);
	}
	//	@Override
	//	public void unbind(final Practicum object) {
	//		assert object != null;
	//		Tuple tuple;
	//		tuple = super.unbind(object, "code", "title", "abstractPracticum", "goals", "draftMode");
	//		super.getResponse().setData(tuple);
	//	}

}

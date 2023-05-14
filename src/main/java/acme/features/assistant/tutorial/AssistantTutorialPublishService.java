
package acme.features.assistant.tutorial;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.course.Course;
import acme.entities.tutorial.Tutorial;
import acme.entities.tutorialSession.TutorialSession;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantTutorialPublishService extends AbstractService<Assistant, Tutorial> {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected AssistantTutorialRepository repository;


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
		int tutorialId;
		Tutorial tutorial;
		Assistant assistant;
		tutorialId = super.getRequest().getData("id", int.class);
		tutorial = this.repository.findTutorialById(tutorialId);
		assistant = tutorial == null ? null : tutorial.getAssistant();
		status = tutorial != null && tutorial.isDraftMode() == false || super.getRequest().getPrincipal().hasRole(assistant);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Tutorial object;
		int id;
		id = super.getRequest().getData("id", int.class);
		object = this.repository.findTutorialById(id);
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Tutorial object) {
		assert object != null;
		int assistantId;
		Assistant assistant;
		int courseId;
		Course course;
		assistantId = super.getRequest().getPrincipal().getActiveRoleId();
		assistant = this.repository.findAssistantById(assistantId);
		courseId = super.getRequest().getData("course", int.class);
		course = this.repository.findCourseById(courseId);
		super.bind(object, "code", "title", "abstractTutorial", "goals");
		object.setAssistant(assistant);
		object.setCourse(course);
	}

	@Override
	public void validate(final Tutorial object) {
		assert object != null;
		int id;
		final Tutorial otherTutorial;
		// El código de un tutorial debe ser único.
		if (!super.getBuffer().getErrors().hasErrors("code")) {
			id = super.getRequest().getData("id", int.class);
			otherTutorial = this.repository.findATutorialByCode(object.getCode());
			super.state(otherTutorial == null || otherTutorial.getCode().equals(object.getCode()) && otherTutorial.getId() == object.getId(), "code", "assistant.tutorial.form.error.code-uniqueness");
		}
		if (!super.getBuffer().getErrors().hasErrors("draftMode")) {
			final boolean draftMode = object.isDraftMode();
			super.state(draftMode, "draftMode", "assistant.tutorial.form.error.draftMode-published");
		}
	}

	@Override
	public void perform(final Tutorial object) {
		assert object != null;
		object.setDraftMode(false);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Tutorial object) {
		assert object != null;
		SelectChoices choices;
		Collection<Course> courses;
		Tuple tuple;
		final Collection<TutorialSession> sessions;
		final Double estimatedTotalTime;

		courses = this.repository.findNotInDraftCourses();
		choices = SelectChoices.from(courses, "title", object.getCourse());
		sessions = this.repository.findSessionsByTutorialId(object.getId());
		estimatedTotalTime = object.computeEstimatedTotalTime(sessions);
		tuple = super.unbind(object, "code", "title", "abstractTutorial", "goals");
		tuple.put("course", choices.getSelected().getKey());
		tuple.put("courses", choices);
		tuple.put("estimatedTotalTime", estimatedTotalTime);
		tuple.put("draftMode", object.isDraftMode());
		super.getResponse().setData(tuple);
	}
}

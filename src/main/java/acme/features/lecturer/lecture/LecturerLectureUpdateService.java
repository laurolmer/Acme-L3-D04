
package acme.features.lecturer.lecture;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.lecture.Lecture;
import acme.entities.lecture.LectureType;
import acme.framework.components.accounts.Principal;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerLectureUpdateService extends AbstractService<Lecturer, Lecture> {

	@Autowired
	protected LecturerLectureRepository repository;


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		Lecture object;
		int objectId;
		final Principal principal;
		final int userAccountId;

		objectId = super.getRequest().getData("id", int.class);
		object = this.repository.findOneLectureById(objectId);
		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();

		status = object.getLecturer().getUserAccount().getId() == userAccountId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Lecture object;
		int objectId;

		objectId = super.getRequest().getData("id", int.class);
		object = this.repository.findOneLectureById(objectId);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Lecture object) {
		assert object != null;

		final double estimatedLearningTime;
		final Date endPeriod;
		int activeRoleId;
		Lecturer lecturer;

		activeRoleId = super.getRequest().getPrincipal().getActiveRoleId();
		lecturer = this.repository.findOneLecturerById(activeRoleId);

		estimatedLearningTime = super.getRequest().getData("endPeriod", Double.class);
		endPeriod = object.deltaFromStartMoment(estimatedLearningTime);

		super.bind(object, "title", "lectureAbstract", "body", "lectureType", "link");
		object.setEndPeriod(endPeriod);
		object.setDraftMode(true);
		object.setLecturer(lecturer);
	}

	@Override
	public void validate(final Lecture object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("draftMode")) {
			final boolean draftMode = object.isDraftMode();
			super.state(draftMode, "draftMode", "lecturer.lecture.error.draftMode.published");
		}

		if (!super.getBuffer().getErrors().hasErrors("endPeriod")) {
			final boolean eval = object.computeEstimatedLearningTime() > 0.0;
			super.state(eval, "endPeriod", "lecturer.lecture.error.estimatedLearningTime");
		}
	}

	@Override
	public void perform(final Lecture object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Lecture object) {
		assert object != null;
		Tuple tuple;
		final SelectChoices choices;
		Double estimatedLearningTime;

		choices = SelectChoices.from(LectureType.class, object.getLectureType());

		tuple = super.unbind(object, "id", "title", "lectureAbstract", "body", "lectureType", "link");

		estimatedLearningTime = object.computeEstimatedLearningTime();
		if (estimatedLearningTime != null)
			tuple.put("endPeriod", estimatedLearningTime);

		tuple.put("lectureType", choices.getSelected().getKey());
		tuple.put("lectureTypes", choices);
		tuple.put("draftMode", object.isDraftMode());
		super.getResponse().setData(tuple);
	}

}

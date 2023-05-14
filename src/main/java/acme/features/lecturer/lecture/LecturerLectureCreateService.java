
package acme.features.lecturer.lecture;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.lecture.Lecture;
import acme.entities.lecture.LectureType;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.controllers.HttpMethod;
import acme.framework.helpers.MomentHelper;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerLectureCreateService extends AbstractService<Lecturer, Lecture> {

	@Autowired
	protected LecturerLectureRepository repository;


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		boolean status;
		status = super.getRequest().getPrincipal().hasRole(Lecturer.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Lecture object;
		Lecturer lecturer;
		int userAccountId;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		lecturer = this.repository.findOneLecturerById(userAccountId);

		object = new Lecture();
		object.setLecturer(lecturer);
		object.setStartPeriod(MomentHelper.getCurrentMoment());
		object.setDraftMode(true);
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Lecture object) {
		assert object != null;

		double estimatedLearningTime;
		Date endPeriod;
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

		tuple = super.unbind(object, "title", "lectureAbstract", "body", "lectureType", "link");

		estimatedLearningTime = object.computeEstimatedLearningTime();
		if (estimatedLearningTime != null)
			tuple.put("endPeriod", estimatedLearningTime);

		choices = SelectChoices.from(LectureType.class, object.getLectureType());
		tuple.put("lectureType", choices.getSelected().getKey());
		tuple.put("lectureTypes", choices);
		tuple.put("draftMode", object.isDraftMode());
		super.getResponse().setData(tuple);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();
	}

}

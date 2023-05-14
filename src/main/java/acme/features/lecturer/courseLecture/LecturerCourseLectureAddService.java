
package acme.features.lecturer.courseLecture;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.course.Course;
import acme.entities.course.CourseLecture;
import acme.entities.lecture.Lecture;
import acme.framework.components.accounts.Principal;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.controllers.HttpMethod;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerCourseLectureAddService extends AbstractService<Lecturer, CourseLecture> {

	@Autowired
	protected LecturerCourseLectureRepository repository;


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("courseId", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		final boolean status;
		final Principal principal;
		final int userAccountId;
		int objectId;
		final Course object;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		objectId = super.getRequest().getData("courseId", int.class);
		object = this.repository.findOneCourseById(objectId);

		status = object.getLecturer().getUserAccount().getId() == userAccountId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		CourseLecture object;
		final int courseId;
		final Course course;

		courseId = super.getRequest().getData("courseId", int.class);
		course = this.repository.findOneCourseById(courseId);

		object = new CourseLecture();
		object.setCourse(course);
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final CourseLecture object) {
		assert object != null;
		int lectureId;
		Lecture lecture;
		int courseId;
		Course course;

		lectureId = super.getRequest().getData("lectureId", int.class);
		lecture = this.repository.findOneLectureById(lectureId);
		courseId = super.getRequest().getData("courseId", int.class);
		course = this.repository.findOneCourseById(courseId);

		super.bind(object, "id");
		object.setLecture(lecture);
		object.setCourse(course);
	}

	@Override
	public void validate(final CourseLecture object) {
		assert object != null;
		final boolean lectureInDraftMode;
		final boolean courseInDraftMode;

		lectureInDraftMode = this.repository.isLectureInDraftModeByCourseId(object.getLecture().getId());
		courseInDraftMode = this.repository.isCourseInDraftModeByCourseId(object.getCourse().getId());

		super.state(!lectureInDraftMode, "*", "lecturer.course-lecture.error.lecture.published");
		super.state(courseInDraftMode, "*", "lecturer.course-lecture.error.course.published.add");

	}

	@Override
	public void perform(final CourseLecture object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final CourseLecture object) {
		assert object != null;
		Tuple tuple;
		int courseId;
		int lecturerId;
		Course course;
		Collection<Lecture> lecturesAvailables;
		final SelectChoices choices;

		courseId = super.getRequest().getData("courseId", int.class);
		lecturerId = super.getRequest().getPrincipal().getActiveRoleId();
		course = this.repository.findOneCourseById(courseId);
		lecturesAvailables = this.repository.findPublishedLecturesByLecturerId(lecturerId);
		choices = SelectChoices.from(lecturesAvailables, "title", object.getLecture());

		tuple = super.unbind(object, "course", "lecture");
		tuple.put("lecture", choices.getSelected().getKey());
		tuple.put("lectures", choices);
		tuple.put("courseId", courseId);
		tuple.put("courseCode", course.getCode());
		super.getResponse().setData(tuple);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();
	}

}

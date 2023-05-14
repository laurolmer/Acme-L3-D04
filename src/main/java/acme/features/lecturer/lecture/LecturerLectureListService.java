
package acme.features.lecturer.lecture;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.course.Course;
import acme.entities.lecture.Lecture;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerLectureListService extends AbstractService<Lecturer, Lecture> {

	@Autowired
	protected LecturerLectureRepository repository;


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("courseId", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		Course object;
		int courseId;
		final Principal principal;
		final int userAccountId;

		courseId = super.getRequest().getData("courseId", int.class);
		object = this.repository.findOneCourseById(courseId);
		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();

		status = object.getLecturer().getUserAccount().getId() == userAccountId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		final Collection<Lecture> objects;
		int courseId;

		courseId = super.getRequest().getData("courseId", int.class);
		objects = this.repository.findLecturesByCourseId(courseId);

		super.getBuffer().setData(objects);
	}

	@Override
	public void unbind(final Lecture object) {
		assert object != null;
		Tuple tuple;
		final int courseId;
		final Course course;
		final boolean showAddToCourse;
		final double estimatedLearningTime;
		final Principal principal;
		final int userAccountId;

		tuple = super.unbind(object, "title", "lectureAbstract");

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();

		courseId = super.getRequest().getData("courseId", int.class);
		course = this.repository.findOneCourseById(courseId);
		showAddToCourse = course.isDraftMode() && course.getLecturer().getUserAccount().getId() == userAccountId;

		estimatedLearningTime = object.computeEstimatedLearningTime();

		tuple.put("estimatedLearningTime", estimatedLearningTime);
		super.getResponse().setGlobal("courseId", courseId);
		super.getResponse().setGlobal("showAddToCourse", showAddToCourse);
		super.getResponse().setData(tuple);
	}

	@Override
	public void unbind(final Collection<Lecture> object) {
		assert object != null;
		int courseId;
		final Course course;
		final boolean showAddToCourse;
		final boolean showDeleteOfCourse;
		final Principal principal;
		final int userAccountId;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();

		courseId = super.getRequest().getData("courseId", int.class);
		course = this.repository.findOneCourseById(courseId);
		showAddToCourse = course.isDraftMode() && course.getLecturer().getUserAccount().getId() == userAccountId;
		showDeleteOfCourse = course.isDraftMode() && course.getLecturer().getUserAccount().getId() == userAccountId && this.repository.hasACourseAnyLecture(courseId);

		super.getResponse().setGlobal("courseId", courseId);
		super.getResponse().setGlobal("showAddToCourse", showAddToCourse);
		super.getResponse().setGlobal("showDeleteOfCourse", showDeleteOfCourse);
	}

}


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
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerCourseLectureDeleteService extends AbstractService<Lecturer, CourseLecture> {

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
		final Collection<CourseLecture> objectLectures;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		objectId = super.getRequest().getData("courseId", int.class);
		object = this.repository.findOneCourseById(objectId);
		objectLectures = this.repository.findCourseLecturesByCourseId(objectId);

		status = object.getLecturer().getUserAccount().getId() == userAccountId && !objectLectures.isEmpty();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		final CourseLecture object;
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
		final int lectureId;
		final Lecture lecture;
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
		final boolean courseInDraftMode;
		boolean validLectureId = false;

		if (!super.getBuffer().getErrors().hasErrors("course"))
			super.state(object.getCourse().isDraftMode(), "course", "lecturer.course-lecture.error.course.published.delete");

		if (!super.getBuffer().getErrors().hasErrors("lectureId")) {
			final Integer lectureId = super.getRequest().getData("lectureId", Integer.class);
			validLectureId = lectureId != null && lectureId > 0;
			super.state(validLectureId, "*", "lecturer.course-lecture.error.lectureIdNull");
		}

		if (validLectureId) {
			courseInDraftMode = this.repository.isCourseInDraftModeByCourseId(object.getCourse().getId());
			super.state(courseInDraftMode, "*", "lecturer.course-lecture.error.course.published.delete");
		}
	}

	@Override
	public void perform(final CourseLecture object) {
		assert object != null;
		final CourseLecture courseLecture;
		final int lectureId;
		final int courseId;

		lectureId = object.getLecture().getId();
		courseId = object.getCourse().getId();
		courseLecture = this.repository.findOneCourseLectureByLectureIdAndCourseId(lectureId, courseId);

		this.repository.delete(courseLecture);
	}

	@Override
	public void unbind(final CourseLecture object) {
		assert object != null;
		Tuple tuple;
		int courseId;
		Course course;
		final Collection<Lecture> lecturesInCourse;
		final SelectChoices choices;

		courseId = super.getRequest().getData("courseId", int.class);
		course = this.repository.findOneCourseById(courseId);
		lecturesInCourse = this.repository.findLecturesByCourseId(courseId);
		choices = SelectChoices.from(lecturesInCourse, "title", object.getLecture());

		tuple = super.unbind(object, "course", "lecture");
		tuple.put("lecture", choices.getSelected().getKey());
		tuple.put("lectures", choices);
		tuple.put("courseId", courseId);
		tuple.put("courseCode", course.getCode());
		super.getResponse().setData(tuple);
	}
}

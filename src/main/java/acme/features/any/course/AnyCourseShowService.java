
package acme.features.any.course;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.course.Course;
import acme.entities.course.CourseType;
import acme.entities.lecture.Lecture;
import acme.framework.components.accounts.Any;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;

@Service
public class AnyCourseShowService extends AbstractService<Any, Course> {

	@Autowired
	protected AnyCourseRepository repository;

	// AbstractService interface -----------------------------------------


	@Override
	public void check() {
		boolean status;
		status = super.getRequest().hasData("id", int.class);
		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Course object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findCourseById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void unbind(final Course object) {
		assert object != null;
		Tuple tuple;
		final Collection<Lecture> lectures;
		final CourseType courseType;
		final Double estimatedTotalTime;

		lectures = this.repository.findLecturesByCourseId(object.getId());
		courseType = object.computeCourseType(lectures);
		estimatedTotalTime = object.computeEstimatedTotalTime(lectures);

		tuple = super.unbind(object, "code", "title", "courseAbstract", "retailPrice", "link");
		tuple.put("courseType", courseType);
		tuple.put("estimatedTotalTime", estimatedTotalTime);
		tuple.put("published", !object.isDraftMode());
		super.getResponse().setData(tuple);
	}
}

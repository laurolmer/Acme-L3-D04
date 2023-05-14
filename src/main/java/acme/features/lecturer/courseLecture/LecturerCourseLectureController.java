
package acme.features.lecturer.courseLecture;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.entities.course.CourseLecture;
import acme.framework.controllers.AbstractController;
import acme.roles.Lecturer;

@Controller
public class LecturerCourseLectureController extends AbstractController<Lecturer, CourseLecture> {

	@Autowired
	protected LecturerCourseLectureAddService		addService;

	@Autowired
	protected LecturerCourseLectureDeleteService	deleteService;


	@PostConstruct
	public void initialise() {
		super.addBasicCommand("delete", this.deleteService);

		super.addCustomCommand("add", "create", this.addService);
	}

}

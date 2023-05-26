
package acme.testing.lecturer.lecture;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.course.Course;
import acme.testing.TestHarness;
import acme.testing.lecturer.course.LecturerCourseRepositoryTest;

public class LecturerLectureShowTest extends TestHarness {

	@Autowired
	protected LecturerCourseRepositoryTest repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lecture/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String title, final String abstractLecture, final String lectureLearningTime, final String details, final String type, final String published) {
		super.signIn("lecturer2", "lecturer2");

		super.clickOnMenu("Lecturer", "List my lectures");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.checkInputBoxHasValue("published", published);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("lectureAbstract", abstractLecture);
		super.checkInputBoxHasValue("endPeriod", lectureLearningTime);
		super.checkInputBoxHasValue("body", details);
		super.checkInputBoxHasValue("lectureType", type);

		if (!Boolean.getBoolean(published)) {
			super.checkSubmitExists("Update");
			super.checkSubmitExists("Delete");
			super.checkSubmitExists("Publish");
		} else {
			super.checkNotSubmitExists("Update");
			super.checkNotSubmitExists("Delete");
			super.checkNotSubmitExists("Publish");
		}
		super.checkLinkExists("Return");

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT:there aren't any negative tests for this feature since it's a show
		// that doesn't involve entering anydata into any forms.
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to show an unpublished lecture by someone who is not the owner
		// of the lectures or is not "Lecturer".
		Collection<Course> courses;
		String param;

		courses = this.repository.findCoursesByLecturerUsername("lecturer2");
		for (final Course course : courses)
			if (course.isDraftMode()) {
				param = String.format("id=%d", course.getId());

				super.checkLinkExists("Sign in");
				super.request("/lecturer/course/show", param);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/lecturer/course/show", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("auditor1", "auditor1");
				super.request("/lecturer/course/show", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company1", "company1");
				super.request("/lecturer/course/show", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/lecturer/course/show", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("assistant1", "assistant1");
				super.request("/lecturer/course/show", param);
				super.checkPanicExists();
				super.signOut();
			}
	}

	@Test
	public void test301Hacking() {
		// HINT: this test tries to show a lecture unpublished
		// without being the owner.

		Collection<Course> courses;
		String param;

		super.signIn("lecturer1", "lecturer1");
		courses = this.repository.findCoursesByLecturerUsername("lecturer2");
		for (final Course course : courses)
			if (course.isDraftMode()) {
				param = String.format("id=%d", course.getId());
				super.request("/lecturer/course/update", param);
				super.checkPanicExists();
			}
		super.signOut();
	}
}


package acme.testing.lecturer.courseLecture;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.course.Course;
import acme.testing.TestHarness;

public class LecturerCourseLectureAddTest extends TestHarness {

	@Autowired
	protected LecturerCourseLectureRepositoryTest repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/course-lecture/add-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int courseRecordIndex, final String courseCode, final String lectureTitle) {
		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "List my courses");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(courseRecordIndex);
		super.checkFormExists();
		super.checkInputBoxHasValue("code", courseCode);
		super.checkInputBoxHasValue("published", "false");

		super.checkLinkExists("Lectures list");
		super.clickOnButton("Lectures list");
		super.checkLinkExists("Add");
		super.clickOnButton("Add");

		super.checkInputBoxHasValue("courseCode", courseCode);
		super.fillInputBoxIn("lectureId", lectureTitle);
		super.checkSubmitExists("Add");
		super.clickOnSubmit("Add");
		super.checkNotErrorsExist();

		super.signOut();
	}

	@Test
	public void test200Negative() {

	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to add lectures to a unpublished course by someone 
		// who is not the owner or a "Lecturer".
		Collection<Course> courses;
		String param;

		courses = this.repository.findCoursesByLecturerUsername("lecturer1");
		for (final Course course : courses)
			if (course.isDraftMode()) {
				param = String.format("id=%d", course.getId());

				super.checkLinkExists("Sign in");
				super.request("/lecturer/course-lecture/add", param);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/lecturer/course-lecture/add", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("auditor1", "auditor1");
				super.request("/lecturer/course-lecture/add", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company1", "company1");
				super.request("/lecturer/course-lecture/add", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/lecturer/course-lecture/add", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("assistant1", "assistant1");
				super.request("/lecturer/course-lecture/add", param);
				super.checkPanicExists();
				super.signOut();
			}
	}
}

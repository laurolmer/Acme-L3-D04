
package acme.testing.lecturer.course;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.course.Course;
import acme.testing.TestHarness;

public class LecturerCourseShowPublishedTest extends TestHarness {

	@Autowired
	protected LecturerCourseRepositoryTest repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/course/show-published-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String code, final String title, final String abstractCourse, final String link, final String published, final String courseType, final String price, final String estimatedTotalTime) {
		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "List my courses");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("courseAbstract", abstractCourse);
		super.checkInputBoxHasValue("retailPrice", price);
		super.checkInputBoxHasValue("link", link);
		super.checkInputBoxHasValue("published", published);
		super.checkInputBoxHasValue("courseType", courseType);
		super.checkInputBoxHasValue("estimatedTotalTime", estimatedTotalTime);
		super.checkLinkExists("Lectures list");
		super.checkLinkExists("Return");
		super.checkNotSubmitExists("Update");
		super.checkNotSubmitExists("Delete");
		super.checkNotSubmitExists("Publish");

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT:there aren't any negative tests for this feature since it's a show
		// that doesn't involve entering anydata into any forms.
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to show a published course by someone who is not the owner
		// or a "Lecturer".
		Collection<Course> courses;
		String param;

		courses = this.repository.findCoursesByLecturerUsername("lecturer1");
		for (final Course course : courses)
			if (!course.isDraftMode()) {
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
}

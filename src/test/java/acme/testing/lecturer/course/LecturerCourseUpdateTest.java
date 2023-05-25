
package acme.testing.lecturer.course;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.course.Course;
import acme.testing.TestHarness;

public class LecturerCourseUpdateTest extends TestHarness {

	@Autowired
	protected LecturerCourseRepositoryTest repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/course/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String code, final String title, final String abstractCourse, final String price, final String link, final String published) {
		// HINT: this test logs in as an lecturer, lists his or her tutorials,
		// selects one of them, updates it, and then checks that the update
		// has actually been performed.

		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "List my courses");
		super.checkListingExists();
		super.sortListing(0, "desc");

		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("published", published);

		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("courseAbstract", abstractCourse);
		super.fillInputBoxIn("link", link);
		super.fillInputBoxIn("retailPrice", price);
		super.clickOnSubmit("Update");

		super.checkListingExists();
		super.sortListing(0, "desc");
		super.checkColumnHasValue(recordIndex, 0, title);
		super.checkColumnHasValue(recordIndex, 1, abstractCourse);
		super.checkColumnHasValue(recordIndex, 2, price);
		super.clickOnListingRecord(recordIndex);

		super.checkFormExists();
		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("courseAbstract", abstractCourse);
		super.checkInputBoxHasValue("retailPrice", price);
		super.checkInputBoxHasValue("link", link);
		super.checkInputBoxHasValue("published", published);

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/course/update-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int recordIndex, final String code, final String title, final String abstractCourse, final String price, final String link, final String published) {
		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "List my courses");
		super.checkListingExists();
		super.sortListing(0, "desc");
		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("published", published);
		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("courseAbstract", abstractCourse);
		super.fillInputBoxIn("link", link);
		super.fillInputBoxIn("retailPrice", price);
		super.clickOnSubmit("Update");

		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {

		// HINT: this test tries to update a course with a role other than "Lecturer".

		Collection<Course> courses;
		String param;

		courses = this.repository.findCoursesByLecturerUsername("lecturer1");
		for (final Course course : courses)
			if (course.isDraftMode()) {
				param = String.format("id=%d", course.getId());

				super.checkLinkExists("Sign in");
				super.request("/lecturer/course/update", param);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/lecturer/course/update", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("auditor1", "auditor1");
				super.request("/lecturer/course/update", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company1", "company1");
				super.request("/lecturer/course/update", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/lecturer/course/update", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("assistant1", "assistant1");
				super.request("/lecturer/course/update", param);
				super.checkPanicExists();
				super.signOut();

			}
	}

	@Test
	public void test301Hacking() {
		// HINT: this test tries to update a course published
		// being the owner.

		Collection<Course> courses;
		String param;

		super.signIn("lecturer1", "lecturer1");
		courses = this.repository.findCoursesByLecturerUsername("lecturer1");
		for (final Course course : courses)
			if (!course.isDraftMode()) {
				param = String.format("id=%d", course.getId());
				super.request("/lecturer/course/update", param);
				super.checkPanicExists();
			}
		super.signOut();
	}

	@Test
	public void test302Hacking() {
		// HINT: this test tries to update a course published or not
		// without being the owner.

		Collection<Course> courses;
		String param;

		super.signIn("lecturer2", "lecturer2");
		courses = this.repository.findCoursesByLecturerUsername("lecturer1");
		for (final Course course : courses) {
			param = String.format("id=%d", course.getId());
			super.request("/lecturer/course/update", param);
			super.checkPanicExists();
		}
		super.signOut();
	}

}

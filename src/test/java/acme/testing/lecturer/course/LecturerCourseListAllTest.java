
package acme.testing.lecturer.course;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.testing.TestHarness;

public class LecturerCourseListAllTest extends TestHarness {

	@Autowired
	protected LecturerCourseRepositoryTest repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/course/list-all-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String title, final String abstractCourse, final String price) {
		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "List courses");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(recordIndex, 0, title);
		super.checkColumnHasValue(recordIndex, 1, abstractCourse);
		super.checkColumnHasValue(recordIndex, 2, price);

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT:there aren't any negative tests for this feature since it's a listing
		// that doesn'tinvolve entering anydata into any forms.
	}

	@Test
	public void test300Hacking() {
		super.checkLinkExists("Sign in");
		super.request("/lecturer/course/list-all");
		super.checkPanicExists();

		super.signIn("administrator", "administrator");
		super.request("/lecturer/course/list-all");
		super.checkPanicExists();
		super.signOut();

		super.signIn("auditor1", "auditor1");
		super.request("/lecturer/course/list-all");
		super.checkPanicExists();
		super.signOut();

		super.signIn("company1", "company1");
		super.request("/lecturer/course/list-all");
		super.checkPanicExists();
		super.signOut();

		super.signIn("student1", "student1");
		super.request("/lecturer/course/list-all");
		super.checkPanicExists();
		super.signOut();

		super.signIn("assistant1", "assistant1");
		super.request("/lecturer/course/list-all");
		super.checkPanicExists();
		super.signOut();
	}
}

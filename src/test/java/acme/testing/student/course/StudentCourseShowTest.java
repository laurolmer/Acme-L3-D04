
package acme.testing.student.course;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class StudentCourseShowTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/student/course/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test100Positive(final int recordIndex, final String code, final String title, final String courseAbstract, final String courseType, final String retailPrice, final String link, final String lecturer) {

		super.signIn("student1", "student1");

		super.clickOnMenu("Student", "Course List");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("courseAbstract", courseAbstract);
		super.checkInputBoxHasValue("courseType", courseType);
		super.checkInputBoxHasValue("retailPrice", retailPrice);
		super.checkInputBoxHasValue("link", link);
		super.checkInputBoxHasValue("lecturer", lecturer);

		super.checkLinkExists("Lectures:");

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: there aren't any negative tests for this feature since it's a listing that
		// doesn't involve entering any data into any forms.
	}

	@Test
	void test300Hacking() {
		// HINT: this test tries to show an enrolment not finalised by someone who is not the principal.

		super.checkLinkExists("Sign in");
		super.request("/student/enrolment/list");
		super.checkPanicExists();

		super.signIn("administrator", "administrator");
		super.request("/student/enrolment/list");
		super.checkPanicExists();
		super.signOut();

		super.signIn("auditor1", "auditor1");
		super.request("/student/enrolment/list");
		super.checkPanicExists();
		super.signOut();

		super.signIn("company1", "company1");
		super.request("/student/enrolment/list");
		super.checkPanicExists();
		super.signOut();

		super.signIn("lecturer1", "lecturer1");
		super.request("/student/enrolment/list");
		super.checkPanicExists();
		super.signOut();

		super.signIn("assistant1", "assistant1");
		super.request("/student/enrolment/list");
		super.checkPanicExists();
		super.signOut();
	}

}

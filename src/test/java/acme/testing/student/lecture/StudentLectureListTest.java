
package acme.testing.student.lecture;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.testing.TestHarness;
import acme.testing.student.activity.StudentActivityTestRepository;

public class StudentLectureListTest extends TestHarness {

	@Autowired
	protected StudentActivityTestRepository repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/student/lecture/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test100Positive(final int courseRecordIndex, final int lectureRecordIndex, final String title, final String lectureType) {
		super.signIn("student1", "student1");

		super.clickOnMenu("Student", "Course List");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(courseRecordIndex);
		super.checkFormExists();

		super.clickOnButton("Lectures:");
		super.checkListingExists();

		super.checkColumnHasValue(lectureRecordIndex, 0, title);
		super.checkColumnHasValue(lectureRecordIndex, 1, lectureType);

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: there aren't any negative tests for this feature since it's a listing that
		// doesn't involve entering any data into any forms.
	}

	@Test
	void test300Hacking() {
		// HINT: this test tries to list student Activities using wrong principals

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

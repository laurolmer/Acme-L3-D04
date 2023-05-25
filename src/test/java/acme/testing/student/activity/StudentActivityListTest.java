
package acme.testing.student.activity;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.enrolment.Enrolment;
import acme.testing.TestHarness;

public class StudentActivityListTest extends TestHarness {

	@Autowired
	protected StudentActivityTestRepository repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/student/activity/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test100Positive(final int enrolmentRecordIndex, final String code, final int activityRecordIndex, final String title, final String activityType) {
		super.signIn("student1", "student1");

		super.clickOnMenu("Student", "Enrolment List");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(enrolmentRecordIndex, 0, code);
		super.clickOnListingRecord(enrolmentRecordIndex);
		super.checkInputBoxHasValue("code", code);
		super.clickOnButton("Activities:");

		super.checkListingExists();
		super.checkColumnHasValue(activityRecordIndex, 0, title);
		super.checkColumnHasValue(activityRecordIndex, 1, activityType);
		super.clickOnListingRecord(activityRecordIndex);

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

		Collection<Enrolment> enrolments;
		String param;

		enrolments = this.repository.findAllEnrolmentsByStudentUsername("student1");
		for (final Enrolment enrolment : enrolments)
			if (!enrolment.isDraftMode()) {
				param = String.format("enrolmentId=%d", enrolment.getId());

				super.checkLinkExists("Sign in");
				super.request("/student/activity/list", param);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/student/activity/list", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("auditor1", "auditor1");
				super.request("/student/activity/list", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company1", "company1");
				super.request("/student/activity/list", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/student/activity/list", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("assistant1", "assistant1");
				super.request("/student/activity/list", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student2", "student2");
				super.request("/student/activity/list", param);
				super.checkPanicExists();
				super.signOut();
			}
	}
}

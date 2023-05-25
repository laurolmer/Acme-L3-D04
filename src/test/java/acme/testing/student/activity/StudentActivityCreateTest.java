
package acme.testing.student.activity;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.activity.Activity;
import acme.testing.TestHarness;

public class StudentActivityCreateTest extends TestHarness {

	@Autowired
	protected StudentActivityTestRepository repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/student/activity/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test100Positive(final int enrolmentRecordIndex, final int activityRecordIndex, final String title, final String abstractActivity, final String activityType, final String startPeriod, final String endPeriod, final String link) {

		super.signIn("student1", "student1");

		super.clickOnMenu("Student", "Enrolment List");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(enrolmentRecordIndex);
		super.clickOnButton("Activities:");
		super.checkListingExists();

		super.clickOnButton("Create:");
		super.checkFormExists();
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractActivity", abstractActivity);
		super.fillInputBoxIn("activityType", activityType);
		super.fillInputBoxIn("startPeriod", startPeriod);
		super.fillInputBoxIn("endPeriod", endPeriod);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Create:");

		super.sortListing(0, "asc");

		super.clickOnListingRecord(activityRecordIndex);
		super.checkFormExists();
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("abstractActivity", abstractActivity);
		super.checkInputBoxHasValue("activityType", activityType);
		super.checkInputBoxHasValue("startPeriod", startPeriod);
		super.checkInputBoxHasValue("endPeriod", endPeriod);
		super.checkInputBoxHasValue("link", link);

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/student/activity/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test200Negative(final int enrolmentRecordIndex, final int activityRecordIndex, final String title, final String abstractActivity, final String activityType, final String startPeriod, final String endPeriod, final String link) {
		// HINT: this test attempts to create jobs with incorrect data.

		super.signIn("student1", "student1");

		super.clickOnMenu("Student", "Enrolment List");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(enrolmentRecordIndex);
		super.clickOnButton("Activities:");
		super.checkListingExists();

		super.clickOnButton("Create:");
		super.checkFormExists();
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractActivity", abstractActivity);
		super.fillInputBoxIn("activityType", activityType);
		super.fillInputBoxIn("startPeriod", startPeriod);
		super.fillInputBoxIn("endPeriod", endPeriod);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Create:");

		super.clickOnMenu("Student", "Enrolment List");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(enrolmentRecordIndex);
		super.clickOnButton("Activities:");
		super.checkListingExists();

		super.clickOnListingRecord(activityRecordIndex);
		super.checkFormExists();
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("abstractActivity", abstractActivity);
		super.checkInputBoxHasValue("activityType", activityType);
		super.checkInputBoxHasValue("startPeriod", startPeriod);
		super.checkInputBoxHasValue("endPeriod", endPeriod);
		super.checkInputBoxHasValue("link", link);

		super.signOut();
	}

	@Test
	void test300Hacking() {
		// HINT: this test tries to create an Activity using principals with
		// HINT+ inappropriate roles.

		Collection<Activity> Activities;
		String param;

		Activities = this.repository.findAllActivitiesByStudentIdUsername("student1");
		for (final Activity activity : Activities)
			if (!activity.getEnrolment().isDraftMode()) {
				param = String.format("id=%d", activity.getId());

				super.checkLinkExists("Sign in");
				super.request("/student/activity/create");
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/student/activity/create");
				super.checkPanicExists();
				super.signOut();

				super.signIn("auditor1", "auditor1");
				super.request("/student/activity/create");
				super.checkPanicExists();
				super.signOut();

				super.signIn("company1", "company1");
				super.request("/student/activity/create");
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/student/activity/create");
				super.checkPanicExists();
				super.signOut();

				super.signIn("assistant1", "assistant1");
				super.request("/student/activity/create");
				super.checkPanicExists();
				super.signOut();

				super.signIn("student2", "student2");
				super.request("/student/activity/create");
				super.checkPanicExists();
				super.signOut();
			} else {
				// HINT: this test tries to create an activity to a not finalised enrolment that was registered by the principal.
				param = String.format("id=%d", activity.getId());
				super.signIn("student1", "student1");
				super.request("/student/activity/create", param);
				super.checkPanicExists();
				super.signOut();
			}

	}
}

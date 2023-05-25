
package acme.testing.student.activity;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.activity.Activity;
import acme.testing.TestHarness;

/*
 * EmployerJobUpdateTest.java
 *
 * Copyright (C) 2012-2023 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

class StudentActivityDeleteTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected StudentActivityTestRepository repository;

	// Test methods ------------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/student/activity/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test100Positive(final int enrolmentRecordIndex, final int activityRecordIndex, final String title, final String nextTitle) {

		super.signIn("student1", "student1");

		super.clickOnMenu("Student", "Enrolment List");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(enrolmentRecordIndex);
		super.clickOnButton("Activities:");
		super.checkListingExists();
		super.clickOnListingRecord(activityRecordIndex);
		super.clickOnSubmit("Delete:");

		super.clickOnMenu("Student", "Enrolment List");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(enrolmentRecordIndex);
		super.clickOnButton("Activities:");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(activityRecordIndex, 0, nextTitle);

		super.checkNotPanicExists();

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: there aren't any negative tests for this feature since it's a listing that
		// doesn't involve entering any data into any forms.
	}

	@Test
	void test300Hacking() {
		// HINT: this test tries to delete an Activity with a role other than "Student",
		// HINT+ or using an student who is not the owner.

		Collection<Activity> Activities;
		String param;

		Activities = this.repository.findAllActivitiesByStudentIdUsername("student1");
		for (final Activity activity : Activities)
			if (activity.getEnrolment().isDraftMode()) {
				param = String.format("id=%d", activity.getId());

				super.checkLinkExists("Sign in");
				super.request("/student/activity/delete", param);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/student/activity/delete", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("auditor1", "auditor1");
				super.request("/student/activity/delete", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company1", "company1");
				super.request("/student/activity/delete", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/student/activity/delete", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("assistant1", "assistant1");
				super.request("/student/activity/delete", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student2", "student2");
				super.request("/student/activity/delete", param);
				super.checkPanicExists();
				super.signOut();
			} else {
				// HINT: this test tries to update a not finalised job that was registered by the principal.
				param = String.format("id=%d", activity.getId());
				super.signIn("student1", "student1");
				super.request("/student/activity/delete", param);
				super.checkPanicExists();
				super.signOut();

			}
	}

}

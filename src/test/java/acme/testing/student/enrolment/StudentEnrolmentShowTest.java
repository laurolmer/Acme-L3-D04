/*
 * EmployerJobShowTest.java
 *
 * Copyright (C) 2012-2023 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.testing.student.enrolment;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.enrolment.Enrolment;
import acme.testing.TestHarness;

class StudentEnrolmentShowTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected StudentEnrolmentTestRepository repository;

	// Test data --------------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/student/enrolment/show-positive-draftmode-true.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test100Positive(final int recordIndex, final String code, final String motivation, final String goals, final String estimatedTotalTime, final String creditCard, final String holderName, final String expiryDate, final String cvc,
		final String course) {

		super.signIn("student1", "student1");

		super.clickOnMenu("Student", "Enrolment List");
		super.sortListing(0, "asc");
		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("motivation", motivation);
		super.checkInputBoxHasValue("goals", goals);
		super.checkInputBoxHasValue("estimatedTotalTime", estimatedTotalTime);
		super.checkInputBoxHasValue("course", course);

		super.fillInputBoxIn("creditCard", creditCard);
		super.fillInputBoxIn("holderName", holderName);
		super.fillInputBoxIn("expiryDate", expiryDate);
		super.fillInputBoxIn("cvc", cvc);

		super.checkSubmitExists("Update:");
		super.checkSubmitExists("Delete:");
		super.checkSubmitExists("Finalize:");
		super.checkLinkExists("Activities:");

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/student/enrolment/show-positive-draftmode-false.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test101Positive(final int recordIndex, final String code, final String motivation, final String goals, final String estimatedTotalTime, final String holderName, final String lowerNibble, final String course) {

		super.signIn("student1", "student1");

		super.clickOnMenu("Student", "Enrolment List");
		super.sortListing(0, "asc");
		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("motivation", motivation);
		super.checkInputBoxHasValue("goals", goals);
		super.checkInputBoxHasValue("estimatedTotalTime", estimatedTotalTime);
		super.checkInputBoxHasValue("course", course);

		super.checkInputBoxHasValue("holderName", holderName);
		super.checkInputBoxHasValue("lowerNibble", lowerNibble);

		super.checkNotSubmitExists("Create:");
		super.checkNotSubmitExists("Update:");
		super.checkNotSubmitExists("Delete:");
		super.checkNotSubmitExists("Finalize:");

		super.checkLinkExists("Activities:");

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

		Collection<Enrolment> enrolments;
		String param;

		enrolments = this.repository.findAllEnrolmentsByStudentUsername("student1");
		for (final Enrolment enrolment : enrolments)
			if (enrolment.isDraftMode()) {
				param = String.format("id=%d", enrolment.getId());

				super.checkLinkExists("Sign in");
				super.request("/student/enrolment/show", param);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/student/enrolment/show", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("auditor1", "auditor1");
				super.request("/student/enrolment/show", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company1", "company1");
				super.request("/student/enrolment/show", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/student/enrolment/show", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("assistant1", "assistant1");
				super.request("/student/enrolment/show", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student2", "student2");
				super.request("/student/enrolment/show", param);
				super.checkPanicExists();
				super.signOut();
			}
	}

}

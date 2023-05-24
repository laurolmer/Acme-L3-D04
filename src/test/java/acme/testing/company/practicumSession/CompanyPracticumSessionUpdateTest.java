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

package acme.testing.company.practicumSession;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.practicumSession.PracticumSession;
import acme.testing.TestHarness;

public class CompanyPracticumSessionUpdateTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected CompanyPracticumSessionTestRepository repository;

	// Test methods ------------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicumSession/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordpracticumIndex, final int recordSessionIndex, final String title, final String abstractSession, final String code, final String start, final String end, final String link) {
		// HINT: this test logs in as an company, lists his or her practicums, 
		// selects one of their sessions and list them, updates it, and then checks that 
		// the update has actually been performed.

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List my practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(recordpracticumIndex);
		super.clickOnButton("List Sessions");

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractSession", abstractSession);
		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("start", start);
		super.fillInputBoxIn("end", end);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Update");

		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(recordSessionIndex, 0, title);
		super.clickOnListingRecord(recordSessionIndex);

		super.checkFormExists();
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("abstractSession", abstractSession);
		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("start", start);
		super.checkInputBoxHasValue("end", end);
		super.checkInputBoxHasValue("link", link);

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicumSession/update-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int practicumRecordIndex, final int sessionRecordIndex, final String title, final String abstractSession, final String code, final String start, final String end, final String link) {
		// HINT: this test attempts to update a session with wrong data.

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List my practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(practicumRecordIndex);
		super.clickOnButton("List Sessions");

		super.checkFormExists();
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractSession", abstractSession);
		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("start", start);
		super.fillInputBoxIn("end", end);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Update");
		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to update a session with a role other than "company",
		// or using an company who is not the owner.
		Collection<PracticumSession> sessions;
		String param;

		super.signIn("company1", "company1");
		sessions = this.repository.findpracticumSessionsBycompanyUsername("company1");
		for (final PracticumSession session : sessions)
			if (!session.getPracticum().getDraftMode()) {
				param = String.format("id=%d", session.getPracticum().getId());

				super.checkLinkExists("Sign in");
				super.request("/company/practicum-session/update", param);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/company/practicum-session/update", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company2", "company2");
				super.request("/company/practicum-session/update", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("auditor1", "auditor1");
				super.request("/company/practicum-session/update", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company1", "company1");
				super.request("/company/practicum-session/update", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("consumer1", "consumer1");
				super.request("/company/practicum-session/update", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/company/practicum-session/update", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("provider1", "provider1");
				super.request("/company/practicum-session/update", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/company/practicum-session/update", param);
				super.checkPanicExists();
				super.signOut();
			}
	}

	@Test
	public void test301Hacking() {
		// HINT: this test tries to update a published session that was registered by the principal.
		Collection<PracticumSession> sessions;
		String params;

		super.signIn("company1", "company1");
		sessions = this.repository.findpracticumSessionsBycompanyUsername("company1");
		for (final PracticumSession session : sessions)
			if (!session.getPracticum().getDraftMode()) {
				params = String.format("id=%d", session.getPracticum().getId());
				super.request("/company/practicum-session/update", params);
			}
		super.signOut();
	}

	@Test
	public void test302Hacking() {
		// HINT: this test tries to update a session that wasn't registered by the principal,
		// be it published or unpublished.
		Collection<PracticumSession> sessions;
		String params;

		super.signIn("company2", "company2");
		sessions = this.repository.findpracticumSessionsBycompanyUsername("company1");
		for (final PracticumSession session : sessions)
			if (!session.getPracticum().getDraftMode()) {
				params = String.format("id=%d", session.getPracticum().getId());
				super.request("/company/practicum-session/update", params);
			}
		super.signOut();
	}

}

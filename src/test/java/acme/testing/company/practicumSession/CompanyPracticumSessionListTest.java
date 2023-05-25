/*
 * EmployerDutyListTest.java
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

import acme.entities.practicum.Practicum;
import acme.testing.TestHarness;

public class CompanyPracticumSessionListTest extends TestHarness {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected CompanyPracticumSessionTestRepository repository;


	// Test methods -----------------------------------------------------------
	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicumSession/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int practicumRecordIndex, final String code, final int sessionRecordIndex, final String title, final String abstractSession) {
		// HINT: this test authenticates as an company, then lists his or her practicums, 
		// selects one of them, and check that it has the expected sessions.
		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List my practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(practicumRecordIndex, 0, code);
		super.clickOnListingRecord(practicumRecordIndex);
		super.checkInputBoxHasValue("code", code);
		super.clickOnButton("List Sessions");

		super.checkListingExists();
		super.checkColumnHasValue(sessionRecordIndex, 0, title);
		super.checkColumnHasValue(sessionRecordIndex, 1, abstractSession);
		super.clickOnListingRecord(sessionRecordIndex);

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: there's no negative test case for this listing, since it doesn't
		// involve filling in any forms.
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to list the sessions of a practicum that is unpublished
		// using a principal that didn't create it. 
		Collection<Practicum> practicums;
		String param;

		practicums = this.repository.findpracticumsBycompanyUsername("company1");
		for (final Practicum practicum : practicums)
			if (practicum.getDraftMode()) {
				param = String.format("masterId=%d", practicum.getId());

				super.checkLinkExists("Sign in");
				super.request("/company/practicum-session/list", param);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/company/practicum-session/list", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company2", "company2");
				super.request("/company/practicum-session/list", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("auditor1", "auditor1");
				super.request("/company/practicum-session/list", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company1", "company1");
				super.request("/company/practicum-session/list", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("consumer1", "consumer1");
				super.request("/company/practicum-session/list", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/company/practicum-session/list", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("provider1", "provider1");
				super.request("/company/practicum-session/list", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/company/practicum-session/list", param);
				super.checkPanicExists();
				super.signOut();
			}
	}

}

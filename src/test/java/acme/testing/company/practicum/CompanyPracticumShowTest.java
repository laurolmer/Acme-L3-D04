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

package acme.testing.company.practicum;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.practicum.Practicum;
import acme.testing.TestHarness;

public class CompanyPracticumShowTest extends TestHarness {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected CompanyPracticumTestRepository repository;


	// Test data --------------------------------------------------------------
	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicum/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int practicumRecordIndex, final String course, final String code, final String title, final String abstractPracticum, final String goals) {
		// HINT: this test signs in as an company, lists all of the practicums, click on  
		// one of them, and checks that the form has the expected data.

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List all practicums");
		super.sortListing(0, "asc");
		super.clickOnListingRecord(practicumRecordIndex);
		super.checkFormExists();

		super.fillInputBoxIn("course", course);
		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractPracticum", abstractPracticum);
		super.fillInputBoxIn("goals", goals);

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: there aren't any negative tests for this feature because it's a listing
		// that doesn't involve entering any data in any forms.
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to show an unpublished practicum by someone who is not the principal.
		Collection<Practicum> practicums;
		String param;

		practicums = this.repository.findpracticumsBycompanyUsername("company1");
		for (final Practicum practicum : practicums)
			if (practicum.getDraftMode()) {
				param = String.format("id=%d", practicum.getId());

				super.checkLinkExists("Sign in");
				super.request("/company/practicum/show", param);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/company/practicum/show", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("employer2", "employer2");
				super.request("/company/practicum/show", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("auditor1", "auditor1");
				super.request("/company/practicum/show", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company1", "company1");
				super.request("/company/practicum/show", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("consumer1", "consumer1");
				super.request("/company/practicum/show", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/company/practicum/show", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("provider1", "provider1");
				super.request("/company/practicum/show", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/company/practicum/show", param);
				super.checkPanicExists();
				super.signOut();
			}
	}

}

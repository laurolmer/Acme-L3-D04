/*
 * EmployerJobPublishTest.java
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

public class CompanyPracticumPublishTest extends TestHarness {

	// Internal data ----------------------------------------------------------
	@Autowired
	protected CompanyPracticumTestRepository repository;


	// Test methods -----------------------------------------------------------
	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicum/publish-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordpracticumIndex, final String code) {
		// HINT: this test authenticates as an company, lists his or her practicums,
		// then selects one of them, and publishes it.
		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List my practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(recordpracticumIndex, 0, code);

		super.clickOnListingRecord(recordpracticumIndex);
		super.checkFormExists();
		super.clickOnSubmit("Publish");
		super.checkNotErrorsExist();

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicum/publish-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int recordpracticumIndex, final String code) {
		// HINT: this test attempts to publish a practicum that cannot be published, yet.
		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List my practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(recordpracticumIndex, 0, code);
		super.clickOnListingRecord(recordpracticumIndex);
		super.checkFormExists();
		super.clickOnSubmit("Publish");
		super.checkAlertExists(false);

		super.signOut();
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to publish a practicum with a role other than "company".
		Collection<Practicum> practicums;
		String params;

		practicums = this.repository.findpracticumsBycompanyUsername("company1");
		for (final Practicum practicum : practicums)
			if (practicum.getDraftMode()) {
				params = String.format("id=%d", practicum.getId());

				super.checkLinkExists("Sign in");
				super.request("/company/practicum/publish", params);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/company/practicum/publish", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("auditor1", "auditor1");
				super.request("/company/practicum/publish", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company1", "company1");
				super.request("/company/practicum/publish", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("consumer1", "consumer1");
				super.request("/company/practicum/publish", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/company/practicum/publish", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("provider1", "provider1");
				super.request("/company/practicum/publish", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/company/practicum/publish", params);
				super.checkPanicExists();
				super.signOut();
			}
	}

	@Test
	public void test301Hacking() {
		// HINT: this test tries to publish a published practicum that was registered by the principal.
		Collection<Practicum> practicums;
		String params;

		super.signIn("company1", "company1");
		practicums = this.repository.findpracticumsBycompanyUsername("company1");
		for (final Practicum practicum : practicums)
			if (!practicum.getDraftMode()) {
				params = String.format("id=%d", practicum.getId());
				super.request("/company/practicum/publish", params);
			}
		super.signOut();
	}

	@Test
	public void test302Hacking() {
		// HINT: this test tries to publish a practicum that wasn't registered by the principal,
		// be it published or unpublished.
		Collection<Practicum> practicums;
		String params;

		super.signIn("company2", "company2");
		practicums = this.repository.findpracticumsBycompanyUsername("company1");
		for (final Practicum practicum : practicums) {
			params = String.format("id=%d", practicum.getId());
			super.request("/company/practicum/publish", params);
		}
		super.signOut();
	}

}

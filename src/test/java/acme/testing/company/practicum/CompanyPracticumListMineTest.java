/*
 * EmployerJobListMineTest.java
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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class CompanyPracticumListMineTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicum/list-mine-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordpracticumIndex, final String code, final String title) {
		// HINT: this test authenticates as an company, lists his or her practicums only,
		// and then checks that the listing has the expected data.

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List my practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(recordpracticumIndex, 0, code);
		super.checkColumnHasValue(recordpracticumIndex, 1, title);

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: there aren't any negative tests for this feature since it's a listing that
		// doesn't involve entering any data into any forms.
	}

	@Test
	public void test300Hacking() {
		super.checkLinkExists("Sign in");
		super.request("/company/practicum/list-mine");
		super.checkPanicExists();

		super.signIn("administrator", "administrator");
		super.request("/company/practicum/list-mine");
		super.checkPanicExists();
		super.signOut();

		super.signIn("auditor1", "auditor1");
		super.request("/company/practicum/list-mine");
		super.checkPanicExists();
		super.signOut();

		super.signIn("company1", "company1");
		super.request("/company/practicum/list-mine");
		super.checkPanicExists();
		super.signOut();

		super.signIn("consumer1", "consumer1");
		super.request("/company/practicum/list-mine");
		super.checkPanicExists();
		super.signOut();

		super.signIn("lecturer1", "lecturer1");
		super.request("/company/practicum/list-mine");
		super.checkPanicExists();
		super.signOut();

		super.signIn("provider1", "provider1");
		super.request("/company/practicum/list-mine");
		super.checkPanicExists();
		super.signOut();

		super.signIn("student1", "student1");
		super.request("/company/practicum/list-mine");
		super.checkPanicExists();
		super.signOut();
	}

}

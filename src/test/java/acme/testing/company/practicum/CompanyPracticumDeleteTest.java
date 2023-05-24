
package acme.testing.company.practicum;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.practicum.Practicum;
import acme.testing.TestHarness;

public class CompanyPracticumDeleteTest extends TestHarness {

	// Internal data ----------------------------------------------------------
	@Autowired
	protected CompanyPracticumTestRepository repository;


	// Test methods -----------------------------------------------------------
	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicum/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordpracticumIndex, final String code) {

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List my practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(recordpracticumIndex, 0, code);
		super.clickOnListingRecord(recordpracticumIndex);
		super.checkFormExists();
		super.clickOnSubmit("Delete");

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicum/delete-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int recordpracticumIndex, final String code) {
		super.signIn("company1", "company1");

		super.clickOnMenu("company", "List my practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(recordpracticumIndex, 0, code);
		super.clickOnListingRecord(recordpracticumIndex);
		super.checkFormExists();
		super.checkNotSubmitExists("Delete");

		super.signOut();

	}

	public void test300Hacking() {
		// HINT: this test tries to update a practicum with a role other than "company",
		// or using an company who is not the owner.
		Collection<Practicum> practicums;
		String param;

		practicums = this.repository.findpracticumsBycompanyUsername("company1");
		for (final Practicum practicum : practicums)
			if (practicum.getDraftMode()) {
				param = String.format("id=%d", practicum.getId());

				super.checkLinkExists("Sign in");
				super.request("/company/practicum/delete", param);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/company/practicum/delete", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("employer2", "employer2");
				super.request("/company/practicum/delete", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("auditor1", "auditor1");
				super.request("/company/practicum/delete", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company1", "company1");
				super.request("/company/practicum/delete", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("consumer1", "consumer1");
				super.request("/company/practicum/delete", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/company/practicum/delete", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("provider1", "provider1");
				super.request("/company/practicum/delete", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/company/practicum/delete", param);
				super.checkPanicExists();
				super.signOut();
			}
	}

	@Test
	public void test301Hacking() {
		// HINT: this test tries to delete a published practicum that was registered by the principal.
		Collection<Practicum> practicums;
		String params;

		super.signIn("company1", "company1");
		practicums = this.repository.findpracticumsBycompanyUsername("company1");
		for (final Practicum practicum : practicums)
			if (!practicum.getDraftMode()) {
				params = String.format("id=%d", practicum.getId());
				super.request("/company/practicum/delete", params);
			}
		super.signOut();
	}

	@Test
	public void test302Hacking() {
		// HINT: this test tries to delete a practicum that wasn't registered by the principal,
		// be it published or unpublished.
		Collection<Practicum> practicums;
		String params;

		super.signIn("company2", "company2");
		practicums = this.repository.findpracticumsBycompanyUsername("company1");
		for (final Practicum practicum : practicums) {
			params = String.format("id=%d", practicum.getId());
			super.request("/company/practicum/delete", params);
		}
		super.signOut();
	}
}

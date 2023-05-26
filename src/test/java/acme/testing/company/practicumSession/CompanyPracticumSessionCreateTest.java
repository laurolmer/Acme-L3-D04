
package acme.testing.company.practicumSession;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.practicum.Practicum;
import acme.testing.TestHarness;

public class CompanyPracticumSessionCreateTest extends TestHarness {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected CompanyPracticumSessionTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest

	@CsvFileSource(resources = "/company/practicumSession/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test100Positive(final int practicumRecordIndex, final int PracticumSessionRecordIndex, final String title, final String abstractSession, final String start, final String end, final String link, final String code) {
		// HINT: this test authenticates as a company, list his or her practicums, navigates
		// HINT+ to their session practicums, and checks that they have the expected data.

		super.signIn("company2", "company2");

		super.clickOnMenu("Company", "List my practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(practicumRecordIndex);
		super.clickOnButton("Sessions");
		super.clickOnButton("Create");

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractSession", abstractSession);
		super.fillInputBoxIn("start", start);
		super.fillInputBoxIn("end", end);
		super.fillInputBoxIn("link", link);
		super.fillInputBoxIn("code", code);
		super.clickOnSubmit("Create");

		super.clickOnMenu("Company", "List my practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(practicumRecordIndex);
		super.clickOnButton("Sessions");

		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(PracticumSessionRecordIndex, 0, title);
		//		super.checkColumnHasValue(PracticumSessionRecordIndex, 3, start);
		//		super.checkColumnHasValue(PracticumSessionRecordIndex, 2, end);

		super.clickOnListingRecord(PracticumSessionRecordIndex);

		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("abstractSession", abstractSession);
		super.checkInputBoxHasValue("start", start);
		super.checkInputBoxHasValue("end", end);
		super.checkInputBoxHasValue("link", link);
		super.fillInputBoxIn("code", code);

		super.signOut();
	}

	@ParameterizedTest

	@CsvFileSource(resources = "/company/practicumSession/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test200Negative(final int practicumRecordIndex, final int PracticumSessionRecordIndex, final String title, final String abstractSession, final String start, final String end, final String link, final String code) {
		// HINT: this test attempts to create session practicums using wrong data.

		super.signIn("company1", "company1");

		super.clickOnMenu("Company", "List my practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(practicumRecordIndex);
		super.clickOnButton("Sessions");

		super.clickOnButton("Create");
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractSession", abstractSession);
		super.fillInputBoxIn("start", start);
		super.fillInputBoxIn("end", end);
		super.fillInputBoxIn("link", link);
		super.fillInputBoxIn("code", code);
		super.clickOnSubmit("Create");
		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	void test300Hacking() {
		// HINT: this test tries to create a session practicum for a practicum as a principal without
		// HINT: the "Company" role.

		Collection<Practicum> practicums;
		String param;

		practicums = this.repository.findManyPracticumsByCompanyUsername("company2");
		for (final Practicum practicum : practicums) {
			param = String.format("masterId=%d", practicum.getId());

			super.checkLinkExists("Sign in");
			super.request("/company/practicum-session/create", param);
			super.checkPanicExists();

			super.signIn("administrator", "administrator");
			super.request("/company/practicum-session/create", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("student1", "student1");
			super.request("/company/practicum-session/create", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("company1", "company1");
			super.request("/company/practicum-session/create", param);
			super.checkPanicExists();
			super.signOut();
		}
	}

}

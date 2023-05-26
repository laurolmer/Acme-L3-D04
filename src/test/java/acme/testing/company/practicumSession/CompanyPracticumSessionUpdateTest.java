
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
	// Test data --------------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicumSession/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test100Positive(final int practicumRecordIndex, final int PracticumSessionRecordIndex, final String title, final String abstractSession, final String start, final String end, final String link, final String code) {
		// HINT: this test logs in as a company,
		// HINT+ lists his or her practicums, navigates
		// HINT+ to their session practicums, and
		// HINT+ selects one of them, updates it, and then checks that
		// HINT+ the update has actually been performed.

		super.signIn("company2", "company2");

		super.clickOnMenu("Company", "List my practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(practicumRecordIndex);
		super.checkFormExists();

		super.clickOnButton("Sessions");
		super.checkListingExists();

		super.sortListing(0, "asc");
		super.clickOnListingRecord(PracticumSessionRecordIndex);

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractSession", abstractSession);
		super.fillInputBoxIn("start", start);
		super.fillInputBoxIn("end", end);
		super.fillInputBoxIn("link", link);
		super.fillInputBoxIn("code", code);
		super.clickOnSubmit("Update");

		super.clickOnMenu("Company", "List my practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(practicumRecordIndex);
		super.checkFormExists();

		super.clickOnButton("Sessions");
		super.checkListingExists();

		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(PracticumSessionRecordIndex, 0, title);

		super.clickOnListingRecord(PracticumSessionRecordIndex);
		super.checkFormExists();
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("abstractSession", abstractSession);
		super.checkInputBoxHasValue("start", start);
		super.checkInputBoxHasValue("end", end);
		super.checkInputBoxHasValue("link", link);
		super.checkInputBoxHasValue("code", code);

		super.signOut();
	}

	@ParameterizedTest

	@CsvFileSource(resources = "/company/practicumSession/update-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test200Negative(final int practicumRecordIndex, final int PracticumSessionRecordIndex, final String title, final String abstractSession, final String start, final String end, final String link, final String code) {
		// HINT: this test attempts to update a session practicum with wrong data.

		super.signIn("company2", "company2");

		super.clickOnMenu("Company", "List my practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(practicumRecordIndex);
		super.checkFormExists();

		super.clickOnButton("Sessions");
		super.checkListingExists();

		super.sortListing(0, "asc");
		super.clickOnListingRecord(PracticumSessionRecordIndex);

		super.checkFormExists();
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractSession", abstractSession);
		super.fillInputBoxIn("start", start);
		super.fillInputBoxIn("end", end);
		super.fillInputBoxIn("link", link);
		super.fillInputBoxIn("code", code);
		super.clickOnSubmit("Update");

		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	void test300Hacking() {
		// HINT: this test tries to update a session practicum that isn't additional
		// HINT+ with a role other than "Company" or using an employer who is not the owner.

		Collection<PracticumSession> PracticumSessions;
		String param;

		PracticumSessions = this.repository.findManyPracticumSessionsByCompanyUsernameInDraftMode("company1");
		for (final PracticumSession PracticumSession : PracticumSessions)
			if (!PracticumSession.isAdditional()) {
				param = String.format("id=%d", PracticumSession.getId());

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

				super.signIn("student1", "student1");
				super.request("/company/practicum-session/update", param);
				super.checkPanicExists();
				super.signOut();
			}
	}

	@Test
	void test301Hacking() {
		// HINT: this test tries to update a session practicum that is additional
		// HINT+ with a role other than "Company" or using an employer who is not the owner.

		Collection<PracticumSession> PracticumSessions;
		String param;

		PracticumSessions = this.repository.findManyPracticumSessionsByCompanyUsernameInFinalMode("company1");
		for (final PracticumSession PracticumSession : PracticumSessions)
			if (PracticumSession.isAdditional()) {
				param = String.format("id=%d", PracticumSession.getId());

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

				super.signIn("student1", "student1");
				super.request("/company/practicum-session/update", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company1", "company1");
				super.request("/company/practicum-session/update", param);
				super.checkPanicExists();
				super.signOut();
			}
	}

	@Test
	void test302Hacking() {
		// HINT: this test tries to update a session practicum that his practicum is not on draft
		// HINT+ mode with a role other than "Company", with the owner or using an employer who is not the owner.

		Collection<PracticumSession> PracticumSessions;
		String param;

		PracticumSessions = this.repository.findManyPracticumSessionsByCompanyUsernameInFinalMode("company1");
		for (final PracticumSession PracticumSession : PracticumSessions) {
			param = String.format("id=%d", PracticumSession.getId());

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

			super.signIn("student1", "student1");
			super.request("/company/practicum-session/update", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("company1", "company1");
			super.request("/company/practicum-session/update", param);
			super.checkPanicExists();
			super.signOut();
		}
	}
}


package acme.testing.company.practicumSession;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.practicumSession.PracticumSession;
import acme.testing.TestHarness;

public class CompanyPracticumSessionDeleteTest extends TestHarness {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected CompanyPracticumSessionTestRepository repository;

	// Test data --------------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicumSession/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test100Positive(final int practicumRecordIndex, final int PracticumSessionRecordIndex) {
		// HINT: this test authenticates as a company and then lists his or her
		// HINT+ practicums, selects one of them, navigates to the session practicums
		// HINT+ and then deletes one of them.

		super.signIn("company2", "company2");
		super.clickOnMenu("Company", "List my practicums");
		super.checkListingExists();

		super.sortListing(0, "asc");
		super.clickOnListingRecord(practicumRecordIndex);

		super.clickOnButton("Sessions");
		super.checkListingExists();

		super.sortListing(0, "asc");

		super.clickOnListingRecord(PracticumSessionRecordIndex);

		super.clickOnSubmit("Delete");
		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicumSession/delete-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test200Negative(final int practicumRecordIndex, final int PracticumSessionRecordIndex) {
		// HINT: this test authenticates as a company and then lists his or her
		// HINT+ practicums, selects one of them, navigates to the session practicums
		// HINT+ and then tries to delete one of them.

		super.signIn("company2", "company2");
		super.clickOnMenu("Company", "List my practicums");
		super.checkListingExists();

		super.sortListing(0, "asc");
		super.clickOnListingRecord(practicumRecordIndex);

		super.clickOnButton("Sessions");
		super.checkListingExists();

		super.sortListing(0, "asc");
		super.clickOnListingRecord(PracticumSessionRecordIndex);

		super.checkNotButtonExists("Delete");
		super.signOut();
	}

	@Test
	void test300Hacking() {
		// HINT: this test authenticates as a company and tries to delete a session
		// HINT+ practicum that is in draft mode..

		Collection<PracticumSession> PracticumSessions;
		String params;

		PracticumSessions = this.repository.findManyPracticumSessionsByCompanyUsernameInDraftMode("company2");
		System.out.println(PracticumSessions);
		for (final PracticumSession PracticumSession : PracticumSessions)
			if (!PracticumSession.isAdditional()) {
				params = String.format("id=%d", PracticumSession.getId());

				super.checkLinkExists("Sign in");
				super.request("/company/session-practicum/delete", params);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/company/session-practicum/delete", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/company/session-practicum/delete", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company2", "company2");
				super.request("/company/session-practicum/delete", params);
				super.checkPanicExists();
				super.signOut();
			}
	}

	@Test
	void test301Hacking() {
		// HINT: this test authenticates as a company and tries to delete a session
		// HINT+ practicum that isn't in draft mode and is additional.

		Collection<PracticumSession> PracticumSessions;
		String params;

		PracticumSessions = this.repository.findManyPracticumSessionsByCompanyUsernameInFinalMode("company1");
		for (final PracticumSession PracticumSession : PracticumSessions)
			if (PracticumSession.isAdditional()) {
				params = String.format("id=%d", PracticumSession.getId());

				super.checkLinkExists("Sign in");
				super.request("/company/session-practicum/delete", params);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/company/session-practicum/delete", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/company/session-practicum/delete", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company1", "company1");
				super.request("/company/session-practicum/delete", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company2", "company2");
				super.request("/company/session-practicum/delete", params);
				super.checkPanicExists();
				super.signOut();
			}
	}

	@Test
	void test302Hacking() {
		// HINT: this test authenticates as a company and tries to delete a session
		// HINT+ from another company.

		Collection<PracticumSession> PracticumSessions;
		String params;

		super.signIn("company2", "company2");
		PracticumSessions = this.repository.findManyPracticumSessionsByCompanyUsername("company1");
		for (final PracticumSession PracticumSession : PracticumSessions) {
			params = String.format("id=%d", PracticumSession.getId());
			super.request("/company/session-practicum/delete", params);
			super.checkPanicExists();
		}
		super.signOut();
	}
}

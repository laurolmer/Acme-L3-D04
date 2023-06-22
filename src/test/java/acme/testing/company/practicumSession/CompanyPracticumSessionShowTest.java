
package acme.testing.company.practicumSession;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.practicumSession.PracticumSession;
import acme.testing.TestHarness;

public class CompanyPracticumSessionShowTest extends TestHarness {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected CompanyPracticumSessionTestRepository repository;

	// Test methods -----------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicumSession/show-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test100Positive(final int practicumRecordIndex, final int PracticumSessionRecordIndex, final String code, final String title, final String abstractSession, final String start, final String end, final String link) {
		// HINT: this test signs in as a company, lists his or her practicums, selects
		// HINT+ one of them and checks that it's as expected.

		super.signIn("company2", "company2");

		super.clickOnMenu("Company", "List my practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(practicumRecordIndex);
		super.clickOnButton("Sessions");
		super.checkListingExists();
		super.clickOnListingRecord(PracticumSessionRecordIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("abstractSession", abstractSession);
		super.checkInputBoxHasValue("start", start);
		super.checkInputBoxHasValue("end", end);
		super.checkInputBoxHasValue("link", link);

		super.signOut();
	}

	@Test
	void test200Negative() {
		// HINT: there's no negative test case for this listing, since it doesn't
		// HINT+ involve filling in any forms.
	}

	@Test
	void test300Hacking() {
		// HINT: this test tries to show a session practicum of a practicum that is in draft mode or
		// HINT+ not available, but wasn't published by the principal;

		Collection<PracticumSession> PracticumSessions;
		String param;

		PracticumSessions = this.repository.findManyPracticumSessionsByCompanyUsernameInDraftMode("company2");
		for (final PracticumSession PracticumSession : PracticumSessions) {
			param = String.format("id=%d", PracticumSession.getId());

			super.checkLinkExists("Sign in");
			super.request("/company/practicum-session/show", param);
			super.checkPanicExists();

			super.signIn("administrator", "administrator");
			super.request("/company/practicum-session/show", param);
			super.checkPanicExists();
			super.signOut();

			super.signIn("student1", "student1");
			super.request("/company/practicum-session/show", param);
			super.checkPanicExists();
			super.signOut();
		}

	}
}

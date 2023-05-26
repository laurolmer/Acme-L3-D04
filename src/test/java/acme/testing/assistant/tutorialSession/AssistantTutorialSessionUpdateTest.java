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

package acme.testing.assistant.tutorialSession;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.tutorialSession.TutorialSession;
import acme.testing.TestHarness;

public class AssistantTutorialSessionUpdateTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantTutorialSessionTestRepository repository;

	// Test methods ------------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/tutorialSession/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordTutorialIndex, final int recordSessionIndex, final String title, final String abstractSession, final String sessionType, final String startPeriod, final String finishPeriod, final String link) {
		// HINT: this test logs in as an assistant, lists his or her tutorials, 
		// selects one of their sessions and list them, updates it, and then checks that 
		// the update has actually been performed.

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "List my tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(recordTutorialIndex);
		super.clickOnButton("List of Sessions");

		super.clickOnListingRecord(recordSessionIndex);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractSession", abstractSession);
		super.fillInputBoxIn("sessionType", sessionType);
		super.fillInputBoxIn("startPeriod", startPeriod);
		super.fillInputBoxIn("finishPeriod", finishPeriod);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Update");

		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(recordSessionIndex);

		super.checkFormExists();
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("abstractSession", abstractSession);
		super.checkInputBoxHasValue("sessionType", sessionType);
		super.checkInputBoxHasValue("startPeriod", startPeriod);
		super.checkInputBoxHasValue("finishPeriod", finishPeriod);
		super.checkInputBoxHasValue("link", link);

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/tutorialSession/update-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int tutorialRecordIndex, final int sessionRecordIndex, final String title, final String abstractSession, final String sessionType, final String startPeriod, final String finishPeriod, final String link) {
		// HINT: this test attempts to update a session with wrong data.

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "List My Tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(tutorialRecordIndex);
		super.clickOnButton("List Sessions");

		super.clickOnListingRecord(sessionRecordIndex);
		super.checkFormExists();
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractSession", abstractSession);
		super.fillInputBoxIn("sessionType", sessionType);
		super.fillInputBoxIn("startPeriod", startPeriod);
		super.fillInputBoxIn("finishPeriod", finishPeriod);
		super.fillInputBoxIn("link", link);
		super.clickOnSubmit("Update");
		super.checkErrorsExist();

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/tutorialSession/update2-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test201Negative(final int recordTutorialIndex, final int sessionRecordIndex) {
		// HINT: this test attempts to publish a tutorial that cannot be published.
		super.signIn("assistant1", "assistant1");
		super.clickOnMenu("Assistant", "List My Tutorials");
		super.checkListingExists();

		super.sortListing(0, "asc");
		super.clickOnListingRecord(recordTutorialIndex);
		super.clickOnButton("List Sessions");

		super.clickOnListingRecord(sessionRecordIndex);
		super.checkNotButtonExists("Update");
		super.signOut();
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to update a session with a role other than "Assistant",
		// or using an assistant who is not the owner.
		Collection<TutorialSession> sessions;
		String param;

		super.signIn("assistant1", "assistant1");
		sessions = this.repository.findTutorialSessionsByAssistantUsername("assistant1");
		for (final TutorialSession session : sessions)
			if (!session.getTutorial().isDraftMode()) {
				param = String.format("id=%d", session.getTutorial().getId());

				super.checkLinkExists("Sign in");
				super.request("/assistant/tutorial-session/update", param);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/assistant/tutorial-session/update", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("auditor1", "auditor1");
				super.request("/assistant/tutorial-session/update", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company1", "company1");
				super.request("/assistant/tutorial-session/update", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/assistant/tutorial-session/update", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/assistant/tutorial-session/update", param);
				super.checkPanicExists();
				super.signOut();
			}
	}

	@Test
	public void test301Hacking() {
		// HINT: this test tries to update a published session that was registered by the principal.
		Collection<TutorialSession> sessions;
		String params;

		super.signIn("assistant1", "assistant1");
		sessions = this.repository.findTutorialSessionsByAssistantUsername("assistant1");
		for (final TutorialSession session : sessions)
			if (!session.getTutorial().isDraftMode()) {
				params = String.format("id=%d", session.getTutorial().getId());
				super.request("/assistant/tutorial-session/update", params);
			}
		super.signOut();
	}

	@Test
	public void test302Hacking() {
		// HINT: this test tries to update a session that wasn't registered by the principal,
		// be it published or unpublished.
		Collection<TutorialSession> sessions;
		String params;

		super.signIn("assistant2", "assistant2");
		sessions = this.repository.findTutorialSessionsByAssistantUsername("assistant1");
		for (final TutorialSession session : sessions)
			if (!session.getTutorial().isDraftMode()) {
				params = String.format("id=%d", session.getTutorial().getId());
				super.request("/assistant/tutorial-session/update", params);
			}
		super.signOut();
	}

}

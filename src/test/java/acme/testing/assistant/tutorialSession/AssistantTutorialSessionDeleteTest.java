/*
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

public class AssistantTutorialSessionDeleteTest extends TestHarness {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantTutorialSessionTestRepository repository;

	// Test methods ------------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/tutorialSession/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordTutorialIndex, final int recordSessionIndex, final String title, final String nextTitle) {
		// HINT: this test logs in as an assistant, lists his or her tutorials, 
		// selects one of their sessions and list them, deletes it, and then checks that 
		// the delete has actually been performed.
		super.signIn("assistant1", "assistant1");
		super.clickOnMenu("Assistant", "List My Tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(recordTutorialIndex);

		super.clickOnButton("List Sessions");
		super.checkListingExists();
		super.clickOnListingRecord(recordSessionIndex);
		super.clickOnSubmit("Delete");

		super.clickOnMenu("Assistant", "List My Tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(recordTutorialIndex);
		super.clickOnButton("List Sessions");
		super.sortListing(0, "asc");
		super.checkColumnHasValue(recordSessionIndex, 0, nextTitle);

		super.checkNotPanicExists();

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/tutorialSession/delete-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int recordTutorialIndex, final int sessionRecordIndex) {
		// HINT: this test attempts to delete a session with wrong data.
		super.signIn("assistant1", "assistant1");
		super.clickOnMenu("Assistant", "List My Tutorials");
		super.checkListingExists();

		super.sortListing(0, "asc");
		super.clickOnListingRecord(recordTutorialIndex);

		super.clickOnButton("List Sessions");
		super.checkListingExists();

		super.sortListing(0, "asc");
		super.clickOnListingRecord(sessionRecordIndex);

		super.checkNotButtonExists("Delete");
		super.signOut();
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to delete a session with a role other than "Assistant",
		// or using an assistant who is not the owner.
		Collection<TutorialSession> sessions;
		String param;

		super.signIn("assistant1", "assistant1");
		sessions = this.repository.findTutorialSessionsByAssistantUsername("assistant1");
		for (final TutorialSession session : sessions)
			if (!session.getTutorial().isDraftMode()) {
				param = String.format("id=%d", session.getTutorial().getId());

				super.checkLinkExists("Sign in");
				super.request("/assistant/tutorial-session/delete", param);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/assistant/tutorial-session/delete", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("auditor1", "auditor1");
				super.request("/assistant/tutorial-session/delete", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company1", "company1");
				super.request("/assistant/tutorial-session/delete", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/assistant/tutorial-session/delete", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/assistant/tutorial-session/delete", param);
				super.checkPanicExists();
				super.signOut();
			}
	}

	@Test
	public void test301Hacking() {
		// HINT: this test tries to delete a published session that was registered by the principal.
		Collection<TutorialSession> sessions;
		String params;

		super.signIn("assistant1", "assistant1");
		sessions = this.repository.findTutorialSessionsByAssistantUsername("assistant1");
		for (final TutorialSession session : sessions)
			if (!session.getTutorial().isDraftMode()) {
				params = String.format("id=%d", session.getTutorial().getId());
				super.request("/assistant/tutorial-session/delete", params);
			}
		super.signOut();
	}

	@Test
	public void test302Hacking() {
		// HINT: this test tries to delete a session that wasn't registered by the principal,
		// be it published or unpublished.
		Collection<TutorialSession> sessions;
		String params;

		super.signIn("assistant2", "assistant2");
		sessions = this.repository.findTutorialSessionsByAssistantUsername("assistant1");
		for (final TutorialSession session : sessions)
			if (!session.getTutorial().isDraftMode()) {
				params = String.format("id=%d", session.getTutorial().getId());
				super.request("/assistant/tutorial-session/delete", params);
			}
		super.signOut();
	}
}

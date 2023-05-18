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

package acme.testing.assistant.tutorialSession;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.tutorialSession.TutorialSession;
import acme.testing.TestHarness;

public class AssistantTutorialSessionPublishTest extends TestHarness {

	// Internal data ----------------------------------------------------------
	@Autowired
	protected AssistantTutorialSessionTestRepository repository;


	// Test methods -----------------------------------------------------------
	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/tutorialSession/publish-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordTutorialIndex, final String code, final int recordSessionIndex, final String title) {
		// HINT: this test authenticates as an assistant, lists his or her tutorials,
		// then selects one of them, and publishes it.
		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "List my tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(recordTutorialIndex);
		super.clickOnButton("Sessions");

		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(recordSessionIndex, 0, title);
		super.clickOnListingRecord(recordSessionIndex);

		super.checkFormExists();
		super.clickOnSubmit("Publish");
		super.checkNotErrorsExist();
		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/tutorialSession/publish-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int recordTutorialIndex, final String code, final int recordSessionIndex, final String title) {
		// HINT: this test attempts to publish a tutorial that cannot be published, yet.
		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "List my tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(recordTutorialIndex);
		super.clickOnButton("Sessions");

		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(recordSessionIndex, 0, title);
		super.clickOnListingRecord(recordSessionIndex);
		super.checkFormExists();
		super.clickOnSubmit("Publish");
		super.checkAlertExists(false);

		super.signOut();
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to publish a tutorial with a role other than "Assistant".
		Collection<TutorialSession> sessions;
		String params;

		super.signIn("assistant1", "assistant1");
		sessions = this.repository.findTutorialSessionsByAssistantUsername("assistant1");
		for (final TutorialSession session : sessions)
			if (!session.getTutorial().isDraftMode() && session.isDraftMode()) {
				params = String.format("id=%d", session.getTutorial().getId());

				super.checkLinkExists("Sign in");
				super.request("/assistant/tutorialSession/publish", params);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/assistant/tutorialSession/publish", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("auditor1", "auditor1");
				super.request("/assistant/tutorialSession/publish", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company1", "company1");
				super.request("/assistant/tutorialSession/publish", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("consumer1", "consumer1");
				super.request("/assistant/tutorialSession/publish", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/assistant/tutorialSession/publish", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("provider1", "provider1");
				super.request("/assistant/tutorialSession/publish", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/assistant/tutorialSession/publish", params);
				super.checkPanicExists();
				super.signOut();
			}
	}

	@Test
	public void test301Hacking() {
		// HINT: this test tries to publish a published tutorial that was registered by the principal.
		Collection<TutorialSession> sessions;
		String params;

		super.signIn("assistant1", "assistant1");
		sessions = this.repository.findTutorialSessionsByAssistantUsername("assistant1");
		for (final TutorialSession session : sessions)
			if (!session.getTutorial().isDraftMode() && session.isDraftMode()) {
				params = String.format("id=%d", session.getTutorial().getId());
				super.request("/assistant/tutorialSession/publish", params);
			}
		super.signOut();
	}

	@Test
	public void test302Hacking() {
		// HINT: this test tries to publish a tutorial that wasn't registered by the principal,
		// be it published or unpublished.
		Collection<TutorialSession> sessions;
		String params;

		super.signIn("assistant2", "assistant2");
		sessions = this.repository.findTutorialSessionsByAssistantUsername("assistant1");
		for (final TutorialSession session : sessions)
			if (!session.getTutorial().isDraftMode() && session.isDraftMode()) {
				params = String.format("id=%d", session.getTutorial().getId());
				super.request("/assistant/tutorialSession/publish", params);
			}
		super.signOut();
	}

}

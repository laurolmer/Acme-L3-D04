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

package acme.testing.assistant.tutorial;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.tutorial.Tutorial;
import acme.testing.TestHarness;

public class AssistantTutorialPublishTest extends TestHarness {

	// Internal data ----------------------------------------------------------
	@Autowired
	protected AssistantTutorialTestRepository repository;


	// Test methods -----------------------------------------------------------
	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/tutorial/publish-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordTutorialIndex, final String course, final String code, final String title, final String abstractTutorial, final String goals) {
		// HINT: this test authenticates as an assistant, lists his or her tutorials,
		// then selects one of them, and publishes it.
		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "List My Tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(recordTutorialIndex);
		super.checkFormExists();
		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("course", course);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractTutorial", abstractTutorial);
		super.fillInputBoxIn("goals", goals);
		super.clickOnSubmit("Publish");

		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(recordTutorialIndex);
		super.checkFormExists();
		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("course", course);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("abstractTutorial", abstractTutorial);
		super.checkInputBoxHasValue("goals", goals);

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/tutorial/publish-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int recordTutorialIndex, final String course, final String code, final String title, final String abstractTutorial, final String goals) {
		// HINT: this test attempts to publish a tutorial that cannot be published.
		/*
		 * super.signIn("assistant1", "assistant1");
		 * super.clickOnMenu("Assistant", "List My Tutorials");
		 * super.checkListingExists();
		 * super.sortListing(0, "asc");
		 * 
		 * super.clickOnListingRecord(recordTutorialIndex);
		 * super.checkFormExists();
		 * super.fillInputBoxIn("course", course);
		 * super.fillInputBoxIn("code", code);
		 * super.fillInputBoxIn("title", title);
		 * super.fillInputBoxIn("abstractTutorial", abstractTutorial);
		 * super.fillInputBoxIn("goals", goals);
		 * super.clickOnSubmit("Publish");
		 * 
		 * super.checkErrorsExist();
		 * 
		 * super.signOut();
		 */
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to publish a tutorial with a role other than "Assistant".
		Collection<Tutorial> tutorials;
		String params;

		tutorials = this.repository.findTutorialsByAssistantUsername("assistant1");
		for (final Tutorial tutorial : tutorials)
			if (tutorial.isDraftMode()) {
				params = String.format("id=%d", tutorial.getId());

				super.checkLinkExists("Sign in");
				super.request("/assistant/tutorial/publish", params);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/assistant/tutorial/publish", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("auditor1", "auditor1");
				super.request("/assistant/tutorial/publish", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company1", "company1");
				super.request("/assistant/tutorial/publish", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/assistant/tutorial/publish", params);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/assistant/tutorial/publish", params);
				super.checkPanicExists();
				super.signOut();
			}
	}

	@Test
	public void test301Hacking() {
		// HINT: this test tries to publish a published tutorial that was registered by the principal.
		Collection<Tutorial> tutorials;
		String params;

		super.signIn("assistant1", "assistant1");
		tutorials = this.repository.findTutorialsByAssistantUsername("assistant1");
		for (final Tutorial tutorial : tutorials)
			if (!tutorial.isDraftMode()) {
				params = String.format("id=%d", tutorial.getId());
				super.request("/assistant/tutorial/publish", params);
			}
		super.signOut();
	}

	@Test
	public void test302Hacking() {
		// HINT: this test tries to publish a tutorial that wasn't registered by the principal,
		// be it published or unpublished.
		Collection<Tutorial> tutorials;
		String params;

		super.signIn("assistant2", "assistant2");
		tutorials = this.repository.findTutorialsByAssistantUsername("assistant1");
		for (final Tutorial tutorial : tutorials) {
			params = String.format("id=%d", tutorial.getId());
			super.request("/assistant/tutorial/publish", params);
		}
		super.signOut();
	}

}

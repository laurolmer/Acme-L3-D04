
package acme.testing.assistant.tutorial;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.tutorial.Tutorial;
import acme.testing.TestHarness;

public class AssistantTutorialDeleteTest extends TestHarness {

	// Internal data ----------------------------------------------------------
	@Autowired
	protected AssistantTutorialTestRepository repository;


	// Test methods -----------------------------------------------------------
	@ParameterizedTest
	@CsvFileSource(resources = "/assistant/tutorial/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordTutorialIndex, final String code, final String nextCode) {

		super.signIn("assistant1", "assistant1");

		super.clickOnMenu("Assistant", "List My Tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(recordTutorialIndex, 0, code);
		super.clickOnListingRecord(recordTutorialIndex);

		super.checkFormExists();
		super.clickOnSubmit("Delete");

		super.clickOnMenu("Assistant", "List My Tutorials");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(recordTutorialIndex, 0, nextCode);

		super.signOut();
	}

	@Test
	public void test200Negative() {
		// HINT: there aren't any negative tests for this feature because it's a delete
		// that doesn't involve entering any data in any forms.
	}

	public void test300Hacking() {
		// HINT: this test tries to update a tutorial with a role other than "Assistant",
		// or using an assistant who is not the owner.
		Collection<Tutorial> tutorials;
		String param;

		tutorials = this.repository.findTutorialsByAssistantUsername("assistant1");
		for (final Tutorial tutorial : tutorials)
			if (tutorial.isDraftMode()) {
				param = String.format("id=%d", tutorial.getId());

				super.checkLinkExists("Sign in");
				super.request("/assistant/tutorial/delete", param);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/assistant/tutorial/delete", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("auditor1", "auditor1");
				super.request("/assistant/tutorial/delete", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company1", "company1");
				super.request("/assistant/tutorial/delete", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("consumer1", "consumer1");
				super.request("/assistant/tutorial/delete", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/assistant/tutorial/delete", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("provider1", "provider1");
				super.request("/assistant/tutorial/delete", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/assistant/tutorial/delete", param);
				super.checkPanicExists();
				super.signOut();
			}
	}

	@Test
	public void test301Hacking() {
		// HINT: this test tries to delete a published tutorial that was registered by the principal.
		Collection<Tutorial> tutorials;
		String params;

		super.signIn("assistant1", "assistant1");
		tutorials = this.repository.findTutorialsByAssistantUsername("assistant1");
		for (final Tutorial tutorial : tutorials)
			if (!tutorial.isDraftMode()) {
				params = String.format("id=%d", tutorial.getId());
				super.request("/assistant/tutorial/delete", params);
			}
		super.signOut();
	}

	@Test
	public void test302Hacking() {
		// HINT: this test tries to delete a tutorial that wasn't registered by the principal,
		// be it published or unpublished.
		Collection<Tutorial> tutorials;
		String params;

		super.signIn("assistant2", "assistant2");
		tutorials = this.repository.findTutorialsByAssistantUsername("assistant1");
		for (final Tutorial tutorial : tutorials) {
			params = String.format("id=%d", tutorial.getId());
			super.request("/assistant/tutorial/delete", params);
		}
		super.signOut();
	}
}

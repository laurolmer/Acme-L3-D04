
package acme.testing.any.peep;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class AnyPeepCreateTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/any/peep/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String title, final String nick, final String message, final String link, final String email) {

		super.clickOnMenu("Peep List");
		super.checkListingExists();

		super.clickOnButton("Create Peep");
		super.checkFormExists();
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("nick", nick);
		super.fillInputBoxIn("message", message);
		super.fillInputBoxIn("link", link);
		super.fillInputBoxIn("email", email);
		super.clickOnSubmit("Publish Peep");

		super.clickOnMenu("Peep List");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(recordIndex, 0, title);
		super.checkColumnHasValue(recordIndex, 1, nick);

		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("nick", nick);
		super.checkInputBoxHasValue("message", message);
		super.checkInputBoxHasValue("link", link);
		super.checkInputBoxHasValue("email", email);
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/any/peep/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int recordIndex, final String title, final String nick, final String message, final String link, final String email) {

		super.clickOnMenu("Peep List");
		super.checkListingExists();

		super.clickOnButton("Create Peep");
		super.checkFormExists();
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("nick", nick);
		super.fillInputBoxIn("message", message);
		super.fillInputBoxIn("link", link);
		super.fillInputBoxIn("email", email);
		super.clickOnSubmit("Publish Peep");

		super.checkErrorsExist();
	}

	@Test
	public void test300Hacking() {
		// Hacking test unnecessary 
	}

}

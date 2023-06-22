
package acme.testing.company.practicum;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class CompanyPracticumCreateTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicum/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int practicumRecordIndex, final String course, final String code, final String title, final String abstractPracticum, final String goals) {
		// HINT: this test authenticates as an company and then lists his or her
		// practicums, creates a new one, and check that it's been created properly.

		super.signIn("company2", "company2");

		super.clickOnMenu("Company", "List my practicums");
		super.checkListingExists();

		super.clickOnButton("Create");
		super.fillInputBoxIn("course", course);
		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractPracticum", abstractPracticum);
		super.fillInputBoxIn("goals", goals);
		super.clickOnSubmit("Create");

		super.clickOnMenu("Company", "List my practicums");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(practicumRecordIndex, 0, code);
		super.checkColumnHasValue(practicumRecordIndex, 1, title);
		super.clickOnListingRecord(practicumRecordIndex);

		super.checkFormExists();
		super.checkInputBoxHasValue("course", course);
		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("abstractPracticum", abstractPracticum);
		super.checkInputBoxHasValue("goals", goals);

		super.clickOnButton("Sessions");

		super.checkListingExists();
		super.checkListingEmpty();

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/company/practicum/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int practicumRecordIndex, final String course, final String code, final String title, final String abstractPracticum, final String goals) {
		// HINT: this test attempts to create practicums with incorrect data.

		super.signIn("company2", "company2");

		super.clickOnMenu("Company", "List my practicums");
		super.clickOnButton("Create");
		super.checkFormExists();

		super.fillInputBoxIn("course", course);
		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("abstractPracticum", abstractPracticum);
		super.fillInputBoxIn("goals", goals);
		super.clickOnSubmit("Create");

		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to create a practicum using principals with
		// inappropriate roles.

		super.checkLinkExists("Sign in");
		super.request("/company/practicum/create");
		super.checkPanicExists();

		super.signIn("administrator", "administrator");
		super.request("/company/practicum/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("auditor1", "auditor1");
		super.request("/company/practicum/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("assistant1", "assistant1");
		super.request("/company/practicum/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("lecturer1", "lecturer1");
		super.request("/company/practicum/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("student1", "student1");
		super.request("/company/practicum/create");
		super.checkPanicExists();
		super.signOut();
	}

}

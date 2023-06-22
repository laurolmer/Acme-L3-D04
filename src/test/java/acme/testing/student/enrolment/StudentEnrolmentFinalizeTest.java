
package acme.testing.student.enrolment;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.enrolment.Enrolment;
import acme.testing.TestHarness;

public class StudentEnrolmentFinalizeTest extends TestHarness {

	@Autowired
	protected StudentEnrolmentTestRepository repository;

	// Test methods ------------------------------------------------------------


	@ParameterizedTest
	@CsvFileSource(resources = "/student/enrolment/finalize-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test100Positive(final int recordIndex, final String code, final String creditCard, final String holderName, final String expiryDate, final String cvc, final String lowerNibble) {

		super.signIn("student1", "student1");

		super.clickOnMenu("Student", "Enrolment List");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.checkColumnHasValue(recordIndex, 0, code);
		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.fillInputBoxIn("creditCard", creditCard);
		super.fillInputBoxIn("holderName", holderName);
		super.fillInputBoxIn("expiryDate", expiryDate);
		super.fillInputBoxIn("cvc", cvc);
		super.clickOnSubmit("Finalize:");

		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(recordIndex, 0, code);

		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.checkInputBoxHasValue("holderName", holderName);
		super.checkInputBoxHasValue("lowerNibble", lowerNibble);

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/student/enrolment/finalize-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test200Negative(final int recordIndex, final String creditCard, final String holderName, final String expiryDate, final String cvc) {

		super.signIn("student1", "student1");

		super.clickOnMenu("Student", "Enrolment List");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.fillInputBoxIn("creditCard", creditCard);
		super.fillInputBoxIn("holderName", holderName);
		super.fillInputBoxIn("expiryDate", expiryDate);
		super.fillInputBoxIn("cvc", cvc);
		super.clickOnSubmit("Finalize:");

		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	void test300Hacking() {
		// HINT: this test tries to update an enrolment with a role other than "Student",
		// HINT+ or using an student who is not the owner.

		Collection<Enrolment> enrolments;
		String param;

		enrolments = this.repository.findAllEnrolmentsByStudentUsername("student1");
		for (final Enrolment enrolment : enrolments)
			if (enrolment.isDraftMode()) {
				param = String.format("id=%d", enrolment.getId());

				super.checkLinkExists("Sign in");
				super.request("/student/enrolment/finalize", param);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/student/enrolment/finalize", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("auditor1", "auditor1");
				super.request("/student/enrolment/finalize", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company1", "company1");
				super.request("/student/enrolment/finalize", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/student/enrolment/finalize", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("assistant1", "assistant1");
				super.request("/student/enrolment/finalize", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student2", "student2");
				super.request("/student/enrolment/finalize", param);
				super.checkPanicExists();
				super.signOut();
			} else {
				// HINT: this test tries to update a finalised enrolment that was registered by the principal.

				param = String.format("id=%d", enrolment.getId());
				super.signIn("student1", "student1");
				super.request("/student/enrolment/finalize", param);
				super.checkPanicExists();
				super.signOut();

			}
	}

}


package acme.testing.auditor.auditRecord;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.auditRecord.AuditRecord;
import acme.testing.TestHarness;

public class AuditorAuditRecordCorrectionTest extends TestHarness {

	@Autowired
	protected AuditorAuditRecordTestRepository repo;


	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/auditRecord/correction-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test100Positive(final int auditRecordIndex, final int auditRecordRecordIndex, final String subject, final String link, final String assesment, final String startDate, final String finishDate, final String mark, final String confirmation) {

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "My audits");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(auditRecordIndex);
		super.clickOnButton("Audit Records List");
		super.checkListingExists();
		super.clickOnButton("Return");

		super.clickOnButton("Add correction");
		super.checkFormExists();
		super.fillInputBoxIn("subject", subject);
		super.fillInputBoxIn("link", link);
		super.fillInputBoxIn("assesment", assesment);
		super.fillInputBoxIn("periodStart", startDate);
		super.fillInputBoxIn("periodFin", finishDate);
		super.fillInputBoxIn("mark", mark);
		super.fillInputBoxIn("confirmation", confirmation);
		super.clickOnSubmit("Create");
		super.clickOnMenu("Auditor", "My audits");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(auditRecordIndex);
		super.clickOnButton("Audit Records List");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(auditRecordRecordIndex);
		super.checkFormExists();
		super.checkInputBoxHasValue("subject", subject);
		super.checkInputBoxHasValue("assesment", assesment);
		super.checkInputBoxHasValue("link", link);
		super.checkInputBoxHasValue("periodStart", startDate);
		super.checkInputBoxHasValue("periodFin", finishDate);
		super.checkInputBoxHasValue("mark", mark);
		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/auditRecord/correction-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test200Negative(final int auditRecordIndex, final int auditRecordRecordIndex, final String subject, final String link, final String assesment, final String startDate, final String finishDate, final String mark, final String confirmation) {
		// HINT: this test attempts to create jobs with incorrect data.

		super.signIn("auditor1", "auditor1");

		super.clickOnMenu("Auditor", "My audits");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.clickOnListingRecord(auditRecordIndex);
		super.clickOnButton("Audit Records List");
		super.checkListingExists();
		super.clickOnButton("Return");

		super.clickOnButton("Add correction");
		super.checkFormExists();
		super.fillInputBoxIn("subject", subject);
		super.fillInputBoxIn("link", link);
		super.fillInputBoxIn("assesment", assesment);
		super.fillInputBoxIn("periodStart", startDate);
		super.fillInputBoxIn("periodFin", finishDate);
		super.fillInputBoxIn("mark", mark);
		super.fillInputBoxIn("confirmation", confirmation);
		super.clickOnSubmit("Create");

		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	void test300Hacking() {
		// HINT: this test tries to create an Activity using principals with
		// HINT+ inappropriate roles.

		Collection<AuditRecord> AuditRecords;
		String param;

		AuditRecords = this.repo.findAllAuditRecordsByAuditorUsername("auditor1");
		for (final AuditRecord record : AuditRecords)
			if (!record.getAudit().isDraftMode()) {
				param = String.format("id=%d", record.getId());

				super.checkLinkExists("Sign in");
				super.request("/auditor/audit-record/correction", param);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/auditor/audit-record/correction", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/auditor/audit-record/correction", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company1", "company1");
				super.request("/auditor/audit-record/correction", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/auditor/audit-record/correction", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("assistant1", "assistant1");
				super.request("/auditor/audit-record/correction", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("auditor2", "auditor2");
				super.request("/auditor/audit-record/correction", param);
				super.checkPanicExists();
				super.signOut();
			} else {
				// HINT: this test tries to create an activity to a not finalised enrolment that was registered by the principal.
				param = String.format("id=%d", record.getId());
				super.signIn("auditor1", "auditor1");
				super.request("/auditor/audit-record/correction", param);
				super.checkPanicExists();
				super.signOut();
			}

	}
}

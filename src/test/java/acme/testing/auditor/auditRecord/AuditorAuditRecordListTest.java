
package acme.testing.auditor.auditRecord;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.auditRecord.AuditRecord;
import acme.testing.TestHarness;

public class AuditorAuditRecordListTest extends TestHarness {

	@Autowired
	protected AuditorAuditRecordTestRepository repo;


	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/auditRecord/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	void test100Positive(final int auditRecordIndex, final int auditRecordRecordIndex, final String subject, final String abstractAuditRecord, final String nextSubject, final String code, final String mark) {

		super.signIn("auditor1", "auditor1");
		super.clickOnMenu("Auditor", "My audits");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(auditRecordIndex, 0, code);
		super.clickOnListingRecord(auditRecordIndex);
		super.checkInputBoxHasValue("code", code);
		super.clickOnButton("Audit Records List");
		super.checkListingExists();
		super.checkColumnHasValue(auditRecordRecordIndex, 0, subject);
		super.checkColumnHasValue(auditRecordRecordIndex, 2, mark);
		super.clickOnListingRecord(auditRecordRecordIndex);
		super.signOut();
	}

	@Test
	void test200Negative(final int auditRecordIndex, final int auditRecordRecordIndex, final String subject, final String abstractAuditRecord) {
		// HINT: there is no negative case
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
				super.request("/auditor/audit-record/list", param);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/auditor/audit-record/list", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/auditor/audit-record/list", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company1", "company1");
				super.request("/auditor/audit-record/list", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/auditor/audit-record/list", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("assistant1", "assistant1");
				super.request("/auditor/audit-record/list", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("auditor2", "auditor2");
				super.request("/auditor/audit-record/list", param);
				super.checkPanicExists();
				super.signOut();
			} else {
				// HINT: this test tries to create an activity to a not finalised enrolment that was registered by the principal.
				param = String.format("id=%d", record.getId());
				super.signIn("auditor1", "auditor1");
				super.request("/auditor/audit-record/list", param);
				super.checkPanicExists();
				super.signOut();
			}

	}
}

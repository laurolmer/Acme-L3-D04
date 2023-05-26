
package acme.testing.auditor.audit;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.audit.Audit;
import acme.testing.TestHarness;

public class AuditorAuditListTest extends TestHarness {

	@Autowired
	protected AuditorAuditTestRepository repo;


	@ParameterizedTest
	@CsvFileSource(resources = "/auditor/audit/list-positive.csv", encoding = "utf-8", numLinesToSkip = 1)

	public void test100Positive(final int auditIndex, final String code, final String conclusion) {

		super.signIn("auditor1", "auditor1");
		super.clickOnMenu("Auditor", "My audits");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(auditIndex, 0, code);
		super.checkColumnHasValue(auditIndex, 1, conclusion);
		super.signOut();
	}

	@Test
	public void test200Negative() {
		//there is no negative
	}

	@Test
	public void test300Hacking() {
		Collection<Audit> audits;
		String param;

		audits = this.repo.findAllAuditsByAuditorUsername("auditor1");
		for (final Audit audit : audits)
			if (audit.isDraftMode()) {
				param = String.format("id=%d", audit.getId());

				super.checkLinkExists("Sign in");
				super.request("/auditor/audit/list", param);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/auditor/audit/list", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("auditor1", "auditor1");
				super.request("/auditor/audit/list", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company1", "company1");
				super.request("/auditor/audit/list", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("lecturer1", "lecturer1");
				super.request("/auditor/audit/list", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("assistant1", "assistant1");
				super.request("/auditor/audit/list", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student2", "student2");
				super.request("/auditor/audit/list", param);
				super.checkPanicExists();
				super.signOut();
			} else {
				// HINT: this test tries to delete a finalised enrolment that was registered by the principal.

				param = String.format("id=%d", audit.getId());
				super.signIn("student1", "student1");
				super.request("/auditor/audit/list", param);
				super.checkPanicExists();
				super.signOut();

			}
	}
}

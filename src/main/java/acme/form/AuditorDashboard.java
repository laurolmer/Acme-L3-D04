
package acme.form;

import acme.framework.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditorDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	// Total number of audits that an auditor have written for theory and hand-on courses
	private Integer				totalNumAudits;

	// Average, deviation, minimum, and maximum number of auditing records
	//of their audits.
	private Statistic			numAuditRecord;

	// Average, deviation, minimum, and maximum time of period lengths
	//in their auditing records.
	private Statistic			auditingTimePeriod;
}

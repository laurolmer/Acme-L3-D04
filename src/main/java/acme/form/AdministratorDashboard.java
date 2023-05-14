
package acme.form;

import java.util.Map;

import acme.framework.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdministratorDashboard extends AbstractForm {
	// Serialisation identifier -----------------------------------------------

	protected static final long		serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	// Total number of principals with each role.
	private Map<String, Integer>	totalUsers;

	// Ratio of PEEPS with both an email address and a link.
	private Double					linkAndEmailPeepsRatio;

	// Ratios of critical and non-critical BULLETINS.
	private Double					criticalBulletinsRatio;

	// Average, minimum, maximum, and standard deviation of the budget in the OFFERS grouped by currency.
	private Map<String, Statistic>	budgetByCurrencyOfferData;

	// Average, minimum, maximum, and standard deviation of the number of NOTES posted over the last 10 weeks.
	private Statistic				notesInLast10WeeksData;

}


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

	// Total number of principals with each role
	private Map<String, Integer>	principalsByRole;

	// Ratio of peeps with both an email address and a link
	private Double					linkAndEmailPeepsRatio;

	// Ratios of critical and non-critical bulletins
	Double							ratioOfCriticalBulletins;
	Double							ratioOfNonCriticalBulletins;

	// Average, minimum, maximum, and standard deviation of the budget in the offers grouped by currency
	private Map<String, Statistic>	currentsOffersStats;

	// Average, minimum, maximum, and standard deviation of the number of notes posted over the last 10 weeks
	private Map<Integer, Integer>	notesInLast10WeeksStats;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}

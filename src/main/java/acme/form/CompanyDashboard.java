
package acme.form;

import java.util.Map;

import acme.framework.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyDashboard extends AbstractForm {
	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	//total number of practica regarding theory or hands-on courses grouped by month during the last year
	//{[Enero,0],[Febrero,1],[Marzo,2],...} 
	private Map<String, Long>	TotalNumberOfPractica;

	// Average, deviation, minimum, and maximum time of his or her sessions.
	private Statistic			sessionLength;

	// Average, deviation, minimum, and maximum time of his or her sessions.
	private Statistic			practicaLength;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}

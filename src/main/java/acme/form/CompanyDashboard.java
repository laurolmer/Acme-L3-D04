
package acme.form;

import java.util.List;

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
	//[0,1,2,3,4,5,6,7,8] 0=Enero; 1=Febrero; 2=Marzo;....
	private List<Integer>		numTotalPractica;

	// Average, deviation, minimum, and maximum time of his or her sessions.
	private Statistic			sessionLength;

	// Average, deviation, minimum, and maximum time of his or her sessions.
	private Statistic			practicaLength;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}

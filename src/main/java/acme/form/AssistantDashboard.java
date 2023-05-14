
package acme.form;

import acme.framework.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AssistantDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	// Total number of tutorials regarding theory or hands-on courses.
	protected Integer			totalNumTheoryTutorials;

	protected Integer			totalNumHandsOnTutorials;

	// Average, deviation, minimum, and maximum time of his or her sessions.
	protected Statistic			sessionTime;

	// Average, deviation, minimum, and maximum time of his or her tutorials.
	protected Statistic			tutorialTime;

}

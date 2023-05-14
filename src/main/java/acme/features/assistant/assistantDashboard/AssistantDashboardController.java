
package acme.features.assistant.assistantDashboard;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.form.AssistantDashboard;
import acme.framework.controllers.AbstractController;
import acme.roles.Assistant;

@Controller
public class AssistantDashboardController extends AbstractController<Assistant, AssistantDashboard> {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected AssistantDashboardShowService showService;


	// Constructors -----------------------------------------------------------
	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
	}

}

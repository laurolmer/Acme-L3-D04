
package acme.features.administrator.administratorDashboard;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.form.AdministratorDashboard;
import acme.framework.components.accounts.Administrator;
import acme.framework.controllers.AbstractController;

@Controller
public class AdministratorDashboardController extends AbstractController<Administrator, AdministratorDashboard> {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected AdministratorDashboardShowService showService;


	// Constructors -----------------------------------------------------------
	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
	}
}

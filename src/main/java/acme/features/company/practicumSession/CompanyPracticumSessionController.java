
package acme.features.company.practicumSession;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.entities.practicumSession.PracticumSession;
import acme.framework.controllers.AbstractController;
import acme.roles.Company;

@Controller
public class CompanyPracticumSessionController extends AbstractController<Company, PracticumSession> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private CompanyPracticumSessionShowService		showService;
	@Autowired
	private CompanyPracticumSessionCreateService	createService;
	@Autowired
	private CompanyPracticumSessionUpdateService	updateService;
	@Autowired
	private CompanyPracticumSessionDeleteService	deleteService;
	@Autowired
	private CompanyPracticumSessionConfirmService	confirmService;
	@Autowired
	private CompanyPracticumSessionListService		listService;


	// Constructors -----------------------------------------------------------
	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("list", this.listService);

		super.addCustomCommand("confirm", "update", this.confirmService);
	}
}

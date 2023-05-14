
package acme.features.assistant.tutorialSession;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.entities.tutorialSession.TutorialSession;
import acme.framework.controllers.AbstractController;
import acme.roles.Assistant;

@Controller
public class AssistantTutorialSessionController extends AbstractController<Assistant, TutorialSession> {

	@Autowired
	protected AssistantTutorialSessionShowService		showService;

	@Autowired
	protected AssistantTutorialSessionCreateService		createService;

	@Autowired
	protected AssistantTutorialSessionUpdateService		updateService;

	@Autowired
	protected AssistantTutorialSessionDeleteService		deleteService;

	@Autowired
	protected AssistantTutorialSessionListService		listService;

	@Autowired
	protected AssistantTutorialSessionPublishService	publishService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);
		super.addCustomCommand("publish", "update", this.publishService);
		super.addBasicCommand("list", this.listService);
	}
}

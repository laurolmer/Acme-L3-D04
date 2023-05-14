
package acme.features.assistant.tutorialSession;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.tutorial.Tutorial;
import acme.entities.tutorialSession.TutorialSession;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantTutorialSessionListService extends AbstractService<Assistant, TutorialSession> {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected AssistantTutorialSessionRepository repository;


	// AbstractService interface ----------------------------------------------
	@Override
	public void check() {
		boolean status;
		status = super.getRequest().hasData("masterId", int.class);
		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		Principal principal;
		Tutorial tutorial;
		int tutorialId;
		tutorialId = super.getRequest().getData("masterId", int.class);
		tutorial = this.repository.findTutorialById(tutorialId);
		principal = super.getRequest().getPrincipal();
		status = tutorial != null && (!tutorial.isDraftMode() || principal.hasRole(Assistant.class));
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<TutorialSession> tutorialSession;
		int tutorialId;
		tutorialId = super.getRequest().getData("masterId", int.class);
		tutorialSession = this.repository.findAllSessionsByTutorialId(tutorialId);
		super.getBuffer().setData(tutorialSession);
	}

	@Override
	public void unbind(final TutorialSession tutorialSession) {
		assert tutorialSession != null;
		Tuple tuple;
		String payload;
		tuple = super.unbind(tutorialSession, "title", "abstractSession");
		payload = String.format("%s; %s", tutorialSession.getTitle(), tutorialSession.getAbstractSession());
		tuple.put("payload", payload);
		tuple.put("estimatedTotalTime", tutorialSession.computeEstimatedTotalTime());
		super.getResponse().setData(tuple);
	}

	@Override
	public void unbind(final Collection<TutorialSession> tutorialSession) {
		assert tutorialSession != null;
		int tutorialId;
		final Tutorial tutorial;
		final boolean showCreate;
		tutorialId = super.getRequest().getData("masterId", int.class);
		tutorial = this.repository.findTutorialById(tutorialId);
		showCreate = super.getRequest().getPrincipal().hasRole(tutorial.getAssistant()) && tutorial.isDraftMode();
		super.getResponse().setGlobal("masterId", tutorialId);
		super.getResponse().setGlobal("showCreate", showCreate);
	}
}

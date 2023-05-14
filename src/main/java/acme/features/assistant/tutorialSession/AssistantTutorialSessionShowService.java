
package acme.features.assistant.tutorialSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.tutorial.Tutorial;
import acme.entities.tutorialSession.SessionType;
import acme.entities.tutorialSession.TutorialSession;
import acme.framework.components.accounts.Principal;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantTutorialSessionShowService extends AbstractService<Assistant, TutorialSession> {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected AssistantTutorialSessionRepository repository;


	// AbstractService interface ----------------------------------------------
	@Override
	public void check() {
		boolean status;
		status = super.getRequest().hasData("id", int.class);
		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		Principal principal;
		final TutorialSession session;
		final Assistant assistant;
		int sessionId;
		Tutorial tutorial;
		principal = super.getRequest().getPrincipal();
		sessionId = super.getRequest().getData("id", int.class);
		session = this.repository.findTutorialSessionById(sessionId);
		tutorial = this.repository.findTutorialByTutorialSessionId(sessionId);
		assistant = session == null ? null : session.getTutorial().getAssistant();
		status = session != null && (!tutorial.isDraftMode() || principal.hasRole(assistant));
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TutorialSession tutorialSession;
		int id;
		id = super.getRequest().getData("id", int.class);
		tutorialSession = this.repository.findTutorialSessionById(id);
		super.getBuffer().setData(tutorialSession);
	}

	@Override
	public void unbind(final TutorialSession tutorialSession) {
		assert tutorialSession != null;
		Tuple tuple;
		Double estimatedTotalTime;
		SelectChoices choices;
		choices = SelectChoices.from(SessionType.class, tutorialSession.getSessionType());
		tuple = super.unbind(tutorialSession, "title", "abstractSession", "sessionType", "startPeriod", "finishPeriod", "link", "draftMode");
		estimatedTotalTime = tutorialSession.computeEstimatedTotalTime();
		if (estimatedTotalTime != null)
			tuple.put("finishPeriod", estimatedTotalTime);
		tuple.put("finishPeriod", tutorialSession.computeEstimatedTotalTime());
		tuple.put("masterId", super.getRequest().getData("id", int.class));
		tuple.put("sessionType", choices);
		tuple.put("draftMode", tutorialSession.getTutorial().isDraftMode() && tutorialSession.isDraftMode());
		super.getResponse().setData(tuple);
	}
}

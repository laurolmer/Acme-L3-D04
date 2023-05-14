
package acme.features.assistant.tutorialSession;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.tutorial.Tutorial;
import acme.entities.tutorialSession.SessionType;
import acme.entities.tutorialSession.TutorialSession;
import acme.framework.components.accounts.Principal;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantTutorialSessionUpdateService extends AbstractService<Assistant, TutorialSession> {

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
		int sessionId;
		final TutorialSession session;
		final Principal principal;
		final Assistant assistant;
		Tutorial tutorial;
		principal = super.getRequest().getPrincipal();
		sessionId = super.getRequest().getData("id", int.class);
		session = this.repository.findTutorialSessionById(sessionId);
		assistant = session == null ? null : session.getTutorial().getAssistant();
		tutorial = this.repository.findTutorialByTutorialSessionId(sessionId);
		status = tutorial != null && session != null && (tutorial.isDraftMode() || principal.hasRole(assistant));
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
	public void bind(final TutorialSession tutorialSession) {
		assert tutorialSession != null;
		final double estimatedTotalTime;
		final Date finishPeriod;
		estimatedTotalTime = super.getRequest().getData("finishPeriod", Double.class);
		finishPeriod = tutorialSession.deltaFromStartMoment(estimatedTotalTime);
		super.bind(tutorialSession, "title", "abstractSession", "sessionType", "startPeriod", "finishPeriod", "link");
		tutorialSession.setFinishPeriod(finishPeriod);
	}

	@Override
	public void validate(final TutorialSession tutorialSession) {
		assert tutorialSession != null;
		Date minStartPeriod;
		boolean condition1;
		boolean condition2;
		final Date maxValue = new Date("2100/12/31 23:59");
		final Date minValue = new Date("2000/01/01 00:00");
		// El periodo de inicio de la sesión de tutoría debe ser mínimo un día después a la fecha actual.
		if (!super.getBuffer().getErrors().hasErrors("startPeriod")) {
			minStartPeriod = MomentHelper.deltaFromCurrentMoment(1, ChronoUnit.DAYS);
			super.state(MomentHelper.isAfter(tutorialSession.getStartPeriod(), minStartPeriod), "startPeriod", "assistant.session.startPeriod-before-instantiationMoment");
		}
		// El periodo de finalización debe ser posterior al periodo de inicio.
		if (!super.getBuffer().getErrors().hasErrors("finishPeriod"))
			super.state(MomentHelper.isAfter(tutorialSession.getFinishPeriod(), tutorialSession.getStartPeriod()), "finishPeriod", "assistant.session.finishPeriod-before-startPeriod");
		if (!super.getBuffer().getErrors().hasErrors("finishPeriod")) {
			// El periodo de finalización debe ser 1 hora posterior como mínimo respecto al periodo de inicio.
			condition1 = MomentHelper.isLongEnough(tutorialSession.getStartPeriod(), tutorialSession.getFinishPeriod(), 1, ChronoUnit.HOURS);
			// El periodo de finalización debe durar 5 horas como máximo respecto al periodo de inicio.
			condition2 = MomentHelper.computeDuration(tutorialSession.getStartPeriod(), tutorialSession.getFinishPeriod()).getSeconds() <= Duration.ofHours(5).getSeconds();
			super.state(condition1 && condition2, "finishPeriod", "assistant.session.bad-finishPeriod-time");
		}
		// EndPeriod must be before 2100/12/31 23:59
		if (!super.getBuffer().getErrors().hasErrors("finishPeriod"))
			super.state(!MomentHelper.isAfter(tutorialSession.getFinishPeriod(), maxValue), "finishPeriod", "assistant.session.end-reached-max-value");
		// StartPeriod must be after 2000/01/01 00:00
		if (!super.getBuffer().getErrors().hasErrors("startPeriod"))
			super.state(MomentHelper.isAfter(tutorialSession.getStartPeriod(), minValue), "startPeriod", "assistant.session.start-didnot-reach-min-value");
		if (!super.getBuffer().getErrors().hasErrors("finishPeriod")) {
			final boolean eval = tutorialSession.computeEstimatedTotalTime() > 0.0;
			super.state(eval, "finishPeriod", "assistant.session.estimatedTotalTime");
		}
	}

	@Override
	public void perform(final TutorialSession tutorialSession) {
		assert tutorialSession != null;
		this.repository.save(tutorialSession);
	}

	@Override
	public void unbind(final TutorialSession tutorialSession) {
		assert tutorialSession != null;
		Tuple tuple;
		SelectChoices choices;
		Double estimatedTotalTime;
		choices = SelectChoices.from(SessionType.class, tutorialSession.getSessionType());
		tuple = super.unbind(tutorialSession, "title", "abstractSession", "sessionType", "startPeriod", "finishPeriod", "link");
		estimatedTotalTime = tutorialSession.computeEstimatedTotalTime();
		if (estimatedTotalTime != null)
			tuple.put("finishPeriod", estimatedTotalTime);
		tuple.put("masterId", super.getRequest().getData("id", int.class));
		tuple.put("sessionType", choices);
		tuple.put("draftMode", tutorialSession.getTutorial().isDraftMode() && tutorialSession.isDraftMode());
		super.getResponse().setData(tuple);
	}
}

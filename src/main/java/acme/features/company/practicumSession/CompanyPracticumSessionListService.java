
package acme.features.company.practicumSession;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.practicum.Practicum;
import acme.entities.practicumSession.PracticumSession;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MessageHelper;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanyPracticumSessionListService extends AbstractService<Company, PracticumSession> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private CompanyPracticumSessionRepository repository;


	// AbstractService Interface ----------------------------------------------
	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("masterId", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		int practicumId;
		Practicum practicum;
		Principal principal;
		Company company;

		principal = super.getRequest().getPrincipal();
		practicumId = super.getRequest().getData("masterId", int.class);
		practicum = this.repository.findOnePracticumById(practicumId);
		company = practicum == null ? null : practicum.getCompany();
		status = practicum != null && principal.hasRole(company);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<PracticumSession> PracticumSessions;
		int practicumId;

		practicumId = super.getRequest().getData("masterId", int.class);
		PracticumSessions = this.repository.findManyPracticumSessionsByPracticumId(practicumId);

		super.getBuffer().setData(PracticumSessions);
	}

	@Override
	public void unbind(final PracticumSession PracticumSession) {
		assert PracticumSession != null;

		Tuple tuple;
		final String additional;
		String payload;
		Date start;
		Date end;

		start = PracticumSession.getStart();
		end = PracticumSession.getEnd();
		tuple = super.unbind(PracticumSession, "code", "title", "abstractSession", "start", "end", "link");
		additional = MessageHelper.getMessage(PracticumSession.isAdditional() ? "company.practicum-session.list.label.yes" : "company.practicum-session.list.label.no");
		payload = String.format("%s", PracticumSession.getAbstractSession());
		tuple.put("payload", payload);
		tuple.put("additional", additional);
		tuple.put("exactDuration", MomentHelper.computeDuration(start, end).toHours());

		super.getResponse().setData(tuple);
	}

	@Override
	public void unbind(final Collection<PracticumSession> PracticumSessions) {
		assert PracticumSessions != null;

		int practicumId;
		Practicum practicum;
		boolean showCreate;
		Principal principal;
		boolean extraAvailable;

		principal = super.getRequest().getPrincipal();
		practicumId = super.getRequest().getData("masterId", int.class);
		practicum = this.repository.findOnePracticumById(practicumId);
		showCreate = practicum.getDraftMode() && principal.hasRole(practicum.getCompany());
		extraAvailable = PracticumSessions.stream().noneMatch(PracticumSession::isAdditional);

		super.getResponse().setGlobal("masterId", practicumId);
		super.getResponse().setGlobal("showCreate", showCreate);
		super.getResponse().setGlobal("extraAvailable", extraAvailable);
	}
}

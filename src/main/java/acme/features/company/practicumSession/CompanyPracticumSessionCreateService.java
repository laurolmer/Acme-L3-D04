
package acme.features.company.practicumSession;

import java.time.temporal.ChronoUnit;
import java.util.Date;

// import acme.services.SpamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.practicum.Practicum;
import acme.entities.practicumSession.PracticumSession;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.controllers.HttpMethod;
import acme.framework.helpers.MomentHelper;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanyPracticumSessionCreateService extends AbstractService<Company, PracticumSession> {

	// Constants -------------------------------------------------------------
	public static final int						ONE_WEEK	= 1;

	// Internal state ---------------------------------------------------------
	@Autowired
	private CompanyPracticumSessionRepository	repository;

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
		boolean hasExtraAvailable;
		Principal principal;
		Company company;

		principal = super.getRequest().getPrincipal();
		practicumId = super.getRequest().getData("masterId", int.class);
		practicum = this.repository.findOnePracticumById(practicumId);
		status = false;

		if (practicum != null) {
			hasExtraAvailable = this.repository.findManyPracticumSessionsByExtraAvailableAndPracticumId(practicum.getId()).isEmpty();
			company = practicum.getCompany();

			status = (practicum.getDraftMode() || hasExtraAvailable) && principal.hasRole(company);
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		PracticumSession PracticumSession;
		int practicumId;
		Practicum practicum;
		boolean draftMode;

		practicumId = super.getRequest().getData("masterId", int.class);
		practicum = this.repository.findOnePracticumById(practicumId);
		draftMode = practicum.getDraftMode();

		PracticumSession = new PracticumSession();
		PracticumSession.setPracticum(practicum);
		PracticumSession.setAdditional(!draftMode);

		super.getBuffer().setData(PracticumSession);
	}

	@Override
	public void bind(final PracticumSession PracticumSession) {
		assert PracticumSession != null;

		super.bind(PracticumSession, "code", "title", "abstractSession", "start", "end", "link");
	}

	@Override
	public void validate(final PracticumSession PracticumSession) {
		assert PracticumSession != null;

		if (!super.getBuffer().getErrors().hasErrors("start") || !super.getBuffer().getErrors().hasErrors("end")) {
			Date start;
			Date end;
			Date inAWeekFromNow;
			Date inAWeekFromStart;

			start = PracticumSession.getStart();
			end = PracticumSession.getEnd();
			inAWeekFromNow = MomentHelper.deltaFromCurrentMoment(CompanyPracticumSessionCreateService.ONE_WEEK, ChronoUnit.WEEKS);
			inAWeekFromStart = MomentHelper.deltaFromMoment(start, CompanyPracticumSessionCreateService.ONE_WEEK, ChronoUnit.WEEKS);

			if (!super.getBuffer().getErrors().hasErrors("start"))
				super.state(MomentHelper.isAfter(start, inAWeekFromNow), "start", "company.practicum-session.error.start-after-now");
			if (!super.getBuffer().getErrors().hasErrors("end"))
				super.state(MomentHelper.isAfter(end, inAWeekFromStart), "end", "company.practicum-session.error.end-after-start");
		}

		if (super.getRequest().hasData("confirmed") && !super.getBuffer().getErrors().hasErrors("confirmed")) {
			Practicum practicum;
			boolean confirmed;

			confirmed = super.getRequest().getData("confirmed", boolean.class);
			practicum = PracticumSession.getPracticum();

			super.state(confirmed || practicum.getDraftMode(), "confirmed", "company.practicum-session.error.confirmed");
		}
	}

	@Override
	public void perform(final PracticumSession PracticumSession) {
		assert PracticumSession != null;

		this.repository.save(PracticumSession);
	}

	@Override
	public void unbind(final PracticumSession PracticumSession) {
		assert PracticumSession != null;

		Practicum practicum;
		Tuple tuple;

		practicum = PracticumSession.getPracticum();
		tuple = super.unbind(PracticumSession, "code", "title", "abstractSession", "start", "end", "link", "additional");
		tuple.put("masterId", practicum.getId());
		tuple.put("draftMode", practicum.getDraftMode());

		super.getResponse().setData(tuple);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();
	}
}

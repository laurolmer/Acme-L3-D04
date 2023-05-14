
package acme.features.company.practicumSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.practicum.Practicum;
import acme.entities.practicumSession.PracticumSession;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanyPracticumSessionDeleteService extends AbstractService<Company, PracticumSession> {

	// Constants --------------------------------------------------------------
	protected static final String[]				PROPERTIES	= {
		"code", "title", "abstractSession", "start", "end", "link"
	};

	// Internal state ---------------------------------------------------------
	@Autowired
	private CompanyPracticumSessionRepository	repository;


	// AbstractService Interface ----------------------------------------------
	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		int PracticumSessionId;
		PracticumSession PracticumSession;
		Principal principal;
		Practicum practicum;
		Boolean getDraftMode;
		Boolean isAdditional;

		principal = super.getRequest().getPrincipal();
		PracticumSessionId = super.getRequest().getData("id", int.class);
		PracticumSession = this.repository.findOnePracticumSessionById(PracticumSessionId);
		status = false;

		if (PracticumSession != null) {
			practicum = PracticumSession.getPracticum();

			getDraftMode = practicum.getDraftMode();
			isAdditional = !PracticumSession.isConfirmed() && !getDraftMode;

			status = (getDraftMode || isAdditional) && principal.hasRole(practicum.getCompany());
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		PracticumSession PracticumSession;
		int PracticumSessionId;

		PracticumSessionId = super.getRequest().getData("id", int.class);
		PracticumSession = this.repository.findOnePracticumSessionById(PracticumSessionId);

		super.getBuffer().setData(PracticumSession);
	}

	@Override
	public void bind(final PracticumSession PracticumSession) {
		assert PracticumSession != null;

		super.bind(PracticumSession, CompanyPracticumSessionDeleteService.PROPERTIES);
	}

	@Override
	public void validate(final PracticumSession PracticumSession) {
		assert PracticumSession != null;
	}

	@Override
	public void perform(final PracticumSession PracticumSession) {
		assert PracticumSession != null;

		this.repository.delete(PracticumSession);
	}

	@Override
	public void unbind(final PracticumSession PracticumSession) {
		assert PracticumSession != null;

		Practicum practicum;
		Tuple tuple;

		practicum = PracticumSession.getPracticum();
		tuple = super.unbind(PracticumSession, CompanyPracticumSessionUpdateService.PROPERTIES_UNBIND);
		tuple.put("masterId", practicum.getId());
		tuple.put("draftMode", practicum.getDraftMode());

		super.getResponse().setData(tuple);
	}
}

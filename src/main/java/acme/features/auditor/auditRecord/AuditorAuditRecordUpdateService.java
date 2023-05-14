
package acme.features.auditor.auditRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.auditRecord.AuditRecord;
import acme.entities.auditRecord.MarkType;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuditorAuditRecordUpdateService extends AbstractService<Auditor, AuditRecord> {

	@Autowired
	protected AuditorAuditRecordRepository repository;


	@Override
	public void check() {
		final boolean status = super.getRequest().hasData("id", int.class);
		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {

		boolean status;
		final AuditRecord ar = this.repository.findAuditRecordById(super.getRequest().getData("id", int.class));
		status = super.getRequest().getPrincipal().hasRole(ar.getAudit().getAuditor()) && ar.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		AuditRecord object;

		object = this.repository.findAuditRecordById(super.getRequest().getData("id", int.class));
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final AuditRecord object) {
		assert object != null;

		final String mark = super.getRequest().getData("mark", String.class);
		super.bind(object, "subject", "assesment", "periodStart", "periodFin", "link", "draftMode");
		object.setMark(MarkType.transform(mark));
	}

	@Override
	public void validate(final AuditRecord object) {
		assert object != null;
		if (!super.getBuffer().getErrors().hasErrors("periodStart") && !super.getBuffer().getErrors().hasErrors("periodFin"))
			if (!MomentHelper.isBefore(object.getPeriodStart(), object.getPeriodFin()))
				super.state(false, "periodStart", "auditor.auditRecord.error.date.StartFinError");
			else
				super.state(!(object.getARDuration() <= 1), "periodStart", "auditor.auditRecord.error.date.oneHourRule");

	}

	@Override
	public void perform(final AuditRecord object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final AuditRecord object) {
		assert object != null;
		final Tuple tuple;
		final SelectChoices elecs = SelectChoices.from(MarkType.class, object.getMark());
		tuple = super.unbind(object, "subject", "assesment", "periodStart", "periodFin", "mark", "link", "draftMode");
		tuple.put("elecs", elecs);

		super.getResponse().setData(tuple);
	}
}

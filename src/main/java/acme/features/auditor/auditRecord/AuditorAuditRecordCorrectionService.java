
package acme.features.auditor.auditRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.audit.Audit;
import acme.entities.auditRecord.AuditRecord;
import acme.entities.auditRecord.MarkType;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.controllers.HttpMethod;
import acme.framework.helpers.MomentHelper;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuditorAuditRecordCorrectionService extends AbstractService<Auditor, AuditRecord> {

	@Autowired
	protected AuditorAuditRecordRepository repository;


	@Override
	public void check() {
		final boolean status = super.getRequest().hasData("auditId", int.class);
		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {

		boolean status;
		final Audit audit = this.repository.findAuditById(super.getRequest().getData("auditId", int.class));
		status = super.getRequest().getPrincipal().hasRole(audit.getAuditor()) && !audit.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		AuditRecord object;

		object = new AuditRecord();
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final AuditRecord object) {
		assert object != null;

		final int auditId = super.getRequest().getData("auditId", int.class);
		final Audit audit = this.repository.findAuditById(auditId);
		final String mark = super.getRequest().getData("mark", String.class);
		super.bind(object, "subject", "assesment", "periodStart", "periodFin", "link");
		object.setMark(MarkType.transform(mark));
		object.setAudit(audit);
		object.setDraftMode(false);
		object.setCorrection(true);
	}

	@Override
	public void validate(final AuditRecord object) {
		assert object != null;
		final Boolean confirm = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirm, "*", "auditor.auditRecord.correction.confirmation");

		if (!super.getBuffer().getErrors().hasErrors("periodStart") && !super.getBuffer().getErrors().hasErrors("periodFin"))
			if (!MomentHelper.isBefore(object.getPeriodStart(), object.getPeriodFin()))
				super.state(false, "periodStart", "auditor.auditRecord.error.date.StartFinError");
			else
				super.state((object.getARDuration() > 1), "periodStart", "auditor.auditRecord.error.date.oneHourRule");

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
		final SelectChoices marks = SelectChoices.from(MarkType.class, object.getMark());

		tuple = super.unbind(object, "subject", "assesment", "periodStart", "periodFin", "link", "mark");
		tuple.put("elecs", marks);
		tuple.put("auditId", super.getRequest().getData("auditId", int.class));

		super.getResponse().setData(tuple);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();
	}
}


package acme.features.auditor.auditRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.auditRecord.AuditRecord;
import acme.entities.auditRecord.MarkType;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuditorAuditRecordDeleteService extends AbstractService<Auditor, AuditRecord> {

	@Autowired
	protected AuditorAuditRecordRepository repository;


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		AuditRecord ar;

		ar = this.repository.findAuditRecordById(super.getRequest().getData("id", int.class));

		status = ar != null && super.getRequest().getPrincipal().hasRole(ar.getAudit().getAuditor()) && ar.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		AuditRecord object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findAuditRecordById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final AuditRecord object) {
		assert object != null;

		final String mark = super.getRequest().getData("mark", String.class);
		super.bind(object, "subject", "assessment", "link", "startDate", "finishDate");
		object.setMark(MarkType.transform(mark));
		object.setDraftMode(true);
		object.setCorrection(false);
	}

	@Override
	public void validate(final AuditRecord object) {
		assert object != null;

	}

	@Override
	public void perform(final AuditRecord object) {
		assert object != null;
		this.repository.delete(object);
	}

	@Override
	public void unbind(final AuditRecord object) {
		assert object != null;

		final Tuple tuple;
		final SelectChoices marks = SelectChoices.from(MarkType.class, object.getMark());

		tuple = super.unbind(object, "subject", "assesment", "periodStart", "periodFin", "link", "mark");
		tuple.put("elecs", marks);

		super.getResponse().setData(tuple);
	}
}

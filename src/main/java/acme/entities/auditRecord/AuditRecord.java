
package acme.entities.auditRecord;

import java.time.Duration;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.entities.audit.Audit;
import acme.framework.data.AbstractEntity;
import acme.framework.helpers.MomentHelper;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AuditRecord extends AbstractEntity {

	protected static final long	serialVersionUID	= 1L;

	@NotBlank
	@Length(max = 75)
	protected String			subject;

	@NotBlank
	@Length(max = 100)
	protected String			assesment;

	@PastOrPresent
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	protected Date				periodStart;

	@PastOrPresent
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	protected Date				periodFin;

	@NotNull
	protected MarkType			mark;

	@URL
	protected String			link;

	protected boolean			draftMode;

	protected boolean			correction;

	//Relaciones
	@Valid
	@NotNull
	@ManyToOne(optional = false)
	protected Audit				audit;

	//Derivados


	@Transient
	public Double getARDuration() {
		final Duration duration = MomentHelper.computeDuration(this.periodStart, this.periodFin);
		return duration.getSeconds() / 3600.0;
	}
}

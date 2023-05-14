
package acme.entities.practicumSession;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.entities.practicum.Practicum;
import acme.framework.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PracticumSession extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------
	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "^[A-Z]{1,3}[0-9]{3}$")
	protected String			code;

	@NotBlank
	@Length(max = 75)
	protected String			title;

	@NotBlank
	@Length(max = 100)
	protected String			abstractSession;

	@NotNull
	@Temporal(TemporalType.DATE)
	protected Date				start;

	@NotNull								//(at least one week ahead, at least one week long)
	@Temporal(TemporalType.DATE)
	protected Date				end;

	@URL
	protected String			link;

	protected boolean			additional;

	protected boolean			confirmed;

	// Derived attributes -----------------------------------------------------

	//	public double computeEstimatedTotalTime() {
	//		double estimatedTotalTime;
	//		Duration timeBetween;
	//		timeBetween = MomentHelper.computeDuration(this.start, this.end);
	//		estimatedTotalTime = timeBetween.toHours();
	//		return estimatedTotalTime;
	//	}

	// Relationships ----------------------------------------------------------

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	protected Practicum			practicum;
}


package acme.entities.tutorialSession;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.entities.tutorial.Tutorial;
import acme.framework.data.AbstractEntity;
import acme.framework.helpers.MomentHelper;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TutorialSession extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------
	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------
	@NotBlank
	@Length(max = 75)
	protected String			title;

	@NotBlank
	@Length(max = 100)
	protected String			abstractSession;

	@NotNull
	protected SessionType		sessionType;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	protected Date				startPeriod;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	protected Date				finishPeriod;

	@URL
	protected String			link;

	protected boolean			draftMode;


	// Métodos derivados -----------------------------------------------------
	public Date deltaFromStartMoment(final double amount) {
		assert this.startPeriod != null;
		Date result;
		long hour, minutes;
		long delta, millis;
		hour = (long) Math.floor(amount);
		minutes = (long) ((amount - hour) * ChronoUnit.HOURS.getDuration().toMinutes());
		delta = hour * ChronoUnit.HOURS.getDuration().toMillis() + minutes * ChronoUnit.MINUTES.getDuration().toMillis();
		millis = this.startPeriod.getTime() + delta;
		result = new Date(millis);
		return result;
	}

	public double computeEstimatedTotalTime() {
		Duration timeBetween;
		if (this.finishPeriod != null) {
			timeBetween = MomentHelper.computeDuration(this.startPeriod, this.finishPeriod);
			return (double) timeBetween.toMinutes() / ChronoUnit.HOURS.getDuration().toMinutes();
		}
		return 0.0;
	}


	// Relationships ----------------------------------------------------------
	@NotNull
	@Valid
	@ManyToOne(optional = false)
	protected Tutorial tutorial;

}

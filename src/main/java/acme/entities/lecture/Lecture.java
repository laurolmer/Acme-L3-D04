
package acme.entities.lecture;

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

import acme.framework.data.AbstractEntity;
import acme.framework.helpers.MomentHelper;
import acme.roles.Lecturer;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Lecture extends AbstractEntity {

	//	Serialisation identifier ---------------------------
	protected static final long	serialVersionUID	= 1L;

	//	Attributes -----------------------------------------
	@NotBlank
	@Length(max = 75)
	protected String			title;

	@NotBlank
	@Length(max = 100)
	protected String			lectureAbstract;

	//estimated learning time
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	protected Date				startPeriod;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	protected Date				endPeriod;

	@NotBlank
	@Length(max = 100)
	protected String			body;

	@NotNull
	protected LectureType		lectureType;

	@URL
	protected String			link;

	@ManyToOne(optional = false)
	@NotNull
	@Valid
	protected Lecturer			lecturer;

	protected boolean			draftMode;

	//	Methods ---------------------------------------------------


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

	public double computeEstimatedLearningTime() {
		Duration timeBetween;

		if (this.endPeriod != null) {
			timeBetween = MomentHelper.computeDuration(this.startPeriod, this.endPeriod);
			return (double) timeBetween.toMinutes() / ChronoUnit.HOURS.getDuration().toMinutes();
		}

		return 0.0;
	}

}

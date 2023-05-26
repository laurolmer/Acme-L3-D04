
package acme.entities.enrolment;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import acme.entities.activity.Activity;
import acme.entities.course.Course;
import acme.framework.data.AbstractEntity;
import acme.roles.Student;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Enrolment extends AbstractEntity {

	protected static final long	serialVersionUID	= 1L;

	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "^[A-Z]{1,3}[0-9]{3}$")
	protected String			code;

	@Length(max = 75)
	@NotBlank
	protected String			motivation;

	@Length(max = 100)
	@NotBlank
	protected String			goals;

	protected boolean			draftMode;

	@Length(max = 75)
	protected String			holderName;

	@Length(max = 4)
	protected String			lowerNibble;

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	protected Student			student;

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	protected Course			course;


	//Metodos derivados ----------------------------------------------------
	public Double workTime(final Collection<Activity> activities) {
		double res = 0.0;
		if (!activities.isEmpty())
			for (final Activity activity : activities) {
				Double hours;
				final Date startDate = activity.getStartPeriod();
				final Date endDate = activity.getEndPeriod();
				hours = Math.abs(endDate.getTime() / 3600000. - startDate.getTime() / 3600000.);
				res += hours;
			}
		return res;
	}

}

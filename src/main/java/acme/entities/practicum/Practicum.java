
package acme.entities.practicum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import acme.entities.course.Course;
import acme.framework.data.AbstractEntity;
import acme.roles.Company;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Practicum extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------
	protected static final long	serialVersionUID	= 1L;

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
	protected String			abstractPracticum;

	@NotBlank
	@Length(max = 100)
	protected String			goals;

	@NotNull
	protected Boolean			draftMode;

	// Derived attributes -----------------------------------------------------

	// computed from sessions plus/minus 10%
	// Métodos derivados ------------------------------------------------------
	//	public Double computeEstimatedTotalTime(final Collection<PracticumSession> sessions) {
	//		double estimatedTotalTime = 0.;
	//		Optional<Double> optEstimatedTotalTime;
	//		optEstimatedTotalTime = sessions.stream().map(PracticumSession::computeEstimatedTotalTime).reduce(Double::sum);
	//		if (optEstimatedTotalTime.isPresent())
	//			estimatedTotalTime = optEstimatedTotalTime.get();
	//		return estimatedTotalTime;
	//	}

	// Relationships ----------------------------------------------------------

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	protected Company			company;

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	private Course				course;

}

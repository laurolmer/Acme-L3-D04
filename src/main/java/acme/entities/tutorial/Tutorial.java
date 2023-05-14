
package acme.entities.tutorial;

import java.util.Collection;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import acme.entities.course.Course;
import acme.entities.tutorialSession.TutorialSession;
import acme.framework.data.AbstractEntity;
import acme.roles.Assistant;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Tutorial extends AbstractEntity {

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
	protected String			abstractTutorial;

	@NotBlank
	@Length(max = 100)
	protected String			goals;

	protected boolean			draftMode;


	// Métodos derivados ------------------------------------------------------
	public Double computeEstimatedTotalTime(final Collection<TutorialSession> sessions) {
		double estimatedTotalTime = 0.;
		Optional<Double> optEstimatedTotalTime;
		optEstimatedTotalTime = sessions.stream().map(TutorialSession::computeEstimatedTotalTime).reduce(Double::sum);
		if (optEstimatedTotalTime.isPresent())
			estimatedTotalTime = optEstimatedTotalTime.get();
		return estimatedTotalTime;
	}


	// Relationships ----------------------------------------------------------
	@NotNull
	@Valid
	@ManyToOne(optional = false)
	protected Assistant	assistant;

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	protected Course	course;

}


package acme.entities.banner;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.framework.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Banner extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------
	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotNull
	@PastOrPresent
	@Temporal(TemporalType.TIMESTAMP)
	protected Date				upgrade;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP) 			//a display period (must start at any moment after the instantiation/update 
	protected Date				startDisplay;			//moment and must last for at least one week)

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	protected Date				endDisplay;

	@NotNull
	@URL
	protected String			imgLink;

	@NotBlank
	@Length(max = 75)
	protected String			slogan;

	@NotNull
	@URL
	protected String			docLink;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------
}

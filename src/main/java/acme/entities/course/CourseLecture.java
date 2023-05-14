
package acme.entities.course;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import acme.entities.lecture.Lecture;
import acme.framework.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CourseLecture extends AbstractEntity {

	//	Serialisation identifier ----------------------------
	protected static final long	serialVersionUID	= 1L;

	//	Relationships ---------------------------------------
	@Valid
	@NotNull
	@ManyToOne(optional = false)
	protected Course			course;

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	protected Lecture			lecture;

}

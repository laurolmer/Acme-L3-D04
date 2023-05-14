
package acme.form;

import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import acme.framework.components.datatypes.Money;
import acme.framework.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExchangeMoney extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------

	@NotNull
	@Valid
	public Money				sourceMoney;

	@NotBlank
	@Pattern(regexp = "^[A-Z]{3}$")
	public String				targetCurrency;

	@Valid
	public Money				targetMoney;

	public Date					date;

}


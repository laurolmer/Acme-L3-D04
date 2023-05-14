
package acme.features.any.peep;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.peep.Peep;
import acme.framework.components.accounts.Any;
import acme.framework.components.accounts.Authenticated;
import acme.framework.components.accounts.Principal;
import acme.framework.components.accounts.UserAccount;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;

@Service
public class AnyPeepCreateService extends AbstractService<Any, Peep> {
	// Internal state ---------------------------------------------------------

	@Autowired
	protected AnyPeepRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {

		final Peep object;
		Principal principal;
		int userAccountId;
		final UserAccount userAccount;

		//Default
		String Name = "";

		object = new Peep();
		principal = super.getRequest().getPrincipal();
		if (principal.hasRole(Authenticated.class)) {
			userAccountId = principal.getAccountId();
			userAccount = this.repository.findOneUserAccountById(userAccountId);
			Name = userAccount.getIdentity().getFullName();
		}
		object.setNick(Name);
		object.setPublish(false);
		super.getBuffer().setData(object);

	}

	@Override
	public void bind(final Peep object) {
		assert object != null;
		super.bind(object, "moment", "title", "nick", "message", "link", "email", "publish");
	}

	@Override
	public void validate(final Peep object) {
		assert object != null;
		Date actualMoment;
		final Date maxValue = new Date("2100/12/31 23:59");
		final Date minValue = new Date("2000/01/01 00:00");
		if (!super.getBuffer().getErrors().hasErrors("moment")) {
			actualMoment = MomentHelper.getCurrentMoment();
			super.state(MomentHelper.isBeforeOrEqual(object.getMoment(), actualMoment), "moment", "any.peep.moment-after-actualMoment");
		}
		// EndPeriod must be before 2100/12/31 23:59
		if (!super.getBuffer().getErrors().hasErrors("moment"))
			super.state(MomentHelper.isBefore(object.getMoment(), maxValue), "moment", "any.peep.moment-reached-max-value");
		// StartPeriod must be after 2000/01/01 00:00
		if (!super.getBuffer().getErrors().hasErrors("moment"))
			super.state(MomentHelper.isAfter(object.getMoment(), minValue), "moment", "any.peep.moment-didnot-reach-min-value");
	}

	@Override
	public void perform(final Peep object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final Peep object) {
		Tuple tuple;
		tuple = super.unbind(object, "moment", "title", "nick", "message", "link", "email", "publish");
		tuple.put("publish", true);
		super.getResponse().setData(tuple);

	}

}

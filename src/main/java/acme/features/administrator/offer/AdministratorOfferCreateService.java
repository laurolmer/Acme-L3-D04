
package acme.features.administrator.offer;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.offer.Offer;
import acme.framework.components.accounts.Administrator;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;

@Service
public class AdministratorOfferCreateService extends AbstractService<Administrator, Offer> {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected AdministratorOfferRepository repository;


	// AbstractService interface ----------------------------------------------
	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		boolean status;
		status = super.getRequest().getPrincipal().hasRole(Administrator.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Offer object;
		Date instantiationMoment;
		instantiationMoment = MomentHelper.getCurrentMoment();
		object = new Offer();
		object.setInstantiationMoment(instantiationMoment);
		object.setHeading("");
		object.setSummary("");
		object.setLink("");
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Offer object) {
		assert object != null;
		super.bind(object, "heading", "summary", "price", "availabilityPeriodStart", "availabilityPeriodEnd", "link");
	}

	@Override
	public void validate(final Offer object) {
		assert object != null;
		Date minStartPeriod;
		final Date maxValue = new Date("2100/12/31 23:59");
		final Date minValue = new Date("2000/01/01 00:00");
		// StartPeriod -> At least one day after the offer is instantiated.
		if (!super.getBuffer().getErrors().hasErrors("availabilityPeriodStart")) {
			minStartPeriod = MomentHelper.deltaFromCurrentMoment(1, ChronoUnit.DAYS);
			super.state(MomentHelper.isAfter(object.getAvailabilityPeriodStart(), minStartPeriod), "availabilityPeriodStart", "administrator.offer.start-close-to-instantiation");
		}
		// EndPeriod -> Must last for at least one week (7 days)
		if (!super.getBuffer().getErrors().hasErrors("availabilityPeriodEnd"))
			super.state(MomentHelper.isLongEnough(object.getAvailabilityPeriodStart(), object.getAvailabilityPeriodEnd(), 7, ChronoUnit.DAYS), "availabilityPeriodEnd", "administrator.offer.end-duration-insufficient");
		// EndPeriod must be after StartPeriod.
		if (!super.getBuffer().getErrors().hasErrors("availabilityPeriodEnd"))
			super.state(MomentHelper.isAfter(object.getAvailabilityPeriodEnd(), object.getAvailabilityPeriodStart()), "availabilityPeriodEnd", "administrator.offer.end-after-start");
		// EndPeriod must be before 2100/12/31 23:59
		if (!super.getBuffer().getErrors().hasErrors("availabilityPeriodEnd"))
			super.state(!MomentHelper.isAfter(object.getAvailabilityPeriodEnd(), maxValue), "availabilityPeriodEnd", "administrator.offer.end-reached-max-value");
		// StartPeriod must be after 2000/01/01 00:00
		if (!super.getBuffer().getErrors().hasErrors("availabilityPeriodStart"))
			super.state(MomentHelper.isAfter(object.getAvailabilityPeriodStart(), minValue), "getAvailabilityPeriodStart", "administrator.offer.start-didnot-reach-min-value");
		// Price -> Positive, possibly nought.
		if (!super.getBuffer().getErrors().hasErrors("price"))
			super.state(object.getPrice().getAmount() > 0, "price", "administrator.offer.positive-naught-price");
		// Max price -> 1,000,000
		if (!super.getBuffer().getErrors().hasErrors("price"))
			super.state(object.getPrice().getAmount() <= 1000000, "price", "administrator.offer.price-reached-limit-value");
	}

	@Override
	public void perform(final Offer object) {
		assert object != null;
		Date instantiationMoment;
		instantiationMoment = MomentHelper.getCurrentMoment();
		object.setInstantiationMoment(instantiationMoment);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Offer object) {
		assert object != null;
		Tuple tuple;
		tuple = super.unbind(object, "instantiationMoment", "heading", "summary", "price", "availabilityPeriodStart", "availabilityPeriodEnd", "link");
		super.getResponse().setData(tuple);
	}

}


package acme.features.administrator.banner;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.banner.Banner;
import acme.framework.components.accounts.Administrator;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;

@Service
public class AdministratorBannerUpdateService extends AbstractService<Administrator, Banner> {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected AdministratorBannerRepository repository;


	// AbstractService interface ----------------------------------------------
	@Override
	public void check() {
		boolean status;
		status = super.getRequest().hasData("id", int.class);
		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		status = super.getRequest().getPrincipal().hasRole(Administrator.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Banner object;
		int id;
		id = super.getRequest().getData("id", int.class);
		object = this.repository.findBannerById(id);

		Date instantiationMoment;
		instantiationMoment = MomentHelper.getCurrentMoment();
		object.setUpgrade(instantiationMoment);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Banner object) {
		assert object != null;
		super.bind(object, "upgrade", "startDisplay", "endDisplay", "imgLink", "slogan", "docLink");
	}

	@Override
	public void validate(final Banner object) {

		Date instantiationMoment;
		instantiationMoment = MomentHelper.getCurrentMoment();
		object.setUpgrade(instantiationMoment);

		//a display period (must start at any moment after the instantiation/update 
		//moment and must last for at least one week)

		if (!super.getBuffer().getErrors().hasErrors("startDisplay") || !super.getBuffer().getErrors().hasErrors("endDisplay")) {

			Date start;
			Date startCondition;
			Date end;
			Date inAWeekFromStart;

			startCondition = object.getUpgrade();
			start = object.getStartDisplay();
			end = object.getEndDisplay();

			inAWeekFromStart = MomentHelper.deltaFromMoment(start, AdministratorBannerCreateService.ONE_WEEK, ChronoUnit.WEEKS);

			if (!super.getBuffer().getErrors().hasErrors("startDisplay")) {
				super.state(MomentHelper.isAfter(start, startCondition), "startDisplay", "admin.banner.error.start-after-now");
				if (!super.getBuffer().getErrors().hasErrors("endDisplay"))
					super.state(MomentHelper.isAfter(end, inAWeekFromStart), "endDisplay", "admin.banner.error.end-after-start");
			}
		}
	}
	@Override
	public void perform(final Banner object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final Banner object) {
		assert object != null;
		Tuple tuple;
		tuple = super.unbind(object, "upgrade", "startDisplay", "endDisplay", "imgLink", "slogan", "docLink");
		super.getResponse().setData(tuple);
	}

}

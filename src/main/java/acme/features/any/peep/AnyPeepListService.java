
package acme.features.any.peep;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.peep.Peep;
import acme.framework.components.accounts.Any;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;

@Service
public class AnyPeepListService extends AbstractService<Any, Peep> {

	@Autowired
	protected AnyPeepRepository repository;

	// AbstractService interface -----------------------------------------


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
		final Collection<Peep> objects;
		objects = this.repository.findAllPeep();
		super.getBuffer().setData(objects);
	}

	@Override
	public void bind(final Peep object) {
		assert object != null;
		super.bind(object, "moment", "title", "nick", "message", "link", "email", "draftMode");
	}

	@Override
	public void validate(final Peep object) {
		assert object != null;

	}

	@Override
	public void perform(final Peep object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final Peep object) {
		assert object != null;
		Tuple tuple;
		String payload;
		tuple = super.unbind(object, "moment", "title", "nick", "message", "link", "email");
		payload = String.format("%s; %s; %s", object.getTitle(), object.getNick(), object.getMessage());
		tuple.put("payload", payload);
		super.getResponse().setData(tuple);
	}
}

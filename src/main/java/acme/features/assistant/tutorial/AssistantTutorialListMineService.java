
package acme.features.assistant.tutorial;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.tutorial.Tutorial;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantTutorialListMineService extends AbstractService<Assistant, Tutorial> {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected AssistantTutorialRepository repository;


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
		Collection<Tutorial> objects;
		Principal principal;
		principal = super.getRequest().getPrincipal();
		objects = this.repository.findTutorialsByAssistantId(principal.getActiveRoleId());
		super.getBuffer().setData(objects);
	}

	@Override
	public void unbind(final Tutorial object) {
		assert object != null;
		String payload;
		Tuple tuple;
		tuple = super.unbind(object, "code", "title");
		payload = String.format("%s; %s; %s; %s", object.getCode(), object.getTitle(), object.getAssistant().getIdentity().getFullName(), object.getAssistant().getSupervisor());
		tuple.put("payload", payload);
		super.getResponse().setData(tuple);
	}
}

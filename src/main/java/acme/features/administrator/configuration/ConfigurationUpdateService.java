/*
 * ConfigurationUpdateService.java
 *
 * Copyright (C) 2012-2023 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.administrator.configuration;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.configuration.Configuration;
import acme.entities.course.Course;
import acme.entities.offer.Offer;
import acme.framework.components.accounts.Administrator;
import acme.framework.components.datatypes.Money;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;

@Service
public class ConfigurationUpdateService extends AbstractService<Administrator, Configuration> {

	@Autowired
	protected ConfigurationRepository repository;


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
		Configuration object;

		object = this.repository.findConfiguration();

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Configuration object) {
		assert object != null;

		super.bind(object, "defaultCurrency", "acceptedCurrencies");
	}

	@Override
	public void validate(final Configuration object) {
		assert object != null;
		final Collection<Offer> offers = this.repository.findAllOffers();
		final Collection<Course> courses = this.repository.findAllCourses();
		final Set<String> currenciesOffer = offers.stream().map(Offer::getPrice).map(Money::getCurrency).collect(Collectors.toSet());
		final Set<String> currenciesCourse = courses.stream().map(Course::getRetailPrice).map(Money::getCurrency).collect(Collectors.toSet());
		final Set<String> currencies = Stream.concat(currenciesOffer.stream(), currenciesCourse.stream()).collect(Collectors.toSet());
		final List<String> systemCurrency = Arrays.asList(object.getAcceptedCurrencies().split(","));
		final boolean currencyInList = systemCurrency.contains(object.getDefaultCurrency());
		super.state(currencyInList, "defaultCurrency", "administrator.configuration.form.error.not-found-in-list");
		final boolean currencyInSystem = systemCurrency.containsAll(currencies);
		super.state(currencyInSystem, "acceptedCurrencies", "administrator.configuration.form.error.not-found-in-system");
	}

	@Override
	public void perform(final Configuration object) {
		assert object != null;
		String currencyList = "";
		final String[] currencies = super.getRequest().getData("acceptedCurrencies", String.class).split(",");
		final Set<String> currenciesSet = new HashSet<>();
		for (final String currency : currencies)
			currenciesSet.add(currency);
		currencyList = currenciesSet.toString().replaceAll("[\\[\\]]", "");
		object.setAcceptedCurrencies(currencyList.replaceAll(", ", ","));
		this.repository.save(object);
	}

	@Override
	public void unbind(final Configuration object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "defaultCurrency", "acceptedCurrencies");

		super.getResponse().setData(tuple);
	}
}

/*
 * AuthenticatedMoneyExchangePerformService.java
 *
 * Copyright (C) 2012-2023 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.authenticated.moneyExchange;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import acme.components.ExchangeRate;
import acme.form.ExchangeMoney;
import acme.framework.components.accounts.Authenticated;
import acme.framework.components.datatypes.Money;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.StringHelper;
import acme.framework.services.AbstractService;

@Service
public class AuthenticatedMoneyExchangePerformService extends AbstractService<Authenticated, ExchangeMoney> {

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
		ExchangeMoney object;

		object = new ExchangeMoney();

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final ExchangeMoney object) {
		assert object != null;

		super.bind(object, "sourceMoney", "targetCurrency", "date", "targetMoney");
	}

	@Override
	public void validate(final ExchangeMoney object) {
		assert object != null;
		Money sourceMoney;
		String targetCurrency;
		ExchangeMoney exchangeMoney;
		if (!super.getBuffer().getErrors().hasErrors("sourceMoney") && !super.getBuffer().getErrors().hasErrors("targetCurrency")) {
			sourceMoney = super.getRequest().getData("sourceMoney", Money.class);
			targetCurrency = super.getRequest().getData("targetCurrency", String.class);
			exchangeMoney = this.computeExchangeMoney(sourceMoney, targetCurrency);
			if (exchangeMoney == null)
				super.state(false, "*", "authenticated.exchange-money.form.label.error");
		}
	}

	@Override
	public void perform(final ExchangeMoney object) {
		assert object != null;
		Money sourceMoney;
		String targetCurrency;
		ExchangeMoney exchangeMoney;

		sourceMoney = super.getRequest().getData("sourceMoney", Money.class);
		targetCurrency = super.getRequest().getData("targetCurrency", String.class);
		exchangeMoney = this.computeExchangeMoney(sourceMoney, targetCurrency);
		if (exchangeMoney != null) {
			object.setTargetMoney(exchangeMoney.getTargetMoney());
			object.setDate(exchangeMoney.getDate());
		} else {
			object.setTargetMoney(null);
			object.setDate(null);
		}
	}

	@Override
	public void unbind(final ExchangeMoney object) {
		assert object != null;
		Tuple tuple;
		tuple = super.unbind(object, "sourceMoney", "targetCurrency", "date", "targetMoney");
		super.getResponse().setData(tuple);
	}

	// Ancillary method ------------------------------------------------------

	public ExchangeMoney computeExchangeMoney(final Money sourceMoney, final String targetCurrency) {
		assert sourceMoney != null;
		assert !StringHelper.isBlank(targetCurrency);

		ExchangeMoney result;
		RestTemplate api;
		ExchangeRate record;
		String sourceCurrency;
		Double sourceAmount, targetAmount, rates;
		Money target;
		final String url;
		try {
			api = new RestTemplate();
			sourceCurrency = sourceMoney.getCurrency();
			sourceAmount = sourceMoney.getAmount();
			url = "https://api.exchangerate.host/latest?base=" + sourceCurrency + "&symbols=" + targetCurrency;
			record = api.getForObject(url, ExchangeRate.class);

			assert record != null;
			rates = record.getRates().get(targetCurrency);
			targetAmount = rates * sourceAmount;
			target = new Money();
			target.setAmount(targetAmount);
			target.setCurrency(targetCurrency);

			result = new ExchangeMoney();
			result.setSourceMoney(sourceMoney);
			result.setTargetCurrency(targetCurrency);
			result.setDate(record.getDate());
			result.setTargetMoney(target);
		} catch (final Throwable oops) {
			result = null;
		}
		return result;
	}
}

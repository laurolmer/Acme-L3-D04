
package acme.features.any.currency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import acme.components.ExchangeRate;
import acme.framework.components.datatypes.Money;

@Service
public class CurrencyService {

	// Internal state ---------------------------------------------------------
	@Autowired
	private CurrencyRepository repository;


	// Methods ----------------------------------------------------------------
	public Money changeIntoSystemCurrency(final Money source) {
		assert source != null;

		String targetCurrency;
		RestTemplate api;
		ExchangeRate record;
		Double rate;
		double targetAmount;
		Money target;

		targetCurrency = this.repository.findCurrentCurrency();
		api = new RestTemplate();
		record = api.getForObject("https://api.exchangerate.host/latest?base={0}&symbols={1}", ExchangeRate.class, source.getCurrency(), targetCurrency);

		assert record != null;

		rate = record.getRates().get(targetCurrency);
		targetAmount = rate * source.getAmount();
		target = new Money();
		target.setAmount(targetAmount);
		target.setCurrency(targetCurrency);
		return target;
	}
}

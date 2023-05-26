
package acme.features.administrator.administratorDashboard;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.form.AdministratorDashboard;
import acme.form.Statistic;
import acme.framework.components.accounts.Administrator;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;

@Service
public class AdministratorDashboardShowService extends AbstractService<Administrator, AdministratorDashboard> {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected AdministratorDashboardRepository repository;


	// AbstractService interface ----------------------------------------------
	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		boolean status;
		Principal principal;

		principal = super.getRequest().getPrincipal();
		status = principal.hasRole(Administrator.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		final AdministratorDashboard dashboard = new AdministratorDashboard();

		Map<String, Integer> principalsByRole;
		Double ratioOfCriticalBulletins;
		double ratioOfNonCriticalBulletins;
		Map<String, Statistic> currentOfferStatistic;
		double ratioOfPeeps;
		Map<Integer, Integer> notesPerWeek;

		principalsByRole = this.getStatisticsPrincipalsByRole();
		ratioOfCriticalBulletins = this.getRatioOfBulletinsByCriticality();
		ratioOfNonCriticalBulletins = 1 - ratioOfCriticalBulletins;
		currentOfferStatistic = this.getCurrencyStatisticsForOffer();
		ratioOfPeeps = this.getRatioOfPeeps();
		notesPerWeek = this.getNotesInLastTenWeeks();

		dashboard.setPrincipalsByRole(principalsByRole);
		dashboard.setRatioOfCriticalBulletins(ratioOfCriticalBulletins);
		dashboard.setRatioOfNonCriticalBulletins(ratioOfNonCriticalBulletins);
		dashboard.setCurrentsOffersStats(currentOfferStatistic);
		dashboard.setLinkAndEmailPeepsRatio(ratioOfPeeps);
		dashboard.setNotesInLast10WeeksStats(notesPerWeek);

		super.getBuffer().setData(dashboard);
	}

	@Override
	public void unbind(final AdministratorDashboard object) {
		Tuple tuple;

		tuple = super.unbind(object, "principalsByRole", "ratioOfCriticalBulletins", "ratioOfNonCriticalBulletins", "currentsOffersStats", "notesInLast10WeeksStats");

		super.getResponse().setData(tuple);
	}

	// Ancillary methods ------------------------------------------------------
	private Map<Integer, Integer> getNotesInLastTenWeeks() {
		final Date tenWeeks = MomentHelper.deltaFromCurrentMoment(-10, ChronoUnit.WEEKS);
		final Map<Integer, Integer> notesPerWeek = new HashMap<>();
		int i = 1;
		for (Date week = tenWeeks; MomentHelper.isPast(week); week = MomentHelper.deltaFromMoment(week, 1, ChronoUnit.WEEKS)) {
			final Date nextWeek = MomentHelper.deltaFromMoment(week, 1, ChronoUnit.WEEKS);
			final Integer weekNumber = this.repository.findNotesBetweenDates(week, nextWeek);
			notesPerWeek.put(i, weekNumber);
			i++;
		}
		return notesPerWeek;
	}

	private double getRatioOfPeeps() {
		return this.repository.countAllPeepsWithBoth() / this.repository.countAllPeeps();
	}

	private Map<String, Integer> getStatisticsPrincipalsByRole() {
		final Map<String, Integer> res = new HashMap<>();
		final Integer lecturerCount = this.repository.countPrincipalByLecturer();
		final Integer assistantCount = this.repository.countPrincipalByAssistant();
		final Integer companyCount = this.repository.countPrincipalByCompany();
		final Integer studentCount = this.repository.countPrincipalByStudent();
		final Integer auditorCount = this.repository.countPrincipalByAuditor();
		final Integer administratorCount = this.repository.countPrincipalByAdministrator();

		res.put("Lecturer", lecturerCount != null ? lecturerCount : 0);
		res.put("Assistant", assistantCount != null ? assistantCount : 0);
		res.put("Company", companyCount != null ? companyCount : 0);
		res.put("Student", studentCount != null ? studentCount : 0);
		res.put("Auditor", auditorCount != null ? auditorCount : 0);
		res.put("Administrator", administratorCount != null ? administratorCount : 0);
		return res;
	}

	private Double getRatioOfBulletinsByCriticality() {
		final double countAllBulletin = this.repository.countAllBulletin();
		return this.repository.countAllCriticalBulletin() * 1.0 / countAllBulletin;
	}

	private Map<String, Statistic> getCurrencyStatisticsForOffer() {
		final Map<String, Statistic> res = new HashMap<>();
		for (String currency : this.repository.findAcceptedCurrencies().split("-")) {
			currency = currency.trim();
			final Statistic stats = new Statistic();
			final Integer count = this.repository.countPriceOfferByCurrency(currency);
			final Double max = this.repository.maxPriceOfferByCurrency(currency);
			final Double min = this.repository.minPriceOfferByCurrency(currency);
			final Double avg = this.repository.avgPriceOfferByCurrency(currency);
			final Double stddev = this.repository.stddevPriceOfferByCurrency(currency);
			stats.setCount(count != null ? count : 0);
			stats.setMaximum(max != null ? max : 0.0);
			stats.setMinimum(min != null ? min : 0.0);
			stats.setAverage(avg != null ? avg : 0.0);
			stats.setDeviation(stddev != null ? stddev : 0.0);
			res.put(currency, stats);
		}
		return res;
	}
}

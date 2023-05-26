
package acme.features.administrator.administratorDashboard;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;

import acme.entities.note.Note;
import acme.framework.repositories.AbstractRepository;

public interface AdministratorDashboardRepository extends AbstractRepository {

	// PRINCIPALS -------------------------------------------------------------------

	@Query("select count(u) from Lecturer l inner join UserAccount u on l member of u.userRoles")
	Integer countPrincipalByLecturer();

	@Query("select count(u) from Assistant a inner join UserAccount u on a member of u.userRoles")
	Integer countPrincipalByAssistant();

	@Query("select count(u) from Auditor a inner join UserAccount u on a member of u.userRoles")
	Integer countPrincipalByAuditor();

	@Query("select count(u) from Company c inner join UserAccount u on c member of u.userRoles")
	Integer countPrincipalByCompany();

	@Query("select count(u) from Student s inner join UserAccount u on s member of u.userRoles")
	Integer countPrincipalByStudent();

	@Query("select count(u) from Administrator a inner join UserAccount u on a member of u.userRoles")
	Integer countPrincipalByAdministrator();

	@Query("select count(p) from Peep p")
	Double countAllPeeps();

	@Query("select count(p) from Peep p where p.email is not null and p.link is not null")
	Double countAllPeepsWithBoth();

	@Query("select count(b) from Bulletin b")
	Double countAllBulletin();

	@Query("select count(b) from Bulletin b where b.critical = true")
	Double countAllCriticalBulletin();

	@Query("select count(o.price.amount) from Offer o where o.price.currency = ?1")
	Integer countPriceOfferByCurrency(String currency);

	@Query("select max(o.price.amount) from Offer o where o.price.currency = ?1")
	Double maxPriceOfferByCurrency(String currency);

	@Query("select min(o.price.amount) from Offer o where o.price.currency = ?1")
	Double minPriceOfferByCurrency(String currency);

	@Query("select avg(o.price.amount) from Offer o where o.price.currency = ?1")
	Double avgPriceOfferByCurrency(String currency);

	@Query("select stddev(o.price.amount) from Offer o where o.price.currency = ?1")
	Double stddevPriceOfferByCurrency(String currency);

	@Query("select n from Note n where n.instantiationMoment >= ?1")
	Collection<Note> findNotesInLast10Weeks(Date date);

	@Query("select count(n) from Note n where n.instantiationMoment >= ?1 and n.instantiationMoment <= ?2")
	Integer findNotesBetweenDates(Date date1, Date date2);

	@Query("select c.acceptedCurrencies from Configuration c")
	String findAcceptedCurrencies();
}

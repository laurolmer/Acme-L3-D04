
package acme.features.administrator.offer;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.offer.Offer;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AdministratorOfferRepository extends AbstractRepository {

	@Query("select o from Offer o where o.id = :id")
	Offer findAnOfferById(int id);

	@Query("select o from Offer o")
	Collection<Offer> findAllOffers();

	@Query("select o from Offer o where o.availabilityPeriodEnd > :currentMoment")
	Collection<Offer> findAllNotFinishedOffers(Date currentMoment);
}

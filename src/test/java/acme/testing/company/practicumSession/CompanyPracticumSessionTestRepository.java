/*
 * EmployerDutyTestRepository.java
 *
 * Copyright (C) 2012-2023 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.testing.company.practicumSession;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.entities.practicum.Practicum;
import acme.entities.practicumSession.PracticumSession;
import acme.framework.repositories.AbstractRepository;

public interface CompanyPracticumSessionTestRepository extends AbstractRepository {

	@Query("select t from Practicum t where t.company.userAccount.username = :username")
	Collection<Practicum> findpracticumsBycompanyUsername(String username);

	@Query("select ts from PracticumSession ts where ts.practicum.company.userAccount.username = :username")
	Collection<PracticumSession> findpracticumSessionsBycompanyUsername(String username);

}

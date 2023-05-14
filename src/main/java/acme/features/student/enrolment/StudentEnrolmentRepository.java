/*
 * StudentEnrolmentRepository.java
 *
 * Copyright (C) 2012-2023 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.student.enrolment;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.activity.Activity;
import acme.entities.course.Course;
import acme.entities.enrolment.Enrolment;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Student;

@Repository
public interface StudentEnrolmentRepository extends AbstractRepository {

	@Query("select e from Enrolment e where e.id = :enrolmentId")
	Enrolment findEnrolmentById(int enrolmentId);

	@Query("select c from Course c left join Enrolment e where e.id = :enrolmentId")
	Course findCourseByEnrolmentId(int enrolmentId);

	@Query("select e from Enrolment e where e.student.id = :id")
	Collection<Enrolment> findEnrolmentsByStudentId(int id);

	@Query("select a from Activity a where a.enrolment.id = :id")
	Collection<Activity> findManyActivitiesByEnrolmentId(int id);

	@Query("select e from Enrolment e")
	Collection<Enrolment> findAllEnrolments();

	@Query("select c from Course c")
	Collection<Course> findAllCourses();

	@Query("select c from Course c where c.draftMode = false")
	Collection<Course> findNotInDraftCourses();

	@Query("select s from Student s where s.id = :id")
	Student findStudentById(int id);

	@Query("select c from Course c where c.id = :id")
	Course findCourseById(int id);

	@Query("select e.code from Enrolment e")
	Collection<String> findAllCodesFromEnrolments();

	@Query("select e from Enrolment e where e.code = :code")
	Enrolment findAEnrolmentByCode(String code);

}

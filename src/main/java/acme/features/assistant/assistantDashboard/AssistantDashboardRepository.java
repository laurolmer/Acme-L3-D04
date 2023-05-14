
package acme.features.assistant.assistantDashboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.course.Course;
import acme.entities.course.CourseType;
import acme.entities.lecture.Lecture;
import acme.entities.tutorial.Tutorial;
import acme.entities.tutorialSession.TutorialSession;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Assistant;

@Repository
public interface AssistantDashboardRepository extends AbstractRepository {

	// SESSION TIME
	@Query("select a from Assistant a where a.userAccount.id = :userAccountId")
	Assistant findAssistantByUserAccountId(int userAccountId);

	@Query("select avg(time_to_sec(timediff(ts.finishPeriod,ts.startPeriod)))/3600.0 from TutorialSession ts where ts.tutorial.assistant.id = :assistantId")
	Double findAverageTutorialSessionLength(int assistantId);

	@Query("select stddev(time_to_sec(timediff(ts.finishPeriod,ts.startPeriod)))/3600.0 from TutorialSession ts where ts.tutorial.assistant.id = :assistantId")
	Double findDeviationTutorialSessionLength(int assistantId);

	@Query("select min(time_to_sec(timediff(ts.finishPeriod,ts.startPeriod)))/3600.0 from TutorialSession ts where ts.tutorial.assistant.id = :assistantId")
	Double findMinimumTutorialSessionLength(int assistantId);

	@Query("select max(time_to_sec(timediff(ts.finishPeriod,ts.startPeriod)))/3600.0 from TutorialSession ts where ts.tutorial.assistant.id = :assistantId")
	Double findMaximumTutorialSessionLength(int assistantId);

	@Query("select count(ts) from TutorialSession ts where ts.tutorial.assistant.id = :assistantId")
	int findCountTutorialSession(int assistantId);

	// TUTORIAL TIME
	@Query("select avg(time_to_sec(timediff(ts.finishPeriod,ts.startPeriod)))/3600.0 from TutorialSession ts where ts.tutorial.id in (select t.id from Tutorial t where t.assistant.id = :assistantId)")
	Double findAvgTutorialLength(int assistantId);

	@Query("select stddev(time_to_sec(timediff(ts.finishPeriod,ts.startPeriod)))/3600.0 from TutorialSession ts where ts.tutorial.id in (select t.id from Tutorial t where t.assistant.id = :assistantId)")
	Double findDevTutorialLength(int assistantId);

	@Query("select min(time_to_sec(timediff(ts.finishPeriod,ts.startPeriod)))/3600.0 from TutorialSession ts where ts.tutorial.id in (select t.id from Tutorial t where t.assistant.id = :assistantId)")
	Double findMinTutorialLength(int assistantId);

	@Query("select max(time_to_sec(timediff(ts.finishPeriod,ts.startPeriod)))/3600.0 from TutorialSession ts where ts.tutorial.id in (select t.id from Tutorial t where t.assistant.id = :assistantId)")
	Double findMaxTutorialLength(int assistantId);

	//AUXILIARY QUERIES
	@Query("select count(t) from Tutorial t where t.assistant.id = :assistantId")
	int findCountTutorial(int assistantId);

	@Query("select count(t) from Tutorial t where t.assistant.id = :assistantId")
	Long findTotalNumberOfTutorial(int assistantId);

	@Query("select ts from TutorialSession ts where ts.tutorial.id = :id")
	Collection<TutorialSession> findSessionsByTutorialId(int id);

	@Query("select t.course from Tutorial t")
	Collection<Course> findAllCourses();

	@Query("select l from Lecture l inner join CourseLecture cl on l = cl.lecture inner join Course c on cl.course = c where c.id = :id")
	Collection<Lecture> findLecturesByCourseId(int id);

	@Query("select t from Tutorial t where t.course.id = :id")
	Collection<Tutorial> findTutorialsByCourse(int id);

	@Query("select t from Tutorial t where t.assistant.id = :id")
	Collection<Tutorial> findTutorialsByAssistantId(int id);

	// AUXILIARY METHODS
	default Map<CourseType, Collection<Course>> coursesRegardingCourseType() {
		final Map<CourseType, Collection<Course>> coursesByType = new HashMap<>();
		final Collection<Course> allCourses = this.findAllCourses();
		for (final Course c : allCourses) {
			final CourseType ct = c.computeCourseType(this.findLecturesByCourseId(c.getId()));
			Collection<Course> coursesListByType = new ArrayList<>();
			if (coursesByType.containsKey(ct)) {
				coursesListByType = coursesByType.get(ct);
				coursesListByType.add(c);
			} else
				coursesListByType.add(c);
			coursesByType.put(ct, coursesListByType);
		}
		return coursesByType;
	}

	default Integer findCountTutorialRegardingCourse(final Collection<Course> courses) {
		int totalNumberTutorialsByCoursesCollection = 0;
		for (final Course c : courses) {
			final Collection<Tutorial> tutorialsByCourse = this.findTutorialsByCourse(c.getId());
			totalNumberTutorialsByCoursesCollection += tutorialsByCourse.size();
		}
		return totalNumberTutorialsByCoursesCollection;
	}

	@Query("select a from Assistant a where a.userAccount.id = :accountId")
	Assistant findAssistantByAccountId(int accountId);

	@Query("select count(t) from Tutorial t where t.assistant.id = :id")
	Double totalNumberOfTutorials(int id);

	@Query("select au from TutorialSession au where au.tutorial.assistant.id = :id")
	List<TutorialSession> findAllTutorialSessionsByAssistantId(int id);

	@Query("select t from Tutorial t where t.assistant.id = :id")
	List<Tutorial> findAllTutorialsByAssistantId(int id);

}

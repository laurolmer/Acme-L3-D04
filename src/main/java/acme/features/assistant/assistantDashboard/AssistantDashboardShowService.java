
package acme.features.assistant.assistantDashboard;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.course.Course;
import acme.entities.course.CourseType;
import acme.form.AssistantDashboard;
import acme.form.Statistic;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantDashboardShowService extends AbstractService<Assistant, AssistantDashboard> {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected AssistantDashboardRepository repository;


	// AbstractService Interface ----------------------------------------------
	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		boolean status;
		final Assistant assistant;
		Principal principal;
		int userAccountId;
		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		assistant = this.repository.findAssistantByUserAccountId(userAccountId);
		status = assistant != null && principal.hasRole(Assistant.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		final int assistantId;
		final AssistantDashboard assistantDashboard;
		final Principal principal;
		int userAccountId;
		final Assistant assistant;

		final Statistic sessionLength;
		final double averageSessionLength;
		final double deviationSessionLength;
		final double minimumSessionLength;
		final double maximumSessionLength;
		int countSession;

		final Statistic tutorialLength;
		final double averageTutorialLength;
		final double deviationTutorialLength;
		final double minimumTutorialLength;
		final double maximumTutorialLength;
		final int countTutorial;

		final Integer totalNumberOfTheoryTutorial;
		final Integer totalNumOfHandsOnTutorials;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		assistant = this.repository.findAssistantByUserAccountId(userAccountId);
		assistantId = assistant.getId();

		averageSessionLength = this.repository.findAverageTutorialSessionLength(assistantId);
		deviationSessionLength = this.repository.findDeviationTutorialSessionLength(assistantId);
		minimumSessionLength = this.repository.findMinimumTutorialSessionLength(assistantId);
		maximumSessionLength = this.repository.findMaximumTutorialSessionLength(assistantId);
		countSession = this.repository.findCountTutorialSession(assistantId);
		sessionLength = new Statistic(countSession, averageSessionLength, maximumSessionLength, minimumSessionLength, deviationSessionLength);

		averageTutorialLength = this.repository.findAvgTutorialLength(assistantId);
		deviationTutorialLength = this.repository.findDevTutorialLength(assistantId);
		minimumTutorialLength = this.repository.findMinTutorialLength(assistantId);
		maximumTutorialLength = this.repository.findMaxTutorialLength(assistantId);
		countTutorial = this.repository.findCountTutorial(assistantId);
		tutorialLength = new Statistic(countTutorial, averageTutorialLength, maximumTutorialLength, minimumTutorialLength, deviationTutorialLength);

		final Map<CourseType, Collection<Course>> courseType = this.repository.coursesRegardingCourseType();
		totalNumberOfTheoryTutorial = this.repository.findCountTutorialRegardingCourse(courseType.get(CourseType.THEORY_COURSE));
		totalNumOfHandsOnTutorials = this.repository.findCountTutorialRegardingCourse(courseType.get(CourseType.HANDS_ON));

		assistantDashboard = new AssistantDashboard();
		assistantDashboard.setTotalNumTheoryTutorials(totalNumberOfTheoryTutorial);
		assistantDashboard.setTotalNumHandsOnTutorials(totalNumOfHandsOnTutorials);
		assistantDashboard.setSessionTime(sessionLength);
		assistantDashboard.setTutorialTime(tutorialLength);
		super.getBuffer().setData(assistantDashboard);
	}

	@Override
	public void unbind(final AssistantDashboard assistantDashboard) {
		Tuple tuple;
		tuple = super.unbind(assistantDashboard, "totalNumTheoryTutorials", "totalNumHandsOnTutorials", "sessionTime", "tutorialTime");
		super.getResponse().setData(tuple);
	}
}

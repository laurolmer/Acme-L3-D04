
package acme.features.assistant.tutorialSession;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.tutorial.Tutorial;
import acme.entities.tutorialSession.SessionType;
import acme.entities.tutorialSession.TutorialSession;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Assistant;

@Repository
public interface AssistantTutorialSessionRepository extends AbstractRepository {

	@Query("select t from Tutorial t where t.assistant.id = :id")
	Collection<Tutorial> findTutorialsByAssistantId(int id);

	@Query("select t from Tutorial t where t.id = :id")
	Tutorial findTutorialById(int id);

	@Query("select t from Tutorial t where t.draftMode = false")
	Collection<Tutorial> findNotInDraftTutorials();

	@Query("select a from Assistant a where a.id = :id")
	Assistant findAssistantById(int id);

	@Query("select t from Tutorial t")
	Collection<Tutorial> findAllTutorials();

	@Query("select ts from TutorialSession ts where ts.tutorial.id = :masterId")
	Collection<TutorialSession> findAllSessionsByTutorialId(int masterId);

	@Query("select ts from TutorialSession ts where ts.id = :id")
	TutorialSession findTutorialSessionById(int id);

	@Query("select s.tutorial from TutorialSession s where s.id = :sessionId")
	Tutorial findTutorialByTutorialSessionId(int sessionId);

	@Query("select s.sessionType from TutorialSession s")
	Collection<SessionType> findSessionTypes();

}

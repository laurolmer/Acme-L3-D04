
package acme.features.auditor.audit;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.audit.Audit;
import acme.entities.auditRecord.AuditRecord;
import acme.entities.auditRecord.MarkType;
import acme.entities.course.Course;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Auditor;

@Repository
public interface AuditorAuditRepository extends AbstractRepository {

	@Query("select A from Audit A where A.auditor.id = :aid")
	List<Audit> findAllAuditsByAuditorId(int aid);

	@Query("select A from Audit A where A.id = :aid")
	Audit findAuditById(int aid);

	@Query("select A from Auditor A where A.userAccount.id = :aId")
	Auditor findAuditorByAccountId(int aId);

	@Query("select C from Course C where C.id = :cid")
	Course findCourseById(int cid);

	@Query("select count(A)>0 from Audit A where A.code = :code and A.id != :aid")
	boolean existsAuditWithCodeAndSame(String code, int aid);

	@Query("select count(A)>0 from Audit A where A.code = :code")
	boolean existsAuditWithCode(String code);

	@Query("select count(C)>0 from Course C where C.code = :code")
	boolean existsCourseWithCode(String code);

	@Query("select C from Course C where C.code = :code")
	Course findCourseByCode(String code);

	@Query("select C from Course C where C.draftMode = false")
	List<Course> findAllCoursesPublished();

	@Query("select C from Course C")
	List<Course> findAllCourses();

	@Query("select AR from AuditRecord AR where AR.audit.id = :aid")
	List<AuditRecord> findAllAuditRecordsByAId(int aid);

	@Query("select AR.mark from AuditRecord AR where AR.audit.id = :aid")
	List<MarkType> findMarksByAuditId(int aid);
}

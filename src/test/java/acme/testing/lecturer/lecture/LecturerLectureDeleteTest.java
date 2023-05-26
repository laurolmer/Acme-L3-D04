
package acme.testing.lecturer.lecture;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.testing.TestHarness;
import acme.testing.lecturer.course.LecturerCourseRepositoryTest;

public class LecturerLectureDeleteTest extends TestHarness {

	@Autowired
	protected LecturerCourseRepositoryTest repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lecture/delete-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String title) {
		// HINT: this test authenticates as an lecturer, lists his or her lectures,
		// then selects one of them, and deletes it.
		super.signIn("lecturer2", "lecturer2");

		super.clickOnMenu("Lecturer", "List my lectures");
		super.checkListingExists();
		super.sortListing(0, "desc");

		super.clickOnListingRecord(recordIndex);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("published", "false");
		super.checkSubmitExists("Delete");
		super.clickOnSubmit("Delete");
		super.checkNotErrorsExist();

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lecture/delete-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int recordIndex, final String title) {
		// HINT: this test authenticates as an lecturer, lists his or her lectures,
		// then selects one of them, and tries to delete it.
		super.signIn("lecturer2", "lecturer2");

		super.clickOnMenu("Lecturer", "List my lectures");
		super.checkListingExists();
		super.sortListing(0, "asc");

		super.clickOnListingRecord(recordIndex);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("published", "false");
		super.clickOnSubmit("Delete");
		super.checkErrorsExist();

		super.signOut();
	}

	//	@Test
	//	public void test300Hacking() {
	//
	//		// HINT: this test tries to delete a course with a role other than "Lecturer".
	//
	//		Collection<Lecture> lectures;
	//		String param;
	//
	//		lectures = this.repository.findLecturesByLecturerUsername("lecturer2");
	//		for (final Lecture lecture : lectures)
	//			if (lecture.isDraftMode()) {
	//				param = String.format("id=%d", lecture.getId());
	//
	//				super.checkLinkExists("Sign in");
	//				super.request("/lecturer/lecture/delete", param);
	//				super.checkPanicExists();
	//
	//				super.signIn("administrator", "administrator");
	//				super.request("/lecturer/lecture/delete", param);
	//				super.checkPanicExists();
	//				super.signOut();
	//
	//				super.signIn("auditor1", "auditor1");
	//				super.request("/lecturer/lecture/delete", param);
	//				super.checkPanicExists();
	//				super.signOut();
	//
	//				super.signIn("company1", "company1");
	//				super.request("/lecturer/lecture/delete", param);
	//				super.checkPanicExists();
	//				super.signOut();
	//
	//				super.signIn("student1", "student1");
	//				super.request("/lecturer/lecture/delete", param);
	//				super.checkPanicExists();
	//				super.signOut();
	//
	//				super.signIn("assistant1", "assistant1");
	//				super.request("/lecturer/lecture/delete", param);
	//				super.checkPanicExists();
	//				super.signOut();
	//
	//			}
	//	}

	//	@Test
	//	public void test301Hacking() {
	//		// HINT: this test tries to delete a course published
	//		// being the owner.
	//
	//		Collection<Lecture> lectures;
	//		String param;
	//
	//		super.signIn("lecturer1", "lecturer1");
	//		lectures = this.repository.findLecturesByLecturerUsername("lecturer1");
	//		for (final Lecture lecture : lectures)
	//			if (!lecture.isDraftMode()) {
	//				param = String.format("id=%d", lecture.getId());
	//				super.request("/lecturer/lecture/delete", param);
	//				super.checkPanicExists();
	//			}
	//		super.signOut();
	//	}
	//
	//	@Test
	//	public void test302Hacking() {
	//		// HINT: this test tries to delete a course published or not
	//		// without being the owner.
	//
	//		Collection<Lecture> lectures;
	//		String param;
	//
	//		super.signIn("lecturer2", "lecturer2");
	//		lectures = this.repository.findLecturesByLecturerUsername("lecturer1");
	//		for (final Lecture lecture : lectures) {
	//			param = String.format("id=%d", lecture.getId());
	//			super.request("/lecturer/lecture/delete", param);
	//			super.checkPanicExists();
	//		}
	//		super.signOut();
	//	}
}

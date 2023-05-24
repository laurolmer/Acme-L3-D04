
package acme.testing.lecturer.lecture;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.lecture.Lecture;
import acme.testing.TestHarness;
import acme.testing.lecturer.course.LecturerCourseRepositoryTest;

public class LecturerLecturePublishTest extends TestHarness {

	@Autowired
	protected LecturerCourseRepositoryTest repository;

	//	@ParameterizedTest
	//	@CsvFileSource(resources = "/lecturer/lecture/publish-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	//	public void test100Positive(final int recordIndex, final String title) {
	//		// HINT: this test authenticates as an lecturer, lists his or her lectures,
	//		// then selects one of them, and publishes it.
	//		super.signIn("lecturer2", "lecturer2");
	//
	//		super.clickOnMenu("Lecturer", "List my lectures");
	//		super.checkListingExists();
	//		super.sortListing(0, "asc");
	//
	//		super.clickOnListingRecord(recordIndex);
	//		super.checkInputBoxHasValue("published", "false");
	//		super.checkInputBoxHasValue("title", title);
	//		super.clickOnSubmit("Publish");
	//		super.checkNotErrorsExist();
	//
	//		super.checkListingExists();
	//		super.sortListing(0, "asc");
	//		super.clickOnListingRecord(recordIndex);
	//		super.checkInputBoxHasValue("published", "true");
	//		super.checkInputBoxHasValue("title", title);
	//
	//		super.signOut();
	//	}
	//
	//	@ParameterizedTest
	//	@CsvFileSource(resources = "/lecturer/lecture/publish-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	//	public void test200Negative(final int recordIndex, final String title) {
	//		// HINT: this test authenticates as an lecturer, lists his or her lectures,
	//		// then selects one of them, and tries to publish it.
	//		super.signIn("lecturer2", "lecturer2");
	//
	//		super.clickOnMenu("Lecturer", "List my lectures");
	//		super.checkListingExists();
	//		super.sortListing(0, "asc");
	//
	//		super.clickOnListingRecord(recordIndex);
	//		super.checkInputBoxHasValue("published", "false");
	//		super.fillInputBoxIn("title", title);
	//		super.clickOnSubmit("Publish");
	//		super.checkAlertExists(false);
	//
	//		super.signOut();
	//	}
	//
	//	@Test
	//	public void test300Hacking() {
	//
	//		// HINT: this test tries to update a lecture with a role other than "Lecturer".
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
	//				super.request("/lecturer/lecture/publish", param);
	//				super.checkPanicExists();
	//
	//				super.signIn("administrator", "administrator");
	//				super.request("/lecturer/lecture/publish", param);
	//				super.checkPanicExists();
	//				super.signOut();
	//
	//				super.signIn("auditor1", "auditor1");
	//				super.request("/lecturer/lecture/publish", param);
	//				super.checkPanicExists();
	//				super.signOut();
	//
	//				super.signIn("company1", "company1");
	//				super.request("/lecturer/lecture/publish", param);
	//				super.checkPanicExists();
	//				super.signOut();
	//
	//				super.signIn("student1", "student1");
	//				super.request("/lecturer/lecture/publish", param);
	//				super.checkPanicExists();
	//				super.signOut();
	//
	//				super.signIn("assistant1", "assistant1");
	//				super.request("/lecturer/lecture/publish", param);
	//				super.checkPanicExists();
	//				super.signOut();
	//
	//			}
	//	}


	@Test
	public void test301Hacking() {
		// HINT: this test tries to publish a course published
		// being the owner.

		Collection<Lecture> lectures;
		String param;

		super.signIn("lecturer1", "lecturer1");
		lectures = this.repository.findLecturesByLecturerUsername("lecturer1");
		for (final Lecture lecture : lectures)
			if (!lecture.isDraftMode()) {
				param = String.format("id=%d", lecture.getId());
				super.request("/lecturer/lecture/publish", param);
				super.checkPanicExists();
			}
		super.signOut();
	}

	@Test
	public void test302Hacking() {
		// HINT: this test tries to update a course published or not
		// without being the owner.

		Collection<Lecture> lectures;
		String param;

		super.signIn("lecturer2", "lecturer2");
		lectures = this.repository.findLecturesByLecturerUsername("lecturer1");
		for (final Lecture lecture : lectures) {
			param = String.format("id=%d", lecture.getId());
			super.request("/lecturer/lecture/publish", param);
			super.checkPanicExists();
		}
		super.signOut();
	}
}

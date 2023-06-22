
package acme.testing.lecturer.lecture;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.lecture.Lecture;
import acme.testing.TestHarness;
import acme.testing.lecturer.course.LecturerCourseRepositoryTest;

public class LecturerLectureUpdateTest extends TestHarness {

	@Autowired
	protected LecturerCourseRepositoryTest repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lecture/update-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String title, final String lectureAbstract, final String lectureLearningTime, final String details, final String type, final String published) {
		// HINT: this test logs in as an lecturer, lists his or her tutorials,
		// selects one of them, updates it, and then checks that the update
		// has actually been performed.

		super.signIn("lecturer2", "lecturer2");

		super.clickOnMenu("Lecturer", "List my lectures");
		super.checkListingExists();
		super.sortListing(0, "desc");

		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.checkInputBoxHasValue("published", published);

		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("lectureAbstract", lectureAbstract);
		super.fillInputBoxIn("endPeriod", lectureLearningTime);
		super.fillInputBoxIn("body", details);
		super.fillInputBoxIn("lectureType", type);
		super.clickOnSubmit("Update");

		super.checkListingExists();
		super.sortListing(0, "desc");
		super.checkColumnHasValue(recordIndex, 0, title);
		super.checkColumnHasValue(recordIndex, 1, lectureAbstract);
		super.checkColumnHasValue(recordIndex, 2, lectureLearningTime);

		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.checkInputBoxHasValue("published", published);

		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("lectureAbstract", lectureAbstract);
		super.checkInputBoxHasValue("endPeriod", lectureLearningTime);
		super.checkInputBoxHasValue("body", details);
		super.checkInputBoxHasValue("lectureType", type);

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lecture/update-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int recordIndex, final String title, final String lectureAbstract, final String lectureLearningTime, final String details, final String type, final String published) {
		super.signIn("lecturer2", "lecturer2");

		super.clickOnMenu("Lecturer", "List my lectures");
		super.checkListingExists();
		super.sortListing(0, "desc");
		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();

		super.checkInputBoxHasValue("published", published);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("lectureAbstract", lectureAbstract);
		super.fillInputBoxIn("endPeriod", lectureLearningTime);
		super.fillInputBoxIn("body", details);
		super.fillInputBoxIn("lectureType", type);
		super.clickOnSubmit("Update");

		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {
		// HINT: this test tries to update a lecture with a role other than "Lecturer".

		final Collection<Lecture> lectures;
		String param;

		lectures = this.repository.findLecturesByLecturerUsername("lecturer2");
		for (final Lecture lecture : lectures)
			if (lecture.isDraftMode()) {
				param = String.format("id=%d", lecture.getId());

				super.checkLinkExists("Sign in");
				super.request("/lecturer/lecture/update", param);
				super.checkPanicExists();

				super.signIn("administrator", "administrator");
				super.request("/lecturer/lecture/update", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("auditor1", "auditor1");
				super.request("/lecturer/lecture/update", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("company1", "company1");
				super.request("/lecturer/lecture/update", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("student1", "student1");
				super.request("/lecturer/lecture/update", param);
				super.checkPanicExists();
				super.signOut();

				super.signIn("assistant1", "assistant1");
				super.request("/lecturer/lecture/update", param);
				super.checkPanicExists();
				super.signOut();

			}
	}

	@Test
	public void test301Hacking() {
		// HINT: this test tries to update a course published
		// being the owner.

		Collection<Lecture> lectures;
		String param;

		super.signIn("lecturer2", "lecturer2");
		lectures = this.repository.findLecturesByLecturerUsername("lecturer2");
		for (final Lecture lecture : lectures)
			if (!lecture.isDraftMode()) {
				param = String.format("id=%d", lecture.getId());
				super.request("/lecturer/lecture/update", param);
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

		super.signIn("lecturer1", "lecturer1");
		lectures = this.repository.findLecturesByLecturerUsername("lecturer2");
		for (final Lecture lecture : lectures) {
			param = String.format("id=%d", lecture.getId());
			super.request("/lecturer/lecture/update", param);
			super.checkPanicExists();
		}
		super.signOut();
	}
}


package acme.testing.lecturer.lecture;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.testing.TestHarness;
import acme.testing.lecturer.course.LecturerCourseRepositoryTest;

public class LecturerLectureCreateTest extends TestHarness {

	@Autowired
	protected LecturerCourseRepositoryTest repository;

	//	@ParameterizedTest
	//	@CsvFileSource(resources = "/lecturer/lecture/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	//	public void test100Positive(final int recordIndex, final String title, final String lectureAbstract, final String lectureLearningTime, final String details, final String type, final String published) {
	//		super.signIn("lecturer2", "lecturer2");
	//
	//		super.clickOnMenu("Lecturer", "Create lecture");
	//		super.checkFormExists();
	//
	//		super.checkInputBoxHasValue("published", published);
	//		super.fillInputBoxIn("title", title);
	//		super.fillInputBoxIn("lectureAbstract", lectureAbstract);
	//		super.fillInputBoxIn("endPeriod", lectureLearningTime);
	//		super.fillInputBoxIn("body", details);
	//		super.fillInputBoxIn("lectureType", type);
	//
	//		super.clickOnSubmit("Create");
	//
	//		super.clickOnMenu("Lecturer", "List my lectures");
	//		super.checkListingExists();
	//		super.sortListing(0, "asc");
	//		super.checkColumnHasValue(recordIndex, 0, title);
	//		super.checkColumnHasValue(recordIndex, 1, lectureAbstract);
	//		super.checkColumnHasValue(recordIndex, 2, lectureLearningTime);
	//		super.clickOnListingRecord(recordIndex);
	//
	//		super.checkFormExists();
	//		super.checkInputBoxHasValue("published", published);
	//		super.checkInputBoxHasValue("title", title);
	//		super.checkInputBoxHasValue("lectureAbstract", lectureAbstract);
	//		super.checkInputBoxHasValue("endPeriod", lectureLearningTime);
	//		super.checkInputBoxHasValue("body", details);
	//		super.checkInputBoxHasValue("lectureType", type);
	//
	//		super.signOut();
	//	}


	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/lecture/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int recordIndex, final String title, final String lectureAbstract, final String lectureLearningTime, final String details, final String type) {
		super.signIn("lecturer2", "lecturer2");

		super.clickOnMenu("Lecturer", "Create lecture");
		super.checkFormExists();

		super.checkInputBoxHasValue("published", "false");
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("lectureAbstract", lectureAbstract);
		super.fillInputBoxIn("endPeriod", lectureLearningTime);
		super.fillInputBoxIn("body", details);
		super.fillInputBoxIn("lectureType", type);

		super.clickOnSubmit("Create");

		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {
		super.checkLinkExists("Sign in");
		super.request("/lecturer/lecture/create");
		super.checkPanicExists();

		super.signIn("administrator", "administrator");
		super.request("/lecturer/lecture/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("auditor1", "auditor1");
		super.request("/lecturer/lecture/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("company1", "company1");
		super.request("/lecturer/lecture/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("student1", "student1");
		super.request("/lecturer/lecture/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("assistant1", "assistant1");
		super.request("/lecturer/lecture/create");
		super.checkPanicExists();
		super.signOut();
	}
}

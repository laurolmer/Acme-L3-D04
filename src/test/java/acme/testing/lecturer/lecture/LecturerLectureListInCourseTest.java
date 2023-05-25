
package acme.testing.lecturer.lecture;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import acme.testing.TestHarness;

public class LecturerLectureListInCourseTest extends TestHarness {

	@Autowired
	protected LecturerLectureTestRepository repository;

	//	@ParameterizedTest
	//	@CsvFileSource(resources = "/lecturer/lecture/list-in-course-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	//	public void test100Positive(final int courseRecordIndex, final String courseTitle, final String coursePublished, final int lectureRecordIndex, final String lectureTitle, final String abstractLecture, final String lectureLearningTime) {
	//		// HINT: this test tries to check lectures listing at courses. If course if published it must be filled, else,
	//		// it can be empty (specified with value -1 at csv's) or filled.
	//
	//		super.signIn("lecturer1", "lecturer1");
	//
	//		super.clickOnMenu("Lecturer", "List my courses");
	//		super.checkListingExists();
	//		if (lectureRecordIndex > -1)
	//			super.sortListing(0, "asc");
	//		else
	//			super.sortListing(0, "desc");
	//
	//		super.checkColumnHasValue(courseRecordIndex, 0, courseTitle);
	//
	//		super.clickOnListingRecord(courseRecordIndex);
	//		super.checkInputBoxHasValue("published", coursePublished);
	//		super.checkLinkExists("Lectures list");
	//		super.clickOnLink("Lectures list");
	//		if (lectureRecordIndex > -1) {
	//			super.checkListingExists();
	//			super.checkColumnHasValue(lectureRecordIndex, 0, lectureTitle);
	//			super.checkColumnHasValue(lectureRecordIndex, 1, abstractLecture);
	//			super.checkColumnHasValue(lectureRecordIndex, 2, lectureLearningTime);
	//		} else
	//			super.checkListingEmpty();
	//
	//		super.signOut();
	//	}
	//
	//	@Test
	//	public void test200Negative() {
	//		// HINT:there aren't any negative tests for this feature since it's a listing
	//		// that doesn't involve entering anydata into any forms.
	//	}


	@Test
	public void test300Hacking() {
		// HINT: this test tries to list lectures with a role other than "Lecturer".

		super.checkLinkExists("Sign in");
		super.request("/lecturer/lecture/list");
		super.checkPanicExists();

		super.signIn("administrator", "administrator");
		super.request("/lecturer/lecture/list");
		super.checkPanicExists();
		super.signOut();

		super.signIn("auditor1", "auditor1");
		super.request("/lecturer/lecture/list");
		super.checkPanicExists();
		super.signOut();

		super.signIn("company1", "company1");
		super.request("/lecturer/lecture/list");
		super.checkPanicExists();
		super.signOut();

		super.signIn("student1", "student1");
		super.request("/lecturer/lecture/list");
		super.checkPanicExists();
		super.signOut();

		super.signIn("assistant1", "assistant1");
		super.request("/lecturer/lecture/list");
		super.checkPanicExists();
		super.signOut();
	}

}

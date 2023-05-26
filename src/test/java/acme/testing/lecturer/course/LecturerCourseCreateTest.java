
package acme.testing.lecturer.course;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;

import acme.testing.TestHarness;

public class LecturerCourseCreateTest extends TestHarness {

	@Autowired
	protected LecturerCourseRepositoryTest repository;


	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/course/create-positive.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test100Positive(final int recordIndex, final String code, final String title, final String abstractCourse, final String price, final String link, final String published) {
		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "Create course");
		super.checkFormExists();

		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("courseAbstract", abstractCourse);
		super.fillInputBoxIn("link", link);
		super.fillInputBoxIn("retailPrice", price);
		super.checkSubmitExists("Create");
		super.clickOnSubmit("Create");

		super.clickOnMenu("Lecturer", "List my courses");
		super.checkListingExists();
		super.sortListing(0, "asc");
		super.checkColumnHasValue(recordIndex, 0, title);
		super.checkColumnHasValue(recordIndex, 1, abstractCourse);
		super.checkColumnHasValue(recordIndex, 2, price);
		super.clickOnListingRecord(recordIndex);

		super.checkFormExists();
		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("title", title);
		super.checkInputBoxHasValue("courseAbstract", abstractCourse);
		super.checkInputBoxHasValue("retailPrice", price);
		super.checkInputBoxHasValue("link", link);
		super.checkInputBoxHasValue("published", published);

		super.checkLinkExists("Lectures list");
		super.checkLinkExists("Return");
		super.checkSubmitExists("Update");
		super.checkSubmitExists("Delete");
		super.checkSubmitExists("Publish");
		super.clickOnButton("Lectures list");
		super.checkListingExists();
		super.checkListingEmpty();

		super.signOut();
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/lecturer/course/create-negative.csv", encoding = "utf-8", numLinesToSkip = 1)
	public void test200Negative(final int recordIndex, final String code, final String title, final String abstractCourse, final String price, final String link) {
		super.signIn("lecturer1", "lecturer1");

		super.clickOnMenu("Lecturer", "Create course");
		super.checkFormExists();

		super.fillInputBoxIn("code", code);
		super.fillInputBoxIn("title", title);
		super.fillInputBoxIn("courseAbstract", abstractCourse);
		super.fillInputBoxIn("link", link);
		super.fillInputBoxIn("retailPrice", price);
		super.clickOnSubmit("Create");

		super.checkErrorsExist();

		super.signOut();
	}

	@Test
	public void test300Hacking() {
		super.checkLinkExists("Sign in");
		super.request("/lecturer/course/create");
		super.checkPanicExists();

		super.signIn("administrator", "administrator");
		super.request("/lecturer/course/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("auditor1", "auditor1");
		super.request("/lecturer/course/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("company1", "company1");
		super.request("/lecturer/course/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("student1", "student1");
		super.request("/lecturer/course/create");
		super.checkPanicExists();
		super.signOut();

		super.signIn("assistant1", "assistant1");
		super.request("/lecturer/course/create");
		super.checkPanicExists();
		super.signOut();
	}
}

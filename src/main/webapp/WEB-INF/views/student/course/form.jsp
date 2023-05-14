<%--
- form.jsp
-
- Copyright (C) 2012-2023 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="student.course.form.label.code" path="code"/>
	<acme:input-textbox code="student.course.form.label.title" path="title"/>
	<acme:input-textarea code="student.course.form.label.courseAbstract" path="courseAbstract"/>
	<acme:input-textbox code="student.course.form.label.courseType" path="courseType"/>
	<acme:input-textbox code="student.course.form.label.retailPrice" path="retailPrice"/>
	<acme:input-textbox code="student.course.form.label.link" path="link"/>
	<acme:input-textbox code="student.course.form.label.lecturer" path="lecturer"/>
	
	<acme:button code="student.course.form.button.lecture" action="/student/lecture/list?courseId=${id}"/>
</acme:form>

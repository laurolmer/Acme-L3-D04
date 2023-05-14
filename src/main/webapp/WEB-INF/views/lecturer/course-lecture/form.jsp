<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form>

	<acme:input-textbox code="lecturer.course-ecture.label.course" path="courseCode" readonly="true"/>
	<jstl:if test="${_command=='add'}">
		<acme:input-select code="lecturer.course-ecture.label.lecture" path="lectureId" choices="${lectures}"/>
		<acme:submit code="lecturer.course-lecture.button.add" action="/lecturer/course-lecture/add?courseId=${courseId}"/>
	</jstl:if>
	
	<jstl:if test="${_command=='delete'}">
		<acme:input-select code="lecturer.course-ecture.label.lecture" path="lectureId" choices="${lectures}"/>
		<acme:submit code="lecturer.course-lecture.button.delete" action="/lecturer/course-lecture/delete?courseId=${courseId}"/>
	</jstl:if>

</acme:form>

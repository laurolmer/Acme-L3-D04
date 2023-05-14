<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form>

	<acme:hidden-data path="id"/>
	<acme:input-checkbox code="lecturer.course.label.draftMode" path="published" readonly="true"/>
	<acme:input-textbox code="lecturer.course.label.code" path="code" placeholder="XYZ123"/>
	<acme:input-textbox code="lecturer.course.label.title" path="title" placeholder="course title"/>
	<acme:input-textarea code="lecturer.course.label.courseAbstract" path="courseAbstract" placeholder="course abstract"/>	
	<acme:input-double code="lecturer.course.label.retailPrice" path="retailPrice" placeholder="10.0 EUR"/>
	<acme:input-url code="lecturer.course.label.link" path="link" placeholder="http://acme-l3-d03.com"/>
	<jstl:if test="${ _command != 'create' && draftMode != true && courseType != null && estimatedTotalTime > 0.0 }">
		<acme:input-double code="lecturer.course.label.courseType" path="courseType" readonly="true"/>
		<acme:input-textbox code="lecturer.course.label.estimatedTotalTime" path="estimatedTotalTime" readonly="true"/>
	</jstl:if>
	
	<jstl:if test="${ _command == 'create' }" >
		<acme:submit code="lecturer.course.button.create" action="/lecturer/course/create"/>
	</jstl:if>
	
	<jstl:if test="${ _command != 'create' && !lectureList}">
		<acme:button code="lecturer.course.button.lectureList" action="/lecturer/lecture/list?courseId=${id}"/>
	</jstl:if>
	
	<jstl:if test="${ _command != 'create' && draftMode }">	
		<acme:submit code="lecturer.course.button.update" action="/lecturer/course/update"/>
		<acme:submit code="lecturer.course.button.delete" action="/lecturer/course/delete"/>		
		<acme:submit code="lecturer.course.button.publish" action="/lecturer/course/publish"/>		
	</jstl:if>
	
</acme:form>
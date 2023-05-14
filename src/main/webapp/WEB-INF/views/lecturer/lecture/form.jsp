<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form>

	<acme:hidden-data path="id"/>
	<acme:input-checkbox code="lecturer.lecture.label.draftMode" path="published" readonly="true"/>
	<acme:input-textbox code="lecturer.lecture.label.title" path="title"/>
	<acme:input-textbox code="lecturer.lecture.label.lectureAbstract" path="lectureAbstract"/>	
	<acme:input-double code="lecturer.lecture.label.estimatedLearningTime" path="endPeriod"/>
	<acme:input-textbox code="lecturer.lecture.label.body" path="body"/>
	<acme:input-select code="lecturer.lecture.label.lectureType" path="lectureType" choices="${lectureTypes}"/>

	<jstl:if test="${_command != 'create' && draftMode != false}">
		<acme:submit code="lecturer.lecture.button.update" action="/lecturer/lecture/update"/>
		<acme:submit code="lecturer.lecture.button.delete" action="/lecturer/lecture/delete"/>
		<acme:submit code="lecturer.lecture.button.publish" action="/lecturer/lecture/publish"/>
	</jstl:if>
	
	<jstl:if test="${_command == 'create'}">
		<acme:submit code="lecturer.lecture.button.create" action="/lecturer/lecture/create"/>
	</jstl:if>
	
</acme:form>


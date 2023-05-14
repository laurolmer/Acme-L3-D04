<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form>

	<acme:hidden-data path="id"/>
	<acme:input-checkbox code="lecturer.course.label.draftMode" path="published" readonly="true"/>
	<acme:input-textbox code="lecturer.course.label.code" path="code"/>
	<acme:input-textbox code="lecturer.course.label.title" path="title"/>
	<acme:input-textarea code="lecturer.course.label.courseAbstract" path="courseAbstract"/>	
	<acme:input-double code="lecturer.course.label.retailPrice" path="retailPrice"/>
	<acme:input-url code="lecturer.course.label.link" path="link"/>
	<jstl:if test="${courseType != null && estimatedTotalTime > 0.0}">
		<acme:input-double code="lecturer.course.label.courseType" path="courseType" readonly="true"/>
		<acme:input-textbox code="lecturer.course.label.estimatedTotalTime" path="estimatedTotalTime" readonly="true"/>
	</jstl:if>
</acme:form>
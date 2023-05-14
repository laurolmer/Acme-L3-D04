<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:list>
	<acme:list-column code="lecturer.course.label.title" path="title"  width="40%"/>
	<acme:list-column code="lecturer.course.label.courseAbstract" path="courseAbstract" width="40%" />
	<acme:list-column code="lecturer.course.label.retailPrice" path="retailPrice" width="20%" />
</acme:list>
<jstl:if test="${_command == 'list-mine'}">
	<acme:button code="lecturer.course.button.create" action="/lecturer/course/create"/>
</jstl:if>

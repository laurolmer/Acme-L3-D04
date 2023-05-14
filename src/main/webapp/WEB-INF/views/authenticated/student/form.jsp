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
	<acme:input-textbox code="authenticated.student.form.label.statement" path="statement"/>
	<acme:input-textbox code="authenticated.student.form.label.strongFeatures" path="strongFeatures"/>
	<acme:input-textbox code="authenticated.student.form.label.weakFeatures" path="weakFeatures"/>
	<acme:input-textbox code="authenticated.student.form.label.link" path="link"/>
	
	<acme:submit test="${_command == 'create'}" code="authenticated.student.form.button.create" action="/authenticated/student/create"/>
	<acme:submit test="${_command == 'update'}" code="authenticated.student.form.button.update" action="/authenticated/student/update"/>
</acme:form>

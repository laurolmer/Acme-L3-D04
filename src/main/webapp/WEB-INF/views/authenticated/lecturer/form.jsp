<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="authenticated.lecturer.form.label.almaMater" path="almaMater"/>
	<acme:input-textbox code="authenticated.lecturer.form.label.resume" path="resume"/>
	<acme:input-textbox code="authenticated.lecturer.form.label.qualifications" path="qualificationsList"/>
	<acme:input-textbox code="authenticated.lecturer.form.label.link" path="link"/>
	
	<acme:submit test="${_command == 'create'}" code="authenticated.lecturer.form.button.create" action="/authenticated/lecturer/create"/>
	<acme:submit test="${_command == 'update'}" code="authenticated.lecturer.form.button.update" action="/authenticated/lecturer/update"/>
</acme:form>
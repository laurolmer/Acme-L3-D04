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
	<acme:input-textbox code="authenticated.audit.form.label.code" path="code"/>
	<acme:input-textbox code="authenticated.audit.form.label.conclusion" path="conclusion"/>
	<acme:input-textbox code="authenticated.audit.form.label.strongPoints" path="strongPoints"/>
	<acme:input-textbox code="authenticated.audit.form.label.weakPoints" path="weakPoints"/>
	<acme:input-textbox code="authenticated.audit.form.label.auditor" path="auditor" readonly = "true"/>
	<acme:input-textbox code="authenticated.audit.form.label.draftMode" path="draftMode" readonly = "true"/>
	<acme:input-textbox code="authenticated.audit.form.label.courseName" path="title" readonly = "true"/>
	<acme:input-textbox code="authenticated.audit.form.label.lecturer" path="lecturer" readonly = "true"/>		

</acme:form>
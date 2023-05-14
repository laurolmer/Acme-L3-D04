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

<jstl:if test="${correction != null && correction == true}">
	<div style="margin-bottom: 5%;">
		<h2>
			<acme:message code="auditor.auditRecord.announce.correction"/>
		</h2>
	</div>
</jstl:if>

<acme:form>
	<acme:input-textbox code="auditor.auditRecord.form.label.subject" path="subject"/>
	<acme:input-textbox code="auditor.auditRecord.form.label.assesment" path="assesment"/>
	<acme:input-url code="auditor.auditRecord.form.label.link" path="link"/>
	<acme:input-textbox code="auditor.auditRecord.form.label.released" path="draftMode" readonly="true"/>
	<acme:input-textbox code="auditor.auditRecord.form.label.periodStart" path="periodStart"/>
	<acme:input-moment code="auditor.auditRecord.form.label.periodFin" path="periodFin"/>
	<acme:input-moment code="auditor.auditRecord.form.label.hours" path="hours" readonly="true"/>
	<acme:input-select code="auditor.auditRecord.form.label.mark" path="mark" choices="${elecs}"/>
	
	
	<jstl:if test="${_command == 'correction'}">
		<acme:input-checkbox code="auditor.auditRecord.form.button.confirmation" path="confirmation"/>
	</jstl:if>
<jstl:choose>	 
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:submit code="auditor.auditRecord.form.button.update" action="/auditor/audit-record/update"/>
			<acme:submit code="auditor.auditRecord.form.button.delete" action="/auditor/audit-record/delete"/>
			<acme:submit code="auditor.auditRecord.form.button.publish" action="/auditor/audit-record/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="auditor.auditRecord.form.button.create" action="/auditor/audit-record/create?auditId=${auditId}"/>
		</jstl:when>
		<jstl:when test="${_command == 'correction'}">
			<acme:submit code="auditor.auditRecord.form.button.create" action="/auditor/audit-record/correction?auditId=${auditId}"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>
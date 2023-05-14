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
	<acme:input-checkbox code="auditor.audit.form.label.draftMode" path="published" readonly="true"/>
	<acme:input-textbox code="auditor.audit.form.label.code" path="code"/>
	<acme:input-textbox code="auditor.audit.form.label.conclusion" path="conclusion"/>
	<acme:input-textbox code="auditor.audit.form.label.strongPoints" path="strongPoints"/>
	<acme:input-textbox code="auditor.audit.form.label.weakPoints" path="weakPoints"/>

	<jstl:if test="${_command != 'create' && allMarks != null}">
		<acme:input-textarea code="auditor.audit.form.label.marks" path="allMarks" readonly="true"/>
	</jstl:if>
	<acme:input-textbox code="auditor.audit.form.label.released" path="draftMode" readonly="true"/>
	<acme:input-select code="auditor.audit.form.label.courseCode" path="course" choices="${elecs}"/>
	
	<jstl:if test="${_command != 'create'}">
		<acme:button code="auditor.audit.form.button.auditRecords.list" action="/auditor/audit-record/list?auditId=${id}"/>		
	</jstl:if>

	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete') && draftMode == true}">
			<acme:submit code="auditor.audit.form.button.update" action="/auditor/audit/update"/>
			<acme:submit code="auditor.audit.form.button.delete" action="/auditor/audit/delete"/>
			<acme:submit code="auditor.audit.form.button.publish" action="/auditor/audit/publish"/>
			<acme:button code="auditor.audit.form.button.auditRecords.create" action="/auditor/audit-record/create?auditId=${id}"/>
		</jstl:when>
		
		<jstl:when test="${_command == 'show' && draftMode == false}">
			<acme:button code="auditor.audit.form.button.addCorrect" action="/auditor/audit-record/correction?auditId=${id}"/>
		</jstl:when>	
		
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="auditor.audit.form.button.create" action="/auditor/audit/create"/>
		</jstl:when>	
	</jstl:choose>
</acme:form>
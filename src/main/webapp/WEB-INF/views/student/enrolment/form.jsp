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
	<h3><acme:message code="student.enrolment.form.title.enrolment"/></h3>
			<acme:input-checkbox code="student.enrolment.form.label.draftMode" path="published" readonly="true"/>
			<acme:input-textbox code="student.enrolment.form.label.code" path="code"/>
			<acme:input-textbox code="student.enrolment.form.label.motivation" path="motivation"/>
			<acme:input-textbox code="student.enrolment.form.label.goals" path="goals"/>
			<acme:input-select code="student.enrolment.form.label.course" path="course" choices="${courses}"/>	
	<jstl:if test="${_command != 'create'}">
		<br>	
		<h3><acme:message code="student.enrolment.form.title.finalize"/></h3>		
			<jstl:if test="${draftMode}">
				<acme:input-textbox code="student.enrolment.form.label.creditCard" path="creditCard" placeholder="XXXX/XXXX/XXXX/XXXX"/>
			</jstl:if>
				<acme:input-textbox code="student.enrolment.form.label.holderName" path="holderName"/>
			<jstl:if test="${draftMode}">
				<acme:input-textbox code="student.enrolment.form.label.expiryDate" path="expiryDate" placeholder="MM/YY"/>
				<acme:input-textbox code="student.enrolment.form.label.cvc" path="cvc" placeholder="XXX"/>
			</jstl:if>
			<jstl:if test="${!draftMode}">
				<acme:input-textbox code="student.enrolment.form.label.lowerNibble" path="lowerNibble"/>
			</jstl:if>
	</jstl:if>
	
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|finalize') && draftMode}">	
            <acme:submit code="student.enrolment.form.button.update" action="/student/enrolment/update"/>
			<acme:submit code="student.enrolment.form.button.delete" action="/student/enrolment/delete"/>
			<acme:submit code="student.enrolment.form.button.finalize" action="/student/enrolment/finalize"/>
		</jstl:when>	
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="student.enrolment.form.button.create" action="/student/enrolment/create"/>
		</jstl:when>
	</jstl:choose>	
	<jstl:if test="${ _command == 'show' }" >
		<acme:button code="student.enrolment.form.button.activities" action="/student/activity/list?enrolmentId=${id}"/>
	</jstl:if>	
</acme:form>

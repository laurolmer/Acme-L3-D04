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

<h2>
	<acme:message code="assistant.dashboard.form.title.general-metrics"/>
</h2>
<h3>
	<acme:message code="assistant.dashboard.form.title.tutorial-time"/>
</h3>
<table class="table table-sm">
	<tr>
	<th scope="row">
			<acme:message code="assistant.dashboard.form.label.tutorial-time.average"/>
		</th>
		<td>
			<acme:print value="${tutorialTime.count}"/>
		</td>
		<th scope="row">
			<acme:message code="assistant.dashboard.form.label.tutorial-time.average"/>
		</th>
		<td>
			<acme:print value="${tutorialTime.average}"/>
		</td>
		<th scope="row">
			<acme:message code="assistant.dashboard.form.label.tutorial-time.minimum"/>
		</th>
		<td>
			<acme:print value="${tutorialTime.minimum}"/>
		</td>
		<th scope="row">
			<acme:message code="assistant.dashboard.form.label.tutorial-time.maximum"/>
		</th>
		<td>
			<acme:print value="${tutorialTime.maximum}"/>
		</td>
		<th scope="row">
			<acme:message code="assistant.dashboard.form.label.tutorial-time.deviation"/>
		</th>
		<td>
			<acme:print value="${tutorialTime.deviation}"/>
		</td>
	</tr>
</table>


<h3>
	<acme:message code="assistant.dashboard.form.title.session-time"/>
</h3>
<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:message code="assistant.dashboard.form.label.session-time.average"/>
		</th>
		<td>
			<acme:print value="${sessionTime.count}"/>
		</td>
		<th scope="row">
			<acme:message code="assistant.dashboard.form.label.session-time.average"/>
		</th>
		<td>
			<acme:print value="${sessionTime.average}"/>
		</td>
		<th scope="row">
			<acme:message code="assistant.dashboard.form.label.session-time.minimum"/>
		</th>
		<td>
			<acme:print value="${sessionTime.minimum}"/>
		</td>
		<th scope="row">
			<acme:message code="assistant.dashboard.form.label.session-time.maximum"/>
		</th>
		<td>
			<acme:print value="${sessionTime.maximum}"/>
		</td>
		<th scope="row">
			<acme:message code="assistant.dashboard.form.label.session-time.deviation"/>
		</th>
		<td>
			<acme:print value="${sessionTime.deviation}"/>
		</td>
	</tr>
</table>

	<h2>
		<acme:message code="assistant.dashboard.form.label.theory-tutorials"/>
		<acme:print value="${totalNumTheoryTutorials}"/>
	</h2>
	<h2>
		<acme:message code="assistant.dashboard.form.label.handson-tutorials"/>
		<acme:print value="${totalNumHandsOnTutorials}"/>
	</h2>

<acme:return/>
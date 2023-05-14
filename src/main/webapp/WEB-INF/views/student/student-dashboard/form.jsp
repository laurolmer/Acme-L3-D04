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
	<acme:message code="student.dashboard.form.title.general-metrics"/>
</h2>
<h3>
	<acme:message code="student.dashboard.form.title.course-time"/>
</h3>
<table class="table table-sm">
	<tr>
	<th scope="row">
			<acme:message code="student.dashboard.form.label.course-time.total"/>
		</th>
		<td>
			<acme:print value="${courseTime.count}"/>
		</td>
		<th scope="row">
			<acme:message code="student.dashboard.form.label.course-time.average"/>
		</th>
		<td>
			<acme:print value="${courseTime.average}"/>
		</td>
		<th scope="row">
			<acme:message code="student.dashboard.form.label.course-time.minimum"/>
		</th>
		<td>
			<acme:print value="${courseTime.minimum}"/>
		</td>
		<th scope="row">
			<acme:message code="student.dashboard.form.label.course-time.maximum"/>
		</th>
		<td>
			<acme:print value="${courseTime.maximum}"/>
		</td>
		<th scope="row">
			<acme:message code="student.dashboard.form.label.course-time.deviation"/>
		</th>
		<td>
			<acme:print value="${courseTime.deviation}"/>
		</td>
	</tr>
</table>


<h3>
	<acme:message code="student.dashboard.form.title.activity-time"/>
</h3>
<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:message code="student.dashboard.form.label.activity-time.total"/>
		</th>
		<td>
			<acme:print value="${activityTime.count}"/>
		</td>
		<th scope="row">
			<acme:message code="student.dashboard.form.label.activity-time.average"/>
		</th>
		<td>
			<acme:print value="${activityTime.average}"/>
		</td>
		<th scope="row">
			<acme:message code="student.dashboard.form.label.activity-time.minimum"/>
		</th>
		<td>
			<acme:print value="${activityTime.minimum}"/>
		</td>
		<th scope="row">
			<acme:message code="student.dashboard.form.label.activity-time.maximum"/>
		</th>
		<td>
			<acme:print value="${activityTime.maximum}"/>
		</td>
		<th scope="row">
			<acme:message code="student.dashboard.form.label.activity-time.deviation"/>
		</th>
		<td>
			<acme:print value="${activityTime.deviation}"/>
		</td>
	</tr>
</table>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:message code="student.dashboard.form.label.theory-activity"/>
		</th>
		<td>
			<acme:print value="${totalActivitiesByActivityType.getOrDefault('THEORY',0)}"/>
		</td>
		<th scope="row">
			<acme:message code="student.dashboard.form.label.hands-on-activity"/>
		</th>
		<td>
			<acme:print value="${totalActivitiesByActivityType.getOrDefault('HANDS_ON',0)}"/>
		</td>
	</tr>
</table>
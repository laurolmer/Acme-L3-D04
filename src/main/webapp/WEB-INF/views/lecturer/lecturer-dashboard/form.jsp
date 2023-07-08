<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<h2><acme:message code="lecturer.dashboard.lecture.num"/></h2>
<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:message code="lecturer.dashboard.lecture.numTheory"/>
		</th>
		<td>
			<acme:print value="${totalNumTheoryLectures}"/>
		</td>
		<th scope="row">
			<acme:message code="lecturer.dashboard.lecture.numHandsOn"/>
		</th>
		<td>
			<acme:print value="${totalNumHandsOnLectures}"/>
		</td>
		<th scope="row">
			<acme:message code="lecturer.dashboard.lecture.numBalanced"/>
		</th>
		<td>
			<acme:print value="${totalNumBalancedLectures}"/>
		</td>
	</tr>
</table>

<h2><acme:message code="lecturer.dashboard.lecture.data"/></h2>
<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:message code="lecturer.dashboard.lecture.count"/>
		</th>
		<td>
			<acme:print value="${countLectures}"/>
		</td>
		<th scope="row">
			<acme:message code="lecturer.dashboard.lecture.avg"/>
		</th>
		<td>
			<acme:print value="${avgLectureTime}"/>
		</td>
		<th scope="row">
			<acme:message code="lecturer.dashboard.lecture.max"/>
		</th>
		<td>
			<acme:print value="${maxLectureTime}"/>
		</td>
		<th scope="row">
			<acme:message code="lecturer.dashboard.lecture.min"/>
		</th>
		<td>
			<acme:print value="${minLectureTime}"/>
		</td>
		<th scope="row">
			<acme:message code="lecturer.dashboard.lecture.dev"/>
		</th>
		<td>
			<acme:print value="${devLectureTime}"/>
		</td>
	</tr>
</table>

<h2><acme:message code="lecturer.dashboard.course.data"/></h2>
<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:message code="lecturer.dashboard.course.count"/>
		</th>
		<td>
			<acme:print value="${countCourses}"/>
		</td>
		<th scope="row">
			<acme:message code="lecturer.dashboard.course.avg"/>
		</th>
		<td>
			<acme:print value="${avgCourseTime}"/>
		</td>
		<th scope="row">
			<acme:message code="lecturer.dashboard.course.max"/>
		</th>
		<td>
			<acme:print value="${maxCourseTime}"/>
		</td>
		<th scope="row">
			<acme:message code="lecturer.dashboard.course.min"/>
		</th>
		<td>
			<acme:print value="${minCourseTime}"/>
		</td>
		<th scope="row">
			<acme:message code="lecturer.dashboard.course.dev"/>
		</th>
		<td>
			<acme:print value="${devCourseTime}"/>
		</td>
	</tr>
</table>

<acme:return/>


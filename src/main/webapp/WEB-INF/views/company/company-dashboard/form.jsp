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
	<acme:message code="company.dashboard.form.title.general-indicators"/>
</h2>

<h3>
	<acme:message code="company.dashboard.form.title.practica-length"/>
</h3>
<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:message code="company.dashboard.form.label.practica-length.count"/>
		</th>
		<td>
			<acme:print value="${practicaLength.count}"/>
		</td>
		<th scope="row">
			<acme:message code="company.dashboard.form.label.practica-length.average"/>
		</th>
		<td>
			<acme:print value="${practicaLength.average}"/>
		</td>
		<th scope="row">
			<acme:message code="company.dashboard.form.label.practica-length.min"/>
		</th>
		<td>
			<acme:print value="${practicaLength.minimum}"/>
		</td>
		<th scope="row">
			<acme:message code="company.dashboard.form.label.practica-length.max"/>
		</th>
		<td>
			<acme:print value="${practicaLength.maximum}"/>
		</td>
		<th scope="row">
			<acme:message code="company.dashboard.form.label.practica-length.deviation"/>
		</th>
		<td>
			<acme:print value="${practicaLength.deviation}"/>
		</td>
	</tr>
</table>

<h3>
	<acme:message code="company.dashboard.form.title.session-length"/>
</h3>
<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:message code="company.dashboard.form.label.session-length.count"/>
		</th>
		<td>
			<acme:print value="${sessionLength.count}"/>
		</td>
		<th scope="row">
			<acme:message code="company.dashboard.form.label.session-length.average"/>
		</th>
		<td>
			<acme:print value="${sessionLength.average}"/>
		</td>
		<th scope="row">
			<acme:message code="company.dashboard.form.label.session-length.min"/>
		</th>
		<td>
			<acme:print value="${sessionLength.minimum}"/>
		</td>
		<th scope="row">
			<acme:message code="company.dashboard.form.label.session-length.max"/>
		</th>
		<td>
			<acme:print value="${sessionLength.maximum}"/>
		</td>
		<th scope="row">
			<acme:message code="company.dashboard.form.label.session-length.deviation"/>
		</th>
		<td>
			<acme:print value="${sessionLength.deviation}"/>
		</td>
	</tr>
</table>


<h2>
	<acme:message code="company.dashboard.form.title.total-number-practica-by-month"/>
</h2>

<div>
	<canvas id="canvas"></canvas>
	Total ${totalNumberOfPracticaByMonth.get('FEBRUARY')}
</div>

<script type="text/javascript">
	$(document).ready(function() {
		var data = {
			labels : [
					"JANUARY","FEBRUARY","MARCH","APRIL","MAY","JUNE","JULY","AUGUST","SEPTEMBER","OCTOBER","NOVEMBER","DECEMBER"
			],
			datasets : [
				{
					data : [
						<jstl:out value="${totalNumberOfPracticaByMonth.get('JANUARY')}"/>,
						<jstl:out value="${totalNumberOfPracticaByMonth.get('FEBRUARY')}"/>,
						<jstl:out value="${totalNumberOfPracticaByMonth.get('MARCH')}"/>,
						<jstl:out value="${totalNumberOfPracticaByMonth.get('APRIL')}"/>,
						<jstl:out value="${totalNumberOfPracticaByMonth.get('MAY')}"/>,
						<jstl:out value="${totalNumberOfPracticaByMonth.get('JUNE')}"/>,
						<jstl:out value="${totalNumberOfPracticaByMonth.get('JULY')}"/>,
						<jstl:out value="${totalNumberOfPracticaByMonth.get('AUGUST')}"/>,
						<jstl:out value="${totalNumberOfPracticaByMonth.get('SEPTEMBER')}"/>,
						<jstl:out value="${totalNumberOfPracticaByMonth.get('OCTOBER')}"/>,
						<jstl:out value="${totalNumberOfPracticaByMonth.get('NOVEMBER')}"/>,
						<jstl:out value="${totalNumberOfPracticaByMonth.get('DECEMBER')}"/>
					]
				}
			]
		};
		var options = {
			scales : {
				yAxes : [
					{
						ticks : {
							suggestedMin : 0.0,
							suggestedMax : 1.0
						}
					}
				]
			},
			legend : {
				display : false
			}
		};
	
		var canvas, context;
	
		canvas = document.getElementById("canvas");
		context = canvas.getContext("2d");
		new Chart(context, {
			type : "bar",
			data : data,
			options : options
		});
	});
</script>

<acme:return/>

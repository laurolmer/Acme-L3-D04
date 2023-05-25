<%@page language="java" %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h2>
    <acme:message code="administrator.dashboard.form.title.general-indicators"/>
</h2>

<h3>
    <acme:message code="administrator.dashboard.form.title.num-principals"/>
</h3>
<table class="table table-sm">
    <tr>
        <th scope="row">
            <acme:message code="administrator.dashboard.form.label.lecturer"/>
        </th>
        <td>
            <acme:print value="${principalsByRole['Lecturer']}"/>
        </td>
        <th scope="row">
            <acme:message code="administrator.dashboard.form.label.assistant"/>
        </th>
        <td>
            <acme:print value="${principalsByRole['Assistant']}"/>
        </td>
        <th scope="row">
            <acme:message code="administrator.dashboard.form.label.company"/>
        </th>
        <td>
            <acme:print value="${principalsByRole['Company']}"/>
        </td>
        <th scope="row">
            <acme:message code="administrator.dashboard.form.label.student"/>
        </th>
        <td>
            <acme:print value="${principalsByRole['Student']}"/>
        </td>
        <th scope="row">
            <acme:message code="administrator.dashboard.form.label.administrator"/>
        </th>
        <td>
            <acme:print value="${principalsByRole['Administrator']}"/>
        </td>
        <th scope="row">
            <acme:message code="administrator.dashboard.form.label.auditor"/>
        </th>
        <td>
            <acme:print value="${principalsByRole['Auditor']}"/>
        </td>
    </tr>
</table>

<h3>
    <acme:message code="administrator.dashboard.form.title.bulletins-by-critically"/>
</h3>
<table class="table table-sm">
    <tr>
        <th scope="row">
            <acme:message code="administrator.dashboard.form.label.critical"/>
        </th>
        <td>
            <acme:print value="${ratioOfCriticalBulletins}"/>
        </td>
        <th scope="row">
            <acme:message code="administrator.dashboard.form.label.non-critical"/>
        </th>
        <td>
            <acme:print value="${ratioOfNonCriticalBulletins}"/>
        </td>
    </tr>
</table>

<c:forEach items="${currentsOffersStats.keySet()}" var="currency">
    <c:set var="currencyData" value="${currentsOffersStats[currency]}"/>
    <div>
        <h3>
            <acme:print value="${currency}"/>
        </h3>
        <table class="table table-sm">
            <tr>
                <th scope="row">
                    <acme:message code="administrator.dashboard.form.label.offer.count"/>
                </th>
                <td>
                    <acme:print value="${currencyData.count}"/>
                </td>
                <th scope="row">
                    <acme:message code="administrator.dashboard.form.label.offer.average"/>
        </th>
        <td>
            <acme:print value="${currencyData.average}"/>
        </td>
        <th scope="row">
            <acme:message code="administrator.dashboard.form.label.offer.min"/>
        </th>
        <td>
            <acme:print value="${currencyData.minimum}"/>
        </td>
        <th scope="row">
            <acme:message code="administrator.dashboard.form.label.offer.max"/>
        </th>
        <td>
            <acme:print value="${currencyData.maximum}"/>
        </td>
        <th scope="row">
            <acme:message code="administrator.dashboard.form.label.offer.deviation"/>
        </th>
        <td>
            <acme:print value="${currencyData.deviation}"/>
        </td>
            </tr>
        </table>
    </div>
</c:forEach>

<h3>
    <acme:message code="administrator.dashboard.form.title.notes-in-last-ten-weeks"/>
</h3>
<table class="table table-sm">
    <tr>
        <c:forEach items="${notesInLast10WeeksStats.keySet()}" var="week">
            <c:set var="amount" value="${notesInLast10WeeksStats[week]}"/>
        <th scope="row">
            <acme:print value="${week}"/>
        </th>
        <td>
            <acme:print value="${amount}"/>
        </td>
        </c:forEach>
    <tr>
</table>

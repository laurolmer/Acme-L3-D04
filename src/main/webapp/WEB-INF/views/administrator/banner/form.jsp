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

	<acme:input-moment code="administrator.banner.form.label.upgrade" path="upgrade"/>
	<acme:input-moment code="administrator.banner.form.label.startDisplay" path="startDisplay"/>
	<acme:input-moment code="administrator.banner.form.label.endDisplay" path="endDisplay"/>
	<acme:input-url code="administrator.banner.form.label.imgLink" path="imgLink"/>
	<acme:input-textbox code="administrator.banner.form.label.slogan" path="slogan"/>
	<acme:input-textbox code="administrator.banner.form.label.docLink" path="docLink"/>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete')}">
			<acme:submit  code="administrator.banner.form.button.update" action="/administrator/banner/update"/>
			<acme:submit  code="administrator.banner.form.button.delete" action="/administrator/banner/delete"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit test="${_command == 'create'}" code="administrator.banner.form.button.create" action="/administrator/banner/create"/>
		</jstl:when>		
	</jstl:choose>	
	
	
	
</acme:form>
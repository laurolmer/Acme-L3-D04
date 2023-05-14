<%--
- menu.jsp
-
- Copyright (C) 2012-2023 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page language="java" import="acme.framework.helpers.PrincipalHelper,acme.roles.Provider,acme.roles.Consumer"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:menu-bar code="master.menu.home">
	<acme:menu-left>
		<acme:menu-option code="master.menu.anonymous" access="isAnonymous()">
			<acme:menu-suboption code="master.menu.anonymous.favourite-link.laurolmer" action="https://www.pinterest.es/"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link.alvhidrod" action="https://www.games-workshop.com/es-ES/Warhammer-40-000"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link.ivasansan1" action="https://www.pccomponentes.com/?gclid=Cj0KCQiAorKfBhC0ARIsAHDzslt99E547u_P3Y0T83IpZVmEN9UchYjv1DXHDp9Uj7M-2ZpSh_JSs4UaAiLSEALw_wcB"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link.albertoperleo" action="https://www.youtube.com/watch?v=eVrYbKBrI7o"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link.danguepre" action="https://ev.us.es/webapps/portal/execute/tabs/tabAction?tab_tab_group_id=_29_1"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.administrator" access="hasRole('Administrator')">
			<acme:menu-suboption code="master.menu.administrator.user-accounts" action="/administrator/user-account/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.bulletin.form" action="/administrator/bulletin/create"/>
			<acme:menu-suboption code="master.menu.administrator.populate-initial" action="/administrator/populate-initial"/>
			<acme:menu-suboption code="master.menu.administrator.populate-sample" action="/administrator/populate-sample"/>			
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.shut-down" action="/administrator/shut-down"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.configuration" action="/administrator/configuration/show"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.banner" action="/administrator/banner/list"/>
	
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.authenticated" access="isAuthenticated()">
			<acme:menu-suboption code="master.menu.authenticated.note.list" action="/authenticated/note/list"/>
		  	<acme:menu-suboption code="master.menu.any.course" action="/any/course/list"/>
			<acme:menu-suboption code="master.menu.offer.list" action="/authenticated/offer/list"/>
			<acme:menu-suboption code="master.menu.bulletin.list" action="/authenticated/bulletin/list"/>
			<acme:menu-suboption code="master.menu.authenticated.exchange-money.perform" action="/authenticated/exchange-money/perform"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.auditor" access="hasRole('Auditor')">
			<acme:menu-suboption code="master.menu.auditor.audit.list" action="/auditor/audit/list"/>
			</acme:menu-option>
			
		<acme:menu-option code="master.menu.provider" access="hasRole('Provider')">
			<acme:menu-suboption code="master.menu.provider.favourite-link" action="http://www.example.com/"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.consumer" access="hasRole('Consumer')">
			<acme:menu-suboption code="master.menu.consumer.favourite-link" action="http://www.example.com/"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.assistant" access="hasRole('Assistant')">
            <acme:menu-suboption code="master.menu.assistant.tutorial.list-mine" action="/assistant/tutorial/list-mine"/>
            <acme:menu-suboption code="master.menu.assistant.tutorial.list-all" action="/assistant/tutorial/list-all"/>  
            <acme:menu-separator/>
            <acme:menu-suboption code="master.menu.assistant.dashboard" action="/assistant/assistant-dashboard/show"/>      
        </acme:menu-option>

		<acme:menu-option code="master.menu.lecturer" access="hasRole('Lecturer')">
			<acme:menu-suboption code="master.menu.lecturer.course.create" action="/lecturer/course/create"/>
			<acme:menu-suboption code="master.menu.lecturer.course.list.all" action="/lecturer/course/list-all"/>
			<acme:menu-suboption code="master.menu.lecturer.course.list.mine" action="/lecturer/course/list-mine"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.lecturer.lecture.create" action="/lecturer/lecture/create"/>
			<acme:menu-suboption code="master.menu.lecturer.lecture.list.all" action="/lecturer/lecture/list-all"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.lecturer.lecture.dashboard" action="/lecturer/lecturer-dashboard/show"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.student" access="hasRole('Student')">
			<acme:menu-suboption code="master.menu.student.studentCourseList" action="/student/course/list"/>
			<acme:menu-suboption code="master.menu.student.studentEnrolmentList" action="/student/enrolment/list"/>	
			<acme:menu-suboption code="master.menu.student.dashboard" action="/student/student-dashboard/show"/>	
		</acme:menu-option>

		<acme:menu-option code="master.menu.assistant" access="hasRole('Assistant')">
			<acme:menu-suboption code="master.menu.assistant.tutorial.list-mine" action="/assistant/tutorial/list-mine"/>
			<acme:menu-suboption code="master.menu.assistant.tutorial.list-all" action="/assistant/tutorial/list-all"/>		
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.administrator.offer" access="hasRole('Administrator')">
			<acme:menu-suboption code="master.menu.administrator.offer.list" action="/administrator/offer/list"/>		

		</acme:menu-option>

		<acme:menu-option code="master.menu.company" access="hasRole('Company')">
			<acme:menu-suboption code="master.menu.company.practicum.list-mine" action="/company/practicum/list-mine"/>
			<acme:menu-suboption code="master.menu.company.practicum.list-all" action="/company/practicum/list-all"/>	
		
				
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.any.peep" action="/any/peep/list"/>
		
	</acme:menu-left>

	<acme:menu-right>
		<acme:menu-option code="master.menu.sign-up" action="/anonymous/user-account/create" access="isAnonymous()"/>
		<acme:menu-option code="master.menu.sign-in" action="/master/sign-in" access="isAnonymous()"/>

		<acme:menu-option code="master.menu.user-account" access="isAuthenticated()">
			<acme:menu-suboption code="master.menu.user-account.general-data" action="/authenticated/user-account/update"/>
			
			<acme:menu-suboption code="master.menu.user-account.become-provider" action="/authenticated/provider/create" access="!hasRole('Provider')"/>
			<acme:menu-suboption code="master.menu.user-account.provider" action="/authenticated/provider/update" access="hasRole('Provider')"/>
			<acme:menu-suboption code="master.menu.user-account.become-consumer" action="/authenticated/consumer/create" access="!hasRole('Consumer')"/>
			<acme:menu-suboption code="master.menu.user-account.consumer" action="/authenticated/consumer/update" access="hasRole('Consumer')"/>

			<acme:menu-suboption code="master.menu.user-account.become-company" action="/authenticated/company/create" access="!hasRole('Company')"/>
			<acme:menu-suboption code="master.menu.user-account.company" action="/authenticated/company/update" access="hasRole('Company')"/>

			<acme:menu-suboption code="master.menu.user-account.become-assistant" action="/authenticated/assistant/create" access="!hasRole('Assistant')"/>
			<acme:menu-suboption code="master.menu.user-account.assistant" action="/authenticated/assistant/update" access="hasRole('Assistant')"/>
			<acme:menu-suboption code="master.menu.user-account.become-student" action="/authenticated/student/create" access="!hasRole('Student')"/>
			<acme:menu-suboption code="master.menu.user-account.student" action="/authenticated/student/update" access="hasRole('Student')"/>
			<acme:menu-suboption code="master.menu.user-account.become-lecturer" action="/authenticated/lecturer/create" access="!hasRole('Lecturer')"/>
			<acme:menu-suboption code="master.menu.user-account.lecturer" action="/authenticated/lecturer/update" access="hasRole('Lecturer')"/>

		</acme:menu-option>

		<acme:menu-option code="master.menu.sign-out" action="/master/sign-out" access="isAuthenticated()"/>
	</acme:menu-right>
</acme:menu-bar>


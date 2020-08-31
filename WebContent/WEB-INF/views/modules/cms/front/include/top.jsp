<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<header class="header">
  <div class="wrapper">
    <h1 class="fl"><a href="${ctx}">溯源管理平台</a></h1>
    <div class="fr search">
    	<form action="${ctx}/search" method="get">
          	<input class="input_txt" type="text" name="q" placeholder="全站搜索..." value="${q}">
        </form>
    </div>
    <nav class="fr head_nav">
      <ul>
      <shiro:authenticated>
      	<li class="yellow"><shiro:principal property="name"/></li>
      	<li><a href="${ctxNo}/a" target="_blank">后台管理</a></li>
      	<li><a href="${ctxNo}/a/logout">退出</a></li>
      </shiro:authenticated>
      <shiro:notAuthenticated>
      	<li><a href="${ctxNo}/a">登录</a></li>
      </shiro:notAuthenticated>
      </ul>
    </nav>
  </div>
</header>
<menu class="fr maxbox menu">
  <ul class="wrapper">
      <c:forEach items="${fnc:getMainNavList()}" var="category" varStatus="status">
      	<c:if test="${category.inMenu eq '1'}">
      		<c:set var="menuCategoryId" value=",${category.id},"/>
        	<li><a class="${requestScope.categoryFirst.id eq category.id || isIndex == false?'cur':''}" 
        		href="${category.url}" target="${category.target}">${category.name}</a></li>
        </c:if>
      </c:forEach>
  </ul>
</menu>
<menu class="fr maxbox submenu">
  <ul class="wrapper">
      <c:forEach items="${fnc:getCategoryList(requestScope.categoryFirst.id eq null?3:requestScope.categoryFirst.id,100,null)}" var="category" varStatus="status">
      	<c:set var="menuCategoryId" value=",${category.id},"/>
        <li><a class="${requestScope.category.id eq category.id?'cur':''}" 
        href="${category.url}" target="${category.target}">${category.name}</a></li>
      </c:forEach>
  </ul>
</menu>
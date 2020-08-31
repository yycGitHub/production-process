<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>${fns:getConfig('productName')}</title>
<meta name="decorator" content="cms_default"/>
</head>
<body class="bg">
<div class="fl maxbox container">
  <div class="wrapper">
    <section class="fl mainbody">
    <c:forEach items="${fnc:getMainNavList()}" var="category" varStatus="status">
      	<c:if test="${category.inMenu eq '1'}">
      	  <c:if test="${status.index == 0}">
	      	<figure class="fl maxbox list">
	      </c:if>
	      <c:if test="${status.index != 0}">
	      	<figure class="fl maxbox list martop20">
	      </c:if>
	        <figcaption>${category.name}</figcaption>
	        <ul>
	         <c:forEach items="${fnc:getArticleList(category.id, 5, '')}" var="article">
	          <li>
	            <a href="${article.url}">${article.title}</a>
	            <p>
	              <span>${article.createBy.name}</span>
	              <span class="marleft"><fmt:formatDate value="${article.updateDate}" pattern="yyyy.MM.dd"/></span>
	              <span class="marleft orange"><i class="iconfont">&#xe6e6;</i>${article.hits}</span>
	            </p>
	          </li>
			 </c:forEach>
	        </ul>
	      	</figure>
	      </c:if>
      </c:forEach>
    </section>
    <aside class="fr right_sidebar">
      <figure class="fl maxbox list_side">
        <figcaption>热门通道</figcaption>
        <ul>
        <c:forEach items="${fnc:getArticleList(null, not empty pageSize?pageSize:8, 'posid:2, orderBy: \"hits desc\"')}" var="article">
			<li><a href="${ctx}/view-${article.category.id}-${article.id}${urlSuffix}" style="color:${article.color}" title="${article.title}">${fns:abbr(article.title,16)}</a></li>
		</c:forEach>
        </ul>
      </figure>
    </aside>
  </div>
</div>
</body>
</html>
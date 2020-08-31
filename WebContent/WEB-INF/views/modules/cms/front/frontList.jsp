<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>${category.name}</title>
<meta name="decorator" content="cms_default"/>
<script type="text/javascript">
	function page(n,s){
		location="${ctx}/list-${category.id}${urlSuffix}?pageNo="+n+"&pageSize="+s;
	}
</script>
</head>
<body class="bg">
<form:form id="searchForm" modelAttribute="article" action="" 
	method="post" class="breadcrumb form-search">
	<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
	<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
</form:form>
<div class="fl maxbox container">
  <div class="wrapper">
    <section class="fl mainbody">
      <figure class="fl maxbox list">
        <figcaption>${category.name}</figcaption>
        <ul>
         <c:if test="${category.module eq 'article'}">
         <c:forEach items="${page.list}" var="article">
          <li>
            <a href="${article.url}">${fns:abbr(article.title,96)}</a>
            <p>
              <span>${article.article_author}</span>
              <span class="marleft"><fmt:formatDate value="${article.updateDate}" pattern="yyyy.MM.dd"/></span>
              <span class="marleft orange"><i class="iconfont">&#xe6e6;</i>${article.hits}</span>
            </p>
          </li>
          </c:forEach>
          </c:if>
        </ul>
      </figure>
	  <div class="fr">
		<div class="page">${page.frontPage}</div>
	  </div>
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
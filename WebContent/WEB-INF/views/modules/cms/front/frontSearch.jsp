<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>搜索</title>
<meta name="decorator" content="cms_default"/>
</head>
<body class="bg">
<div class="fl maxbox container">
  <div class="wrapper">
    <section class="fl mainbody">
      <figure class="fl maxbox list">
		<form:form id="searchForm" method="get" class="search">
			<input type="hidden" id="pageNo" name="pageNo" value="${page.pageNo}"/>
			<input type="hidden" id="t" name="t" value="${not empty t?t:'article'}"/>
			<input type="hidden" id="cid" name="cid" value="${cid}"/>
			<input type="hidden" id="a" name="a" value="${not empty t?t:'0'}"/>
			<div class="sel">
				<a href="javascript:" onclick="$('#t').val('article');$('.sel a').removeClass('act');$(this).addClass('act')" class="${empty t || t eq 'article'?'act':''}">文章搜索</a> &nbsp;
				<a href="javascript:" onclick="$('#t').val('guestbook');$('.sel a').removeClass('act');$(this).addClass('act')" class="${t eq 'guestbook'?'act':''}">留言搜索</a>
			</div>
			<c:choose>
				<c:when test="${param.a eq '1'}">
					<table>
						<tr><td><input type="text" name="q" value="${q}" class="txt"/>
							<input type="submit" value="搜  索" class="btn" onclick="$('#a').val('1')"/>
							</td>
						</tr>
					</table>
				</c:when><c:otherwise>
					<input type="hidden" id="pageSize" name="pageSize" value="${page.pageSize}"/>
					<input type="text" name="q" value="${q}" class="txt"/>
					<input type="submit" value="搜  索" class="btn" onclick="$('#a').val('0')"/>
				</c:otherwise>
			</c:choose>
		</form:form>
      </figure>
      <figure class="fl maxbox list martop20">
      	<ul>
			<c:if test="${empty t || t eq 'article'}">
				<c:forEach items="${page.list}" var="article">
				  <li>
					<a href="${ctx}/view-${article.category.id}-${article.id}${urlSuffix}" target="_blank">${article.title}</a>
					<p>
						<span>${article.description}</span>
						<span class="info">发布者：${article.createBy.name}</span>
						<span>点击数：${article.hits}</span> 
						<span>发布时间：<fmt:formatDate value="${article.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
						<br/>
					</p>
					</li>
				</c:forEach>
			</c:if>
			<c:if test="${t eq 'guestbook'}">
				<c:forEach items="${page.list}" var="guestbook">
					<dt>${fns:getDictLabel(guestbook.type,'cms_guestbook','')}</dt>
					<dd>${guestbook.content}<span class="info">
					<br/>姓名：${guestbook.name} &nbsp; 
					留言时间：<fmt:formatDate value="${guestbook.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></span></dd>
					<dd>回复：${guestbook.reContent}
					<span class="info"><br/>回复人：${guestbook.reUser.name} &nbsp; 
					回复时间：<fmt:formatDate value="${guestbook.reDate}" pattern="yyyy-MM-dd HH:mm:ss"/></span></dd>
				</c:forEach>
			</c:if>
			<c:if test="${fn:length(page.list) eq 0}">
				<dt><c:if test="${empty q}">请键入要查找的关键字。</c:if><c:if test="${not empty q}">抱歉，没有找到与“${q}”相关内容。</c:if><br/><br/>建议：</dt>
				<dd><ul><li>检查输入是否正确；</li><li>简化输入词；</li><li>尝试其他相关词，如同义、近义词等。</li></ul></dd>
			</c:if>
		</ul>
		<ul>
			<li>
				${page.frontPage}
			</li>
	    </ul>
		<script type="text/javascript">
			function page(n,s){
				$("#pageNo").val(n);
				$("#pageSize").val(s);
				$("#searchForm").submit();
		    	return false;
		    }
		</script>
      </figure>
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
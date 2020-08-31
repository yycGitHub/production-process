<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>${article.title} - ${category.name}</title>
<meta name="decorator" content="cms_default_no_menu"/>
<script type="text/javascript">
	$(document).ready(function() {
		if ("${category.allowComment}"=="1" && "${article.allowComment}"=="1"){
			$("#comment").show();
			page(1);
		}
	});
	function page(n,s){
		$.get("${ctx}/comment",{'category.id': '${category.id}',
			contentId: '${article.id}', title: '${article.title}', pageNo: n, pageSize: s, date: new Date().getTime()
		},function(data){
			$("#comment").html(data);
		});
	}
</script>
</head>
<body class="bg">
<div class="fl maxbox container">
  <div class="wrapper">
    <section class="fl mainbody">
      <div class="fl show">
        <h2>${article.title}</h2>
        <h6>
          <span>${article.createBy.name}</span>
          <span class="marleft"><fmt:formatDate value="${article.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
          <span class="marleft"><i class="iconfont">&#xe6e6;</i> ${article.hits}</span>
        </h6>
        <div class="fl maxbox show_main">
          <p>${article.content}</p>
        </div>
        <span>附件</span>
        <div class="fl maxbox comment">
			<c:forEach items="${fileInfos}" var="fileInfo" varStatus="status">
	    		<c:if test="${fileInfo.type == 'image/png' || fileInfo.type == 'image/jpeg'}">
	    			<a href="${fileInfo.url}" target="_blank"><img src="${fileInfo.url}" style="width: 50px;height: 50px;"/>${fileInfo.fileName}</a>
	    		</c:if>
	    		<c:if test="${fileInfo.type != 'image/png' && fileInfo.type != 'image/jpeg'}">
	    			<a href="${fileInfo.url}">${fileInfo.fileName}</a>
	    		</c:if>
	    		<br/>
	   		</c:forEach>
   		</div>
      </div>
		<div id="comment" class="hide">
			正在加载评论...
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
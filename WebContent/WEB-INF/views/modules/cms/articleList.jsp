<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>文章管理</title>
	<meta name="decorator" content="cms_admin_default"/>
	<script src="${ctxStatic}/common/pony.js" type="text/javascript"></script>
	<script type="text/javascript">
	$(document).ready(function() {
		$("#btnDelete").click(function(){
		   	if(Pn.checkedCount('ids')<=0) {
		   	   $.jBox.alert("请选择要删除的项!","提示");
		       return;
		  	}
			top.$.jBox.confirm("确认要删除吗？","系统提示",function(v,h,f){
				if(v == "ok"){
					 $("#contentTableForm").attr("action", "${ctx}/cms/article/delete?categoryId="+${article.category.id}); 
					 $("#contentTableForm").submit();
				}
			},{buttonsFocus:1});
		});
	});
		function viewComment(href){
			top.$.jBox.open('iframe:'+href,'查看评论',$(top.document).width()-220,$(top.document).height()-120,{
				buttons:{"关闭":true},
				loaded:function(h){
					$(".jbox-content", top.document).css("overflow-y","hidden");
					$(".nav,.form-actions,[class=btn]", h.find("iframe").contents()).hide();
					$("body", h.find("iframe").contents()).css("margin","10px");
				}
			});
			return false;
		}
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
<div class="miandody">
<div class="con">
     <div class="con_nav">
		<ul class="fl">
	        <li><a class="cur" href="${ctx}/cms/article/?category.id=${article.category.id}">文章列表</a></li>
	        <shiro:hasPermission name="cms:article:edit">
	        <li><a href="<c:url value='${fns:getAdminPath()}/cms/article/form?id=${article.id}&category.id=${article.category.id}'><c:param name='category.name' value='${article.category.name}'/>
				<c:param name='category.name' value='${article.category.name}'/></c:url>">文章添加</a></li>
			</shiro:hasPermission>
      	</ul>
    </div>	
    <!--  
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/cms/article/?category.id=${article.category.id}">文章列表</a></li>
		<shiro:hasPermission name="cms:article:edit"><li><a href="<c:url value='${fns:getAdminPath()}/cms/article/form?id=${article.id}&category.id=${article.category.id}'><c:param name='category.name' value='${article.category.name}'/></c:url>">文章添加</a></li></shiro:hasPermission>
	</ul>
	-->
	<form:form id="searchForm" modelAttribute="article" action="${ctx}/cms/article/" method="post" class="breadcrumb form-search">
	<div class="search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<span><label>标题：</label><form:input path="title" htmlEscape="false" maxlength="50" class="input_txt w200"/></span>
		<input id="btnSubmit" class="btn btn_primary" type="submit" value="查询"/>&nbsp;&nbsp;
		<input id="btnDelete" class="btn btn_primary" type="button" value="删除"/>&nbsp;&nbsp;
		<!-- 
		<label>状态：</label><form:radiobuttons onclick="$('#searchForm').submit();" path="delFlag" items="${fns:getDictList('cms_del_flag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
		 -->
		</div>
	</form:form>
	<tags:message content="${message}"/>
	<form id="contentTableForm" name="contentTableForm" action="" method="post">
	<table id="contentTable" class="tab" cellpadding="0" cellspacing="0">
		<thead>
		<tr>
			<th><input type="checkbox"  onclick="Pn.checkbox('ids',this.checked)"/></th>
			<th>栏目</th>
			<th>标题</th>
			<th>权重</th>
			<th>点击数</th>
			<th>发布者</th>
			<th>文章作者</th>
			<th>更新时间</th>
			<th>操作</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="article">
			<tr>
				<td align="center">
				<c:if test="${article.createBy.id == user.id or user.isPlateAdmin}">
					<input type="checkbox" name="ids" value="${article.id}" />
				</c:if>
				</td>
				<td><a href="javascript:" onclick="$('#categoryId').val('${article.category.id}');$('#categoryName').val('${article.category.name}');$('#searchForm').submit();return false;">${article.category.name}</a></td>
				<td><a href="${ctx}/cms/article/form?id=${article.id}" title="${article.title}">${fns:abbr(article.title,40)}</a></td>
				<td>${article.weight}</td>
				<td>${article.hits}</td>
				<td>${article.createBy.name}</td>
				<td>${article.article_author}</td>
				<td><fmt:formatDate value="${article.updateDate}" type="both"/></td>
				<td>
					<a href="${pageContext.request.contextPath}${fns:getFrontPath()}/view-${article.category.id}-${article.id}${fns:getUrlSuffix()}" target="_blank">访问</a>
					<shiro:hasPermission name="cms:article:edit">
						<c:if test="${article.category.allowComment eq '1'}">
							<a href="${ctx}/cms/comment/?module=article&contentId=${article.id}&delFlag=2" onclick="return viewComment(this.href);">评论</a>
						</c:if>
	    				<a href="${ctx}/cms/article/form?id=${article.id}">修改</a>
	    				<shiro:hasPermission name="cms:article:audit">
							<a href="${ctx}/cms/article/delete?ids=${article.id}${article.delFlag ne 0?'&isRe=true':''}&categoryId=${article.category.id}" onclick="return confirmx('确认要${article.delFlag ne 0?'发布':'删除'}该文章吗？', this.href)" >${article.delFlag ne 0?'发布':'删除'}</a>
						</shiro:hasPermission>
					</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
		<tfoot>
			<tr>
				<td colspan="9">
					<div class="fr">
						<div class="page">${page}</div>
					</div>
				</td>
			</tr>
		</tfoot>
	</table>
	</form>
	</div>
	</div>
</body>
</html>
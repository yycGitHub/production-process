<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>评论管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>
	<style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
	<script src="${ctxStatic}/common/pony.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnDelete").click(function(){
			   	if(Pn.checkedCount('ids')<=0) {
			   	   $.jBox.alert("请选择要删除的评论!","提示");
			       return;
			  	}
				top.$.jBox.confirm("确认要删除吗？","系统提示",function(v,h,f){
					if(v == "ok"){
						var categoryId=$("#categoryId").val();
						 $("#contentTableForm").attr("action", "${ctx}/cms/comment/delete?categoryId="+categoryId); 
						 $("#contentTableForm").submit();
					}
				},{buttonsFocus:1});
			});
			$("#btnF").click(function(){
			   	if(Pn.checkedCount('ids')<=0) {
			   	   $.jBox.alert("请选择要恢复的评论!","提示");
			       return;
			  	}
				top.$.jBox.confirm("确认要恢复吗？","系统提示",function(v,h,f){
					if(v == "ok"){
						var categoryId=$("#categoryId").val();
						 $("#contentTableForm").attr("action", "${ctx}/cms/comment/delete?isRe=true&categoryId="+categoryId); 
						 $("#contentTableForm").submit();
					}
				},{buttonsFocus:1});
			});
		});
		
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			var categoryId=$("#categoryId").val();
			$("#searchForm").attr("action","${ctx}/cms/comment?category.id="+categoryId).submit();
	    	return false;
	    }
		
		function clearInput(){
			$(".search dt :text").val("");
			$(".search dt select").val("");
		}
		
		function view(href){
			top.$.jBox.open('iframe:'+href,'查看文档',$(top.document).width()-220,$(top.document).height()-180,{
				buttons:{"关闭":true},
				loaded:function(h){
					//$(".jbox-content", top.document).css("overflow-y","hidden");
					//$(".nav,.form-actions,[class=btn]", h.find("iframe").contents()).hide();
				}
			});
			return false;
		}
	</script>
</head>
<body>
	<div class="miandody">
	<div class="crumb">
		<i class="iconfont">&#xe60d;</i><strong>内容管理</strong><span
			class="songti">&gt;</span><span>评论管理</span>
		<b class="fr">
			<a class="min_btn btn_secondary marright" id="btnDelete">删除</a>
		</b>
		<b class="fr">
			<a class="min_btn btn_secondary marright" id="btnF">恢复</a>
		</b>
	</div>
	<div class="con">	
		<form:form id="searchForm" modelAttribute="comment" action="${ctx}/cms/comment/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
		<input id="categoryId" name="categoryId" type="hidden" value="${categoryId}"/>
			<div class="search">
				<dl>
					<dt>
						<span>
							<form:input path="title" htmlEscape="false"
								maxlength="50" cssClass="input_txt w260" placeholder="文档标题" />
						</span>
						<span>
							<form:select path="delFlag" label="状态" htmlEscape="false" class="drop_down gray w200">
								<form:options items="${fns:getDictList('cms_del_flag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
							</form:select>
						</span>
					</dt>
					<dd>
			          <span><input id="btnSubmit" class="btn btn_primary" type="submit" value="查询" onclick="return page();"/></span>
			          <span><input class="btn btn_default" name="" type="button" value="清除" onclick="clearInput()"></span>
			        </dd>
				</dl>
			</div>
		</form:form>
		<tags:message content="${message}"/>
		<div class="tabcon">
		<form id="contentTableForm" name="contentTableForm" action="" method="post">
		<tags:tableSort id="orderBy" name="orderBy" value="${GXCP0102.orderBy}" callback="page();"/>
			<table class="tab" id="contentTable" cellpadding="0" cellspacing="0">
				<thead>
					<tr>
						<td><input type="checkbox"  onclick="Pn.checkbox('ids',this.checked)"/></td>
						<td><nobr>评论内容</nobr></td>
						<td><nobr>文档标题</nobr></td>
						<td><nobr>评论人</nobr></td>
						<td><nobr>评论IP</nobr></td>
						<td><nobr>评论时间</nobr></td>
						<td><nobr>操作</nobr></td>
					</tr>
				</thead>
				<tbody>
						<c:forEach items="${page.list}" var="comment">
							<tr>
								<td align="center"><input type="checkbox" name="ids" value="${comment.id}" /></td>
								<td><a href="javascript:" onclick="$('#c_${comment.id}').toggle()">${fns:abbr(fns:replaceHtml(comment.content),40)}</a></td>
								<td><a href="${pageContext.request.contextPath}${fns:getFrontPath()}/view-${comment.category.id}-${comment.contentId}${fns:getUrlSuffix()}" title="${comment.title}" onclick="return view(this.href);">${fns:abbr(comment.title,40)}</a></td>
								<td>${comment.createBy.name}</td>
								<td>${comment.ip}</td>
								<td><fmt:formatDate value="${comment.createDate}" type="both"/></td>
								<td><shiro:hasPermission name="cms:comment:edit">
									<c:if test="${comment.delFlag ne '2'}"><a href="${ctx}/cms/comment/delete?ids=${comment.id}${comment.delFlag ne 0?'&isRe=true':''}&categoryId=${comment.category.id}" 
										onclick="return confirmx('确认要${comment.delFlag ne 0?'恢复审核':'删除'}该审核吗？', this.href)">${comment.delFlag ne 0?'恢复审核':'删除'}</a></c:if>
									<c:if test="${comment.delFlag eq '2'}"><a href="${ctx}/cms/comment/save?id=${comment.id}&category.id=${comment.category.id}">通过审核</a></c:if></shiro:hasPermission>
								</td>
							</tr>
							<tr id="c_${comment.id}" style="background:#fdfdfd;display:none;"><td colspan="6">${fns:replaceHtml(comment.content)}</td></tr>
						</c:forEach>
					</tbody>
				<tfoot>
					<tr>
						<td colspan="40">
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
</div>
</body>
</html>
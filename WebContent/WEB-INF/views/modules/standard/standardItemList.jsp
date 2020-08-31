<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>表单管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>
	<style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
	<script src="${ctxStatic}/common/pony.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(document).ready(function() {		
			$("#btnDelete").click(function(){
			   	if(Pn.checkedCount('ids')<=0) {
			   	   $.jBox.alert("请选择要删除的项!","系统提示");
			       return;
			  	}
				top.$.jBox.confirm("确认要删除吗？","系统提示",function(v,h,f){
					if(v == "ok"){
						 $("#contentTableForm").attr("action", "${ctx}/standard/deleteStandardItem?stId=${stId}"); 
						 $("#contentTableForm").submit();
					}
				},{buttonsFocus:1});
			});
		});
	</script>
</head>
<body>
	<div class="miandody">
	<div class="crumb">
		<i class="iconfont">&#xe60d;</i><strong>表单管理</strong><span
			class="songti">&gt;</span><strong><a href="${ctx}/standard/list">表单列表</a></strong><span
			class="songti">&gt;</span><span>参数项</span>
		<b class="fr">
			<a class="min_btn btn_secondary marright" id="btnDelete">删除</a>
		</b>
		<b class="fr">
			<a href="${ctx}/standard/standardItem?stId=${stId}" class="min_btn btn_secondary marright">新增参数项</a>
		</b>
	</div>
	<div class="con">
	<div class="tabcon">
	<tags:message content="${message}"/>
	<form id="contentTableForm" name="contentTableForm" action="" method="post">
	<table class="tab" id="contentTable" cellpadding="0" cellspacing="0">
		<thead>
			<tr>
				<th><input type="checkbox"  onclick="Pn.checkbox('ids',this.checked)"/></th>
				<th>参数名称</th>
				<th>字段名</th>
				<th>排序</th>
				<th>类型</th>
				<shiro:hasPermission name="sys:standard:expedit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${list}" var="standardItem">
			<tr>
				<td align="center"><input type="checkbox" name="ids" value="${standardItem.id}"/></td>
				<td><a href="${ctx}/standard/standardItem?standardItemId=${standardItem.id}">${standardItem.name}</a></td>
				<td>${standardItem.keyName}</td>
				<td>${standardItem.sort}</td>
				<td>${standardItem.itemType}</td>
				<shiro:hasPermission name="sys:standard:expedit">
				<td>
    				<c:if test="${standardItem.delFlag == '0'}">
    					<a href="${ctx}/standard/standardItem?standardItemId=${standardItem.id}">修改</a>
						<a href="${ctx}/standard/deleteStandardItem?ids=${standardItem.id}&stId=${stId}" onclick="return confirmx('确认要删除吗？', this.href)">删除</a>
					</c:if>
					<c:if test="${standardItem.delFlag == '1'}">
						<a href="${ctx}/standard/standardItemReback?ids=${standardItem.id}$stId=${stId}" onclick="return confirmx('确认要恢复吗？', this.href)">恢复</a>
					</c:if>
					<c:if test="${standardItem.itemType == '1'}">
						<a id="iframe" class="fancybox fancybox.iframe" href="${ctx}/standard/standardItemValueList?standardItemId=${standardItem.id}">添加参数值</a>
					</c:if>
				</td>
				</shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	</form>
    </div>
</div>
</div>
</body>
</html>
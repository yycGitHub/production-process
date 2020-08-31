<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>参数值管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>
	<style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
	<script src="${ctxStatic}/common/pony.js" type="text/javascript"></script>
	<script type="text/javascript">
	$(document).ready(function() {		
		$("#btnDelete").click(function(){
		   	if(Pn.checkedCount('ids')<=0) {
		   	   $.jBox.alert("请选择要删除项!","系统提示");
		       return;
		  	}
			top.$.jBox.confirm("确认要删除吗？","系统提示",function(v,h,f){
				if(v == "ok"){
					 $("#contentTableForm").attr("action", "${ctx}/standard/deleteStandardItemValue?standardItemId=${standardItemId}"); 
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
			class="songti">&gt;</span><strong>
			<a href="${ctx}/standard/standardItemList?stId=${standardItem.targetId}">参数列表</a></strong><span
			class="songti">&gt;</span><span>参数值列表</span>
		<b class="fr">
			<a class="min_btn btn_secondary marright" id="btnDelete">删除</a>
		</b>
		<b class="fr">
			<a href="${ctx}/standard/standardItemValue?standardItemId=${standardItemId}" 
				class="min_btn btn_secondary marright">新增参数值项</a>
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
				<th>名称</th>
				<th>值</th>
				<shiro:hasPermission name="sys:standard:expedit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${list}" var="standardItemValue">
			<tr>
				<td align="center"><input type="checkbox" name="ids" value="${standardItemValue.id}"/></td>
				<td><a href="${ctx}/standard/standardItemValue?standardItemValueId=${standardItemValue.id}">${standardItemValue.name}</a></td>
				<td>${standardItemValue.value}</td>
				<shiro:hasPermission name="sys:standard:expedit">
				<td>
    				<c:if test="${standardItemValue.delFlag == '0'}">
    					<a href="${ctx}/standard/standardItemValue?standardItemValueId=${standardItemValue.id}">修改</a>
						<a href="${ctx}/standard/standardItemValueDelete?ids=${standardItemValue.id}&standardItemId=${standardItemId}" onclick="return confirmx('确认要删除吗？', this.href)">删除</a>
					</c:if>
					<c:if test="${standardItemValue.delFlag == '1'}">
						<a href="${ctx}/standard/standardItemValueReback?ids=${standardItemValue.id}&?standardItemId=${standardItemId}" onclick="return confirmx('确认要恢复吗？', this.href)">恢复</a>
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
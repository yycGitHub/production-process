<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>审批管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/oa/testExpenses/">审批列表</a></li>
		<li class="active"><a href="${ctx}/oa/testExpenses/form/?procInsId=${testExpenses.procInsId}">审批详情</a></li>
	</ul>
	<form:form class="form-horizontal">
		<tags:message content="${message}"/>
		<fieldset>
			<legend>审批详情</legend>
			<table class="table-form">
				<tr>
					<td class="tit">姓名</td><td>${testExpenses.user.name}</td>
					<td class="tit">部门</td><td>${testExpenses.office.name}</td>
					<td class="tit">岗位职级</td><td>${testExpenses.post}</td>
				</tr>
				<tr>
					<td class="tit">报销说明</td>
					<td colspan="5">${testExpenses.content}</td>
				</tr>
				<tr>
					<td class="tit">报销费用</td>
					<td colspan="2">${testExpenses.addNum}</td>
				</tr>
				<tr>
					<td class="tit">人力资源部意见</td>
					<td colspan="5">
						${testExpenses.hrText}
					</td>
				</tr>
				<tr>
					<td class="tit">分管领导意见</td>
					<td colspan="5">
						${testExpenses.leadText}
					</td>
				</tr>
			</table>
		</fieldset>
		<tags:histoicFlow procInsId="${testExpenses.act.procInsId}" />
		<div class="form-actions">
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>

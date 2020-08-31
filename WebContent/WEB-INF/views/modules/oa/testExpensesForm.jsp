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
		<li class="active"><a href="${ctx}/oa/testExpenses/form?id=${testExpenses.id}"><shiro:hasPermission name="oa:testAudit:edit">审批${not empty testExpenses.id?'修改':'申请'}流程</shiro:hasPermission><shiro:lacksPermission name="oa:testAudit:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<form:form id="inputForm" modelAttribute="testExpenses" action="${ctx}/oa/testExpenses/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="act.taskId"/>
		<form:hidden path="act.taskName"/>
		<form:hidden path="act.taskDefKey"/>
		<form:hidden path="act.procInsId"/>
		<form:hidden path="act.procDefId"/>
		<form:hidden id="flag" path="act.flag"/>
		<tags:message content="${message}"/>
		<fieldset>
			<legend>费用报销</legend>
			<table class="table-form">
				<tr>
					<td class="tit">姓名</td>
					<td>
						<input id="userId" name="user.id" type="hidden" value="${testExpenses.user.id}"/>
						<input id="userName" name="userName" readonly="readonly" type="text" value="${user.name}" style="width: 150px;"/>
					</td>
<!-- 					<td class="tit">部门</td><td> -->
<%-- 						<tags:treeselect id="office" name="office.id" value="${testExpenses.office.id}" labelName="office.name" labelValue="${testExpenses.office.name}"  --%>
<%-- 							title="部门" url="/sys/office/treeData" cssClass="required recipient" cssStyle="width:150px"  --%>
<%-- 							allowClear="true" notAllowSelectParent="true"/> --%>
					</td><td class="tit">岗位职级</td><td>
						<form:input path="post" htmlEscape="false" maxlength="50"/>
					</td>
				</tr>
				<tr>
					<td class="tit">报销说明</td>
					<td colspan="5">
						<form:textarea path="content" class="required" rows="5" maxlength="200" cssStyle="width:500px"/>
					</td>
				</tr>
				<tr>
					<td class="tit">报销费用</td>
					<td colspan="2"><form:input path="addNum" htmlEscape="false" maxlength="50"/></td>
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
		<div class="form-actions">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="提交申请" onclick="$('#flag').val('yes')"/>&nbsp;
				<c:if test="${not empty testExpenses.id}">
					<input id="btnSubmit2" class="btn btn-inverse" type="submit" value="销毁申请" onclick="$('#flag').val('no')"/>&nbsp;
				</c:if>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
		<c:if test="${not empty testExpenses.id}">
			<tags:histoicFlow procInsId="${testExpenses.act.procInsId}" />
		</c:if>
	</form:form>
	<script type="text/javascript">	
	$("#userButton").click(function(){     
		// 正常打开	
		top.$.jBox.open("iframe:${ctx}/sys/user/upUserTable","选择员工",700, 520, {
			buttons:{"确定":"ok","关闭":true}, submit:function(v, h, f){
				if(v == "ok"){
					 var selectedUserId = h.find("iframe")[0].contentWindow.$("input[name='selectedUserId']:checked").val();
					 var selectedUserName = h.find("iframe")[0].contentWindow.$("input[name='selectedUserId']:checked").next().val();
					 $("#userId").val(selectedUserId);
					 $("#userName").val(selectedUserName);
					 return true;
				}else{
					 $("#userId").val("");
					 $("#userName").val("");
				}
			}, loaded:function(h){
				$(".jbox-content", top.document).css("overflow-y","hidden");
			}
		});
	});
	</script>
</body>
</html>

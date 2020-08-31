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
<div class="miandody">
	<div class="crumb">
		<i class="iconfont">&#xe60d;</i><strong>流程管理</strong>
		<span class="songti">&gt;</span><span>审批测试</span>
		<b class="fr"><a class="min_btn btn_success marright" href="javascript:history.go(-1)">返回</a></b>
	</div>
	<div class="con">
	<form:form id="inputForm" modelAttribute="testAudit" action="${ctx}/oa/testAudit/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="act.taskId"/>
		<form:hidden path="act.taskName"/>
		<form:hidden path="act.taskDefKey"/>
		<form:hidden path="act.procInsId"/>
		<form:hidden path="act.procDefId"/>
		<form:hidden id="flag" path="act.flag"/>
		<tags:message content="${message}"/>
		<div class="tab_door">
		<div class="tab_door_con">
		<div class="tab_door_show">
			<table class="tab" cellpadding="0" cellspacing="0">
			<tbody>
				<tr>
					<td class="w200" align="right">姓名</td>
					<td align="left" class="w300">
						<input id="userId" name="user.id" type="hidden" value="${testAudit.user.id}"/>
						<input id="userName" name="user.name" readonly="readonly" type="text" value="${testAudit.user.name}" class="input_txt w200"/>
						<input id="userButton" type="button" class="btn btn_secondary min_btn" value="选择">
					</td>
					<td class="w200" align="right">部门</td><td class="w300">
						<tags:treeselect id="office" name="office.id" value="${testAudit.office.id}" labelName="office.name" labelValue="${testAudit.office.name}" 
							title="部门" url="/sys/office/treeData" cssClass="input_txt required"
							allowClear="true" notAllowSelectParent="true"/>
					</td><td class="w200" align="right">岗位职级</td><td>
						<form:input path="post" htmlEscape="false" maxlength="50" class="input_txt w200" />
					</td>
				</tr>
				<tr>
					<td class="w200" align="right">调整原因</td>
					<td align="left" colspan="5">
						<form:textarea path="content" class="required area w_90 h50" rows="5" maxlength="200" />
					</td>
				</tr>
				<tr>
					<td class="w200" align="right" rowspan="3">调整原因</td>
					<td class="w200" align="right">薪酬档级</td>
					<td><form:input path="olda" htmlEscape="false" maxlength="50" class="input_txt w200"/></td>
					<td class="w200" align="right" rowspan="3">拟调整标准</td>
					<td class="w200" align="right">薪酬档级</td>
					<td><form:input path="newa" htmlEscape="false" maxlength="50" class="input_txt w200"/></td>
				</tr>
				<tr>
					<td class="w200" align="right">月工资额</td>
					<td><form:input path="oldb" htmlEscape="false" maxlength="50" class="input_txt w200"/></td>
					<td class="w200" align="right">月工资额</td>
					<td><form:input path="newb" htmlEscape="false" maxlength="50" class="input_txt w200"/></td>
				</tr>
				<tr>
					<td class="w200" align="right">年薪金额</td>
					<td><form:input path="oldc" htmlEscape="false" maxlength="50" class="input_txt w200"/></td>
					<td class="w200" align="right">年薪金额</td>
					<td><form:input path="newc" htmlEscape="false" maxlength="50" class="input_txt w200"/></td>
				</tr>
				<tr>
					<td class="w200" align="right">月增资</td>
					<td colspan="2" align="left"><form:input path="addNum" htmlEscape="false" maxlength="50" class="input_txt w200"/></td>
					<td class="w200" align="right">执行时间</td>
					<td colspan="2" align="left"><form:input path="exeDate" htmlEscape="false" maxlength="50" class="input_txt w200"/></td>
				</tr>
				<tr>
					<td class="w200" align="right">人力资源部意见</td>
					<td colspan="5">
						${testAudit.hrText}
					</td>
				</tr>
				<tr>
					<td class="w200" align="right">分管领导意见</td>
					<td colspan="5">
						${testAudit.leadText}
					</td>
				</tr>
				<tr>
					<td class="w200" align="right">集团主要领导意见</td>
					<td colspan="5">
						${testAudit.mainLeadText}
					</td>
				</tr>
			</tbody>
			</table>
		</div>
		</div>
		</div>
		<div class="tab_btn_show">
			<shiro:hasPermission name="oa:testAudit:edit">
				<input id="btnSubmit" class="btn btn_primary" type="submit" value="提交申请" onclick="$('#flag').val('yes')"/>&nbsp;
				<c:if test="${not empty testAudit.id}">
					<input id="btnSubmit2" class="btn btn_primary" type="submit" value="销毁申请" onclick="$('#flag').val('no')"/>&nbsp;
				</c:if>
			</shiro:hasPermission>
			<input id="btnCancel" class="btn btn_primary" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
		<c:if test="${not empty testAudit.id}">
			<tags:histoicFlow procInsId="${testAudit.act.procInsId}" />
		</c:if>
	</form:form>
	</div>
	</div>
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

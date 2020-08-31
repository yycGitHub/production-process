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
	<form:form class="form-horizontal">
		<tags:message content="${message}"/>
		<fieldset>
			<legend>审批详情</legend>
			<table class="table-form">
				<tr>
					<td class="tit">姓名</td><td>${testAudit.user.name}</td>
					<td class="tit">部门</td><td>${testAudit.office.name}</td>
					<td class="tit">岗位职级</td><td>${testAudit.post}</td>
				</tr>
				<tr>
					<td class="tit">调整原因</td>
					<td colspan="5">${testAudit.content}</td>
				</tr>
				<tr>
					<td class="tit" rowspan="3">调整原因</td>
					<td class="tit">薪酬档级</td>
					<td>${testAudit.olda}</td>
					<td class="tit" rowspan="3">拟调整标准</td>
					<td class="tit">薪酬档级</td>
					<td>${testAudit.newa}</td>
				</tr>
				<tr>
					<td class="tit">月工资额</td>
					<td>${testAudit.oldb}</td>
					<td class="tit">月工资额</td>
					<td>${testAudit.newb}</td>
				</tr>
				<tr>
					<td class="tit">年薪金额</td>
					<td>${testAudit.oldc}</td>
					<td class="tit">年薪金额</td>
					<td>${testAudit.newc}</td>
				</tr>
				<tr>
					<td class="tit">月增资</td>
					<td colspan="2">${testAudit.addNum}</td>
					<td class="tit">执行时间</td>
					<td colspan="2">${testAudit.exeDate}</td>
				</tr>
				<tr>
					<td class="tit">人力资源部意见</td>
					<td colspan="5">
						${testAudit.hrText}
					</td>
				</tr>
				<tr>
					<td class="tit">分管领导意见</td>
					<td colspan="5">
						${testAudit.leadText}
					</td>
				</tr>
				<tr>
					<td class="tit">集团主要领导意见</td>
					<td colspan="5">
						${testAudit.mainLeadText}
					</td>
				</tr>
			</table>
		</fieldset>
		<tags:histoicFlow procInsId="${testAudit.act.procInsId}" />
	</form:form>
	</div>
	</div>
	<div id="userModal" style="width:900px;height:600px;display:none;">
		<form id="contentTableForm" name="contentTableForm" action="" method="post">
	  	<div class="con">
	  	    <div class="tabcon">
		      <table class="tab" cellpadding="0" cellspacing="0">
		        <thead>
		          <tr>
		            <td>选择</td>
					<td>审核用户</td>
		          </tr>
		        </thead>
		        <c:forEach items="${acts}" var="act">
		        ${act.task.name}
		        <tbody>
		        		<c:forEach items="${act.candidateUser}" var="user">
						<tr>
							<td>
							<input type="checkbox" name="id" value="${user.id}"/>
							<input type="hidden" name="taskId" value="${act.task.id}"/>
							</td>
							<td>${user.loginName}</td>
						</tr>
					 	</c:forEach>
		        </tbody>
		        </c:forEach>
		      </table>
		     
		    </div>
		    <div class="tab_btn_show">
				<input id="btnSubmitSave" class="btn btn_primary" type="submit"	value="保 存" />&nbsp;
			</div>
		</div>
		</form>
	</div>
</body>
</html>

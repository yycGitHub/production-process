<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>项目组管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#inputForm").validate();
		});
	</script>
</head>
<body>
<div class="miandody">
	<div class="crumb">
		<i class="iconfont">&#xe60d;</i><strong></strong>
		<span class="songti">&gt;</span><span>项目组</span>
		<b class="fr"><a class="min_btn btn_success marright" href="javascript:history.go(-1)">返回</a></b>
	</div>
	<div class="con">
		<form:form id="inputForm" modelAttribute="group" action="${ctx}/sys/group/save" method="post" class="form-horizontal">
			<form:hidden path="groupId"/>
			<input type="hidden" name="rebackId" value="${rebackId}"/>
			<input type="hidden" name="id" value="${id}"/>
			<tags:message content="${message}"/>
			<div class="tab_door">
				<div class="tab_door_con">
					<div class="tab_door_show">
						<table class="tab" cellpadding="0" cellspacing="0">
							<tbody>
								
								<tr>
									<td align="right">上级组：</td>
									<td align="left">
											<tags:treeselect id="group" name="parent.groupId" value="${group.parent.groupId}" labelName="parent.name" labelValue="${group.parent.name}"
												title="机构" url="/sys/group/treeData" cssClass="input_txt w400"/>	
									</td>
								</tr>
								<tr>
									<td align="right">组名：</td>
									<td align="left"><form:input path="name" htmlEscape="false" cssClass="input_txt w400"/></td>
								</tr>
								<tr>
									<td align="right">备注：</td>
									<td align="left"><form:input path="remarks" htmlEscape="false" cssClass="input_txt w400"/></td>
								</tr>
							
							</tbody>
						</table>
					</div>
					<div class="tab_btn_show">
						<shiro:hasPermission name="sys:group:edit">
							<input id="btnSubmitSave" class="btn btn_primary" type="submit"	value="保 存" />&nbsp;
							</shiro:hasPermission>
					</div>
				</div>
			</div>
		</form:form>
	</div>
</div>

</body>
</html>

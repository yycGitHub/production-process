<#assign columnList = sourceTable.sourceColumnList>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${functionName}管理</title>
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
		<i class="iconfont">&#xe60d;</i><strong>${subModuleName}</strong>
		<span class="songti">&gt;</span><span>${functionName}</span>
		<b class="fr"><a class="min_btn btn_success marright" href="javascript:history.go(-1)">返回</a></b>
	</div>
	<div class="con">
		<form:form id="inputForm" modelAttribute="${className}" action="${r"${ctx}"}/${urlPrefix}/save" method="post" class="form-horizontal">
			<form:hidden path="${sourceTable.idKey}"/>
			<tags:message content="${r"${message}"}"/>
			<div class="tab_door">
				<div class="tab_door_con">
					<div class="tab_door_show">
						<table class="tab" cellpadding="0" cellspacing="0">
							<tbody>
							<#list columnList as column>
								<#if column.instance != sourceTable.idKey>
									<#if column.javaClass == 'Integer'>
								<tr>
									<td align="right">${column.logicalName!""}：</td>
									<td align="left"><form:input path="${column.instance}" htmlEscape="false" cssClass="input_txt w400 number"/></td>
								</tr>
								
									<#elseif column.javaClass == 'Date'>
								<tr>
									<td align="right">${column.logicalName!""}：</td>
									<td align="left">
										<input id="${column.instance}" name="${column.instance}" type="text" readonly="readonly" maxlength="20"  class="Wdate"   
											value="<fmt:formatDate value="${'${'+className+'.'+column.instance+'}'}" 
											pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});"/>
									</td>
								</tr>
								
									<#elseif column.physicalName == 'CLOB'>
								<tr>
									<td align="right">${column.logicalName!""}：</td>
									<td align="left">
										<textarea id="${column.instance}" name="${column.instance}" htmlEscape="false" class="area w_90 h50">${'${'+className+'.'+column.instance+'}'}</textarea>
									</td>
								</tr>
								
									<#elseif column.physicalName == 'BLOB'>
								<tr>
									<td align="right">${column.logicalName!""}：</td>
									<td align="left">
										<textarea id="${column.instance}" name="${column.instance}" htmlEscape="false" class="area w_90 h50">${'${'+className+'.'+column.instance+'String}'}</textarea>
									</td>
								</tr>
								
									<#else>
								<tr>
									<td align="right">${column.logicalName!""}：</td>
									<td align="left"><form:input path="${column.instance}" htmlEscape="false" cssClass="input_txt w400"/></td>
								</tr>
									</#if>
								</#if>
							</#list>
							
							</tbody>
						</table>
					</div>
					<div class="tab_btn_show">
						<shiro:hasPermission name="${permissionPrefix}:edit">
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

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
		<span class="songti">&gt;</span><span>${functionName}详情</span>
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
									<td align="left">${'${'+className+'.'+column.instance+'}'}</td>
								</tr>
								
									<#elseif column.javaClass == 'Date'>
								<tr>
									<td align="right">${column.logicalName!""}：</td>
									<td align="left">
										${'${'+className+'.'+column.instance+'}'}
									</td>
								</tr>
								
									<#elseif column.physicalName == 'CLOB'>
								<tr>
									<td align="right">${column.logicalName!""}：</td>
									<td align="left">
										${'${'+className+'.'+column.instance+'String}'}
									</td>
								</tr>
								
									<#elseif column.physicalName == 'BLOB'>
								<tr>
									<td align="right">${column.logicalName!""}：</td>
									<td align="left">
										${'${'+className+'.'+column.instance+'String}'}
									</td>
								</tr>
								
									<#else>
								<tr>
									<td align="right">${column.logicalName!""}：</td>
									<td align="left">${'${'+className+'.'+column.instance+'}'}</td>
								</tr>
									</#if>
								</#if>
							</#list>
							
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</form:form>
	</div>
</div>

</body>
</html>

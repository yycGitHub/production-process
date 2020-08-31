<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>接口管理</title>
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
		<span class="songti">&gt;</span><span>接口详情</span>
		<b class="fr"><a class="min_btn btn_success marright" href="javascript:history.go(-1)">返回</a></b>
	</div>
	<div class="con">
		<form:form id="inputForm" modelAttribute="apiInterface" action="${ctx}/api/apiInterface/save" method="post" class="form-horizontal">
			<form:hidden path="id"/>
			<tags:message content="${message}"/>
			<div class="tab_door">
				<div class="tab_door_con">
					<div class="tab_door_show">
						<table class="tab" cellpadding="0" cellspacing="0">
							<tbody>
								<tr>
									<td align="right">接口名称：</td>
									<td align="left">${apiInterface.name}</td>
								</tr>
								<tr>
									<td align="right">接口URL：</td>
									<td align="left">${apiInterface.url}</td>
								</tr>
								<tr>
									<td align="right">接口状态：</td>
									<td align="left">${apiInterface.status}</td>
								</tr>
								<tr>
									<td align="right">创建者：</td>
									<td align="left">${apiInterface.createBy.name}</td>
								</tr>
								<tr>
									<td align="right">创建时间：</td>
									<td align="left">${apiInterface.createDate}</td>
								</tr>
								<tr>
									<td align="right">更新者：</td>
									<td align="left">${apiInterface.updateBy.name}</td>
								</tr>
								<tr>
									<td align="right">更新时间：</td>
									<td align="left">${apiInterface.updateDate}</td>
								</tr>
								<tr>
									<td align="right">备注信息：</td>
									<td align="left">${apiInterface.remarks}</td>
								</tr>
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

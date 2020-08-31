<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>接口调用方注册管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#inputForm").validate();
			
			$("#btn").click(function(){
				top.$.jBox.confirm("重置密码？","系统提示",function(v,h,f){
					if(v == "ok"){
						 $.post("${ctx}/api/apiUser/reSetSecretCode",
							  {
							 	id:"${apiUser.id}"
							  },
							  function(data,status){
							    $("#secretCode").val(data);
							    $("#secretCodeSpan").html(data);
							  }
						  );
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
		});
	</script>
</head>
<body>
<div class="miandody">
	<div class="crumb">
		<i class="iconfont">&#xe60d;</i><strong></strong>
		<span class="songti">&gt;</span><span>接入方注册</span>
		<b class="fr"><a class="min_btn btn_success marright" href="javascript:history.go(-1)">返回</a></b>
	</div>
	<div class="con">
		<form:form id="inputForm" modelAttribute="apiUser" action="${ctx}/api/apiUser/save" method="post" class="form-horizontal">
			<form:hidden path="id"/>
			<tags:message content="${message}"/>
			<div class="tab_door">
				<div class="tab_door_con">
					<div class="tab_door_show">
						<table class="tab" cellpadding="0" cellspacing="0">
							<tbody>
								<tr>
									<td align="right">接入方名称：</td>
									<td align="left">
										<form:input path="name" htmlEscape="false" cssClass="input_txt w400"/>
									</td>
								</tr>
								<tr>
									<td align="right">appId：</td>
									<td align="left">
										<form:hidden path="appId"/>
										<span>${apiUser.appId}</span>
									</td>
								</tr>
								<tr>
									<td align="right">secretCode：</td>
									<td align="left">
										<form:hidden path="secretCode"/>
										<span id="secretCodeSpan">${apiUser.secretCode}</span>
										<input class="btn btn_primary" type="button" id="btn" value="重设" />
									</td>
								</tr>
								<tr>
									<td align="right">备注信息：</td>
									<td align="left"><form:input path="remarks" htmlEscape="false" cssClass="input_txt w400"/></td>
								</tr>
							</tbody>
						</table>
					</div>
					<div class="tab_btn_show">
						<shiro:hasPermission name="api:apiUser:edit">
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

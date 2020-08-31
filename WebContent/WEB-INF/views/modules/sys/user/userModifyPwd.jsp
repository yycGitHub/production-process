<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>修改密码</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#oldPassword").focus();
			$("#inputForm").validate({
				messages: {
					confirmNewPassword: {equalTo: "输入与上面相同的密码"}
				}
			});
		});
	</script>
</head>
<body>
<div class="miandody">
  <div class="crumb"><i class="iconfont">&#xe60d;</i><strong>修改密码</strong></div>
  <div class="con">
	<form:form id="inputForm" modelAttribute="user" action="${ctx}/sys/user/modifyPwd" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
	    <div class="list_edit">
		<dl>
			<dt>旧密码:</dt>
			<dd>
		         <input id="oldPassword" name="oldPassword" type="password" value="" maxlength="50" minlength="3" class="required input_txt w400"/>
			</dd>
		</dl>
		<dl>
			<dt>新密码:</dt>
			<dd>
				<input id="newPassword" name="newPassword" type="password" value="" maxlength="50" minlength="3" class="required input_txt w400"/>
			</dd>
		</dl>
	      <dl>
	        <dt>确认新密码：</dt>
	        <dd>
				<input id="confirmNewPassword" name="confirmNewPassword" type="password" 
					value="" maxlength="50" minlength="3" class="required input_txt w400" equalTo="#newPassword"/>
	        </dd>
	      </dl>
	      <dl>
	        <dt>&nbsp;</dt>
	        <dd>
	        	<shiro:hasPermission name="sys:user:edit">
	        		<input id="btnSubmit" class="btn btn_primary" type="submit" value="保 存"/>&nbsp;
	        	</shiro:hasPermission>
				<input id="btnCancel" class="btn btn_primary" type="button" value="返 回" onclick="history.go(-1)"/>
			</dd>
	      </dl>
    </div>
    </form:form>
  </div>
</div>
</body>
</html>
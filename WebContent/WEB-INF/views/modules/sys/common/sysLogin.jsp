<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="renderer" content="webkit">
<title>${fns:getConfig('productName')}</title> 
<link rel="shortcut icon" href="${ctxStatic}/favicon.ico" type="image/x-icon">
<script src="${ctxStatic}/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
<link href="${ctxStatic}/jquery-validation/1.11.1/jquery.validate.min.css" type="text/css" rel="stylesheet" />
<script src="${ctxStatic}/jquery-validation/1.11.1/jquery.validate.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-validation/1.11.1/jquery.validate.method.min.js" type="text/javascript"></script>
<link href="${ctxStatic}/zn/css/login.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript">
		$(document).ready(function() {
			$("#loginForm").validate({
				rules: {
					validateCode: {remote: "${pageContext.request.contextPath}/servlet/validateCodeServlet"}
				},
				messages: {
					username: {required: "请填写用户名."},
					password: {required: "请填写密码."},
					validateCode: {remote: "验证码不正确.", required: "请填写验证码."}
				},
				errorLabelContainer: "#messageBox",
				errorPlacement: function(error, element) {
					error.appendTo($("#loginError").parent());
				} 
			});
		});
		// 如果在框架中，则跳转刷新上级页面
		if(self.frameElement && self.frameElement.tagName=="IFRAME"){
			parent.location.reload();
		}
	</script>
</head>

<body class="bgF9f9f9">
<div class="loginLogo" align="center"></div>
<div class="t01" align="center">溯源管理平台</div>
<div class="box320">
	<%String error = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);%>
	<div id="messageBox" class="alert alert-error <%=error==null?"hide":""%>">
		<label id="loginError" class="error"><%=error==null?"":"com.sureserve.surekam.modules.sys.security.CaptchaException".equals(error)?"验证码错误, 请重试.":"用户或密码错误, 请重试." %></label>
	</div>
  <div class="w320 fl loginCon">
    <form id="inputForm"  class="form login-form" action="${ctx}/login" method="post">
    <input type="hidden" name="rememberMe" value="1">
    <dl>
	  <dt><b>用户/邮箱/手机号码</b><!-- <a href="reg.html">注册账号 <span class="songti">&gt;&gt;</span></a> --></dt>
	  <dd>
	   <input class="inputText w300 required" type="text" value="admin" id="username" name="username">
	  </dd>
	 <!-- <dd class="red">您输入的账号不存在</dd>-->
	  <dt><b>密码</b><!-- <a href="">忘记密码？</a> --></dt>
	  <dd>
	  <input type="password" id="password" name="password" value="surekam@12" class="inputText w300 required"/>
	  </dd>
	  <!--<dd class="red">您输入的密码错误</dd>-->
     <c:if test="${isValidateCodeLogin}">
      <dd>
			<label for="validateCode">验证码：</label>
			<tags:validateCode name="validateCode" inputCssStyle="margin-bottom:0;"/>
	  </dd>
	 </c:if>
	  <dd class="marTop10"><input class="loginBtn" name="" type="submit" value="立即登录" /></dd>
	</dl>
	</form>
  </div>
</div>
</body>
</html>
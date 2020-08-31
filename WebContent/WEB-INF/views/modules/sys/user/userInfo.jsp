<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>个人信息</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
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
  	<i class="iconfont">&#xe60d;</i><strong>个人信息</strong>
  </div>
  <div class="con">
  <form:form id="inputForm" modelAttribute="user" action="${ctx}/sys/user/info" method="post" class="form-horizontal">
    <tags:message content="${message}"/>
    <div class="list_edit">
	<dl>
		<dt>归属公司:</dt>
		<dd>
	        ${user.company.name}
		</dd>
	</dl>
	<dl>
		<dt>归属部门:</dt>
		<dd>
			${user.office.name}
		</dd>
	</dl>
     <dl>
       <dt>姓名：</dt>
       <dd>
		<form:input path="name" htmlEscape="false" maxlength="50" class="required input_txt w400"/>
       </dd>
     </dl>
      <dl>
        <dt>邮箱:</dt>
        <dd>
        	<form:input path="email" htmlEscape="false" maxlength="50" class="input_txt w400"/>
        </dd>
      </dl>
      <dl>
        <dt>用户类型:</dt>
        <dd>
        	<label>${fns:getDictLabel(user.userType, 'sys_user_type', '无')}</label>
        </dd>
      </dl>
     <dl>
        <dt>用户角色:</dt>
        <dd>
          <label>${user.roleNames}</label>
        </dd>
      </dl>
      <dl>
        <dt>最后登录时间:</dt>
        <dd>
          <label class="lbl">IP: ${user.loginIp}&nbsp;&nbsp;&nbsp;&nbsp;时间：
          	<fmt:formatDate value="${user.loginDate}" type="both" dateStyle="full"/>
          </label>
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
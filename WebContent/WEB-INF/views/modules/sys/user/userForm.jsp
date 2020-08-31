<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treeview.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#loginName").focus();
			$("#inputForm").validate({
				errorElement: 'div',
				errorClass: 'help-block',
				focusInvalid: false,
				ignore: "",
				rules: {
					loginName: {remote: "${ctx}/sys/user/checkLoginName?oldLoginName=" + encodeURIComponent('${user.loginName}')}
				},
				messages: {
					loginName: {remote: "角色名已存在"},
					confirmNewPassword: {equalTo: "输入与上面相同的密码"},
					roleIdList: {required: "必须选择一个角色"}
				},
				highlight: function (e) {
					$(e).closest('.form-group').removeClass('has-info').addClass('has-error');
				},
				success: function (e) {
					$(e).closest('.form-group').removeClass('has-error');//.addClass('has-info');
					$(e).remove();
				},
				errorPlacement: function (error, element) {
					if(element.is('input[type=checkbox]') || element.is('input[type=radio]')) {
						var controls = element.closest('div[class*="col-"]');
						if(controls.find(':checkbox,:radio').length > 1) controls.append(error);
						else error.insertAfter(element.nextAll('.lbl:eq(0)').eq(0));
					}
					else if(element.is('.select2')) {
						error.insertAfter(element.siblings('[class*="select2-container"]:eq(0)'));
					}
					else if(element.is('.chosen-select')) {
						error.insertAfter(element.siblings('[class*="chosen-container"]:eq(0)'));
					}
					else error.insertAfter(element.parent());
				}
			});
		});
	</script>
</head>
<div class="main-container" id="main-container">
	<div class="main-content-inner">
		<div class="main-content">
			<div class="page-content">
				<tags:message content="${message}"/>
				<div class="row">
					<form:form id="inputForm" modelAttribute="user" action="${ctx}/sys/user/save" method="post" class="form-horizontal">
						<div class="col-xs-12">
							<form:hidden path="id"/>
							<tags:message content="${message}"/>
							<div class="form-group">
								<label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 归属公司:</label>
								<div class="col-sm-9">
									<tags:treeselect id="company" name="company.id" value="${user.company.id}" labelName="company.name" labelValue="${user.company.name}"
									title="公司" url="/sys/office/treeData?type=1" cssClass="col-xs-10 col-sm-5"/>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 归属部门:</label>
								<div class="col-sm-9">
									<div class="clearfix">
										 <tags:treeselect id="office" name="office.id" value="${user.office.id}" labelName="office.name" labelValue="${user.office.name}"
										 title="部门" url="/sys/office/treeData?type=2" cssClass="col-xs-10 col-sm-5"/>
									</div>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 登录名：</label>
								<div class="col-sm-9">
									<div class="clearfix">
									<input id="oldLoginName" name="oldLoginName" type="hidden" value="${user.loginName}">
									<form:input path="loginName" htmlEscape="false" maxlength="50" class="required col-xs-10 col-sm-5"/>
									</div>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 姓名：</label>
								<div class="col-sm-9">
									<div class="clearfix">
									<form:input path="name" htmlEscape="false" maxlength="50" class="required col-xs-10 col-sm-5"/>
									</div>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 密码：</label>
								<div class="col-sm-9">
									<div class="clearfix">
									<input id="newPassword" name="newPassword" type="password" maxlength="50" class="col-xs-10 col-sm-5 ${empty user.id?'required':''}"/>
									</div>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 确认密码:</label>
								<div class="col-sm-9">
									<div class="clearfix">
									<input name="confirmNewPassword" type="password" maxlength="50" class="col-xs-10 col-sm-5" equalTo="#newPassword"/>
									</div>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 邮箱:</label>
								<div class="col-sm-9">
									<form:input path="email" htmlEscape="false" maxlength="50" class="col-xs-10 col-sm-5"/>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 用户类型:</label>
								<div class="col-sm-9">
									<form:select path="userType" class="col-xs-10 col-sm-5">
									<form:option value="" label="请选择"/>
									<form:options items="${fns:getDictList('sys_user_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
									</form:select>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 用户角色:</label>
								<div class="col-sm-9">
<%-- 									<form:checkboxes path="roleIdList" items="${allRoles}" itemLabel="name" itemValue="id" class="" htmlEscape="false"/> --%>
									<div class="checkbox">
										<c:forEach items="${allRoles}" var="role">
										<label>
											<c:set var="isHas" value="false" />
											<c:forEach items="${user.roleIdList}" var="hasRole">
												<c:if test = "${hasRole eq role.id}">
													<c:set var="isHas" value="true" />
												</c:if>
											</c:forEach>
												<c:if test = "${isHas eq 'true'}">
												<input name="roleIdList" type="checkbox" class="required ace" checked=checked value="${role.id}"/>
											    </c:if>
											    <c:if test = "${isHas eq 'false'}">
												<input name="roleIdList" type="checkbox" class="required ace" value="${role.id}"/>
											    </c:if>
											<span class="lbl"> ${role.name}</span>
										</label>
										</c:forEach>
									</div>
								</div>
							</div>
							<c:if test="${not empty user.id}">
								<div class="form-group">
									<label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 创建时间:</label>
									<div class="col-sm-9">
									<label class="control-label"><fmt:formatDate value="${user.createDate}" type="both" dateStyle="full"/></label>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 最后登陆:</label>
									<div class="col-sm-9">
										<label class="control-label">IP: ${user.loginIp}&nbsp;&nbsp;&nbsp;&nbsp;时间：<fmt:formatDate value="${user.loginDate}" type="both" dateStyle="full"/></label>
									</div>
								</div>
							</c:if>
							<shiro:hasPermission name="sys:role:edit">
								<div class="clearfix form-actions">
									<div class="col-md-offset-3 col-md-9">
									<button id="btnSubmit" class="btn btn-info" type="submit">
										<i class="ace-icon fa fa-check bigger-110"></i>
										保存
									</button>
									&nbsp; &nbsp; &nbsp;
									<button type="reset" class="btn" onclick="history.go(-1)">
										<i class="ace-icon fa fa-reply bigger-110"></i>
										返回
									</button>
									</div>
								</div>
							</shiro:hasPermission>
						</div>
					</form:form>
				</div>
			</div>
		</div>
	</div>
</div>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>中南大学学工系统</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#value").focus();
			$("#inputForm").validate();
		});
	</script>
</head>
<body>
<div class="main-container" id="main-container">
	<div class="main-container-inner">
		<div class="main-content">
			<div class="page-content">
			
				<div class="row">
					<div class="col-xs-12">
					<div class="hr dotted hr-double"></div>
					<form:form id="inputForm" modelAttribute="dict" action="${ctx}/sys/dict/save" method="post" class="form-horizontal">
						<form:hidden path="id"/>
						<tags:message content="${message}"/>
						<div class="form-group">
							<label class="col-sm-3 control-label no-padding-right" for="form-field-1">键值：</label>
							<div class="col-sm-9">
								<form:input path="value" htmlEscape="false" maxlength="50" class="col-xs-10 col-sm-5"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label no-padding-right" for="form-field-1">标签：</label>
							<div class="col-sm-9">
								<form:input path="label" htmlEscape="false" maxlength="50" class="col-xs-10 col-sm-5"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label no-padding-right" for="form-field-1">类型：</label>
							<div class="col-sm-9">
								<form:input path="type" htmlEscape="false" maxlength="50" class="col-xs-10 col-sm-5"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label no-padding-right" for="form-field-1">描述:</label>
							<div class="col-sm-9">
								<form:input path="description" htmlEscape="false" maxlength="50" class="col-xs-10 col-sm-5"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label no-padding-right" for="form-field-1">排序:</label>
							<div class="col-sm-9">
								<form:input path="sort" htmlEscape="false" maxlength="50" class="col-xs-10 col-sm-5"/>
							</div>
						</div>
						<div class="clearfix form-actions">
							<div class="col-md-offset-3 col-md-9">
								<shiro:hasPermission name="sys:dict:edit">
								<button id="btnSubmit" class="btn btn-info" type="submit">
									<i class="ace-icon fa fa-check bigger-110"></i>
									保 存
								</button>
								&nbsp; &nbsp; &nbsp;
								</shiro:hasPermission>
								<button id="btnCancel" class="btn" type="button" onclick="history.go(-1)">
									<i class="ace-icon fa fa-reply bigger-110"></i>
									返 回
								</button>
							</div>
						</div>
					</form:form>


					</div><!-- /span -->
				</div><!-- /row -->

			</div><!-- /.page-content -->
		</div><!-- /.main-content -->
	</div><!-- /.main-container-inner -->
</div><!-- /.main-container -->
</body>
</html>
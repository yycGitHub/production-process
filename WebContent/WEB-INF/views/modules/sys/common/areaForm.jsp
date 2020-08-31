<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>区域管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
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
					<form:form id="inputForm" modelAttribute="area" action="${ctx}/sys/area/save" method="post" class="form-horizontal">
						<form:hidden path="id"/>
						<tags:message content="${message}"/>
						<div class="form-group">
							<label class="col-sm-3 control-label no-padding-right" for="form-field-1">上级区域:</label>
							<div class="col-sm-9">
								<tags:treeselect id="area" name="parent.id" value="${area.parent.id}" labelName="parent.name" labelValue="${area.parent.name}"
								title="区域" url="/sys/area/treeData" extId="${area.id}" cssClass="col-xs-10 col-sm-5 required"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label no-padding-right" for="form-field-1">区域名称:</label>
							<div class="col-sm-9">
								<form:input path="name" htmlEscape="false" maxlength="50" cssClass="col-xs-10 col-sm-5 required"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label no-padding-right" for="form-field-1">区域编码:</label>
							<div class="col-sm-9">
								<form:input path="code" htmlEscape="false" maxlength="50" cssClass="col-xs-10 col-sm-5"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label no-padding-right" for="form-field-1">区域类型:</label>
							<div class="col-sm-9">
								<form:select path="type" class="col-xs-10 col-sm-5">
									<form:options items="${fns:getDictList('sys_area_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
								</form:select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label no-padding-right" for="form-field-1">备注：</label>
							<div class="col-sm-9">
								<textarea name="remarks" htmlEscape="false" class="form-control">${area.remarks }</textarea>
							</div>
						</div>
						<div class="clearfix form-actions">
							<div class="col-md-offset-3 col-md-9">
								<shiro:hasPermission name="sys:area:edit">
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
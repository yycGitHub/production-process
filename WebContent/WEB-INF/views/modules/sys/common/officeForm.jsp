<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>中南大学学工系统</title>
	<script src="${ctxStatic}/assets/js/jquery-2.1.4.min.js"></script>
	<script src="${ctxStatic}/assets/js/jquery-validation/1.11.1/jquery.validate.min.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate();
			
			$("#goBack").click(function(){
				$("#officeDiv").load("${ctx}/sys/office/list?id=${rebackId}", function(response) {
					  $('#officeDiv').html(response);
					});
			});
			
			
		});
		
		function submitDate(){
			jQuery.ajax({
				url: '${ctx}/sys/office/save',
				data: $('#inputForm').serialize(),
				type: "POST",
				dataType: 'json',
				beforeSend: function(){
				//在异步提交前要做的操作
				},
				success: function(data){
					$("#officeDiv").load("${ctx}/sys/office/list?id="+data.bodyData.rebackId+"&message='"+data.message+"'", function(response) {
						  $('#officeDiv').html(response);
						});
				}
			});
			return false;
		}
	</script>
</head>
<body>
<form:form id="inputForm" modelAttribute="office" action="${ctx}/sys/office/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<input type="hidden" name="rebackId" value="${rebackId}"/>
		<input type="hidden" name="grade" value="1"/>
		<tags:message content="${message}"/>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right" for="form-field-1">上级机构：</label>
			<div class="col-sm-9">
				<tags:treeselect id="office" name="parent.id" value="${office.parent.id}" labelName="parent.name" labelValue="${office.parent.name}"
					title="机构" url="/sys/office/treeData" cssClass="col-xs-10 col-sm-5"/>	
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right" for="form-field-1">归属区域：</label>
			<div class="col-sm-9">
				<tags:treeselect id="area" name="area.id" value="${office.area.id}" labelName="area.name" labelValue="${office.area.name}"
					title="区域" url="/sys/area/treeData" cssClass="col-xs-10 col-sm-5"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right" for="form-field-1">机构名称:</label>
			<div class="col-sm-9">
				<form:input path="name" htmlEscape="false" maxlength="50" cssClass="col-xs-10 col-sm-5"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right" for="form-field-1">机构编码：</label>
			<div class="col-sm-9">
				<form:input path="code" htmlEscape="false" maxlength="50" cssClass="col-xs-10 col-sm-5"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right" for="form-field-1">机构类型：</label>
			<div class="col-sm-9">
				<form:select path="type" class="multiselect">
					<form:options items="${fns:getDictList('sys_office_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right" for="form-field-1">联系地址：</label>
			<div class="col-sm-9">
				<form:input path="address" htmlEscape="false" maxlength="50" cssClass="col-xs-10 col-sm-5"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right" for="form-field-1">负责人：</label>
			<div class="col-sm-9">
				<form:input path="master" htmlEscape="false" maxlength="50" cssClass="col-xs-10 col-sm-5"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right" for="form-field-1">电话：</label>
			<div class="col-sm-9">
				<form:input path="phone" htmlEscape="false" maxlength="50" cssClass="col-xs-10 col-sm-5"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right" for="form-field-1">备注：</label>
			<div class="col-sm-9">
				<form:textarea class="area" path="remarks" cssClass="form-control"></form:textarea>
			</div>
		</div>
		<div class="clearfix form-actions">
			<div class="col-md-offset-3 col-md-9">
				<shiro:hasPermission name="sys:user:edit">
				<button id="btnSubmit" class="btn btn-info" type="button" onclick="submitDate();">
					<i class="ace-icon fa fa-check bigger-110"></i>
					保 存
				</button>
				&nbsp; &nbsp; &nbsp;
				</shiro:hasPermission>
				<button id="goBack" class="btn" type="button">
					<i class="ace-icon fa fa-reply bigger-110"></i>
					返 回
				</button>
			</div>
		</div>
	</form:form>

</body>
</html>
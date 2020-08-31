<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>表单管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate();
		});
	</script>
</head>
<body>
<div class="miandody">
  <div class="crumb">
		<i class="iconfont">&#xe60d;</i>
		<strong>表单管理</strong><span class="songti">&gt;</span>
		<strong><a href="${ctx}/standard/standardItemList?stId=${standardItem.targetId}">参数列表</a></strong><span class="songti">&gt;</span>
		<span>参数</span>
  </div>
  <div class="con">
  <form:form id="inputForm" modelAttribute="standardItem" action="${ctx}/standard/saveStandardItem" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="targetId"/>
		<tags:message content="${message}"/>
	    <div class="list_edit">
		<dl>
			<dt>名称:</dt>
			<dd>
		        <form:input path="name" htmlEscape="false" maxlength="50" class="input_txt w400 required"/>
			</dd>
		</dl>
		<dl>
			<dt>字段名:</dt>
			<dd>
		         <form:input path="keyName" htmlEscape="false" maxlength="50" class="input_txt w400 required"/>
			</dd>
		</dl>
		<dl>
			<dt>必填:</dt>
			<dd>
		         <form:select path="isRequired" cssClass="drop_down gray w200">
					<form:option value="1">是</form:option>
					<form:option value="0">否</form:option>
				</form:select>
			</dd>
		</dl>
		<dl>
			<dt>排序:</dt>
			<dd>
		         <form:input path="sort" htmlEscape="false" maxlength="50" class="input_txt w400 required digits"/>
			</dd>
		</dl>
		<dl>
			<dt>类型:</dt>
			<dd>
		         <form:select path="itemType" cssClass="drop_down gray w200">
					<form:options items="${fns:getDictList('sys_standard')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</dd>
		</dl>
		<dl>
			<dt>限制类型:</dt>
			<dd>
		        <form:select path="itemLimitType" cssClass="drop_down gray w200">
					<form:options items="${fns:getDictList('sys_standard_item_Limit_Type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</dd>
		</dl>
		<dl>
			<dt>参考范围:</dt>
			<dd>
		        <form:input path="minValue" htmlEscape="false" maxlength="64" class="input_txt w100 digits"/>至
				<form:input path="maxValue" htmlEscape="false" maxlength="64" class="input_txt w100 digits"/>
			</dd>
		</dl>
		<dl>
			<dt>单位:</dt>
			<dd>
		         <form:input path="itemUnit" htmlEscape="false" maxlength="50" class="input_txt w400 required"/>
			</dd>
		</dl>
		<dl>
			<dt>说明:</dt>
			<dd>
		         <form:textarea path="remarks" htmlEscape="false" maxlength="512" cssClass="w400"/>
			</dd>
		</dl>
	    <dl>
	        <dt>&nbsp;</dt>
	        <dd>
	        	<input id="btnSubmit" class="btn btn_primary" type="submit" value="保 存"/>&nbsp;
				<input id="btnCancel" class="btn btn_primary" type="button" value="返 回" onclick="history.go(-1)"/>
			</dd>
	      </dl>
	    </div>
    </form:form>
  </div>
</div>
</body>
</html>
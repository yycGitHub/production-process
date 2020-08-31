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
		<strong><a href="${ctx}/standard/standardItemValueList?standardItemId=${standardItemValue.standardItem.id}">参数列表</a>
		</strong><span class="songti">&gt;</span>
		<span>参数</span>
  </div>
  <div class="con">
 <form:form id="inputForm" modelAttribute="standardItemValue" 
 	action="${ctx}/standard/saveStandardItemValue" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="standardItem.id"/>
		<tags:message content="${message}"/>
	    <div class="list_edit">
		<dl>
			<dt>名称:</dt>
			<dd>
		        <form:input path="name" htmlEscape="false" maxlength="50" class="input_txt w400 required"/>
			</dd>
		</dl>
		<dl>
			<dt>值:</dt>
			<dd>
		         <form:input path="value" htmlEscape="false" maxlength="50" class="input_txt w400 required"/>
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
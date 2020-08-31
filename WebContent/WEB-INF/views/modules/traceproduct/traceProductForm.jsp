<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>溯源产品管理管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#inputForm").validate();
		});
	</script>
</head>
<body>
<div class="miandody">
	<div class="crumb">
		<i class="iconfont">&#xe60d;</i><strong></strong>
		<span class="songti">&gt;</span><span>溯源产品管理</span>
		<b class="fr"><a class="min_btn btn_success marright" href="javascript:history.go(-1)">返回</a></b>
	</div>
	<div class="con">
		<form:form id="inputForm" modelAttribute="traceProduct" action="${ctx}/traceproduct/traceProduct/save" method="post" class="form-horizontal">
			<form:hidden path="id"/>
			<tags:message content="${message}"/>
			<div class="tab_door">
				<div class="tab_door_con">
					<div class="tab_door_show">
						<table class="tab" cellpadding="0" cellspacing="0">
							<tbody>
								<tr>
									<td align="right">企业编号：</td>
									<td align="left"><form:input path="officeId" htmlEscape="false" cssClass="input_txt w400"/></td>
								</tr>
								<tr>
									<td align="right">溯源产品编号：</td>
									<td align="left"><form:input path="productCode" htmlEscape="false" cssClass="input_txt w400"/></td>
								</tr>
								<tr>
									<td align="right">溯源产品名称：</td>
									<td align="left"><form:input path="productName" htmlEscape="false" cssClass="input_txt w400"/></td>
								</tr>
								<tr>
									<td align="right">溯源产品标题：</td>
									<td align="left"><form:input path="productTitle" htmlEscape="false" cssClass="input_txt w400"/></td>
								</tr>
								<tr>
									<td align="right">显示类型 1模块  2时间轴：</td>
									<td align="left"><form:input path="showType" htmlEscape="false" cssClass="input_txt w400"/></td>
								</tr>
								<tr>
									<td align="right">溯源主题编号：</td>
									<td align="left"><form:input path="themeId" htmlEscape="false" cssClass="input_txt w400"/></td>
								</tr>
								<tr>
									<td align="right">产品图片：</td>
									<td align="left"><form:input path="productPic" htmlEscape="false" cssClass="input_txt w400"/></td>
								</tr>
								<tr>
									<td align="right">：</td>
									<td align="left"><form:input path="productDiscription" htmlEscape="false" cssClass="input_txt w400"/></td>
								</tr>
								<tr>
									<td align="right">溯源标签申请审核开关  0关闭  1开启：</td>
									<td align="left"><form:input path="lableAuditFlag" htmlEscape="false" cssClass="input_txt w400"/></td>
								</tr>
								<tr>
									<td align="right">排序（升序）默认值统一设置为99，按照升序排列：</td>
									<td align="left"><form:input path="sort" htmlEscape="false" cssClass="input_txt w400"/></td>
								</tr>
								<tr>
									<td align="right">是否启用：</td>
									<td align="left"><form:input path="status" htmlEscape="false" cssClass="input_txt w400"/></td>
								</tr>
								<tr>
									<td align="right">首次记录，并且以后不会进行修改：</td>
									<td align="left"><form:input path="creatTime" htmlEscape="false" cssClass="input_txt w400"/></td>
								</tr>
								<tr>
									<td align="right">创建人标识：</td>
									<td align="left"><form:input path="creatUserid" htmlEscape="false" cssClass="input_txt w400"/></td>
								</tr>
								<tr>
									<td align="right">每次对数据进行操作，都要对此字段进行修改：</td>
									<td align="left"><form:input path="updateTime" htmlEscape="false" cssClass="input_txt w400"/></td>
								</tr>
								<tr>
									<td align="right">更新人标识：</td>
									<td align="left"><form:input path="updateUserid" htmlEscape="false" cssClass="input_txt w400"/></td>
								</tr>
								<tr>
									<td align="right">操作状态：A-新增，U-修改，D-删除：</td>
									<td align="left"><form:input path="states" htmlEscape="false" cssClass="input_txt w400"/></td>
								</tr>
							
							</tbody>
						</table>
					</div>
					<div class="tab_btn_show">
						<shiro:hasPermission name="traceproduct:traceProduct:edit">
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

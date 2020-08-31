<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>溯源码管理</title>
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
		<span class="songti">&gt;</span><span>溯源码</span>
		<b class="fr"><a class="min_btn btn_success marright" href="javascript:history.go(-1)">返回</a></b>
	</div>
	<div class="con">
		<form:form id="inputForm" modelAttribute="traceCode" action="${ctx}/tracecode/traceCode/save" method="post" class="form-horizontal">
			<form:hidden path="id"/>
			<tags:message content="${message}"/>
			<div class="tab_door">
				<div class="tab_door_con">
					<div class="tab_door_show">
						<table class="tab" cellpadding="0" cellspacing="0">
							<tbody>
								<tr>
									<td align="right">溯源码（打印时创建溯源码信息）：</td>
									<td align="left"><form:input path="traceCode" htmlEscape="false" cssClass="input_txt w400"/></td>
								</tr>
								<tr>
									<td align="right">标签申请批次：</td>
									<td align="left"><form:input path="applyId" htmlEscape="false" cssClass="input_txt w400"/></td>
								</tr>
								<tr>
									<td align="right">外部系统编号（接口型的系统编号才有数据）：</td>
									<td align="left"><form:input path="sysId" htmlEscape="false" cssClass="input_txt w400"/></td>
								</tr>
								<tr>
									<td align="right">产品批次号（接口型在调用接口返回数据后需回写批次号）：</td>
									<td align="left"><form:input path="batchId" htmlEscape="false" cssClass="input_txt w400"/></td>
								</tr>
								<tr>
									<td align="right">溯源类型 1配置型  2根据二维码调接口  3根据批次号调接口：</td>
									<td align="left"><form:input path="traceType" htmlEscape="false" cssClass="input_txt w400"/></td>
								</tr>
								<tr>
									<td align="right">扫码次数：</td>
									<td align="left"><form:input path="traceCount" htmlEscape="false" cssClass="input_txt w400"/></td>
								</tr>
								<tr>
									<td align="right">包装类型：</td>
									<td align="left"><form:input path="packType" htmlEscape="false" cssClass="input_txt w400"/></td>
								</tr>
								<tr>
									<td align="right">防伪码(预留字段)：</td>
									<td align="left"><form:input path="antiFakeCode" htmlEscape="false" cssClass="input_txt w400"/></td>
								</tr>
								<tr>
									<td align="right">溯源码状态   1已打印  2已激活：</td>
									<td align="left"><form:input path="status" htmlEscape="false" cssClass="input_txt w400"/></td>
								</tr>
								<tr>
									<td align="right">打印日期：</td>
									<td align="left">
										<input id="printDate" name="printDate" type="text" readonly="readonly" maxlength="20"  class="Wdate"   
											value="<fmt:formatDate value="${traceCode.printDate}" 
											pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});"/>
									</td>
								</tr>
								
								<tr>
									<td align="right">激活日期：</td>
									<td align="left">
										<input id="activationDate" name="activationDate" type="text" readonly="readonly" maxlength="20"  class="Wdate"   
											value="<fmt:formatDate value="${traceCode.activationDate}" 
											pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});"/>
									</td>
								</tr>
								
								<tr>
									<td align="right">激活人：</td>
									<td align="left"><form:input path="activator" htmlEscape="false" cssClass="input_txt w400"/></td>
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
									<td align="right">每次对数据进行操作，都需要将此字段修改：</td>
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
						<shiro:hasPermission name="tracecode:traceCode:edit">
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

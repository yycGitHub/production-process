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
		<span class="songti">&gt;</span><span>溯源码详情</span>
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
									<td align="left">${traceCode.traceCode}</td>
								</tr>
								<tr>
									<td align="right">标签申请批次：</td>
									<td align="left">${traceCode.applyId}</td>
								</tr>
								<tr>
									<td align="right">外部系统编号（接口型的系统编号才有数据）：</td>
									<td align="left">${traceCode.sysId}</td>
								</tr>
								<tr>
									<td align="right">产品批次号（接口型在调用接口返回数据后需回写批次号）：</td>
									<td align="left">${traceCode.batchId}</td>
								</tr>
								<tr>
									<td align="right">溯源类型 1配置型  2根据二维码调接口  3根据批次号调接口：</td>
									<td align="left">${traceCode.traceType}</td>
								</tr>
								<tr>
									<td align="right">扫码次数：</td>
									<td align="left">${traceCode.traceCount}</td>
								</tr>
								<tr>
									<td align="right">包装类型：</td>
									<td align="left">${traceCode.packType}</td>
								</tr>
								<tr>
									<td align="right">防伪码(预留字段)：</td>
									<td align="left">${traceCode.antiFakeCode}</td>
								</tr>
								<tr>
									<td align="right">溯源码状态   1已打印  2已激活：</td>
									<td align="left">${traceCode.status}</td>
								</tr>
								<tr>
									<td align="right">打印日期：</td>
									<td align="left">
										${traceCode.printDate}
									</td>
								</tr>
								
								<tr>
									<td align="right">激活日期：</td>
									<td align="left">
										${traceCode.activationDate}
									</td>
								</tr>
								
								<tr>
									<td align="right">激活人：</td>
									<td align="left">${traceCode.activator}</td>
								</tr>
								<tr>
									<td align="right">首次记录，并且以后不会进行修改：</td>
									<td align="left">${traceCode.creatTime}</td>
								</tr>
								<tr>
									<td align="right">创建人标识：</td>
									<td align="left">${traceCode.creatUserid}</td>
								</tr>
								<tr>
									<td align="right">每次对数据进行操作，都需要将此字段修改：</td>
									<td align="left">${traceCode.updateTime}</td>
								</tr>
								<tr>
									<td align="right">更新人标识：</td>
									<td align="left">${traceCode.updateUserid}</td>
								</tr>
								<tr>
									<td align="right">操作状态：A-新增，U-修改，D-删除：</td>
									<td align="left">${traceCode.states}</td>
								</tr>
							
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</form:form>
	</div>
</div>

</body>
</html>

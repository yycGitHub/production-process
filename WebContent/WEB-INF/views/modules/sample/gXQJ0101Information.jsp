<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>请假单管理</title>
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
		<i class="iconfont">&#xe60d;</i><strong>请假单管理</strong>
		<span class="songti">&gt;</span><span>请假单详情</span>
		<b class="fr"><a class="min_btn btn_success marright" href="javascript:history.go(-1)">返回</a></b>
	</div>
	<div class="con">
		<form:form id="inputForm" modelAttribute="gXQJ0101" action="${ctx}/sample/gXQJ0101/save" method="post" class="form-horizontal">
			<form:hidden path="gxqj0101Id"/>
			<tags:message content="${message}"/>
			<div class="tab_door">
				<div class="tab_door_con">
					<div class="tab_door_show">
						<table class="tab" cellpadding="0" cellspacing="0">
							<tbody>
								<tbody>
							 	<tr>
				                  <td class="w200" align="right">学号：</td>
				                  <td align="left" class="w400">
				                  	${gXQJ0101.xh }
				                  </td>
				                  <td class="w200"  align="right">姓名：</td>
				                  <td align="left">${gXQJ0101.xm }</td>
				                </tr>
								<tr>
									<td align="right">请假单标题：</td>
									<td align="left">${gXQJ0101.qjdbt }</td>
									<td align="right">请假类别：</td>
									<td align="left">
										${fns:getDictLabel(gXQJ0101.qjlb, 'XG_STUDENTVACATION_QJLB', '无')}
									</td>
								</tr>
								<tr>
									<td align="right">请假事由：</td>
									<td align="left" colspan="3">
										${gXQJ0101.qjsyString }
									</td>
								</tr>
								<tr>
									<td align="right">请假开始日期：</td>
									<td align="left">
										${fns:getDateString(gXQJ0101.qjksrq)}&nbsp;&nbsp;${fns:getTimeString(gXQJ0101.qjkssj)}
									</td>
									<td align="right">请假结束日期：</td>
									<td align="left">
										${fns:getDateString(gXQJ0101.qjjsrq)}&nbsp;&nbsp;${fns:getTimeString(gXQJ0101.qjjssj)}
									</td>
								</tr>
								<tr>
									<td align="right">请假总时长（天）：</td>
									<td align="left">${gXQJ0101.qjzsc }</td>
									<td align="right">实际请假总时长（天）：</td>
									<td align="left">${gXQJ0101.sjqjzsj }</td>
								</tr>
								<tr>
									<td align="right">请假单状态：</td>
									<td align="left">${fns:getDictLabel(gXQJ0101.zt, 'XG_STUDENTVACATION_ZT', '无')}</td>
									<td align="right">是否为事后补假：</td>
									<td align="left">${fns:getDictLabel(gXQJ0101.sfbl, 'physicalhealth_sf', '无')}</td>
								</tr>
								<tr>
									<td align="right">备注：</td>
									<td align="left" colspan="3">
										${gXQJ0101.bzString }
									</td>
								</tr>
								<c:if test="${not empty fileList}">
									<tr id="fj">
						                  <td align="right">附件：</td>
						                  <td colspan="3" align="left">
							                  	<c:forEach items="${fileList}" var="fileInfo">
							                  		<div id='${fileInfo.id}'>
							                  			<a href="${ctx}/common/downloadFile?id=${fileInfo.id}">${fileInfo.fileName}</a><br/>
							                   		</div>
							                    </c:forEach>  
						                  </td>
				            	    </tr>
			            	    </c:if>
								<tr>
									<td align="right">学院名称：</td>
									<td align="left">${gXQJ0101.xymc}</td>
									<td align="right">专业名称：</td>
									<td align="left">${gXQJ0101.zymc}</td>
								</tr>
								<tr>
									<td align="right">班级名称：</td>
									<td align="left">${gXQJ0101.bjmc}</td>
									<td align="right">请假申请时间：</td>
									<td align="left">${fns:getDateString(gXQJ0101.sqrq)}</td>
								</tr>
								<c:if test="${not empty gXQJ0101.jzyj}">
									<td align="right">家长意见：</td>
									<td align="left" colspan="3">${gXQJ0101.jzyjString}</td>
			            	    </c:if>
								<c:if test="${not empty gXQJ0102SpList}">
									<tr id="fj">
						                  <td align="right">辅导员上传与家长交流附件：</td>
						                  <td colspan="3" align="left">
							                  	<c:forEach items="${gXQJ0102SpList}" var="gXQJ0102">
							                  		<div id='${gXQJ0102.gxqj0102Id}'>
							                  			<a href="${ctx}/studentvacation/gXQJ0102/download?id=${gXQJ0102.gxqj0102Id}">${gXQJ0102.fjmc}</a><br/>
							                   		</div>
							                    </c:forEach>  
						                  </td>
				            	    </tr>
			            	    </c:if>
							
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

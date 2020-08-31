<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/baidu/webuploader/webuploader.css">
	<script type="text/javascript" src="${ctxStatic}/baidu/webuploader/webuploader.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/baidu/uploadcommon.js"></script>
	<title>请假单管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			// 正数验证  可为小数
			jQuery.validator.addMethod("isPositive", function(value,element) { 
			  var positive = /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/; 
			  return this.optional(element) || positive.test(value); 
			}, "请正确填写正数");
			$("#inputForm").validate();
		});
		
		//初始化fileinput控件（第一次初始化）
		function delFujian(id) {    
			var url = "${ctx}/common/delete";
			$.ajax({
				url : url,
				type : "POST",
				data : {id : id},
				success : function(data){
					if(data=="success"){
						alert("附件删除成功");
						var div = document.getElementById(id);
					    while(div.hasChildNodes()) //当div下还存在子节点时 循环继续
					    {
					        div.removeChild(div.firstChild);
					    }
					}else{
						alert("附件删除失败");
					}
				}
			})
		    
		}
		
	</script>
</head>
<body>
<div class="miandody">
	<div class="crumb">
		<i class="iconfont">&#xe60d;</i><strong>请假单管理</strong>
		<span class="songti">&gt;</span><span>请假单管理</span>
		<b class="fr"><a class="min_btn btn_success marright" href="javascript:history.go(-1)">返回</a></b>
	</div>
	<div class="con">
		<form:form id="inputForm" modelAttribute="gXQJ0101" action="${ctx}/sample/gXQJ0101/save" method="post" 
		class="form-horizontal" enctype="multipart/form-data">
			<form:hidden path="gxqj0101Id"/>
			<tags:message content="${message}"/>
			<div class="tab_door">
				<div class="tab_door_con">
					<div class="tab_door_show">
						<table class="tab" cellpadding="0" cellspacing="0">
							<tbody>
							 <tr>
				                  <td class="w200" align="right"><span class="red font18 martop">&lowast;&nbsp;</span>学号：</td>
				                  <td align="left" class="w400">
				                  	<form:input message="学号" class="input_txt w400 required" path="xh"  readonly="true" htmlEscape="false"/>
				                  	<form:hidden path="gxxs0101Id"/>
				                  </td>
				                  <td class="w200"  align="right"><span class="red font18 martop">&lowast;&nbsp;</span>姓名：</td>
				                  <td align="left"><form:input message="姓名" class="input_txt w400 required" path="xm"  readonly="true" htmlEscape="false"/></td>
				                </tr>
								<tr>
									<td align="right"><span class="red font18 martop">&lowast;&nbsp;</span>请假单标题：</td>
									<td align="left"><form:input path="qjdbt" htmlEscape="false" cssClass="input_txt w400 required"/></td>
									<td align="right"><span class="red font18 martop">&lowast;&nbsp;</span>请假类别：</td>
									<td align="left">
										<form:radiobuttons path="qjlb" items="${fns:getDictList('XG_STUDENTVACATION_QJLB')}" itemLabel="label" itemValue="value" class="required"/>
									</td>
								</tr>
								<tr>
									<td align="right"><span class="red font18 martop">&lowast;&nbsp;</span>请假事由：</td>
									<td align="left" colspan="3">
										<textarea name="qjsy" htmlEscape="false" class="area w_90 h50 required">${gXQJ0101.qjsyString }</textarea>
									</td>
								</tr>
								<tr>
									<td align="right"><span class="red font18 martop">&lowast;&nbsp;</span>请假开始日期：</td>
									<td align="left">
									<input type="text" id="qjksrq2" name="qjksrq2" readonly="readonly"  value="${fns:getDateString(gXQJ0101.qjksrq)}"
										onclick="WdatePicker({maxDate:'#F{$dp.$D(\'qjjsrq2\')}',dateFmt:'yyyy-MM-dd',vel:'qjksrq',realDateFmt:'yyyyMMdd'})" class="Wdate input_txt w100 required">
										<input type="hidden" id="qjksrq" name="qjksrq" value="${gXQJ0101.qjksrq }">
										&nbsp;&nbsp;
										<input type="text" id="qjkssj" name="qjkssj"  readonly="readonly"  value="${fns:getTimeString(gXQJ0101.qjkssj)}"
											onclick="WdatePicker({dateFmt:'HH:mm',minDate:'00:00'})" class="Wdate input_txt w100 required">
									</td>
									<td align="right"><span class="red font18 martop">&lowast;&nbsp;</span>请假结束日期：</td>
									<td align="left">
									<input type="text" id="qjjsrq2" name="qjjsrq2" readonly="readonly"  value="${fns:getDateString(gXQJ0101.qjjsrq)}"
										onclick="WdatePicker({minDate:'#F{$dp.$D(\'qjksrq2\')}',dateFmt:'yyyy-MM-dd',vel:'qjjsrq',realDateFmt:'yyyyMMdd'})" class="Wdate input_txt w100 required">
										<input type="hidden" id="qjjsrq" name="qjjsrq" value="${gXQJ0101.qjjsrq }">
										&nbsp;&nbsp;
										<input type="text" id="qjjssj" name="qjjssj"  readonly="readonly"  value="${fns:getTimeString(gXQJ0101.qjjssj)}"
											onclick="WdatePicker({dateFmt:'HH:mm',minDate:'00:00'})" class="Wdate input_txt w100 required">
									</td>
								</tr>
								<tr>
									<td align="right"><span class="red font18 martop">&lowast;&nbsp;</span>请假总时长（天）：</td>
									<td align="left"><form:input path="qjzsc" htmlEscape="false" cssClass="input_txt w400 isPositive required"/></td>
									<td align="right"><span class="red font18 martop">&lowast;&nbsp;</span>是否为事后补假：</td>
									<td align="left">
										<form:radiobuttons path="sfbl" items="${fns:getDictList('physicalhealth_sf')}" itemLabel="label" itemValue="value" class="required"/>
									</td>
								</tr>
								<tr>
									<td align="right">备注：</td>
									<td align="left" colspan="3">
									<textarea name="bz" htmlEscape="false" class="area w_90 h50">${gXQJ0101.bzString }</textarea>
									</td>
								</tr>
								<c:if test="${not empty fileList}">
									<tr id="fj">
						                  <td align="right">已上传的附件：</td>
						                  <td colspan="3" align="left">
							                  	<c:forEach items="${fileList}" var="fileInfo">
							                  		<div id='${fileInfo.id}'>
							                  			<a href="${ctx}/common/downloadFile?id=${fileInfo.id}">${fileInfo.fileName}</a>&nbsp;<a onclick="return delFujian('${fileInfo.id}');" href="javascript:;" >删除</a><br/>
							                   		</div>
							                    </c:forEach>  
						                  </td>
				            	    </tr>
			            	    </c:if>
								<tr>
					                  <td align="right">附件：</td>
					                  <td colspan="2" align="left">
						                  	<div id="hiddenFiles">
											</div>
											<div id="ls"></div>
					                  </td>
					                  <td>
					                  		<div class="btns">
										        <div id="picker"  onclick="addPciButton();"></div>
										    </div>
					                  </td>
			            	    </tr>
								<tr>
									<td align="right">学院名称：</td>
									<td align="left">${student.yxshmc}</td>
									<td align="right">专业名称：</td>
									<td align="left">${student.zymc}</td>
								</tr>
								<tr>
									<td align="right">班级名称：</td>
									<td align="left" colspan="3">${student.bjmc}</td>
								</tr>
							
							</tbody> 
						</table>
					</div>
					<div class="tab_btn_show">
						<shiro:hasPermission name="sample:gXQJ0101:edit">
							<input id="btnSubmitSave" class="btn btn_primary" type="submit"	value="保 存" />&nbsp;
							<input id="btnSubmit" class="btn btn_primary" type="button"
									value="提 交" onclick="submitForm();" />&nbsp;
							</shiro:hasPermission>
					</div>
				</div>
			</div>
		</form:form>
	</div>
</div>
<script type="text/javascript">
	function submitForm() {
		$("#inputForm").attr("action","${ctx}/sample/gXQJ0101/save/save?flag=1");
		$("#inputForm").submit();
	}
	
	//有几个上传按钮，就绑定几个
	uploader.addButton({
	    id: '#picker',//按钮ID
	    innerHTML: '选择文件'//按钮名称
	});
	
	function addPciButton(){
		//设置上传文件显示列表
		$list = $('#ls');
		//设置上传子目录
		uploader.options.formData.moduleFilePath="vacationfiles";
	}
</script>
</body>
</html>

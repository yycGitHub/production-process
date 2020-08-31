<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>选择员工</title>
	<meta name="decorator" content="default"/>
	<style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
	<script src="${ctxStatic}/jquery/jquery.form.js" type="text/javascript"></script>
	<script type="text/javascript">
		function setInterface(){
		    $("#contentTableForm").ajaxSubmit({
		    	success: function(data) {
		            if("1" == data){
		            	alert("权限成功！");
		        		parent.$.fancybox.close();
		            }else{
		            	alert("权限失败:"+data);
		            }
		        }
		    });
		}
	</script>
</head>
<body>	
<div class="miandody">
   <div class="con">
	<div class="crumb">
		<span>接口列表</span>
	</div>
	<br/>
	<div class="tabcon">
	<form id="contentTableForm" name="contentTableForm" action="${ctx}/api/apiInterface/setInterUserface" method="post">
	<input type="hidden" name="appId" value="${appId}">
	<table class="tab" id="contentTable" cellpadding="0" cellspacing="0">
		<thead>
			<tr>
				<th style="width: 20px;"></th>
				<th>接口url</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${apiInterfaces}" var="apiInterface">
			<tr onclick="selectedTr(this);">
				<td align="center">
					<input type="checkbox" id="selectedUserId" name="apiInterfaceIds" value="${apiInterface.id}"
					<c:forEach items="${selectedApiInterfaceIds}" var="selectedApiInterfaceId">
						<c:if  test="${selectedApiInterfaceId == apiInterface.id }">
							checked="checked"
						</c:if>
					</c:forEach>
					/>
					<input type="hidden" id ="selectedUserName" name="selectedUserNames" value="${apiInterface.name}"/>
				</td>
				<td>${apiInterface.name}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	</form>
	</div>
	</div>
		<div class="fr">
			<span class="marright martop"><input class="btn btn_primary" id="btnSave"
						name="" type="button" onclick="setInterface();" value="保存"></span>
				<span class="marright martop"><input class="btn btn_primary" id="btnSave"
						name="" type="button" onclick="parent.$.fancybox.close();" value="取消"></span>
		</div>
	</div>
	<script type="text/javascript">
		function  selectedTr(item){
			$(item).find("input[name^='selectedUserIds']").attr("checked",true);
		}
	</script>
</body>
</html>
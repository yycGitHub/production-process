<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>选择员工</title>
	<meta name="decorator" content="default"/>
	<style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
	<script type="text/javascript">
		$(document).ready(function() {
			if("${selectIds}" != '')
			{
				var idsArr="${selectIds}".split(",");
				var obj=document.getElementsByName('selectedUserId'); 
				for(var i=0; i<obj.length; i++){ 
					for(var j=0; j<idsArr.length; j++)
					{
						if(obj[i].value == idsArr[j])
						{
							obj[i].checked = true; 
						}
					}
				}
			}
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/sys/user/upUserTable").submit();
	    	return false;
	    }
		function saveAssign(){
			var controllerScope = window.parent.jQuery('div[ng-controller="KisBpmAssignmentPopupCtrl"]').scope();
			var ids=''; 
			var names='';
			if("${user.checkType}" == 'assignee')
			{
				ids = window.jQuery('input[name=selectedUserId]:checked').val();
				names = window.jQuery('input[name=selectedUserId]:checked').next().val();
				controllerScope.setAssignee(ids,names);
			}
			else
			{
				var obj=document.getElementsByName('selectedUserId'); 
				for(var i=0; i<obj.length; i++){ 
					if(obj[i].checked) 
					{
						ids+=obj[i].value+','; 
						names+= window.jQuery(obj[i]).next().val()+',';
					}
				}
				controllerScope.setCandidateUsers(ids,names);
			}
			closeModal();
		}
		function closeModal()
		{
			window.parent.jQuery("#handlerChoseModal").hide();
			window.parent.jQuery("#assigentmentModal").show();
		}
	</script>
</head>
<body>	
<div class="miandody">
	<div class="con">
	<form:form id="searchForm" modelAttribute="user" action="${ctx}/sys/user/upUserTable?checkType=${user.checkType}" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
		<div class="search">
			<dl>
				<dt>
					<span><form:input path="loginName" htmlEscape="false" placeholder="登录名" class="input_txt w260"/></span>
					<span><input class="btn btn_primary" id="btnSubmit"
						name="" type="submit" value="查询"></span>
				</dt>
			</dl>
		</div>
	</form:form>
	<br/>
	<div class="tabcon">
	<form id="contentTableForm" name="contentTableForm" action="" method="post">
	<table class="tab" id="contentTable" cellpadding="0" cellspacing="0">
		<thead>
			<tr>
				<th style="width: 20px;"></th>
				<th>登录名</th>
				<th>用户名</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="userTemp">
			<tr>
				<td align="center">
					<input 
					<c:if test="${user.checkType =='assignee'}"> type="radio" </c:if>
					<c:if test="${user.checkType =='delegate'}"> type="radio" </c:if>
					<c:if test="${user.checkType =='assignees'}"> type="checkbox" </c:if>
					 id="selectedUserId" name="selectedUserId" class="${userTemp.loginName}" value="${userTemp.id}"/>
					<input type="hidden" id ="selectedUserName" name="selectedUserName" value="${userTemp.loginName}"/>
				</td>
				<td>${userTemp.loginName}</td>
				<td>${userTemp.name}</td>
			</tr>
		</c:forEach>
		</tbody>
		<tfoot>
			<td colspan="40">
				<div class="fr">
					<div class="page">${page}</div>
				</div>
			</td>
		</tfoot>
	</table>
	</form>
	</div>
	</div>
		<c:if test="${user.checkType != 'delegate'}">
			<div class="fr">
				<span class="marright martop"><input class="btn btn_primary" id="btnSave"
							name="" type="button" onclick="saveAssign();" value="保存"></span>
					<span class="marright martop"><input class="btn btn_primary" id="btnSave"
							name="" type="button" onclick="closeModal();" value="取消"></span>
			</div>
		</c:if>
	</div>
	<script type="text/javascript">
		function  selectedTr(item){
			$(item).find("input[name^='selectedUserId']").attr("checked",true);
		}
	</script>
</body>
</html>
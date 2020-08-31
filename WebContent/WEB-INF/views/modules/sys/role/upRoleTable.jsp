<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>选择角色</title>
	<meta name="decorator" content="default"/>
	<style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
	<script type="text/javascript">
		$(document).ready(function() {
			if("${selectIds}" != '')
			{
				var idsArr="${selectIds}".split(",");
				var obj=document.getElementsByName('selectedRoleId'); 
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
			$("#searchForm").attr("action","${ctx}/sys/role/upRoleTable").submit();
	    	return false;
	    }
		function saveCandidateGroups(){
			var ids=''; 
			var names='';
			var obj=document.getElementsByName('selectedRoleId'); 
			for(var i=0; i<obj.length; i++){ 
				if(obj[i].checked) 
				{
					ids+=obj[i].value+','; 
					names+= window.jQuery(obj[i]).next().val()+',';
				}
			}
			var controllerScope = window.parent.jQuery('div[ng-controller="KisBpmAssignmentPopupCtrl"]').scope();
			controllerScope.setCandidateGroups(ids,names);
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
	<form:form id="searchForm" modelAttribute="role" action="${ctx}/sys/role/upRoleTable" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
		<div class="search">
			<dl>
				<dt>
					<span><form:input path="name" htmlEscape="false" placeholder="角色名" class="input_txt w260"/></span>
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
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="roleTemp">
			<tr>
				<td align="center">
					<input type="checkbox"  id="selectedRoleId" name="selectedRoleId" value="${roleTemp.id}" rolename="${roleTemp.name}"/>
					<input type="hidden" id ="selectedRoleName" name="selectedRoleName" value="${roleTemp.name}"/>
				</td>
				<td>${roleTemp.name}</td>
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
		<div class="fr">
			<span class="marright martop"><input class="btn btn_primary" id="btnSave"
						name="" type="button" onclick="saveCandidateGroups();" value="保存"></span>
				<span class="marright martop"><input class="btn btn_primary" id="btnSave"
						name="" type="button" onclick="closeModal();" value="取消"></span>
		</div>
	</div>
	<script type="text/javascript">
		function  selectedTr(item){
			$(item).find("input[name^='selectedRoleId']").attr("checked",true);
		}
	</script>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>溯源管理平台</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
	<script src="${ctxStatic}/common/pony.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			var categoryId= $("#categoryId").val();
			$("#searchForm").attr("action","${ctx}/sys/group/memberManage?id="+groupId).submit();
	    	return false;
	    }
		
		function searchUser(){
			
		}
		
		function clearInput(){
			$(".search dt :text").val("");
			$(".search dt select").val("");
		}
	</script>
</head>
<body>
<div class="miandody">
  <div class="crumb"><i class="iconfont">&#xe60d;</i><strong>栏目管理员</strong></div>
  
    <div class="con">
    
			<form:form id="searchForm" modelAttribute="user"  action="${ctx}/sys/group/"  method="post" class="breadcrumb form-search">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
			<input path="groupId" id="groupId" name="groupId" type="hidden" value="${groupId}"/>
				<div class="search">
					<dl>
						<dt>
							<span>
								<tags:treeselect id="office" name="office.id" value="${user.office.id}" 
								labelName="office.name" labelValue="${user.office.name}"
								title="部门" url="/sys/office/treeData?type=2" cssClass="input_txt w260" allowClear="true"
								placeholder="归属部门" dynamicTree="true"/>
							</span>
							<span>
								<form:input path="loginName" htmlEscape="false"
									maxlength="50" cssClass="input_txt w260" placeholder="登录名" />
							</span>
							<span>
								<form:input path="name" htmlEscape="false"
									maxlength="50" cssClass="input_txt w260" placeholder="姓名" />
							</span>
							
							<span>
								 
							</span>
						</dt>
						<dd>
				          <span><input id="btnSubmit" class="btn btn_primary" type="submit" value="查询" onclick="return page();"/></span>
				          <span><input class="btn btn_default" name="" type="button" value="清除" onclick="clearInput()"></span>
				        </dd>
					</dl>
				</div>
			</form:form>
			<%-- <tags:message content="${message}"/> --%>
			<div class="tabcon">
			<form id="contentTableForm" name="contentTableForm" action="" method="post">
			<%-- <tags:tableSort id="orderBy" name="orderBy" value="${GXCP0102.orderBy}" callback="page();"/> --%>
				<table class="tab" id="contentTable" cellpadding="0" cellspacing="0">
					<thead>
						<tr>
							<!-- <td><input type="checkbox"  onclick="Pn.checkbox('ids',this.checked)"/></td> -->
							<td><nobr class="sort-column name">登录名</nobr></td>
							<td><nobr>归属部门</nobr></td>
							<td><nobr>姓名</nobr></td>
							<td><nobr>手机</nobr></td>
						 	<td><nobr>组内角色</nobr></td> 
							<td><nobr>操作</nobr></td>
						</tr>
					</thead>
					<tbody>
							<c:forEach items="${page.list}" var="user">
								<tr>
									<%-- <td align="center"><input type="checkbox" name="ids" value="${user.id}" /></td> --%>
									<%-- <td><a href="${ctx}/sys/user/form?id=${user.id}">${user.loginName}</a></td> --%>
									<td>${user.loginName}</td>
									<td>${user.office.name}</td>
									<td>${user.name}</td>
									<td>${user.phone}</td>
									 <td>${user.groupRoleDesc}</td> 
									 
									<shiro:hasPermission name="cms:category:edit">
										<td>
											<c:if test="${user.delFlag == '0' and user.isMember == false}">
												<a href="${ctx}/sys/group/addMember?userId=${user.id}&groupId=${groupId }&add=1"
													onclick="">添加组员</a>
											</c:if>
											<c:if test="${user.isMember == true and user.isMaster == false}">
												<a style='color:#FF0000'  href="${ctx}/sys/group/addMember?userId=${user.id}&groupId=${groupId }&add=0"
													onclick="">移除组员</a>
													<a style='color:#FF0000'  href="${ctx}/sys/group/addMaster?userId=${user.id}&groupId=${groupId }&add=1"
													onclick="">设为组长</a>
											</c:if>
											
											<c:if test="${user.isMaster == true}">
												<a style='color:#FF0000'  href="${ctx}/sys/group/addMaster?userId=${user.id}&groupId=${groupId }&add=0"
													onclick="">取消组长</a>
											</c:if>
											
										</td>
									</shiro:hasPermission>
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
				<div>
				&nbsp;
				</div>
				</form>
			</div>
			
		</div>
  </div>
</body>

</html>
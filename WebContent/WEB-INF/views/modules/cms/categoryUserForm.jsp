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
			// 表格排序
			//tableSort({callBack : page});
			/* $("#inputForm").validate({
				rules: {
					roleIdList: {required: true}
				},
				messages: {
					roleIdList: {required: "必须至少选择一个角色"}
				}
			}); */
			
			
			/* $("#btnDelete").click(function(){
			   	
				top.$.jBox.confirm("确认要删除用户吗？","系统提示",function(v,h,f){
					if(v == "ok"){
						 $("#contentTableForm").attr("action", "${ctx}/sys/user/delete"); 
						 $("#contentTableForm").submit();
					}
				},{buttonsFocus:1});
			}); */
		});
		
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			var categoryId= $("#categoryId").val();
			$("#searchForm").attr("action","${ctx}/cms/categoryRoleUser/toPlateAdmin?id="+categoryId).submit();
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
    
    	<%-- 	<form:form id="inputForm" modelAttribute="categoryRoleUser" action="${ctx}/cms/categoryRoleUser/addPlateAdminRole" method="post" class="form-horizontal">
				<form:hidden path="id"/>
				<input path="categoryId" id="categoryId" name="categoryId" type="hidden" value="${categoryRoleUser.categoryId}"/>
				 <tags:message content="${message}"/> 
			    <div class="list_edit">
				
			     <dl>
			        <dd>栏目绑定角色:</dd>
			        <dd>
			           <form:checkboxes path="roleIdList" items="${allRoles}" itemLabel="name" itemValue="id" htmlEscape="false"/> 
			        </dd>
			      </dl>
			       <dl>
			        <dt>&nbsp;</dt>
			      
			        <dd>
			        	<shiro:hasPermission name="cms:category:edit">
			        		<input id="btnSubmit" class="btn btn_primary" type="submit" value="绑 定"/>&nbsp;
			        	</shiro:hasPermission>
						<!-- <input id="btnCancel" class="btn btn_primary" type="button" value="返 回" onclick="history.go(-1)"/> -->
					</dd>
					
			      </dl> 
			    </div>
		    </form:form> --%>
    
    
    
			<form:form id="searchForm" modelAttribute="user" action="${ctx}/cms/categoryRoleUser/" method="post" class="breadcrumb form-search">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
			<input path="categoryId" id="categoryId" name="categoryId" type="hidden" value="${categoryId}"/>
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
							<td><nobr>角色</nobr></td>
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
									<td>${user.roleNames}</td>
									<shiro:hasPermission name="cms:category:edit">
										<td>
											<c:if test="${user.delFlag == '0' and user.isPlateAdmin == false}">
												<a href="${ctx}/cms/categoryRoleUser/addPlateAdmin?userId=${user.id}&exUserIds=${categoryRoleUser.userId}&categoryId=${categoryId }&add=1"
													onclick="">添加版主</a>
											</c:if>
											<c:if test="${user.isPlateAdmin == true}">
												<a style='color:#FF0000'  href="${ctx}/cms/categoryRoleUser/addPlateAdmin?userId=${user.id}&exUserIds=${categoryRoleUser.userId}&categoryId=${categoryId }&add=0"
													onclick="">取消版主</a>
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
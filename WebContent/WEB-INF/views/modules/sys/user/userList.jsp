<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {	
			bootbox.setDefaults("locale","zh_CN");
		});
	</script>
	<script type="text/javascript">
		$(document).ready(function() {
		
			$("#btnExport").click(function(){
				top.$.jBox.confirm("确认要导出用户数据吗？","系统提示",function(v,h,f){
					if(v == "ok"){
						$("#searchForm").attr("action","${ctx}/sys/user/export").submit();
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
			
			$("#btnImport").click(function(){
				top.$.jBox("iframe:${ctx}/common/uploadData?type=1",{title:"选择导入的文件", width:600,height:300, submit:function(v,h,f){
					if(v == 'ok'){
						location.reload();
						return true;
					}
					return false;
				}});
			});
			
			$("#btnDelete").click(function(){
			   	if(Pn.checkedCount('ids')<=0) {
			   	   $.jBox.alert("请选择要删除的用户!","提示");
			       return;
			  	}
				top.$.jBox.confirm("确认要删除用户吗？","系统提示",function(v,h,f){
					if(v == "ok"){
						 $("#contentTableForm").attr("action", "${ctx}/sys/user/delete"); 
						 $("#contentTableForm").submit();
					}
				},{buttonsFocus:1});
			});
		});
		
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/sys/user/").submit();
	    	return false;
	    }
		
		function clearInput(){
			$(".search dt :text").val("");
			$(".search dt select").val("");
		}
		
		function selectPemissions(url){
			$("#pemissions-iframe").attr('src',url);
			bootbox.dialog({ 
				message: $( "#dialog-pemissions").html(),
				title: "权限配置 ",
		        buttons:             
		        {
		            "success" :
		             {
		                "label" : "<i class='icon-ok'></i> 确定",
		                "className" : "btn-sm btn-success",
		                "callback": function() {

		                }
		            },
		        	"cancel": {
			            "label" : "<i class='icon-info'></i> 取消",
			            "className" : "btn-sm btn-danger"
		            }
		        }
			});
		}
	</script>
</head>
<div class="main-container" id="main-container">
	<div class="main-content-inner">
		<div class="main-content">
			<div class="page-content">
				<tags:message content="${message}"/>
				<div class="row">
					<form:form id="searchForm" modelAttribute="user" action="${ctx}/sys/user/" method="post" class="form-horizontal">
					<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
					<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
					<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
					<div class="table-responsive">
						<span class="col-xs-2">
						<tags:treeselect id="office" name="office.id" value="${user.office.id}" labelName="office.name" 
						labelValue="${user.office.name}" placeholder="请选择归属部门" title="部门" url="/sys/office/treeData?type=2" cssClass="col-xs-9" allowClear="true"/>
						</span>
						<span class="drop_down_triangle">
							<form:select class="multiselect input-md" path="delFlag">
							<form:options items="${fns:getDictList('sys_user_act')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
							</form:select>
				          </span>
						  <span>
						  	<input type="text" name="loginName" class="input-md" placeholder="请输入登录名" />
						  </span>
						  <span>
						  	<input type="text" name="name" class="input-md" placeholder="请输入姓名" />
						  </span>
				          <span>
				          	    <button type="button" class="btn btn-info btn-sm" onclick="return page();">
									<span class="ace-icon fa fa-search icon-on-right bigger-110"></span>查询
								</button>
				          </span>
				          <span class="pull-right">
								<button type="button" class="btn btn-sm btn-success"  onclick="javascript:location.href='${ctx}/sys/user/form'" id="btnup">
									<i class="ace-icon fa fa-plus bigger-110"></i>
									添加用户
								</button>
				          </span>
				          

					</div>
					</form:form>
				</div>
				<div class="hr dotted hr-double"></div>
				<div class="row">
					<div class="col-xs-12">
						<div class="tabbable">
							<div class="tab-content">
								<!-- PAGE CONTENT BEGINS -->
								<table id="contentTable" class="table table-bordered table-hover">
									<thead>
										<tr>
										<th>登录名</th>
										<th>归属部门</th>
										<th>姓名</th>
										<th>手机</th>
										<th>角色</th>
										<shiro:hasPermission name="sys:user:edit">
										<th>操作</th>
										</shiro:hasPermission>
									<tbody>
									<c:forEach items="${page.list}" var="user">
									<tr>
										<td><a href="${ctx}/sys/user/form?id=${user.id}">${user.loginName}</a></td>
										<td>${user.office.name}</td>
										<td>${user.name}</td>
										<td>${user.phone}</td>
										<td>${user.roleNames}</td>
										<shiro:hasPermission name="sys:user:edit">
											<td>
											<div class="hidden-sm hidden-xs action-buttons">
												<c:if test="${user.delFlag == '0'}">
													<a class="" href="${ctx}/sys/user/form?id=${user.id}">
														<i class="green ace-icon fa fa-pencil bigger-130"></i>
													</a>
<%-- 													<a class="" id="editPemissions" href="${ctx}/sys/user/editUserPemissions?userId=${user.id}"> --%>
<!-- 														<i class="grey ace-icon fa fa-cog bigger-130"></i> -->
<!-- 													</a> -->
													<a class="" href="#" onclick="javascript:bootbox.confirm('确认要删除[${user.name}]角色吗?', function(result){if(result) location.href='${ctx}/sys/user/delete?ids=${user.id}';})">
														<i class="red ace-icon fa fa-trash-o bigger-130"></i>
													</a>
												</c:if>
												<c:if test="${user.delFlag == '1'}">
													<a class="orange" href="#" onclick="javascript:bootbox.confirm('确认要恢复[${user.name}]用户吗?', function(result){if(result) location.href='${ctx}/sys/user/rebackUser?ids=${user.id}';})">
														<i class="ace-icon fa fa-undo bigger-130"></i>
													</a>
												</c:if>
											</div>
											</td>
										</shiro:hasPermission>
									</tr>
									</c:forEach>
									</tbody>
								</table>
								<div class="col-xs-6"></div>
								<div class="col-xs-6">
									<div class="dataTables_paginate paging_simple_numbers">
										${page}
									</div>
								</div>
								<!-- PAGE CONTENT ENDS -->
							</div>
						</div>
					</div><!-- /.col -->
				</div><!-- /.row -->	
			</div>				
		</div>
	</div>
</div>
<div id="dialog-pemissions" class="hide">
		<iframe name="pemissions-iframe" id="pemissions-iframe" width="100%" height="100%" marginheight="0" marginwidth="0" frameborder="0" 
			scrolling="auto" src="">
		</iframe>
</div>
</html>
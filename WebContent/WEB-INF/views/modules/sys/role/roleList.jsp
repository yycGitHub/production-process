<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>角色管理</title> 
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {	
			bootbox.setDefaults("locale","zh_CN");
		});
	</script>
</head>
<div class="main-container" id="main-container">
	<div class="main-content-inner">
		<div class="main-content">
			<div class="page-content">
				<tags:message content="${message}"/>
				<div class="row">
					<div class="col-xs-12">
						<div class="tabbable">
							<ul class="nav nav-tabs padding-18 tab-size-bigger" id="myTab">
								<li class="active">
									<a href="${ctx}/sys/role/">
										<i class="blue ace-icon fa fa-align-justify bigger-120"></i>
										角色列表
									</a>
								</li>
								<shiro:hasPermission name="sys:role:edit">
								<li>
									<a href="${ctx}/sys/role/form">
										<i class="green ace-icon fa fa-user bigger-120"></i>
										角色添加
									</a>
								</li>
								</shiro:hasPermission>
							</ul>
							<div class="tab-content">
								<div class="col-xs-12">
									<!-- PAGE CONTENT BEGINS -->
									<table id="contentTable" class="table  table-bordered table-hover">
										<thead>
											<tr><th>角色名称</th>
											<th>归属机构</th>
											<th>数据范围</th>
											<shiro:hasPermission name="sys:role:edit">
											<th>操作</th></shiro:hasPermission>
											</tr>
										</thead>
										<tbody>
										<c:forEach items="${list}" var="role">
										<tr>
											<td><a href="form?id=${role.id}">${role.name}</a></td>
											<td>${role.office.name}</td>
											<td>${fns:getDictLabel(role.dataScope, 'sys_data_scope', '无')}</td>
											<shiro:hasPermission name="sys:role:edit">
											<td>
												<div class="hidden-sm hidden-xs action-buttons">
													<a class="green" href="${ctx}/sys/role/form?id=${role.id}">
														<i class="ace-icon fa fa-pencil bigger-130"></i>
													</a>
						
													<a class="red" href="#" onclick="javascript:bootbox.confirm('确认要删除[${role.name}]角色吗?', function(result){if(result) location.href='${ctx}/sys/role/delete?id=${role.id}';})">
														<i class="ace-icon fa fa-trash-o bigger-130"></i>
													</a>
												</div>
											</td>
											</shiro:hasPermission>	
										</tr>
										</c:forEach>
										</tbody>
									</table>
									<!-- PAGE CONTENT ENDS -->
								</div><!-- /.col -->
							</div>
						</div>
					</div><!-- /.col -->
				</div><!-- /.row -->					
			</div>
		</div>		
	</div>	
</div>
</html>
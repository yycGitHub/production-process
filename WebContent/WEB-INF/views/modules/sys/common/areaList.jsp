<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>区域管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#treeTable").treeTable({expandLevel : 5});
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
	</script>
</head>
<body>
	<div class="main-container" id="main-container">
	<div class="main-container-inner">
		<div class="main-content">
			<div class="page-content">
			
				<div class="row">
					<div class="col-xs-12">
							<div class="table-responsive">
							          <shiro:hasPermission name="sys:area:edit">
								          <span class="pull-right">
												<a class="btn btn-sm btn-success" href="${ctx}/sys/area/form">
													<i class="ace-icon fa fa-plus bigger-110"></i>新增区域
												</a>
								          </span>
							          </shiro:hasPermission>
	
								</div>
				    	<div class="hr dotted hr-double"></div>
				    	
						<div class="table-responsive">
							<table id="treeTable" class="table table-striped table-bordered table-hover">
								<thead>
									<tr>
										<th>区域名称</th>
										<th>区域编码</th>
										<th>区域类型</th>
										<th>备注</th>
										<shiro:hasPermission name="sys:area:edit"><th>操作</th></shiro:hasPermission>
									</tr>
								</thead>

								<tbody>
									<c:forEach items="${list}" var="area">
										<tr id="${area.id}" pId="${area.parent.id ne requestScope.area.id?area.parent.id:'0'}">
											<td><a href="${ctx}/sys/area/form?id=${area.id}">${area.name}</a></td>
											<td>${area.code}</td>
											<td>${fns:getDictLabel(area.type, 'sys_area_type', '无')}</td>
											<td>${area.remarks}</td>
											<shiro:hasPermission name="sys:area:edit"><td>
												<a href="${ctx}/sys/area/form?id=${area.id}">
													<span class="green"><i class="ace-icon fa fa-pencil bigger-130"></i></span>&nbsp;
												</a>
												<a href="#" onclick="javascript:parent.bootbox.confirm('要删除该区域及所有子区域项吗？', function(result){if(result) location.href='${ctx}/sys/area/delete?id=${area.id}';})">
													<span class="red"><i class="ace-icon fa fa-trash-o bigger-130"></i></span>&nbsp;
												</a>
												<a href="${ctx}/sys/area/form?parent.id=${area.id}">
													<span class="green"><i class="ace-icon fa fa-plus bigger-130"></i></span>下级区域
												</a> 
											</td></shiro:hasPermission>
										</tr>
									</c:forEach>
								</tbody>
								<tfoot>
							        <tr>
							            <td colspan="12">
							              <div class="fr">
							                <div class="pull-right">
							                	${page}
							                </div>
							              </div>
							            </td>
							         </tr>
							         <tr>
							         	<div>
											
										</div>
							         </tr>
							        </tfoot>
							</table>
						</div><!-- /.table-responsive -->
					</div><!-- /span -->
				</div><!-- /row -->

			</div><!-- /.page-content -->
		</div><!-- /.main-content -->
	</div><!-- /.main-container-inner -->
</div><!-- /.main-container -->
	
</body>
</html>
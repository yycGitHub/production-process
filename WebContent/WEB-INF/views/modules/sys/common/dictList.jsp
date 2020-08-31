<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:getConfig('productName')}</title> 
	<meta name="decorator" content="default"/>
	
	<script type="text/javascript">
		$(document).ready(function() {	
			$("#btnup").click(function(){
				location.href = "${ctx}/sys/dict/form?sort=10";
			});
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
						 <form:form id="searchForm" modelAttribute="dict" action="${ctx}/sys/dict/" method="post">
						    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
							<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
							<div class="table-responsive">
									<span class="drop_down_triangle">
										  <form:select path="type" class="multiselect">
											<form:option value="" label="选择类型"/>
											<form:options items="${typeList}" htmlEscape="false"/>
										  </form:select>
							          </span>
									  <span>
									  	<form:input path="description" htmlEscape="false" maxlength="50" cssClass="input-sm" placeholder="描述"/>
									  </span>
							          <span>
							          	    <button type="submit" class="btn btn-info btn-sm" id="btnSubmit">
												<span class="ace-icon fa fa-search icon-on-right bigger-110"></span>查询
											</button>
							          </span>
							          
							          <shiro:hasPermission name="sys:dict:edit">
								          <span class="pull-right">
												<button type="button" class="btn btn-sm btn-success" id="btnup">
													<i class="ace-icon fa fa-plus bigger-110"></i>
													字典添加
												</button>
								          </span>
							          </shiro:hasPermission>
	
								</div>
						    </form:form>	
				    	<div class="hr dotted hr-double"></div>
				    	
						<div class="table-responsive">
							<table id="sample-table-1" class="table table-striped table-bordered table-hover">
								<thead>
									<tr>
										<th>键值</th>
										<th>标签</th>
										<th class="hidden-480">类型</th>

										<th>
											<i class="icon-time bigger-110 hidden-480"></i>
											描述
										</th>
										<th class="hidden-480">排序</th>

										<shiro:hasPermission name="sys:dict:edit"><th>操作</th></shiro:hasPermission>
									</tr>
								</thead>

								<tbody>
									<c:forEach items="${page.list}" var="dict">
										<tr>
											<td class="center">${dict.value}</td>
											<td><a href="${ctx}/sys/dict/form?id=${dict.id}">${dict.label}</a></td>
											<td><a href="javascript:" onclick="$('#type').val('${dict.type}');$('#searchForm').submit();return false;">${dict.type}</a></td>
											<td>${dict.description}</td>
											<td class="center">${dict.sort}</td>
											<shiro:hasPermission name="sys:dict:edit"><td>
							    				<a href="${ctx}/sys/dict/form?id=${dict.id}">
							    					<span class="green"><i class="ace-icon fa fa-pencil bigger-130"></i></span>&nbsp;
							    				</a>
												<a href="#" onclick="javascript:parent.bootbox.confirm('确认要删除该字典吗?', function(result){if(result) location.href='${ctx}/sys/dict/delete?id=${dict.id}';})">
													<span class="red"><i class="ace-icon fa fa-trash-o bigger-130"></i></span>&nbsp;
												</a>
							    				<a href="<c:url value='${fns:getAdminPath()}/sys/dict/form?type=${dict.type}&sort=${dict.sort+10}'><c:param name='description' value='${dict.description}'/></c:url>">
							    					<span class="green"><i class="ace-icon fa fa-plus bigger-130"></i></span>&nbsp;
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
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:getConfig('productName')}</title> 

	<link rel="stylesheet" href="${ctxStatic}/assets/css/ui.jqgrid.min.css" />
	<!-- ace styles -->
	<link rel="stylesheet" href="${ctxStatic}/assets/css/ace.min.css" class="ace-main-stylesheet" id="main-ace-style" />
	<script src="${ctxStatic}/assets/js/jquery-2.1.4.min.js"></script>
	<script src="${ctxStatic}/assets/js/jquery-validation/1.11.1/jquery.validate.min.js"></script>
	<script src="${ctxStatic}/common/pony.js" type="text/javascript"></script>
	<script src="${ctxStatic}/assets/js/jquery.jqGrid.min.js"></script>
	<script src="${ctxStatic}/assets/js/grid.locale-en.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jqgriddata/office/officeList.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {	
			
		});
		
		function update(id,rebackId){
			$("#officeDiv").load("${ctx}/sys/office/form?id="+id+"&rebackId="+rebackId, function(response) {
				  $('#officeDiv').html(response);
				});
		}
		
		function deleteById(id,rebackId){
			  parent.bootbox.confirm({ 
      		  size: "small",
      		  message: "你确定删除此机构吗?", 
      		  callback: function(result){ 
      			 if(result)
      			 {
      				$.ajax({
						url: '${ctx}/sys/office/delete',
						data:'id='+ id+'&rebackId='+rebackId,
						type: "POST",
						dataType: 'json',
						beforeSend: function(){
						//在异步提交前要做的操作
						},
						success: function(data){
							$("#officeDiv").load("${ctx}/sys/office/list?id="+data.bodyData.rebackId+"&rebackId="+data.bodyData.rebackId+"&message='"+data.message+"'", function(response) {
								  $('#officeDiv').html(response);
								});
						}
					});
      			 }
      		  }
     		});  
		}
		
		function rebackOffice(id,rebackId){
			  parent.bootbox.confirm({ 
    		  size: "small",
    		  message: "你确定要恢复此机构吗?", 
    		  callback: function(result){ 
    			 if(result)
    			 {
    				$.ajax({
						url: '${ctx}/sys/office/rebackOffice',
						data:'id='+ id+'&rebackId='+rebackId,
						type: "POST",
						dataType: 'json',
						beforeSend: function(){
						//在异步提交前要做的操作
						},
						success: function(data){
							$("#officeDiv").load("${ctx}/sys/office/list?id="+data.bodyData.rebackId+"&rebackId="+data.bodyData.rebackId+"&message='"+data.message+"'", function(response) {
								  $('#officeDiv').html(response);
								});
						}
					});
    			 }
    		  }
   		});  
		}
		
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
	</script>
</head>
<body>
	
	<form:form id="searchForm" modelAttribute="office" action="${ctx}/sys/office/listOffice" method="post" class="breadcrumb">
	<input id="id" name="id" type="hidden" value="${office.id}"/>
		<div class="table-responsive">
				  <span class="drop_down_triangle">
					  <form:select path="delFlag" class="multiselect">
						  <form:options items="${fns:getDictList('sys_user_act')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					  </form:select>
		          </span>
		          <span>
		          	    <button type="button" class="btn btn-info btn-sm" id="find_btn">
							<span class="ace-icon fa fa-search icon-on-right bigger-110"></span>查询
						</button>
		          	    <button type="button" class="btn btn-info btn-sm" id="btnDelete" onclick="deleteMul();";>
							<span class="ace-icon fa fa-trash-o bigger-110"></span>删除
						</button>
		          </span>
			</div>
	    </form:form>	
	    
	 <div class="row">
		<div class="col-xs-12">
	    <table id="officetable"></table>
	    <div id="officepage"></div>
	  </div>
	  </div>
	    
   <%-- 	<!-- <div class="hr dotted hr-double"></div> -->
   	<form id="contentTableForm" name="contentTableForm" action="" method="post">
	<div class="table-responsive">
	<tags:message content="${message}"/>
	<input id="rebackId" name="rebackId" type="hidden" value="${office.id}"/>
		<table id="sample-table-1" class="table table-striped table-bordered table-hover">
			<thead>
				<tr>
					<th><input type="checkbox"  onclick="Pn.checkbox('id',this.checked)"/></th>
					<th>机构名称</th>
					<th>归属区域</th>
					<th>机构编码</th>
					<th>机构类型</th>
					<th>备注</th>
					<shiro:hasPermission name="sys:office:edit"><th>操作</th></shiro:hasPermission>
				</tr>
			</thead>

			<tbody>
				<c:forEach items="${page.list}" var="officeTemp">
					<tr>
						<td class="center"><input type="checkbox" name="id" value="${officeTemp.id}"/></td>
						<td><a href="${ctx}/sys/office/form?id=${officeTemp.id}&rebackId=${office.id}">${officeTemp.name}</a></td>
						<td>${officeTemp.area.name}</td>
						<td>${officeTemp.code}</td>
						<td>${fns:getDictLabel(officeTemp.type, 'sys_office_type', '无')}</td>
						<td>${officeTemp.remarks}</td>
						<shiro:hasPermission name="sys:office:edit"><td>
							<c:if test="${officeTemp.delFlag == '0'}">
					 			<a onclick="update('${officeTemp.id}','${office.id}');">
									<span class="green"><i class="ace-icon fa fa-pencil bigger-130"></i></span>&nbsp;
								</a>
								<a onclick="deleteById('${officeTemp.id}','${office.id}');">
									<span class="red"><i class="ace-icon fa fa-trash-o bigger-130"></i></span>&nbsp;
								</a>
							</c:if>
							<c:if test="${officeTemp.delFlag == '1'}">
								<a href="#" onclick="javascript:parent.bootbox.confirm('确认要恢复该机构吗?', function(result){if(result) location.href='${ctx}/sys/office/rebackOffice?ids=${officeTemp.id}';})">
									<span class="red"><i class="ace-icon fa fa-trash-o bigger-130"></i>恢复</span>&nbsp;
								</a>
							</c:if>
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
</form> --%>
</body>
</html>
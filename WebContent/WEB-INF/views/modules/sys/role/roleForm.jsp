<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>角色管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treeview.jsp" %>
	<script type="text/javascript">
	$(document).ready(function(){
		$("#name").focus();
		$("#inputForm").validate({
			errorElement: 'div',
			errorClass: 'help-block',
			focusInvalid: false,
			ignore: "",
			rules: {
				name: {remote: "${ctx}/sys/role/checkName?oldName=" + encodeURIComponent("${role.name}")},
				name: {
					required: true
				}
			},
			messages: {
				name: {remote: "角色名已存在"}
			},
			highlight: function (e) {
				$(e).closest('.form-group').removeClass('has-info').addClass('has-error');
			},
	
			success: function (e) {
				$(e).closest('.form-group').removeClass('has-error');//.addClass('has-info');
				$(e).remove();
			},
			errorPlacement: function (error, element) {
				if(element.is('input[type=checkbox]') || element.is('input[type=radio]')) {
					var controls = element.closest('div[class*="col-"]');
					if(controls.find(':checkbox,:radio').length > 1) controls.append(error);
					else error.insertAfter(element.nextAll('.lbl:eq(0)').eq(0));
				}
				else if(element.is('.select2')) {
					error.insertAfter(element.siblings('[class*="select2-container"]:eq(0)'));
				}
				else if(element.is('.chosen-select')) {
					error.insertAfter(element.siblings('[class*="chosen-container"]:eq(0)'));
				}
				else error.insertAfter(element.parent());
			},
			submitHandler: function(form){
				var ids = [], nodes = tree.getCheckedNodes(true);
				for(var i=0; i<nodes.length; i++) {
					ids.push(nodes[i].id);
				}
				$("#menuIds").val(ids);
				var ids2 = [], nodes2 = tree2.getCheckedNodes(true);
				for(var i=0; i<nodes2.length; i++) {
					ids2.push(nodes2[i].id);
				}
				$("#officeIds").val(ids2);
				bootbox.alert('正在提交，请稍等...');
				form.submit();
			},
			invalidHandler: function (form) {
			}
		});

		var setting = {
			check:{enable:true,nocheckInherit:true},
			view:{selectedMulti:false},
			data:{
				simpleData:{enable:true}
			},
			callback:{
				beforeClick:function(id, node){
					tree.checkNode(node, !node.checked, true, true);
					return false;
				}
			}
		};
		
		// 用户-菜单
		var zNodes=[
			<c:forEach items="${menuList}" var="menu">
				{id:'${menu.id}', pId:'${not empty menu.parent.id?menu.parent.id:0}', name:"${not empty menu.parent.id?menu.name:'权限列表'}"},
            </c:forEach>];
		// 初始化树结构
		var tree = $.fn.zTree.init($("#menuTree"), setting, zNodes);
		// 默认选择节点
		var ids = "${role.menuIds}".split(",");
		for(var i=0; i<ids.length; i++) {
			var node = tree.getNodeByParam("id", ids[i]);
			try{tree.checkNode(node, true, false);}catch(e){}
		}
		// 默认展开全部节点
		tree.expandAll(true);
		
		// 用户-机构
		var zNodes2=[
			<c:forEach items="${officeList}" var="office">
					{id:'${office.id}', pId:'${not empty office.parent?office.parent.id:0}', name:"${office.name}"},
        	</c:forEach>];
		// 初始化树结构
		var tree2 = $.fn.zTree.init($("#officeTree"), setting, zNodes2);
		// 不选择父节点
		tree2.setting.check.chkboxType = { "Y" : "s", "N" : "s" };
		// 默认选择节点
		var ids2 = "${role.officeIds}".split(",");
		for(var i=0; i<ids2.length; i++) {
			var node = tree2.getNodeByParam("id", ids2[i]);
			try{tree2.checkNode(node, true, false);}catch(e){}
		}
		// 默认展开全部节点
		tree2.expandAll(true);
		// 刷新（显示/隐藏）机构
		refreshOfficeTree();
		$("#dataScope").change(function(){
			refreshOfficeTree();
		});
	});
	
	function refreshOfficeTree(){
		// 按明细设置
		if($("#dataScope").val()==9){ 
			$("#officeTree").show();
		}else{
			$("#officeTree").hide();
		}
	}
	
	function resetForm(){

	}
	</script>
</head>
<div class="main-container" id="main-container">
	<div class="main-content-inner">
		<div class="main-content">
			<div class="page-content">
				<tags:message content="${message}"/>
				<div class="row">
					<div class="tabbable">
						<ul class="nav nav-tabs padding-18 tab-size-bigger" id="myTab">
							<li>
								<a href="${ctx}/sys/role/">
									<i class="blue ace-icon fa fa-align-justify bigger-120"></i>
									角色列表
								</a>
							</li>
			
							<li class="active">
								<a href="#roleDetail">
									<i class="green ace-icon fa fa-user bigger-120"></i>
									角色添加
								</a>
							</li>
						</ul>
						<div class="tab-content">
							<div id="roleDetail" class="tab-pane in active">
								<form:form id="inputForm" modelAttribute="role" action="${ctx}/sys/role/save" method="post" class="form-horizontal">
									<div class="col-xs-12">
										<form:hidden path="id"/>
										<tags:message content="${message}"/>
										<shiro:hasPermission name="sys:role:edit">
										<div class="form-actions center">
											<button id="btnSubmit" class="btn btn-info" type="submit">
												<i class="ace-icon fa fa-check bigger-110"></i>
												保存
											</button>
											&nbsp; &nbsp; &nbsp;
											<button type="reset" class="btn">
												<i class="ace-icon fa fa-undo bigger-110"></i>
												重置
											</button>
										</div>
										</shiro:hasPermission>
										<div class="form-group">
											<label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 归属机构 </label>
											<div class="col-sm-9">
												 <tags:treeselect id="office" name="office.id" value="${role.office.id}" labelName="office.name" labelValue="${role.office.name}"
													title="机构" url="/sys/office/treeData" cssClass="col-xs-10 col-sm-5"/>
											</div>
										</div>
										<div class="form-group">
											<label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 角色名称</label>
											<div class="col-sm-9">
												<div class="clearfix">
													<input type="text" name="name" class="col-xs-10 col-sm-5" value="${role.name}"/>
												</div>
											</div>
										</div>
										<div class="form-group">
											<label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 数据范围</label>
											<div class="col-sm-9">
												<form:select path="dataScope" class="col-xs-10 col-sm-5">
												<form:options items="${fns:getDictList('sys_data_scope')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
												</form:select>
											</div>
										</div>
										<div class="form-group">
											<label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 角色授权</label>
									
											<div class="col-sm-9">
												<div id="menuTree" class="ztree" style="margin-top:3px;float:left;"></div>
												<form:hidden path="menuIds"/>
												<div id="officeTree" class="ztree" style="margin-left:100px;margin-top:3px;float:left;"></div>
												<form:hidden path="officeIds"/>
											</div>
										</div>
									</div>
								</form:form>
							</div>
						</div>
					</div>
				</div>
			</div>	
		</div>	
	</div>	
</div>	
</html>
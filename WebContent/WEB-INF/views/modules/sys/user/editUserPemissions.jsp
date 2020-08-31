<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<script src="${ctxStatic}/jquery/jquery.form.js" type="text/javascript"></script>
	<title>用户权限分配</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treeview.jsp" %>
	<script type="text/javascript">
		$(document).ready(function(){
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
					{id:'${menu.id}', pId:'${not empty menu.parent.id?menu.parent.id:0}', name:"${not empty menu.parent.id?menu.name:'权限列表'}"
						,checked:${menu.isChecked}
						,chkDisabled:${menu.isDisable}
					},
	            </c:forEach>];
			// 初始化树结构
			var tree = $.fn.zTree.init($("#menuTree"), setting, zNodes);
			// 默认展开全部节点
			tree.expandAll(true);
			
            $("#btnSubmit").click(function (){
				var ids = [],treeObj = $.fn.zTree.getZTreeObj("menuTree");
            	// 遍历所有节点，恢复禁用状态为活动状态
            	var dsblNodes = treeObj.getNodesByParam("chkDisabled", true);
            	for (var i=0, l=dsblNodes.length; i < l; i++) {
            	    treeObj.setChkDisabled(dsblNodes[i], false);
            	    treeObj.updateNode(dsblNodes[i]);
            	}
            	var nodes = treeObj.getCheckedNodes(true);
            	// 遍历节点恢复禁用状态
            	for (var i=0, l=dsblNodes.length; i < l; i++) {
            	    treeObj.setChkDisabled(dsblNodes[i], true);
            	    treeObj.updateNode(dsblNodes[i]);
            	}
				console.log(nodes.length);
				for(var i=0; i<nodes.length; i++) {
					ids.push(nodes[i].id);
				}
                $("#menuIds").val(ids);
                $("#inputForm").ajaxSubmit({
                	success: function(data) {
                        if("1" == data){
                        	alert("权限设置成功！");
                    		parent.$.fancybox.close();
                        }else{
                        	alert("权限设置失败:"+data);
                        }
                    }
                });
                return false;
            });
		});
	</script>
</head>
<div class="main-container" id="main-container">
	<div class="main-content-inner">
		<div class="main-content">
			<div class="page-content">
				<tags:message content="${message}"/>
				<div class="row">
					<form id="inputForm" action="${ctx}/sys/user/saveUserPemissions" method="post" class="form-horizontal">
						<div class="col-xs-12">
							<input type="hidden" id="userId" name="userId" value="${userId}">
							<input type="hidden" id="menuIds" name="menuIds">
							<shiro:hasPermission name="sys:role:edit">
								<div class="clearfix form-actions">
									<div class="col-md-offset-3 col-md-9">
									<button id="btnSubmit" class="btn btn-info" type="button">
										<i class="ace-icon fa fa-check bigger-110"></i>
										保存
									</button>
									&nbsp; &nbsp; &nbsp;
									<button type="reset" class="btn" onclick="history.go(-1)">
										<i class="ace-icon fa fa-reply bigger-110"></i>
										返回
									</button>
									</div>
								</div>
							</shiro:hasPermission>
							<div class="form-group">
								<label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 用户授权:</label>
								<div class="col-sm-9">
									<div id="menuTree" class="ztree"></div>
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</div>
</html>
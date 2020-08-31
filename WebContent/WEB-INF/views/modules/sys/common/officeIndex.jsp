<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>机构管理</title> 
<meta name="decorator" content="default"/>
<%@include file="/WEB-INF/views/include/treeview.jsp" %>
<script type="text/javascript">
	var currentId = "";
  var key, 
	  tree, 
	  lastValue = "", 
	  nodeList = [],
	  setting = {
		async: {
			enable: true,
			url: "${ctx}/sys/office/ztreeDynamicData",
			autoParam:["id=itemId"]
		},  
		view:{selectedMulti:false},
		data:{
			// 官方建议:如果设置为 true，请务必设置 setting.data.simpleData 内的其他参数: idKey / pIdKey / rootPId
			simpleData:{ 
				enable:true,
				idKey: "id",
				pIdKey: "pId",
				rootPId: 1,
			}
		},
		view:{
			dblClickExpand: false,
			fontCss:function(treeId, treeNode) {
				return {"font-weight" : (!!treeNode.highlight) ? "bold" : "normal" };
			}
		},
		callback:{
			onClick:function(event, treeId, treeNode){
				currentId = treeNode.id;
				$("#officeDiv").load("${ctx}/sys/office/list?id="+treeNode.id, function(response) {
					  $('#officeDiv').html(response);
					});
			}
		}
	};
			  
	$(document).ready(function(){
		tree = $.fn.zTree.init($("#tree"), setting, null);
		key = $("#key");
		key.bind("focus", focusKey).bind("blur", blurKey).bind("change keydown cut input propertychange", searchNode);
		
		$("#btnExport").click(function(){
			if("" != currentId){
				$("#officeDiv").load("${ctx}/sys/office/form?id="+currentId+"&rebackId="+currentId, function(response) {
					  $('#officeDiv').html(response);
					});
			}
		});
		
		$("#btnImport").click(function(){
			if("" != currentId){
			$("#officeDiv").load("${ctx}/sys/office/form?parent.id="+currentId+"&rebackId="+currentId, function(response) {
				  $('#officeDiv').html(response);
				});
			}
		});

	});
	
  	function focusKey(e) {
		if (key.hasClass("empty")) {
			key.removeClass("empty");
		}
	}	  	
	function blurKey(e) {
		if (key.get(0).value === "") {
			key.addClass("empty");
		}
		searchNode(e);
	}		
	function searchNode(e) {
		// 取得输入的关键字的值
		var value = $.trim(key.get(0).value);
		
		// 按名字查询
		var keyType = "name";
		if (key.hasClass("empty")) {
			value = "";
		}			
		// 如果和上次一样，就退出不查了。
		if (lastValue === value) {
			return;
		}			
		// 保存最后一次
		lastValue = value;			
		// 如果要查空字串，就退出不查了。
		if (value === "") {
			return;
		}			
		updateNodes(false);
		nodeList = tree.getNodesByParamFuzzy(keyType, value);
		updateNodes(true);
	}		
	function updateNodes(highlight) {
		for(var i=0, l=nodeList.length; i<l; i++) {
			nodeList[i].highlight = highlight;				
			tree.updateNode(nodeList[i]);
			tree.expandNode(nodeList[i].getParentNode(), true, false, false);
		}
	}		
	function search() {
		$("#search").slideToggle(200);
		$("#key").focus();
	}
</script>

</head>
<body>
<div class="main-container" id="main-container">
	<div class="main-container-inner">
		<div class="main-content">
			<div class="page-content">
			
				<div class="row">
					<div class="col-sm-6">
								<div class="widget-box widget-color-blue2">
									<div class="widget-header">
										<h4 class="widget-title lighter smaller">选择机构</h4>
									</div>
									<div class="widget-body">
										<div class="widget-main padding-8">
											<p>
												<button id="btnExport" name="" type="button" class="btn btn-sm btn-success">
													<i class="ace-icon fa fa-pencil bigger-120"></i>
													编辑本级
												</button>
												<button id="btnImport" name="" type="button" class="btn btn-sm btn-success">
													<i class="ace-icon fa fa-plus bigger-120"></i>
													添加下级
												</button>
											</p>
											<!-- <ul id= "treeview"></ul> -->
											<div id="tree" class="ztree"></div>
										</div>
										
									</div>
								</div>
							</div>
						
							<div class="col-sm-6">
								<div class="widget-box">
									<div class="widget-header">
										<h4 class="widget-title">机构信息</h4>
									</div>
									<div class="widget-body">
										<div class="widget-main">
										<div id="officeDiv"></div>
										</div>
									</div>
								</div>
							</div>
					
				</div><!-- /row -->
			</div><!-- /.page-content -->
		</div><!-- /.main-content -->
	</div><!-- /.main-container-inner -->
</div><!-- /.main-container -->

</body>
</html>
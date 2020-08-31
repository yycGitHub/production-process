<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<head>
<title></title>
<script src="${ctxStatic}/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
<%@include file="/WEB-INF/views/include/treeview.jsp" %>
<script type="text/javascript">	
	var key, 
	  tree, 
	  lastValue = "", 
	  nodeList = [],
	  setting = {
		view:{selectedMulti:false},
		check:{enable:"${checked}", nocheckInherit:true},
		data:{
			// 官方建议:如果设置为 true，请务必设置 setting.data.simpleData 内的其他参数: idKey / pIdKey / rootPId
			simpleData:{ 
				enable:true,
				idKey: "id",
				pIdKey: "pId",
				rootPId: 0,
			}
		},
		view:{
			fontCss:function(treeId, treeNode) {
				return {"font-weight" : (!!treeNode.highlight) ? "bold" : "normal" };
			}
		},
		callback:{
			beforeClick:function(id, node){
				if("${checked}"){
					tree.checkNode(node, !node.checked, true, true);
					return false;
				}
			},
			onClick:function(event, treeId, treeNode){
				$("#gxcmsFrame", window.parent.document).attr("src",'${ctx}/cms/category/list?id='+treeNode.id);
			}
		}
	};
	  
	$(document).ready(function(){
		$.get("${ctx}/cms/category/allTreeData?t="+new Date().getTime(), function(zNodes){
			// 初始化树结构
			tree = $.fn.zTree.init($("#tree"), setting, zNodes);		
			// 默认展开一级节点
			var nodes = tree.getNodesByParam("level", 0);
			for(var i=0; i<nodes.length; i++) {
				tree.expandNode(nodes[i], true, false, false);
			}		
			// 默认选择节点
			var ids = "${selectIds}".split(",");
			for(var i=0; i<ids.length; i++) {
				var node = tree.getNodeByParam("id", ids[i]);
				if("${checked}"){
					try{tree.checkNode(node, true, true);}catch(e){}
					tree.selectNode(node, false);
				}else{
					tree.selectNode(node, true);
				}
			}
		});	
		key = $("#key");
		key.bind("focus", focusKey).bind("blur", blurKey).bind("change keydown cut input propertychange", searchNode);
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
		$("#txt").toggle();
		$("#key").focus();
	}
</script>
</head>
<body>
<!-- 	<div id="search">
		<input type="text" class="empty" id="key" name="key" maxlength="50">
	</div> -->
	<div id="tree" class="ztree"></div>
</body>
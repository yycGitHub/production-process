<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ attribute name="id" type="java.lang.String" required="true" description="编号"%>
<%@ attribute name="name" type="java.lang.String" required="true" description="隐藏域名称（ID）"%>
<%@ attribute name="value" type="java.lang.String" required="true" description="隐藏域值（ID）"%>
<%@ attribute name="labelName" type="java.lang.String" required="true" description="输入框名称（Name）"%>
<%@ attribute name="labelValue" type="java.lang.String" required="true" description="输入框值（Name）"%>
<%@ attribute name="title" type="java.lang.String" required="true" description="选择框标题"%>
<%@ attribute name="url" type="java.lang.String" required="true" description="树结构数据地址"%>
<%@ attribute name="checked" type="java.lang.Boolean" required="false" description="是否显示复选框"%>
<%@ attribute name="extId" type="java.lang.String" required="false" description="排除掉的编号（不能选择的编号）"%>
<%@ attribute name="notAllowSelectRoot" type="java.lang.Boolean" required="false" description="不允许选择根节点"%>
<%@ attribute name="notAllowSelectParent" type="java.lang.Boolean" required="false" description="不允许选择父节点"%>
<%@ attribute name="module" type="java.lang.String" required="false" description="过滤栏目模型（只显示指定模型，仅针对CMS的Category树）"%>
<%@ attribute name="selectScopeModule" type="java.lang.Boolean" required="false" description="选择范围内的模型（控制不能选择公共模型，不能选择本栏目外的模型）（仅针对CMS的Category树）"%>
<%@ attribute name="allowClear" type="java.lang.Boolean" required="false" description="是否允许清除"%>
<%@ attribute name="cssClass" type="java.lang.String" required="false" description="css样式"%>
<%@ attribute name="cssStyle" type="java.lang.String" required="false" description="css样式"%>
<%@ attribute name="disabled" type="java.lang.String" required="false" description="是否限制选择，如果限制，设置为disabled"%>
<%@ attribute name="nodesLevel" type="java.lang.String" required="false" description="菜单展开层数"%>
<%@ attribute name="nameLevel" type="java.lang.String" required="false" description="返回名称关联级别"%>
<%@ attribute name="placeholder" type="java.lang.String" required="false" description="默认描述项"%>
<%@ attribute name="dynamicTree" type="java.lang.Boolean" required="false" description="是否动态树打开"%>
<%@ attribute name="callbackJsFunction" type="java.lang.String" required="false" description="回调的js函数"%>
<%@ attribute name="icon" type="java.lang.String" required="false" description="图标icon"%>
<input id="${id}Id" name="${name}" class="${cssClass}" type="hidden" value="${value}"${disabled eq 'true' ? ' disabled=\'disabled\'' : ''}/>																															
<input id="${id}Name" name="${labelName}" readonly="readonly" type="text" value="${labelValue}" maxlength="50"${disabled eq "true"? " disabled=\"disabled\"":""}"
	class="${cssClass}" style="${cssStyle}" placeholder="${placeholder}"/> 
<div class="input-group-btn">
	<button type="button" id="${id}Button" class="btn btn-primary btn-sm">
		<i class="ace-icon fa ${icon eq null ? 'fa-search' : icon} icon-on-right bigger-110"></i>
	</button>
</div>		
<div id="dialog-${id}" class="hide">
		<iframe name="${id}-iframe" id="${id}-iframe" width="100%" height="100%" marginheight="0" marginwidth="0" frameborder="0" 
			scrolling="auto" src="${ctx}/tag/treeselect?url=${url}&module=${module}&checked=${checked}&extId=${extId}&nodesLevel=${nodesLevel}&dynamicTree=${dynamicTree}&selectIds="+ $("#${id}Id").val()>
		</iframe>
</div>
<script type="text/javascript">
	$("#${id}Button").click(function(){
		// 是否限制选择，如果限制，设置为disabled
		if ($("#${id}Id").attr("disabled")){
			return true;
		}
        var nameLevel = ${nameLevel eq null ? "1" : nameLevel};        
		// 正常打开	
		bootbox.dialog({ 
			message: $("#dialog-${id}").html(),
			title: "选择上级菜单 ",
	        buttons:             
	        {
	            "success" :
	             {
	                "label" : "<i class='icon-ok'></i> 确定",
	                "className" : "btn-sm btn-success",
	                "callback": function() {
					var ids = [], names = [], nodes = [],
					tree = document.getElementsByName("${id}-iframe")[1].contentWindow.tree; 
					if ("${checked}"){
						nodes = tree.getCheckedNodes(); //省略checked参数，等同于 true
					}else{
						nodes = tree.getSelectedNodes();
					}
					for(var i=0; i<nodes.length; i++) {
						ids.push(nodes[i].id);
						
                        var t_name = "",
                        	  t_node = nodes[i],
                        	  name_l = 0;
                        
                        do{
                            name_l++;
                            t_name = t_node.name + " " + t_name;
                            t_node = t_node.getParentNode();
                        }while(name_l < nameLevel);
                        
						names.push(t_name);
						break; 
					}					
					$("#${id}Id").val(ids);
					$("#${id}Name").val(names);
	                }
	            },
		        "cancel": {
		            "label" : "<i class='icon-info'></i> 取消",
		            "className" : "btn-sm btn-danger"
	            }
	        }
		});
	});
</script>

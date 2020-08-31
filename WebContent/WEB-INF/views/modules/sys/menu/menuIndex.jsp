<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>菜单管理</title> 
	<meta name="decorator" content="default"/>
</head>
<div class="main-container" id="main-container">
	<div class="main-content-inner">
		<div class="main-content">
			<div class="page-content">
				<tags:message content="${message}"/>
				<div class="row">
					<div class="col-xs-12">
						<!-- PAGE CONTENT BEGINS -->
						<div class="row">
							<div class="col-sm-6">
								<div class="widget-box widget-color-blue2">
									<div class="widget-header">
										<h4 class="widget-title lighter smaller">选择菜单</h4>
									</div>
									<div class="widget-body">
										<div class="widget-main padding-8">
											<p>
												<button class="btn btn-white btn-info btn-bold" id="add-button">
													<i class="ace-icon fa fa-floppy-o bigger-120 blue"></i>
													添加下级菜单
												</button>
			
												<button class="btn btn-white btn-warning btn-bold" id="delete-button">
													<i class="ace-icon fa fa-trash-o bigger-120 orange"></i>
													删除
												</button>
											</p>
											<ul id= "treeview"></ul>
										</div>
									</div>
								</div>
							</div>
						
							<div class="col-sm-6">
								<div class="widget-box">
									<div class="widget-header">
										<h4 class="widget-title">菜单信息</h4>
									</div>
									<div class="widget-body">
										<div class="widget-main" id="menuDiv">
										</div>
									</div>
								</div>
							</div>
						</div>
					<!-- PAGE CONTENT ENDS -->
					</div><!-- /.col -->
				</div><!-- /.row -->
			</div>
		</div>
	</div>
</div>
<script type ="text/javascript" >
	var selectItems = new Array();
	 $(function () {
         var remoteUrl = '${ctx}/sys/menu/menuTreeData' ;//动态树数据请求接口
         var remoteDateSource = function (options, callback) {
              var parent_id = null
              if ( !('text' in options || 'type' in options) ){
                 parent_id = 1; //load first level data
             }
              else if ('type' in options && options['type' ] == 'folder' ) { //it has children
                  if ('additionalParameters' in options)
                      parent_id = options.additionalParameters['id']
             }
             if (parent_id !== null) {
                 $.ajax({
                      url: remoteUrl,
                      data: 'itemId=' +parent_id,
                      type: 'POST' ,
                      dataType: 'json' ,
                      success : function (response) {
                           if (response.status == "OK" )
                              callback({ data: response.data })
                      },
                      error: function (response) {
                           //console.log(response);
                      }
                 })
             }
        }

        $('#treeview').ace_tree({
             dataSource: remoteDateSource,
             multiSelect: false,
             loadingHTML: '<div class="tree-loading"><i class="ace-icon fa fa-refresh fa-spin blue"></i></div>',
             'open-icon' : 'ace-icon tree-minus hide',
             'close-icon' : 'ace-icon tree-plus hide',
             'selectable' : true,
             'selected-icon' : 'ace-icon fa fa-check',
             'unselected-icon' : 'ace-icon fa fa-times',
             'cacheItems': false,
             'folderSelect': true,
             'folder-open-icon' : 'ace-icon tree-plus',
             'folder-close-icon' : 'ace-icon tree-minus'
        }).on('selected.fu.tree', function(e, data) {
        	selectItems = data;
//     		$('#menuDiv').ace_ajax({
// 				content_url: function(url) {
// 					return "${ctx}/sys/menu/form?id="+data.selected[0].id;
// 				},
// 				default_url: '${ctx}/sys/menu/form'
// 			});
        	loadPage("${ctx}/sys/menu/form?id="+selectItems.selected[0].id,"#menuDiv");  
        });
     
         //show selected items inside a modal
        $('#submit-button' ).on('click' , function () {
              var output = '' ;
              var items = $('#treeview' ).tree('selectedItems' );
              for (var i in items) if (items.hasOwnProperty(i)) {
                  var item = items[i];
                 output += item.additionalParameters['id' ] + ":"+ item.text+"\n" ;
             }
             
            $( '#modal-tree-items' ).modal('show' );
            $( '#tree-value' ).css({'width' :'98%' , 'height' :'200px' }).val(output);
        
        });
        
        $( '#add-button' ).on('click' , function () {
        	var parentId;
        	if(selectItems.length == 0 || selectItems.selected.length != 1)
        		parentId = 0
        	else
        		parentId = selectItems.selected[0].id;

        	loadPage("${ctx}/sys/menu/form?parent.id="+parentId,"#menuDiv");  
        	return true;
        });
        
        $( '#delete-button' ).on('click' , function () {
        	if(selectItems.length == 0 || selectItems.selected.length != 1)
        	{
        		bootbox.alert("请选择一个节点进行编辑！");
        		return false;
        	}
        	bootbox.confirm({ 
        		  size: "small",
        		  message: "你确定删除["+selectItems.selected[0].text+"]节点吗?", 
        		  callback: function(result){ 
        			 if(result)
        			 {
        				self.location.href="${ctx}/sys/menu/delete?id="+selectItems.selected[0].id
        			 }
        		  }
       		})
        });
	 });
</script>
</html>
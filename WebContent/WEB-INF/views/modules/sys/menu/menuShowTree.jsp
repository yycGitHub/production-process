<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<head>
<title>菜单管理</title>
<meta name="decorator" content="default"/>
<script src="${ctxStatic}/assets/js/tree.min.js"></script>
</head>
<body class="no-skin">
	<div class="row">
		<div class="col-xs-12">
			<!-- PAGE CONTENT BEGINS -->
			<div class="row">
				<div class="col-sm-6">
					<div class="widget-box widget-color-blue2">
						<div class="widget-header">
							<h4 class="widget-title lighter smaller">Choose Categories</h4>
						</div>
	
						<div class="widget-body">
							<div class="widget-main padding-8">
								<ul id= "treeview"></ul>
							</div>
						</div>
					</div>
				</div>
	
				<div class="col-sm-6">
					<div class="widget-box widget-color-green2">
						<div class="widget-header">
							<h4 class="widget-title lighter smaller">
								Browse Files
								<span class="smaller-80">(with selectable folders)</span>
							</h4>
						</div>
	
						<div class="widget-body">
							<div class="widget-main padding-8">
								<ul id="tree2"></ul>
							</div>
						</div>
					</div>
				</div>
			</div>
	
			<!-- PAGE CONTENT ENDS -->
		</div><!-- /.col -->
	</div><!-- /.row -->
	<script type ="text/javascript" >
	 $( function () {
         var remoteUrl = '${ctx}/sys/menu/menuTreeData' ;//动态树数据请求接口
         var remoteDateSource = function (options, callback) {
              var parent_id = null
              if ( !('text' in options || 'type' in options) ){
                 parent_id = 1; //load first level data
             }
              else if ('type' in options && options['type' ] == 'folder' ) { //it has children
                  if ('additionalParameters' in options && 'children' in options.additionalParameters)
                      parent_id = options.additionalParameters['id' ]
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

        $( '#treeview' ).ace_tree({
             dataSource: remoteDateSource ,
             multiSelect: true ,
             loadingHTML: '<div class="tree-loading"><i class="ace-icon fa fa-refresh fa-spin blue"></i></div>',
             'open-icon' : 'ace-icon tree-minus',
             'close-icon' : 'ace-icon tree-plus',
             'selectable' : true ,
             'selected-icon' : 'ace-icon fa fa-check',
             'unselected-icon' : 'ace-icon fa fa-times',
             cacheItems: true ,
             folderSelect: false
        });
        
        
         //show selected items inside a modal
        $( '#submit-button' ).on('click' , function () {
              var output = '' ;
              var items = $('#treeview' ).tree('selectedItems' );
              for (var i in items) if (items.hasOwnProperty(i)) {
                  var item = items[i];
                 output += item.additionalParameters['id' ] + ":"+ item.text+"\n" ;
             }
             
            $( '#modal-tree-items' ).modal('show' );
            $( '#tree-value' ).css({'width' :'98%' , 'height' :'200px' }).val(output);
        
        });
        
         if (location.protocol == 'file:' ) alert("For retrieving data from server, you should access this page using a webserver.");
    });
	</script>
</body>
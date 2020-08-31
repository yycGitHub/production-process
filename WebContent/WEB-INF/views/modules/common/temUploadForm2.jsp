<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>文件导入管理</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/zn/css/progress.css" />
	<script type="text/javascript" src="${ctxStatic}/jquery/jquery.form.js"></script>
	
	<script type="text/javascript">
	var oTimer = null;
	
		$(document).ready(function() {
			$("#process").hide();
			$("#downloadfileDiv").hide();
			$("#resultMessageDiv").hide();
			$("#inputForm").validate();
			$("#file").change(function(){
				var filePath = $(this).val();
				$("#dataFile").val(filePath);
			});
			
			$("#import").click(function(){
				$("#formImport").ajaxSubmit(function(data){
					clearInterval(oTimer);
					$("#process").hide();
					if(null != data && "" != data && data.indexOf("result") > 0 ){
						$("#downloadfileDiv").html("数据导入结果详情已存入文件，<a class='min_btn btn_secondary marright' href='" + 
								"${ctx}/common/download?fileName=" + data + "'>请下载</a>");
						$("#downloadfileDiv").show();
					}else{
						$("#resultMessageDiv").html("<span class='red font18 martop'>" + data + "</span>");
						$("#resultMessageDiv").show();
					}
				});
				$("#process").show();
			    //进度和百分比
			    $("#progress_percent").text("0%");
			    $("#progress_bar").width("0%");
				oTimer = setInterval("getProgress()", 10000);
				setTimeout('setButtonDisabled()',10);
			});
		});
		
		function setButtonDisabled(){
			id = "import";
			$("#"+id).attr("class","min_btn btn_default upload_btn mouse_arrow");
			$("#"+id).attr("disabled","disabled");
		}
		
		function getProgress() {
			var now = new Date();
		    
		    $.post("${ctx}/common/getProgress?time="+now.getTime(), function(data) {
		    	$("#progress_percent").text(data + '%');
	            $("#progress_bar").width(data + '%');
		    	
			 });
		}
	</script>
	
</head>
<body>
<div class="fl pdleft pdtop">
	<form id="formImport" action="${ctx}/common/importFile?type=${type}" enctype="multipart/form-data"  method="post">
		<div class="process clearfix martop marleft" id="process">
			<span class="progress-box">
			<span class="progress-bar" style="width: 0%;" id="progress_bar"></span>
			</span>
            <span id="progress_percent">0%</span>
        </div>
		<div class="clearfix martop marleft" id="downloadfileDiv">
        </div>
		<div class="clearfix martop marleft" id="resultMessageDiv">
        </div>
        
        <span class="upload_con martop marleft">
			<input class="input_txt " id="dataFile"/>
			<a href="javascript:void(0)" class="min_btn btn_primary upload_btn" >浏览文件</a>
		    <input type="file"  class="input_file w330" id="file" name="file" />
		</span>
		<span class="upload_con martop marleft">&nbsp;&nbsp;<a href="javascript:void(0)" class="min_btn btn_primary upload_btn" id="import">导入</a></span>
		<span class="upload_con martop marleft">&nbsp;&nbsp;<a href="${ctx}/common/downLoadTemp?type=${type}" class="min_btn btn_primary upload_btn">下载模板</a></span>
	
	</form>
</div>
</body>
</html>

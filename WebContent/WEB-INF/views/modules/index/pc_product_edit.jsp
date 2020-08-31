<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="renderer" content="webkit">
	<title>产品编辑</title>
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/zn/css/base.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/zn/css/s+.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/pcindex/css/s.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/pcindex/css/base.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/pcindex/css/choice.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/pcindex/css/style.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/jquery-validation/1.11.1/jquery.validate.min.css"/>
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/picker/css/image-picker.css">
	
	<style type="text/css">
		html, body {
		    width: 100%;
		    height: 100%;
		    overflow:scroll;
		    overflow-x:hidden;
		    overflow-y:hidden;
		}
		
		.step-body{
			width: 100%;
		    height: 100%;
			overflow:scroll;
		}
		
		.maxbox {
		    width: 75%;
    		padding-top: 15px;
		}
		
		.maxbox1 {
		    width: 25%;
		    padding-top: 15px;
		}
		
		.input_file {
		    position: absolute;
		    left: -8px;
		    top: 2px;
		    padding: 7px 0;
		    cursor: pointer;
		    z-index: 1;
		    outline: none;
		    opacity: 0;
		    filter: alpha(opacity=0);
		}
		
		.upload_con .upload_txt_show {
		    position: absolute;
		    left: 5px;
		    top: 22px;
		    z-index: 10;
		}
		
		.step-header {
		    width: 100%;
		    margin: 0 auto;
		    margin-left: 19%;
		    margin-bottom: 40px;
		}
		
		.rollbox {
		    position: relative;
		    float: left;
		    margin: 20 10px;
		    height: 285px;
		    overflow: hidden;
		}
		
		.roll_main {
		    -webkit-transition: all 0.5s ease;
		    -moz-transition: all 0.5s ease;
		    -ms-transition: all 0.5s ease;
		    -o-transition: all 0.5s ease;
		    transition: all 0.5s ease;
		    margin-left: 80px;
		}
		
		.roll_main li {
		    float: left;
		    width: 175px;
		    text-align: center;
		}
		
		.roll_main .photo, .roll_main li em {
		    display: block;
		    margin: 0 0 5px 0;
		}

		.picker{
			text-align: center;
		}
		
		ul.thumbnails.image_picker_selector{overflow:auto;list-style-image:none;list-style-position:outside;list-style-type:none;padding:0px;margin-left:40px;margin-top:60px;}
		
		.image_picker_image{
			width:150px;
			border: 1px solid #eadede;
		}
		
		.input_txt1 {
		    padding: 7px 0;
		    height: 16px;
		    line-height: 16px;
		}
		
		ul.thumbnails.image_picker_selector {
		    overflow: auto;
		    list-style-image: none;
		    list-style-position: outside;
		    list-style-type: none;
		    padding: 0px;
		    margin-left: 10px;
		    margin-top: 10px;
		}
	</style>
</head>
<body>
	<div id="step_demo" class="step-body" style="padding-top:30px;">
	    <div class="step-content" align="center">
	        <div class="step-list" style="width:80%;overflow: hidden;">
	        	<form:form id="productForm" modelAttribute="product" action="" method="post" enctype="multipart/form-data" class="form-horizontal">
					<form:hidden path="id"/>
					<form:hidden path="token" value='${token}'/>
					<form:hidden path="productPic"/>
					<table class="tab" cellpadding="0" cellspacing="0">
						<tbody>
							<tr>
								<td align="right" class="w_25"><span class="red font18 martop">&lowast;&nbsp;</span>公司名称：</td>
								<td align="left">
									<select id="officeListId"  class="area h80 w400 required" style="font-size:15px;height: 34px;min-width: 400px;">
										<c:forEach items="${officeList}" var="item">
											<option <c:if test="${item.id == officeId}">selected</c:if> value='${item.id}'>${item.name}</option>
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr>
								<td align="right" class="w_15"><span class="red font18 martop">&lowast;&nbsp;</span>产品名称：</td>
								<td align="left" class="w_85"><form:input path="productName" maxlength="32" htmlEscape="false" cssClass="input_txt1 required w400"/></td>
							</tr>
							<tr>
								<td align="right"><span class="red font18 martop">&lowast;&nbsp;</span>产品简介：</td>
								<td align="left">
									<form:textarea class="area h80 w400 required" path="productDiscription" maxlength="500"></form:textarea>
								</td>
							</tr>
							<tr>
								<td align="right"><span class="red font18 martop">&lowast;&nbsp;</span>产品图片：</td>
								<td align="left">
									<div style="min-height:1px;line-height:200px;text-align:align;position:relative;">
										<img src="${product.productPicUrl}" id="pic" style="width: 200px;height: 200px;margin-bottom:10px" onerror="onerror=null;src='${ctxStatic}/pcindex/images/default.jpg'">
										<div class="" style="width:140px;height:32px;border-radius: 4px;background-color:#ff8a00;color: #FFFFFF;font-size: 14px;text-align:center;line-height:32px;outline:none;margin-left:33px;position:relative;">
											点击上传产品图片
											<input type="file" id="file" name="file" style="cursor:pointer;opacity:0;filter:alpha(opacity=0);width:100%;height:100%;position:absolute;top:0;left:0;" onchange="common_handleFiles(this,'',5120);"/>
										</div>
									</div>
								</td>
							</tr>
							<tr>
								<td align="right"><span class="red font18 martop">&lowast;&nbsp;</span>选择主题：</td>
								<td align="left">
									<div class="picker">
										<select id="themeId" class="image-picker show-labels">
											<c:forEach items="${themeList}" var="theme">
												<c:if test="${theme.id == product.themeId}">
													<option data-img-src='${theme.themeImgUrl}' selected="selected" style="width:150px" value='${theme.id}'>${theme.themeName}</option>
												</c:if>
												<c:if test="${theme.id != product.themeId}">
													<option data-img-src='${theme.themeImgUrl}' style="width:150px" value='${theme.id}'>${theme.themeName}</option>
												</c:if>
											</c:forEach>
										</select>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</form:form>
				<div class="tab_btn_show">
					<input class="btn btn_primary" id="saveBtn" type="button" value="保存"/>
				</div>
	        </div>
	    </div>
	</div>
	
	<script type="text/javascript" src="${ctxStatic}/jquery/jquery-1.9.1.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/layer/layer.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jquery-validation/1.11.1/jquery.validate.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jquery-validation/1.11.1/jquery.validate.method.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/zn/js/common_file.js"></script>
	<script type="text/javascript" src="${ctxStatic}/zn/js/common_file1.js"></script>
	<script type="text/javascript" src="${ctxStatic}/pcindex/js/jquery.SuperSlide2.1.2.js"></script>
	<script type="text/javascript" src="${ctxStatic}/picker/js/image-picker.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
	
	<script type="text/javascript">
		$(document).ready(function() {
			$("select.image-picker.show-labels").imagepicker({
				hide_select:true, 
				show_label:true
			});
			
			var themeId = '${product.themeId}';
			$("#themeId").find("option").each(function(index,element){
				var value = $(this).val();
				$(".thumbnails.image_picker_selector").find("li").eq(index).find("div").removeClass("selected");
				if(value == themeId){
					$(".thumbnails.image_picker_selector").find("li").eq(index).find("div").addClass("selected");
				}
			});
			
			$("#saveBtn").click(function(event) {
		    	if($("#productForm").valid()){
					$("#saveBtn").attr("class","btn btn_default mouse_arrow");
					$("#saveBtn").attr("disabled","disabled");
					var value = $("#themeId option:selected").val();
					var url = '${pageContext.request.contextPath}/api/pcIndex/firstSave';
					var param = {
						token: '${token}',
						productId: '${product.id}',
						productPic: $("#productPic").val(),
						productName: $("#productName").val(),
						productDiscription: $("#productDiscription").val(),
						themeId: value,
						officeId: $("#officeListId").val()
			    	};
					$.get(url, param, function(res) {
						$("#saveBtn").attr("class","btn btn_primary");
						$("#saveBtn").removeAttr("disabled");
						if(res.code==0){
							layer.alert('编辑成功', {icon: 1,title: "提示",yes:function(){
								parent.$("#handle").val('2');
								var index = parent.layer.getFrameIndex(window.name); 
								parent.layer.close(index);
							}});
						}else{
							layer.alert(res.message, {icon: 2,title: "提示"});
						}
					});
				}
		    });
		});
		
		$("#file").change(function(){  
			 var objUrl = getObjectURL(this.files[0]) ;//获取文件信息  
			 console.log("objUrl = "+objUrl);  
			 if (objUrl) {  
			     upload(objUrl);
			 }   
		}); 
		
		function upload(objUrl){  
			var picFile = document.getElementById("file"); 
			var files = picFile.files;
			if(files.length==0){
				layer.alert('请先上传产品图片', {icon: 2,title: "提示"});
				return;
			}
			var formData = new FormData($("#productForm")[0]);  //重点：要用这种方法接收表单的参数  
		    $.ajax({  
		        url : "${pageContext.request.contextPath}/api/pcIndex/uploadImage",  
		        type : 'POST',  
		        data : formData,  
		        // 告诉jQuery不要去处理发送的数据  
		        processData : false,                   
		        // 告诉jQuery不要去设置Content-Type请求头  
		        contentType : false,  
		        async : false,  
		        success : function(data) {  
		        	if(data.indexOf("|")>-1){
		        		var arr = data.split("|");
		        		$("#productPic").val(arr[1]);
		        		$("#pic").attr("src", objUrl);
		        		layer.alert('上传成功', {icon: 1,title: "提示"});
		        	}else{
		        		$("#productPic").val('');
		        		$("#pic").attr("src", '');
		        		layer.alert('上传失败', {icon: 2,title: "提示"});
		        	}
		        }  
		    });  
		}  
		
	</script>
</body>
</html>

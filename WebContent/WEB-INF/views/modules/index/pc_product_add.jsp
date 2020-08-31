<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="renderer" content="webkit">
	<title>产品新增</title>
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/zn/css/base.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/zn/css/s+.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/pcindex/css/s.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/pcindex/css/base.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/pcindex/css/choice.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/pcindex/css/style.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/zn/js/layui/css/layui.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/zn/js/layui/extend/steps/style.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/jquery-validation/1.11.1/jquery.validate.min.css"/>
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/picker/css/image-picker.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/sucail/css/style.css">
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
		    height: 30px;
		    line-height: 30px;
		}
		
		.switch {
            width: 45px;
            height: 20px;
            position: relative;
            border: 1px solid #dfdfdf;
            background-color: #fdfdfd;
            box-shadow: #dfdfdf 0 0 0 0 inset;
            border-radius: 20px;
            background-clip: content-box;
            display: inline-block;
            -webkit-appearance: none;
            user-select: none;
            outline: none;
            margin-left:10px;
        }
        .switch:before {
            content: '';
            width: 18px;
            height: 18px;
            position: absolute;
            top: 0;
            left: 0;
            border-radius: 12px;
            background-color: #fff;
            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.4);
        }
        .switch:checked {
            border-color: #64bd63;
            box-shadow: #64bd63 0 0 0 16px inset;
            background-color: #64bd63;
        }
        .switch:checked:before {
            left: 25px;
        }
        .switch.switch-anim {
            transition: border cubic-bezier(0, 0, 0, 1) 0.4s, box-shadow cubic-bezier(0, 0, 0, 1) 0.4s;
        }
        .switch.switch-anim:before {
            transition: left 0.3s;
        }
        .switch.switch-anim:checked {
            box-shadow: #64bd63 0 0 0 16px inset;
            background-color: #64bd63;
            transition: border ease 0.4s, box-shadow ease 0.4s, background-color ease 1.2s;
        }
        .switch.switch-anim:checked:before {
            transition: left 0.3s;
        }
	</style>
</head>
<body>
	<div id="step_demo" class="step-body" style="padding-top:30px;">
		<div class="step-header" style="width:80%;overflow: hidden;pointer-events: none;">
			<!-- <div class="step-header" style="width:80%;overflow: hidden;"> -->
	        <ul>
	            <li>
	                <span class="step-name">填写产品</span>
	            </li>
	            <li>
	                <span class="step-name">选择主题</span>
	            </li>
	            <li>
	                <span class="step-name">录入数据</span>
	            </li>
	        </ul>
	    </div>
	    
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
										<option value=''>请选择公司</option>
										<c:forEach items="${officeList}" var="item">
											<option <c:if test="${item.id == officeId}">selected</c:if> value='${item.id}'>${item.name}</option>
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr>
								<td align="right" class="w_25"><span class="red font18 martop">&lowast;&nbsp;</span>产品名称：</td>
								<td align="left" class="w_75"><form:input path="productName" maxlength="32" htmlEscape="false" cssClass="input_txt1 required w400"/></td>
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
										<img src="" id="pic" style="width: 200px;height: 200px;margin-bottom:10px" onerror="onerror=null;src='${ctxStatic}/pcindex/images/default.jpg'">
										<div class="" style="width:140px;height:32px;border-radius: 4px;background-color:#ff8a00;color: #FFFFFF;font-size: 14px;text-align:center;line-height:32px;outline:none;margin-left:33px;position:relative;">
											点击上传产品图片
											<input type="file" id="file" name="file" style="cursor:pointer;opacity:0;filter:alpha(opacity=0);width:100%;height:100%;position:absolute;top:0;left:0;" onchange="common_handleFiles(this,'',5120);"/>
										</div>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</form:form>
				<div class="tab_btn_show">
					<input class="btn btn_primary" id="saveBtn1" type="button" value="下一步"/>
				</div>
	        </div>
	        <div class="step-list">
	        	<div id="container" align="center">
					<div class="picker">
						<select id="themeId" class="image-picker show-labels">
							<c:forEach items="${themeList}" var="theme">
								<option data-img-src='${theme.themeImgUrl}' style="width:150px" value='${theme.id}'>${theme.themeName}</option>
							</c:forEach>
						</select>
					</div>
				</div>
	        	<div class="tab_btn_show">
					<input class="btn btn_primary" id="saveBtn2" type="button" value="下一步"/>
				</div>
	        </div>
	        <div class="step-list">
		    	<div class="suCai17-content" align="center">
		            <div class="suCai17-background clearfix" align="left">
		            	<form id="sxForm" action="" method="post" enctype="multipart/form-data" class="form-horizontal">
		                <div class="suCai17-info bd">
		                </div>
		                </form>
		            </div>
					<div class="tab_btn_show">
						<input class="btn btn_primary" id="saveBtn3" type="button" value="完成"/>
					</div>
		        </div>
		    </div>
	    </div>
	</div>
	
	<script type="text/javascript" src="${ctxStatic}/jquery/jquery-1.9.1.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/zn/js/layui/layui.js"></script>
	<script type="text/javascript" src="${ctxStatic}/layer/layer.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jquery-validation/1.11.1/jquery.validate.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jquery-validation/1.11.1/jquery.validate.method.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/zn/js/common_file.js"></script>
	<script type="text/javascript" src="${ctxStatic}/zn/js/common_file1.js"></script>
	<script type="text/javascript" src="${ctxStatic}/pcindex/js/sureserve.js"></script>
	<script type="text/javascript" src="${ctxStatic}/pcindex/js/jquery.SuperSlide2.1.2.js"></script>
	<script type="text/javascript" src="${ctxStatic}/picker/js/image-picker.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
	
	<script type="text/javascript">
		var result = new Array();
		var rjresult = new Array();
		var array = new Array();
		var sxmcArr = new Array();
		var mkArr = new Array();
		var rjArr = new Array();
		var p=0;
		var modelId = "";
		$(document).ready(function() {
		    $("select.image-picker.show-labels").imagepicker({
				hide_select:true, 
				show_label:true
			});
		    
		    jQuery(".suCai17-background").slide({
                autoPlay: false,
                trigger: "click",
                easing: "easeOutCirc",
                delayTime: 1000
            });

			/* $(".suCai17-nav-list").on('click', 'a', function(event) {
			    event.preventDefault;
			    var parent=$(this).parent();
			    var parents=$(this).parents(".suCai17-nav-list");
			    var len=parents.children().length;
			    if($(this).is(".up") && parent.index()==0){
					alert("已经到顶了！");
					return false;
			    }else if($(this).is(".down")&& parent.index()==len-1){
				    alert("已经到底了！");
				    return false;
			    }
			    switch (true) {
					case $(this).is(".up"):
						var prev = parent.prev();
						parent.insertBefore(prev);
						break;
					case $(this).is(".down"):
						var next = parent.next();
						parent.insertAfter(next);
						break;
					case $(this).is(".del"):
						parent.remove();
						$(".suCai17-info").parent();
						break;
				}
			    
			}); */
			
			//getModelData('63ca977869c7414b9a8fc86bf6fa13ee');
		});
		
		layui.config({
		    version: 222222
		    ,debug: true
		    ,base: '${pageContext.request.contextPath}/static/zn/js/layui/extend/'
		}).extend({
		    steps:"steps/steps"
		});
		
		layui.use(['jquery','laydate', 'steps'], function(){
		    var $ = layui.$;
		    var $step= $("#step_demo").step();
		    $("#saveBtn1").click(function(event) {
		    	if($("#productForm").valid()){
		    		var src = $("#pic").attr("src");
					$("#saveBtn1").attr("class","btn btn_default mouse_arrow");
					$("#saveBtn1").attr("disabled","disabled");
					var url = '${pageContext.request.contextPath}/api/pcIndex/firstSave';
					var param = {
						token: '${token}',
						productPic: $("#productPic").val(),
						productName: $("#productName").val(),
						productDiscription: $("#productDiscription").val(),
						officeId:$("#officeListId").val()
			    	};
					$.get(url, param, function(res) {
						$("#saveBtn1").attr("class","btn btn_primary");
						$("#saveBtn1").removeAttr("disabled");
						if(res.code==0){
							var index = layer.alert('操作成功', {icon: 1,title: "提示",yes:function(){
								parent.$("#handle").val('1');
								layer.close(index);
								$("#id").val(res.bodyData.id);
								$step.nextStep();//下一步
							}});
						}else{
							layer.alert(res.message, {icon: 2,title: "提示"});
						}
					});
				}
		    });
		    
		    $("#saveBtn2").click(function(event) {
		    	var value = $("#themeId option:selected").val();
				$("#saveBtn2").attr("class","btn btn_default mouse_arrow");
				$("#saveBtn2").attr("disabled","disabled");
				var url = '${pageContext.request.contextPath}/api/pcIndex/secordSave';
				var param = {
					token: '${token}',
					id: $("#id").val(),
					themeId: value
		    	};
				$.get(url, param, function(res) {
					$("#saveBtn2").attr("class","btn btn_primary");
					$("#saveBtn2").removeAttr("disabled");
					if(res.code==0){
						var index = layer.alert('操作成功', {icon: 1,title: "提示",yes:function(){
							parent.$("#handle").val('1');
							layer.close(index);
							$("#id").val(res.bodyData.id);
							getModelData(res.bodyData.id);
							$step.nextStep();//下一步
						}});
					}else{
						layer.alert(res.message, {icon: 2,title: "提示"});
					}
				});
		    });
		    
		    $("#saveBtn3").click(function(event) {
		    	
		    	
		    	for(var i=0;i<array.length;i++){
		    		var value = $("#"+array[i]).val()==null?"":$("#"+array[i]).val();
		    		//result+= value + ",";
		    		result.push(value);
		    	}
		    	/* if(result.length>0){
		    		result = result.substring(0,result.length-1);
		    	} */
		    	for(var i=0;i<rjArr.length;i++){
		    		var value = $("#"+rjArr[i]).val()==null?"0_0":$("#"+rjArr[i]).val();
		    		//rjresult+= value + ",";
		    		rjresult.push(value);
		    	}
		    	/* if(rjresult.length>0){
		    		rjresult = rjresult.substring(0,rjresult.length-1);
		    	} */
		    	
		    	var ifxz = '';
		    	$('.switch.switch-anim:checked').each(function() {
		    		ifxz+= $(this).val() + ",";
		    	});
		    	
		    	if(ifxz.length>0){
		    		ifxz = ifxz.substring(0,ifxz.length-1);
		    	}
		    	
				$("#saveBtn3").attr("class","btn btn_default mouse_arrow");
				$("#saveBtn3").attr("disabled","disabled");
				var url = '${pageContext.request.contextPath}/api/pcIndex/thirdSave';
				var param = {
					token: '${token}',
					id: $("#id").val(),
					result: result,
					modelArr: array,
					propertyArr: sxmcArr,
					mkArr: mkArr,
					rjArr: rjArr,
					rjresult: rjresult,
					isNew:'1',
					ifxz:ifxz,
					modelId:modelId
		    	};
				jQuery.ajaxSettings.traditional = true;
				$.post(url, param, function(res) {
					$("#saveBtn3").attr("class","btn btn_primary");
					$("#saveBtn3").removeAttr("disabled");
					if(res.code==0){
						layer.alert('新增成功', {icon: 1,title: "提示",yes:function(){
							parent.$("#handle").val('1');
							var index = parent.layer.getFrameIndex(window.name); 
							parent.layer.close(index);
						}});
					}else{
						layer.alert(res.message, {icon: 2,title: "提示"});
					}
				}); 
		    });
		    
		});
		
		$("#file").change(function(){  
			 var objUrl = getObjectURL(this.files[0]) ;//获取文件信息  
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
		        		//layer.alert('上传成功', {icon: 1,title: "提示"});
		        	}else{
		        		$("#productPic").val('');
		        		$("#pic").attr("src", '');
		        		layer.alert('上传失败', {icon: 2,title: "提示"});
		        	}
		        }  
		    });  
		}  
		
		//获取批次列表数据
		function getModelData(productId){
			var host = location.host;
			var url = '${pageContext.request.contextPath}/api/pcIndex/getBatchInformationNew';
			var param = {
				token: '${token}',
				productId: productId,
				isNew: '1'
	    	};
			layer.msg('加载中', {
			    icon: 16
			    ,shade: 0.01
			});
			$.get(url, param, function(res) {
				if(res.code==0){
            		var modelList = res.bodyData.traceModelList;
            		var len1 = modelList.length;
	                var sxhtml = '';
	                $('.suCai17-info ul').remove();
	                for (var i = 0; i < len1; i++) {  
	                	var mkid = modelList[i].id;
	                	var mkmc = modelList[i].modelName;
	                	var mklx = modelList[i].modelShowType;
	                	var states = modelList[i].states;
	                	mkArr.push(mkid + '_' +mkmc + '_' +mklx);
	                	var propertyList = new Array();
	                	if(modelList[i].tracePropertyNewList!=null && modelList[i].tracePropertyNewList.length>0){
	                		propertyList = modelList[i].tracePropertyNewList;
	                	}else{
	                		propertyList = modelList[i].tracePropertyList;
	                	}
	                	if(propertyList){
	                		var len2 = propertyList.length;
	                		if(len2>0){
		                		sxhtml+='<ul><li><h2><span style="font-size: 15px;font-weight:bold;">' + mkmc + '</span>';
		                		if(mkmc!='主模块'){
		                			if(states=='D'){
				                		sxhtml+='<input id="che'+mkid+'" class="switch switch-anim" type="checkbox" value=\"'+ mkid +'\"/>';
				                	}else{
				                		sxhtml+='<input id="che'+mkid+'" class="switch switch-anim" type="checkbox" checked value=\"'+ mkid +'\"/>';
				                	}
		                		}else{
		                			sxhtml+='<input id="che'+mkid+'" class="switch switch-anim" type="checkbox" style="display:none" checked value=\"'+ mkid +'\"/>';
		                		}
		                		if(mklx==1){	
				                	if(mkmc!='主模块'){
				                		sxhtml+='<span class="f_r" style="color: #27af59;padding-top:3px;font-size:12px;cursor:pointer">';
			                			sxhtml+='<select id="sid'+mkid+'"><option value="1">文本内容</option><option value="3">图片控件</option><option value="5">时间控件</option>'
				                		+'<option value="6">链接地址</option><option value="11">数字内容</option><option value="15">生产记录</option></select>&nbsp;&nbsp;&nbsp;'
				                		+'<img src="${ctxStatic}/pcindex/images/icon01.png" title="新增属性" height="18px" width="18px" onclick="addSx(\''+mkid+'\',0)"/>'
				                		+'</span>'
				                		+'</h2>'
				                		+'<div class="suCai17-side">'
				                		+'<ul id=\"'+mkid+'\">';
				                	}else{
				                		sxhtml+='</h2><div class="suCai17-side">'
					                		+'<ul id=\"'+mkid+'\">';
				                	}
			                	}else if(mklx==2){
			                		sxhtml+='<span class="f_r" style="color: #27af59;padding-top:3px;font-size:12px;cursor:pointer">';
			                		sxhtml+='<img src="${ctxStatic}/pcindex/images/icon01.png" title="新增属性" height="18px" width="18px" onclick="addSx(\''+mkid+'\',1)"/>'
			                		+'</span>'
			                		+'</h2>'
			                		+'<div class="suCai17-side">'
			                		+'<ul id=\"'+mkid+'\">';
			                	}
		                		if(mklx==1){	
		                			for(var j = 0; j < len2; j++){
				                		var sxid = propertyList[j].id;
					                	var sxmc = propertyList[j].propertyNameZh;
					                	var type = propertyList[j].propertyType;
					                	var value = '';
					                	var jdValue = '';
					                	if(propertyList[j].propertyValue){
					                		if(type==3){
					                			jdValue = "http://" + host + propertyList[j].propertyValue;
					                		}
					                		value = propertyList[j].propertyValue;
					                	}
					                	array.push(mkid + '_' + sxid + '_' + type);
					                	sxmcArr.push(sxid + '_' +sxmc);
				                		//1.普通文本、2.HTML文本、3.图片、4.相册、5.时间控件 6.链接 7.富文本 11.数字
					                	if(type==3 || type==4){
					                		sxhtml+= '<li><div style="width:120px;text-align:right;padding-top:8px;padding-right:10px;float:left;">'+ sxmc +'</div>'
				                			+'<div style="min-height:1px;line-height:160px;text-align:left;position:relative;">'
				                			+'	<img src=\"' + jdValue + '\" id="pic'+ (mkid + '_' + sxid) +'" style="width: 160px;height: 160px;margin-bottom:10px" onerror="onerror=null;src=\'${ctxStatic}/pcindex/images/default.jpg\'">'
				                			+'<img src="${ctxStatic}/sucail/images/del2.png" style="margin-left:20px;margin-top:2px;cursor:pointer;" height="25" width="25" onclick="delSx(this,\''+ mkid +'\',\''+ sxid +'\',\''+ sxmc +'\',\''+ type +'\')"/>'
				                			+'	<div style="width:120px;height:32px;border-radius: 4px;background-color:#ff8a00;color: #FFFFFF;font-size: 14px;text-align:center;line-height:32px;outline:none;margin-left:140px;position:relative;">'
				                			+'<input type="hidden" id="'+ (mkid + '_' + sxid + '_' + type) +'" value=\"'+ value +'\"/>'
				                			+'上传<input type="file" id="file'+ sxid +'" name="file'+ sxid +'" style="cursor:pointer;opacity:0;filter:alpha(opacity=0);width:100%;height:100%;position:absolute;top:0;left:0;" onchange="common_handleFiles1(this,\'\',5120,\''+mkid+'\',\''+sxid+'\',\''+type+'\');"/>'
				                			+'	</div>'
				                			+'</div>'
				                			+'</li>';
					                	}else if(type==5){
					                		if(mkmc=='主模块'&&sxmc=='生产日期'){
					                			sxhtml+= '<li><div style="width:120px;text-align:right;float:left;padding-top:8px;padding-right:10px;">'+ sxmc +'</div><input type="text" id="'+ (mkid + '_' + sxid + '_' + type) +'" class="input_txt1" readonly="readonly" placeholder=\"'+('请选择'+sxmc)+'\" value=\"'+ value +'\" onclick="WdatePicker({dateFmt:\'yyyy年MM月dd日\',realDateFmt:\'yyyy年MM月dd日\',maxDate:\'%y-%M-%d\'})"/></li>';
					                		}else{
					                			sxhtml+= '<li><div style="width:120px;text-align:right;float:left;padding-top:8px;padding-right:10px;">'+ sxmc +'</div><input type="text" id="'+ (mkid + '_' + sxid + '_' + type) +'" class="input_txt1" readonly="readonly" placeholder=\"'+('请选择'+sxmc)+'\" value=\"'+ value +'\" onclick="WdatePicker({dateFmt:\'yyyy年MM月dd日\',realDateFmt:\'yyyy年MM月dd日\',maxDate:\'%y-%M-%d\'})"/><img src="${ctxStatic}/sucail/images/del2.png" style="margin-left:20px;margin-top:2px;cursor:pointer" height="25" width="25" onclick="delSx(this,\''+ mkid +'\',\''+ sxid +'\',\''+ sxmc +'\',\''+ type +'\')"/></li>';
					                		}
					                	}else if(type==6){
					                		sxhtml+= '<li><div style="width:120px;text-align:right;float:left;padding-top:8px;padding-right:10px;">'+ sxmc +'</div><input type="text" id="'+ (mkid + '_' + sxid + '_' + type) +'" class="input_txt1 w400" placeholder=\"'+('请输入'+sxmc)+'链接，例如:http://www.baidu.com\" value=\"'+ value +'\"/><img src="${ctxStatic}/sucail/images/del2.png" style="margin-left:20px;margin-top:2px;cursor:pointer" height="25" width="25" onclick="delSx(this,\''+ mkid +'\',\''+ sxid +'\',\''+ sxmc +'\',\''+ type +'\')"/></li>';
					                	}else if(type==11){
					                		sxhtml+= '<li><div style="width:120px;text-align:right;float:left;padding-top:8px;padding-right:10px;">'+ sxmc +'</div><input type="text" id="'+ (mkid + '_' + sxid + '_' + type) +'" class="input_txt1 w400" placeholder=\"'+('请输入'+sxmc)+'\" value=\"'+ value +'\"/><img src="${ctxStatic}/sucail/images/del2.png" style="margin-left:20px;margin-top:2px;cursor:pointer" height="25" width="25" onclick="delSx(this,\''+ mkid +'\',\''+ sxid +'\',\''+ sxmc +'\',\''+ type +'\')"/></li>';
					                	}else{
					                		if(mkmc=='主模块'&&sxmc.indexOf('保质期')>-1){
					                			sxhtml+= '<li><div style="width:120px;text-align:right;float:left;padding-top:8px;padding-right:10px;">'+ sxmc +'</div><input type="text" id="'+ (mkid + '_' + sxid + '_' + type) +'" class="input_txt1 w400" placeholder=\"'+('请输入'+sxmc)+'\" value=\"'+ value +'\"/></li>';
					                		}else{
					                			sxhtml+= '<li><div style="width:120px;text-align:right;float:left;padding-top:8px;padding-right:10px;">'+ sxmc +'</div><input type="text" id="'+ (mkid + '_' + sxid + '_' + type) +'" class="input_txt1 w400" placeholder=\"'+('请输入'+sxmc)+'\" value=\"'+ value +'\"/><img src="${ctxStatic}/sucail/images/del2.png" style="margin-left:20px;margin-top:2px;cursor:pointer" height="25" width="25" onclick="delSx(this,\''+ mkid +'\',\''+ sxid +'\',\''+ sxmc +'\',\''+ type +'\')"/></li>';
					                		}
					                		
					                	} 
				                	}
		                		}else if(mklx==2){
			                		for(var j = 0; j < len2; j+=3){
						                var sxid = propertyList[j].id;
							            var sxmc = propertyList[j].propertyNameZh;
							            var type = propertyList[j].propertyType;
							            var value1 = '';
							            var value2 = '';
							            var value3 = '';
							            var jdValue = '';
							            if(type == 5){
							                if(propertyList[j].propertyValue){
								            	value1 = propertyList[j].propertyValue;
								            }
							            }
							            if(propertyList[j+1].propertyType == 1){
							                if(propertyList[j+1].propertyValue){
								            	value2 = propertyList[j+1].propertyValue;
								            }
							            }
							            if(propertyList.length == 3){
							                if(propertyList[j+2].propertyType == 3){
								                if(propertyList[j+2].propertyValue){
									                value3 = propertyList[j+2].propertyValue;
									                jdValue = "http://" + host + value3;
									            }
								            }
							            }
							            var sxid = 'cg' + p;
					                	var type = '';
					                	rjArr.push(mkid + '_' + sxid + '_' + '1');
					            		rjArr.push(mkid + '_' + sxid + '_' + '2');
					            		rjArr.push(mkid + '_' + sxid + '_' + '3');
					            		sxhtml+= '<div style="border:1px dashed #000"><li><div style="width:120px;text-align:right;float:left;padding-top:8px;padding-right:10px;">'+ '操作时间' +'</div><input type="text" id="'+ (mkid + '_' + sxid + '_' + '1') +'" value=\"'+ value1 +'\" class="input_txt1" readonly="readonly" placeholder=\"'+'请选择操作时间'+'\" onclick="WdatePicker({dateFmt:\'yyyy年MM月dd日\',realDateFmt:\'yyyy年MM月dd日\',maxDate:\'%y-%M-%d\'})"/></li>';
			                    		sxhtml+= '<li><div style="width:120px;text-align:right;float:left;padding-top:8px;padding-right:10px;">'+ '操作内容' +'</div><input type="text" id="'+ (mkid + '_' + sxid + '_' + '2') +'" value=\"'+ value2 +'\" class="input_txt1 w400" placeholder=\"'+'请输入操作内容'+'\"/></li>';
			                    		sxhtml+= '<li><div style="width:120px;text-align:right;padding-top:8px;padding-right:10px;float:left;">'+ '操作图' +'</div>'
			                			+'<div style="min-height:1px;line-height:160px;text-align:left;position:relative;">'
			                			+'	<img src=\"' + jdValue + '\" id="pic'+ (mkid + '_' + sxid) +'" style="width: 160px;height: 160px;margin-bottom:10px" onerror="onerror=null;src=\'${ctxStatic}/pcindex/images/default.jpg\'">'
			                			+'<img src="${ctxStatic}/sucail/images/del2.png" style="margin-left:20px;margin-top:2px;cursor:pointer;" height="25" width="25" onclick="delSx(this,\''+ mkid +'\',\''+ sxid +'\',\''+ '生产过程_2' +'\',\''+ type +'\')"/>'
			                			+'	<div style="width:120px;height:32px;border-radius: 4px;background-color:#ff8a00;color: #FFFFFF;font-size: 14px;text-align:center;line-height:32px;outline:none;margin-left:140px;position:relative;">'
			                			+'<input type="hidden" id="'+ (mkid + '_' + sxid + '_' + '3') +'" value=\"'+ value3 +'\"/>'
			                			+'上传<input type="file" id="file'+ sxid +'" name="file'+ sxid +'" style="cursor:pointer;opacity:0;filter:alpha(opacity=0);width:100%;height:100%;position:absolute;top:0;left:0;" onchange="common_handleFiles1(this,\'\',5120,\''+mkid+'\',\''+sxid+'\',\''+'3'+'\');"/>'
			                			+'	</div>'
			                			+'</div>'
			                			+'</li></div>';
			                    		$('#'+mkid).append(sxhtml);
			            				p++;
				                	}
			                	}
			                	sxhtml+='</ul></div></li></ul>';
		                	}
	                	}else{
	                		if(mkmc == '用户评价'){
	                			modelId = mkid;
	                		}
	                		sxhtml+='<ul><li><h2><span style="font-size: 15px;font-weight:bold;">' + mkmc + '</span>';
		                	if(states=='D'){
		                		sxhtml+='<input id="che'+mkid+'" class="switch switch-anim" type="checkbox" value=\"'+ mkid +'\"/>';
		                	}else{
		                		sxhtml+='<input id="che'+mkid+'" class="switch switch-anim" type="checkbox" checked value=\"'+ mkid +'\"/>';
		                	}
		                	if(mkmc!='用户评价'){
		                		sxhtml+='<span class="f_r" style="color: #27af59;padding-top:3px;font-size:12px;cursor:pointer">'
		                			+'<select id="sid'+mkid+'"><option value="1">文本内容</option><option value="3">图片控件</option><option value="5">时间控件</option>'
			                		+'<option value="6">链接地址</option><option value="11">数字内容</option><option value="15">生产记录</option></select>&nbsp;&nbsp;&nbsp;'
			                		+'<img src="${ctxStatic}/pcindex/images/icon01.png" title="新增属性" height="18px" width="18px" onclick="addSx(\''+mkid+'\',0)"/>'
			                		+'</span></h2>'
			                		+'<div class="suCai17-side">'
			                		+'<ul id=\"'+mkid+'\">'
			                		+'</ul></div></li></ul>';
		                	}else{
		                		sxhtml+='</h2></li></ul>';
		                	}
	                		
	                	}
	                }
	                if(sxhtml!=''){
	                	$('.suCai17-info').append(sxhtml);
	                }
            	}else{
            		layer.alert(res.message, {icon: 2,title: "提示"});
            	}
			});
	    }


		function addSx(mkid,lx){
			var sxhtml='';
			if(lx==0){
				var type = $('#sid'+mkid).val();
				var sxid = guid();
				layer.prompt({title: '请输入属性名称'}, function(val, index){
				    layer.close(index);
			    	array.push(mkid + '_' + sxid + '_' + type);
					sxmcArr.push(sxid + '_' +val);
					if(type==3 || type==4){
						sxhtml+= '<li><div style="width:120px;text-align:right;padding-top:8px;padding-right:10px;float:left;"">'+ val +'</div>'
	        			+'<div style="min-height:1px;line-height:160px;text-align:left;position:relative;">'
	        			+'	<img src="" id="pic'+ (mkid + '_' + sxid) +'" style="width: 160px;height: 160px;margin-bottom:10px" onerror="onerror=null;src=\'${ctxStatic}/pcindex/images/default.jpg\'">'
	        			+'<img src="${ctxStatic}/sucail/images/del2.png" style="margin-left:20px;margin-top:2px;cursor:pointer;" height="25" width="25" onclick="delSx(this,\''+ mkid +'\',\''+ sxid +'\',\''+ val +'\',\''+ type +'\')"/>'
	        			+'	<div style="width:120px;height:32px;border-radius: 4px;background-color:#ff8a00;color: #FFFFFF;font-size: 14px;text-align:center;line-height:32px;outline:none;margin-left:140px;position:relative;">'
	        			+'<input type="hidden" id="'+ (mkid + '_' + sxid + '_' + type) +'"/>'
	        			+'上传<input type="file" id="file'+ sxid +'" name="file'+ sxid +'" style="cursor:pointer;opacity:0;filter:alpha(opacity=0);width:100%;height:100%;position:absolute;top:0;left:0;" onchange="common_handleFiles1(this,\'\',5120,\''+mkid+'\',\''+sxid+'\',\''+type+'\');"/>'
	        			+'	</div>'
	        			+'</div>'
	        			+'</li>';
		        	}else if(type==5){
		        		sxhtml+= '<li><div style="width:120px;text-align:right;float:left;padding-top:8px;padding-right:10px;">'+ val +'</div><input type="text" id="'+ (mkid + '_' + sxid + '_' + type) +'" class="input_txt1" readonly="readonly" placeholder=\"'+('请选择'+val)+'\" onclick="WdatePicker({dateFmt:\'yyyy年MM月dd日\',realDateFmt:\'yyyy年MM月dd日\'})"/><img src="${ctxStatic}/sucail/images/del2.png" style="margin-left:20px;margin-top:2px;cursor:pointer" height="25" width="25" onclick="delSx(this,\''+mkid+'\',\''+ sxid +'\',\''+ val +'\',\''+ type +'\')"/></li>';
		        	}else if(type==6){
		        		sxhtml+= '<li><div style="width:120px;text-align:right;float:left;padding-top:8px;padding-right:10px;">'+ val +'</div><input type="text" id="'+ (mkid + '_' + sxid + '_' + type) +'" class="input_txt1 w400" placeholder=\"'+('请输入'+val)+'链接，例如:http://www.baidu.com\"/><img src="${ctxStatic}/sucail/images/del2.png" style="margin-left:20px;margin-top:2px;cursor:pointer" height="25" width="25" onclick="delSx(this,\''+mkid+'\',\''+ sxid +'\',\''+ val +'\',\''+ type +'\')"/></li>';
		        	}else if(type==11){
		        		sxhtml+= '<li><div style="width:120px;text-align:right;float:left;padding-top:8px;padding-right:10px;">'+ val +'</div><input type="number" id="'+ (mkid + '_' + sxid + '_' + type) +'" class="input_txt1 w400" placeholder=\"'+('请输入'+val)+'\"/><img src="${ctxStatic}/sucail/images/del2.png" style="margin-left:20px;margin-top:2px;cursor:pointer" height="25" width="25" onclick="delSx(this,\''+mkid+'\',\''+ sxid +'\',\''+ val +'\',\''+ type +'\')"/></li>';
		        	}else{
		        		sxhtml+= '<li><div style="width:120px;text-align:right;float:left;padding-top:8px;padding-right:10px;">'+ val +'</div><input type="text" id="'+ (mkid + '_' + sxid + '_' + type) +'" class="input_txt1 w400" placeholder=\"'+('请输入'+val)+'\"/><img src="${ctxStatic}/sucail/images/del2.png" style="margin-left:20px;margin-top:2px;cursor:pointer" height="25" width="25" onclick="delSx(this,\''+mkid+'\',\''+ sxid +'\',\''+ val +'\',\''+ type +'\')"/></li>';
		        	}
					$('#'+mkid).append(sxhtml);
				});
			}else{
				var type = '';
				var sxid = 'cg'+p;
				rjArr.push(mkid + '_' + sxid + '_' + '1');
				rjArr.push(mkid + '_' + sxid + '_' + '2');
				rjArr.push(mkid + '_' + sxid + '_' + '3');
				sxhtml+= '<div style="border:1px dashed #000"><li><div style="width:120px;text-align:right;float:left;padding-top:8px;padding-right:10px;">'+ '操作时间' +'</div><input type="text" id="'+ (mkid + '_' + sxid + '_' + '1') +'" class="input_txt1" readonly="readonly" placeholder=\"'+'请选择操作时间'+'\" onclick="WdatePicker({dateFmt:\'yyyy年MM月dd日\',realDateFmt:\'yyyy年MM月dd日\'})"/></li>';
        		sxhtml+= '<li><div style="width:120px;text-align:right;float:left;padding-top:8px;padding-right:10px;">'+ '操作内容' +'</div><input type="text" id="'+ (mkid + '_' + sxid + '_' + '2') +'" class="input_txt1 w400" placeholder=\"'+'请输入操作内容'+'\"/></li>';
        		sxhtml+= '<li><div style="width:120px;text-align:right;padding-top:8px;padding-right:10px;float:left;">'+ '操作图' +'</div>'
    			+'<div style="min-height:1px;line-height:160px;text-align:left;position:relative;">'
    			+'	<img src="" id="pic'+ (mkid + '_' + sxid) +'" style="width: 160px;height: 160px;margin-bottom:10px" onerror="onerror=null;src=\'${ctxStatic}/pcindex/images/default.jpg\'">'
    			+'<img src="${ctxStatic}/sucail/images/del2.png" style="margin-left:20px;margin-top:2px;cursor:pointer;" height="25" width="25" onclick="delSx(this,\''+ mkid +'\',\''+ sxid +'\',\''+ '生产过程_2' +'\',\''+ type +'\')"/>'
    			+'	<div style="width:120px;height:32px;border-radius: 4px;background-color:#ff8a00;color: #FFFFFF;font-size: 14px;text-align:center;line-height:32px;outline:none;margin-left:140px;position:relative;">'
    			+'<input type="hidden" id="'+ (mkid + '_' + sxid + '_' + '3') +'"/>'
    			+'上传<input type="file" id="file'+ sxid +'" name="file'+ sxid +'" style="cursor:pointer;opacity:0;filter:alpha(opacity=0);width:100%;height:100%;position:absolute;top:0;left:0;" onchange="common_handleFiles1(this,\'\',5120,\''+mkid+'\',\''+sxid+'\',\''+'3'+'\');"/>'
    			+'	</div>'
    			+'</div>'
    			+'</li></div>';
        		$('#'+mkid).append(sxhtml);
				p++;
		    }
		}
		
		function delSx(obj,mkid,sxid,sxmc,type){
			if(sxmc == '生产过程_2'){
				rjArr.remove(mkid + '_' + sxid + '_' + '1');
				rjArr.remove(mkid + '_' + sxid + '_' + '2');
				rjArr.remove(mkid + '_' + sxid + '_' + '3');
				$(obj).parent().parent().parent().remove();
			}else{
				array.remove(mkid + '_' + sxid + '_' + type);
				sxmcArr.remove(sxid + '_' + sxmc);
				if(type==3 || type==4){
					$(obj).parent().parent().remove();
				}else{
					$(obj).parent().remove();
				}
			}
		}
		
		Array.prototype.remove = function(val) {
			var index = this.indexOf(val);
			if (index > -1) {
				this.splice(index, 1);
			}
		};
		
		function S4() { 
		   return (((1+Math.random())*0x10000)|0).toString(16).substring(1); 
		}; 
		function guid() { 
		   return (S4()+S4()+S4()+S4()+S4()+S4()+S4()+S4()); 
		}; 
	</script>
</body>
</html>

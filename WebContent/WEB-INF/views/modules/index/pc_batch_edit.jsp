<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="renderer" content="webkit">
	<title>批次编辑</title>
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/zn/css/base.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/zn/css/s+.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/pcindex/css/s.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/pcindex/css/base.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/pcindex/css/choice.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/pcindex/css/style.css">
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
	<div id="step_demo" class="step-body">
	    <div class="step-content" align="center">
	        <div class="step-list">
		    	<div class="suCai17-content" align="center">
		            <div class="suCai17-background clearfix" align="left">
		            	<form id="sxForm" action="" method="post" enctype="multipart/form-data" class="form-horizontal">
		                <div class="suCai17-info bd">
		                </div>
		                </form>
		            </div>
					<div class="tab_btn_show">
						<input class="btn btn_primary" id="saveBtn3" onclick="save()" type="button" value="完成"/>
					</div>
		        </div>
		    </div>
	    </div>
	</div>
	
	<script type="text/javascript" src="${ctxStatic}/jquery/jquery-1.9.1.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/layer/layer.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jquery-validation/1.11.1/jquery.validate.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jquery-validation/1.11.1/jquery.validate.method.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/zn/js/common_file1.js"></script>
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
		    
		    getModelData('${product.id}','${productBatch.id}','${isNew}');

		});
		
		function save(){

	    	for(var i=0;i<array.length;i++){
	    		var value = $("#"+array[i]).val()==null?"":$("#"+array[i]).val();
	    		result.push(value);
	    	}
	    	for(var i=0;i<rjArr.length;i++){
	    		var value = $("#"+rjArr[i]).val()==null?"0_0":$("#"+rjArr[i]).val();
	    		rjresult.push(value);
	    	}
	    	
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
				id: '${product.id}',
				result: result,
				modelArr: array,
				propertyArr: sxmcArr,
				mkArr: mkArr,
				rjArr: rjArr,
				rjresult: rjresult,
				batchId:'${productBatch.id}',
				isNew:'${isNew}',
				ifxz:ifxz,
				modelId:modelId
	    	};
			jQuery.ajaxSettings.traditional = true;
			$.post(url, param, function(res) {
				$("#saveBtn3").attr("class","btn btn_primary");
				$("#saveBtn3").removeAttr("disabled");
				if(res.code==0){
					layer.alert('保存成功', {icon: 1,title: "提示",yes:function(){
						parent.$("#handle").val('5');
						var index = parent.layer.getFrameIndex(window.name); 
						parent.layer.close(index);
					}});
				}else{
					layer.alert(res.message, {icon: 2,title: "提示"});
				}
			}); 
	    };
	  
		//获取批次列表数据
		function getModelData(productId,batchId,isNew){
			var host = location.host;
			var url = '${pageContext.request.contextPath}/api/pcIndex/getBatchInformation';
			var param = {
				token: '${token}',
				productId: productId,
				batchId: batchId,
				isNew: isNew
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
		                			for(var m = 0; m < propertyList.length; m++){
		                				var list = new Array();
		                				list = propertyList[m].list
		                				
		                				for(var j = 0; j < list.length; j+=3){
					                		var sxid = list[j].id;
						                	var sxmc = list[j].propertyNameZh;
						                	var type = list[j].propertyType;
						                	var value1 = '';
						                	var value2 = '';
						                	var value3 = '';
						                	var jdValue = '';
						                	if(type == 5){
						                		if(list[j].propertyValue){
							                		value1 = list[j].propertyValue;
							                	}
						                	}
						                	if(list[j+1].propertyType == 1){
						                		if(list[j+1].propertyValue){
							                		value2 = list[j+1].propertyValue;
							                	}
						                	}
						                	if(list.length == 3){
						                		if(list[j+2].propertyType == 3){
							                		if(list[j+2].propertyValue){
								                		value3 = list[j+2].propertyValue;
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
		                		if(mkmc!='生产过程'){
		                			sxhtml+='<span class="f_r" style="color: #27af59;padding-top:3px;font-size:12px;cursor:pointer">'
			                			+'<select id="sid'+mkid+'"><option value="1">文本内容</option><option value="3">图片控件</option><option value="5">时间控件</option>'
				                		+'<option value="6">链接地址</option><option value="11">数字内容</option><option value="15">生产记录</option></select>&nbsp;&nbsp;&nbsp;'
				                		+'<img src="${ctxStatic}/pcindex/images/icon01.png" title="新增属性" height="18px" width="18px" onclick="addSx(\''+mkid+'\',0)"/>'
				                		+'</span></h2>'
				                		+'<div class="suCai17-side">'
				                		+'<ul id=\"'+mkid+'\">'
				                		+'</ul></div></li></ul>';
		                		}else if(mkmc=='生产过程'){
		                			sxhtml+= '<span class="f_r" style="color: #27af59;padding-top:3px;font-size:12px;cursor:pointer">'
				                		+'<img src="${ctxStatic}/pcindex/images/icon01.png" title="新增属性" height="18px" width="18px" onclick="addSx(\''+mkid+'\',1)"/>'
				                		+'</span></h2>'
				                		+'<div class="suCai17-side">'
				                		+'<ul id=\"'+mkid+'\">'
				                		+'</ul></div></li></ul>';
		                		}
		                	}else{
		                		sxhtml+='</h2></li></ul>';
		                	}
	                		
	                	}
	                }
	                if(sxhtml!=''){
	                	$('.suCai17-info').append(sxhtml);
	                }
	                console.log(array);
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
		        		sxhtml+= '<li><div style="width:120px;text-align:right;float:left;padding-top:8px;padding-right:10px;">'+ val +'</div><input type="text" id="'+ (mkid + '_' + sxid + '_' + type) +'" class="input_txt1" readonly="readonly" placeholder=\"'+('请选择'+val)+'\" onclick="WdatePicker({dateFmt:\'yyyy年MM月dd日\',realDateFmt:\'yyyy年MM月dd日\',maxDate:\'%y-%M-%d\'})"/><img src="${ctxStatic}/sucail/images/del2.png" style="margin-left:20px;margin-top:2px;cursor:pointer" height="25" width="25" onclick="delSx(this,\''+mkid+'\',\''+ sxid +'\',\''+ val +'\',\''+ type +'\')"/></li>';
		        	}else if(type==6){
		        		sxhtml+= '<li><div style="width:120px;text-align:right;float:left;padding-top:8px;padding-right:10px;">'+ val +'</div><input type="text" id="'+ (mkid + '_' + sxid + '_' + type) +'" class="input_txt1 w400" placeholder=\"'+('请输入'+val)+'链接，例如:http://www.baidu.com\"/><img src="${ctxStatic}/sucail/images/del2.png" style="margin-left:20px;margin-top:2px;cursor:pointer" height="25" width="25" onclick="delSx(this,\''+mkid+'\',\''+ sxid +'\',\''+ val +'\',\''+ type +'\')"/></li>';
		        	}else if(type==11){
		        		sxhtml+= '<li><div style="width:120px;text-align:right;float:left;padding-top:8px;padding-right:10px;">'+ val +'</div><input type="text" id="'+ (mkid + '_' + sxid + '_' + type) +'" class="input_txt1 w400" placeholder=\"'+('请输入'+val)+'\"/><img src="${ctxStatic}/sucail/images/del2.png" style="margin-left:20px;margin-top:2px;cursor:pointer" height="25" width="25" onclick="delSx(this,\''+mkid+'\',\''+ sxid +'\',\''+ val +'\',\''+ type +'\')"/></li>';
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

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>标签申请</title>
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/zn/css/base.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/zn/css/s+.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/pcindex/css/s.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/pcindex/css/base.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/layer/skin/layer.css">
	<style type="text/css">
		html, body {
		    width: 100%;
		    height: 100%;
		    overflow:hidden;
		}
		.label{
			padding-left:170px;
			padding-top:50px;
		}
		.divpic{
			height:200px;
			width:416px;
			background: url('http://sh.sureserve.cn/upload/product_model/20190118161037354.png') no-repeat 0px 0px;
			display:block;
			background-size:100% 100%;
		}
		.tr{height:40px}
		.tdOne{width:150px}
		.tdTwo{width:350px;text-align: left;}
		.divOne{margin-right: 10px;text-align: right;}
		.divTwo{padding-left: 20px;}
		.divla{height:24px;width:200px}
		.trTemplate{height:240px}
		.trAddress{height:50px}
		.input_label_content{width:230px;font-size:16px;}
	</style>
</head>
<body>
	<div id="base" class="" style="width:100%;height:100%;overflow-y:scroll;">
      <div id="u1" class="ax_default" style="height:94%;width:98%;padding: 1% 1%;font-size:18px;">
        <div id="u4" class="ax_default" style="text-align: left;height:100%;width:98%;padding: 1% 1%;">
          <input id="token" type="hidden" value="${token}" />
          <form:form id="TraceLableApplyForm" modelAttribute="TraceLableApply" action="" method="post" class="form-horizontal">
          <table border=1 style="cellspacing:10px;cellpadding:10px;background-color: #D9D9D9;" align="center">
          		<tr class="tr">
          			<td class="tdOne">
          				<div class="divOne">
                			<p><span>产品名称:</span></p>
              			</div>
          			</td>
          			<td class="tdTwo">
          				<div class="divTwo">
                			<p><span>${traceProduct.productName}</span></p>
              			</div>
          			</td>
          			<td class="tdOne">
          				<div class="divOne">
                			<p><span>标签单价:</span></p>
              			</div>
          			</td>
          			<td class="tdTwo">
          				<div class="divTwo">
                			<p><span>${TraceLableTemplateList[0].lableUnitPrice}  元</span></p>
              			</div>
          			</td>
          		</tr>
          		<tr class="tr">
          			<td class="tdOne">
          				<div class="divOne">
                			<p><span class="red font18 martop">&lowast;&nbsp;</span><span>标签个数 :</span></p>
              			</div>
          			</td>
          			<td class="tdTwo">
          				<div class="divTwo">
          					<div style="float: left;margin-bottom: 4px;">
          						<input id="applylableNum" type="number" min="1" max="10000" onchange="handleChange(this.value)" style="font-size:18px;height: 25px;">
          					</div>
          					<div style="float: left;margin-top: 4px;">
          						<span style="font-size:16px;width:30px;">&nbsp;(个)</span>
          						<span style="font-size:8px;color: red;">&nbsp;(注：输入个数自动计算总价)</span>
          					</div>
              			</div>
          			</td>
          			<td class="tdOne">
          				<div class="divOne">
                			<p><span>标签总价:</span></p>
              			</div>
          			</td>
          			<td class="tdTwo">
          				<div class="divTwo">
                			<p><span id="currentTotalPrice">0</span><span>  元</span></p>
              			</div>
          			</td>
          		</tr>
          		<tr class="tr">
          			<td class="tdOne">
          				<div class="divOne">
                			<p><span>批 次 号 :</span></p>
              			</div>
          			</td>
          			<td class="tdTwo" colspan="3">
          				<div class="divTwo">
                			<select id="pcid" style="font-size:18px;height: 30px;min-width: 250px">
                				<option value=""></option>
								<c:forEach items="${ProductBatchList}" var="u">
									<option value="${u.id}">
									${u.batchCode}
									</option>
								</c:forEach>
							</select>
							<span style="font-size:8px;color: red;font-family:Microsoft YaHei">(注：选定批次号则申请的是绑定该批次号的标签，否则是无绑定批次号标签)</span>
              			</div>
          			</td>
          		</tr>
          		<tr class="trTemplate">
          			<td class="tdOne">
          				<div class="divOne">
                			<p><span class="red font18 martop">&lowast;&nbsp;</span><span>标签模板:</span></p>
              			</div>
          			</td>
          			<td class="tdTwo" colspan="3">
          				<div class="divTwo">
                			<div style="float: left;width:50%;height:90%;">
      							<div class="divpic">
	  								<div class="label">
					  					<div id="u39_div" class="divla">
				            				<p><input class="input_label_content" id="productName"  type="text" placeholder="自定义值,例如“产品名称:大黄桃”"  ></input></p>
				            			</div>
				            			<div id="u39_div" class="divla">
				            				<p><input class="input_label_content" id="qualityGuaranteePeriod"  type="text" placeholder="自定义值,例如“保质期:30天”" ></input></p>
				            			</div>
				            			<div id="u39_div" class="divla">
				            				<p><input class="input_label_content" id="plantGround"  type="text" placeholder="自定义值,例如“生产地:湖南浏阳”" ></input></p>
				            			</div>
				            			<div id="u39_div" class="divla">
				            				<p><input class="input_label_content" id="farmerName"  type="text" placeholder="自定义值,例如“负责人:王一”" ></input></p>
				            			</div>
				            			<div id="u39_div" class="divla">
				            				<p><input class="input_label_content" id="phoneNum"  type="text" placeholder="自定义值,例如“电话号码:18588889999”"  ></input></p>
				            			</div>
	  								</div>
	  							</div>
	  							<div style="height:9%;width:100%;text-align: center;">
	  								<div>
	  									<input id="u52_input" type="radio" checked="checked" value="${TraceLableTemplateList[0].id}" name="u52">${TraceLableTemplateList[0].labelTemplateName}</input>
	  								</div>
	  							</div>
      						</div>
              			</div>
          			</td>
          		</tr>
          		<tr class="trAddress">
          			<td class="tdOne">
          				<div class="divOne">
                			<p><span class="red font18 martop">&lowast;&nbsp;</span><span>收货地址:</span></p>
              			</div>
          			</td>
          			<td class="tdTwo" colspan="3">
          				<div class="divTwo">
                			<select id="dzid" style="font-size:15px;height: 34px;min-width: 480px;">
								<c:forEach items="${TraceDeliveryAddressList}" var="u">
									<option value="${u.id}">
									${u.addressMosaicing}
									</option>
								</c:forEach>
							</select>
							<div style="float: right;margin-right: 10px;padding-top: 4px;">
								<img alt="${ctxStatic}/images/plus.png" src="${ctxStatic}/images/plus.png" onclick="addAddress()">
							</div>
              			</div>
          			</td>
          		</tr>
          	</table>
          	</form:form>
          	<div style="width:98%;height:30px;padding: 1% 1%;text-align: center;margin-top: 10px;">
	  			<div id="u39" class="ax_default box_1">
            		<div id="u39_div" class="">
            			<input id="saveBtn" class="btn btn_primary" onclick="save()" type="button" value="保存"/>
            		</div>
         		</div>
	  		</div> 
      	</div>
  	  </div> 
    </div>
	<script type="text/javascript" src="${ctxStatic}/jquery/jquery-1.9.1.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jquery-validation/1.11.1/jquery.validate.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jquery-validation/1.11.1/jquery.validate.method.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/layer/layer.js"></script>
	
	<script type="text/javascript">
	var storage=window.localStorage;
		$(document).ready(function() {
			if(storage.length > 0){
				$("#productName").val(storage.productName);
				$("#qualityGuaranteePeriod").val(storage.qualityGuaranteePeriod);
				$("#plantGround").val(storage.plantGround);
				$("#farmerName").val(storage.farmerName);
				$("#phoneNum").val(storage.phoneNum);
				$("#applylableNum").val(storage.applylableNum);
				$("#productName").val(storage.productName);
				$("#currentTotalPrice").html(storage.currentTotalPrice);
				var  ss = document.getElementById('pcid');
				ss[storage.index].selected = true;
			}else{
				$("#productName").val("产品名称："+'${traceProduct.productName}');
				$("#qualityGuaranteePeriod").val("保质期（天）："+'${qualityGuaranteePeriod}');
				$("#plantGround").val("产地："+'${plantGround}');
				$("#farmerName").val("负责人："+'${farmerName}');
				$("#phoneNum").val("联系电话："+'${phoneNum}');
			}
		});
		
		function addAddress(){
			
			storage.setItem("productName",$("#productName").val()); 
			storage.setItem("qualityGuaranteePeriod",$("#qualityGuaranteePeriod").val()); 
			storage.setItem("plantGround",$("#plantGround").val()); 
			storage.setItem("farmerName",$("#farmerName").val()); 
			storage.setItem("phoneNum",$("#phoneNum").val()); 
			storage.setItem("applylableNum",$("#applylableNum").val()); 
			storage.setItem("currentTotalPrice",document.getElementById("currentTotalPrice").innerText);
			var obj = document.getElementById("pcid");
			var index = obj.selectedIndex;
			storage.setItem("index",index);
			layer.open({
    	        type: 2,
    	        title: '新增地址',
    	        shadeClose: true,
    	        area: ['60%', '75%'],
    	        scrollbar: false, // 父页面 滚动条 禁止
    	        content:'${pageContext.request.contextPath}/api/pcLabel/newAddress?token=${token}',
    	    });
		};
		
		function handleChange(num){
			var labelTemplateId = $("#u52_input").val();
			if(labelTemplateId != '' || labelTemplateId != null){
				var url ='${pageContext.request.contextPath}/api/pcLabel/traceLableCalculation';
				var param = {
					token: $("#token").val(),
					lableTemplateId: labelTemplateId,
					applyNum: num,
		    	};
				$.get(url, param, function(res) {
					$("#currentTotalPrice").html(res.bodyData.currentTotalPrice);
				});
			}else{
				layer.alert('请先选择标签!', {
				    skin: 'layui-layer-lan'
				    ,closeBtn: 0
				    ,anim: 4 //动画类型
				  });
			}
		}
		
		function save(){
			if($("#TraceLableApplyForm").valid()){
				var token = $("#token").val();
				var labelTemplateId = $("#u52_input").val();
				var applylableNum = $("#applylableNum").val();
				var addressId = $("#dzid").val();
				var traceProductId = '${traceProduct.id}';
				
				var obj = document.getElementById("pcid");
				var index = obj.selectedIndex;
				var batchId = obj.options[index].value;
				var applyCode = obj.options[index].text;
				var totalPrice = document.getElementById("currentTotalPrice").innerText;
				var productName = $("#productName").val();
				var qualityGuaranteePeriod = $("#qualityGuaranteePeriod").val();
				var plantGround = $("#plantGround").val();
				var farmerName = $("#farmerName").val();
				var phoneNum = $("#phoneNum").val();
				
				var contentList = new Array()
				contentList.push({ applyElementContent: productName, elementId: '11', labelTemplateId: labelTemplateId});
				contentList.push({ applyElementContent: qualityGuaranteePeriod, elementId: '13', labelTemplateId: labelTemplateId});
				contentList.push({ applyElementContent: plantGround, elementId: '14', labelTemplateId: labelTemplateId});
				contentList.push({ applyElementContent: farmerName, elementId: '15', labelTemplateId: labelTemplateId});
				contentList.push({ applyElementContent: phoneNum, elementId: '16', labelTemplateId: labelTemplateId});
				
				if(!applylableNum){
					layer.alert('标签数量不能为空!', {
					    skin: 'layui-layer-lan'
					    ,closeBtn: 0
					    ,anim: 4 //动画类型
					  });
					return false;
				}
				if(!addressId){
					layer.alert('收货地址不能为空!', {
					    skin: 'layui-layer-lan'
					    ,closeBtn: 0
					    ,anim: 4 //动画类型
					  });
					return false;
				}
				if(!labelTemplateId){
					layer.alert('请选择标签模板!', {
					    skin: 'layui-layer-lan'
					    ,closeBtn: 0
					    ,anim: 4 //动画类型
					  });
					return false;
				}
				
				if(!productName){
					layer.alert('标签内容需要填写完整，否则标签不可打印!', {
					    skin: 'layui-layer-lan'
					    ,closeBtn: 0
					    ,anim: 4 //动画类型
					  });
					return false;
				}
				if(!qualityGuaranteePeriod){
					layer.alert('标签内容需要填写完整，否则标签不可打印!', {
					    skin: 'layui-layer-lan'
					    ,closeBtn: 0
					    ,anim: 4 //动画类型
					  });
					return false;
				}
				if(!plantGround){
					layer.alert('标签内容需要填写完整，否则标签不可打印!', {
					    skin: 'layui-layer-lan'
					    ,closeBtn: 0
					    ,anim: 4 //动画类型
					  });
					return false;
				}
				if(!farmerName){
					layer.alert('标签内容需要填写完整，否则标签不可打印!', {
					    skin: 'layui-layer-lan'
					    ,closeBtn: 0
					    ,anim: 4 //动画类型
					  });
					return false;
				}
				if(!phoneNum){
					layer.alert('标签内容需要填写完整，否则标签不可打印!', {
					    skin: 'layui-layer-lan'
					    ,closeBtn: 0
					    ,anim: 4 //动画类型
					  });
					return false;
				}
				
				
				$("#saveBtn").attr("class","btn btn_default mouse_arrow");
				$("#saveBtn").attr("disabled","disabled");
				
				var param = {
						labelTemplateId: labelTemplateId,
						applyNum: applylableNum,
						addressId: addressId,
						applyCode: applyCode,
						batchId: batchId,
						traceProductId: traceProductId,
						totalPrice: totalPrice,
						contentList: contentList
			    	};
			
				var url = '${pageContext.request.contextPath}/api/pcLabel/saveLableApply?token='+token;
				$.ajax({
				        type:"POST",
				        url: url,
				        dataType:"json",
				        contentType:"application/json",
				        data:JSON.stringify(param),
				        success:function (data) {
			            	$("#saveBtn").attr("class","btn btn_primary");
							$("#saveBtn").removeAttr("disabled");
				            if(data.code == 0){
				            	layer.alert('申请标签成功!', {icon: 1,title: "提示",yes:function(){
				            		parent.$("#handle").val('3');
									var index = parent.layer.getFrameIndex(window.name); 
									parent.layer.close(index);
								}});
				            }
				        },
				        error:function (i,n,m) {
				        	$("#saveBtn").attr("class","btn btn_primary");
							$("#saveBtn").removeAttr("disabled");
				            layer.alert('出现错误啦，请联系管理员!!!', {icon: 2,title: "提示"});
				        }
				 });
			}
		};
	</script>
</body>
</html>

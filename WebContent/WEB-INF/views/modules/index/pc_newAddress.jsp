<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>新增地址</title>
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/zn/css/base.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/zn/css/s+.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/pcindex/css/s.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/pcindex/css/base.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/pcindex/css/choice.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/pcindex/css/style.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/layer/skin/layer.css">

	<style type="text/css">
		html, body {
		    width: 100%;
		    height: 100%;
		    overflow:scroll;
		    overflow-x:hidden;
		    overflow-y:hidden;
		}
		.error{color: red}
		.tab_btn_show{padding:15px;}
		.tdOne{width: 80px;} 
		.tdTwo{width: 300px;}
	</style>
</head>
<body>
	<div class="step-content" align="center" style="width:100%;height:100%;">
      <div class="step-list" style="width:90%;overflow: hidden;margin-top: 10px;">
	  	<form:form id="addressForm" modelAttribute="address" action="" method="post" class="form-horizontal">
			<input id="token" type="hidden" value="${token}" />
			<table class="tab" cellpadding="0" cellspacing="0">
				<tbody>
					<tr>
						<td align="right" class="tdOne"><span class="red font18 martop">&lowast;&nbsp;</span>所属公司：</td>
						<td align="left" class="tdTwo">
							<select id="officeId" style="font-size:15px;height: 34px;min-width: 260px;">
								<c:forEach items="${officeList}" var="u">
									<option value="${u.id}">
									${u.name}
									</option>
								</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<td align="right" class="tdOne"><span class="red font18 martop">&lowast;&nbsp;</span>姓名：</td>
						<td align="left" class="tdTwo"><form:input path="receiver" maxlength="32" placeholder="请输入姓名" htmlEscape="false" style="width:260px;" cssClass="input_txt required"/></td>
					</tr>
					<tr>
						<td align="right" class="tdOne"><span class="red font18 martop">&lowast;&nbsp;</span>手机号码：</td>
						<td align="left" class="tdTwo"><form:input path="phoneNum" maxlength="11" onblur="upperCase()" oninput="value=value.replace(/[^\d]/g,'')" placeholder="请输入手机号"  htmlEscape="false" style="width:260px;" cssClass="input_txt required"/></td>
					</tr>
					<tr>
						<td align="right" class="tdOne"><span class="red font18 martop">&lowast;&nbsp;</span>邮编：</td>
						<td align="left" class="tdTwo"><form:input path="zipCode" maxlength="6" onblur="upperCase2()" oninput="value=value.replace(/[^\d]/g,'')" placeholder="请输入邮编" htmlEscape="false" style="width:260px;" cssClass="input_txt required"/></td>
					</tr>
					<tr>
						<td align="right" class="tdOne"><span class="red font18 martop">&lowast;&nbsp;</span>地区：</td>
						<td align="left" class="tdTwo">
							<form class="form-inline">
      							<div id="distpicker5">
        							<div class="form-group">
          								<select data-province="-选择省 -" id="province10" style="height: 30px;width: 100px;"></select>
          								<select data-city="-选择市 -" id="city10" style="height: 30px;width: 90px;"></select>
          								<select data-district="-选择区-" id="district10" style="height: 30px;width: 80px;"></select>
        							</div>
      							</div>
    						</form>
    					</td>
					</tr>
					<tr>
						<td align="right" class="tdOne"><span class="red font18 martop">&lowast;&nbsp;</span>详细地址：</td>
						<td align="left" class="tdTwo"><form:textarea path="detailAddress" maxlength="100" placeholder="请输入详细地址" htmlEscape="false" style="width:260px;" cssClass="input_txt required"/></td>
					</tr>
				</tbody>
			</table>
		</form:form>
		<div class="tab_btn_show">
			<input id="saveBtn" class="btn btn_primary" type="button" onclick="saveAddress()" value="保存"/>
		</div>
	  </div>
    </div>
	<script type="text/javascript" src="${ctxStatic}/jquery/jquery-1.9.1.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/zn/js/layui/layui.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jquery-validation/1.11.1/jquery.validate.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jquery-validation/1.11.1/jquery.validate.method.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/layer/layer.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/distpicker.data.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/distpicker.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/main.js"></script>
	
	<script type="text/javascript">
		$(document).ready(function() {
			$('#province10').val("湖南省");
			$("#province10").trigger("change");
			$('#city10').val("长沙市");
			$("#city10").trigger("change");
			$('#district10').val("芙蓉区");
			$("#district10").trigger("change");
		});
		function upperCase(){
        	var phone = document.getElementById('phoneNum').value;
        	if(!(/^[1][3,4,5,7,8][0-9]{9}$/.test(phone))){ 
            	layer.msg("手机号码有误，请重填!");
            	return false; 
        	} 
        }
		
		function upperCase2(){
        	var phone = document.getElementById('zipCode').value;
        	if(!(/^\d{6}$/.test(phone))){ 
            	layer.msg('邮编有误，请重填!');
            	return false; 
        	} 
        }
		
		function saveAddress(){
			if($("#addressForm").valid()){
				var province = $('#province10').val();
				var city = $('#city10').val();
				var area = $('#district10').val();
				var officeId = $('#officeId option:selected').val();//选中的值
				$("#saveBtn").attr("class","btn btn_default mouse_arrow");
				$("#saveBtn").attr("disabled","disabled");
				
				//得到“mainIframeName”输入框中存储的值
				if(province && city && area){
					var url = '${pageContext.request.contextPath}/api/pcLabel/PC_saveAddress';
					var param = {
						token: $("#token").val(),
						province: province,
						city: city,
						area: area,
						receiver: $("#receiver").val(),
						phoneNum: $("#phoneNum").val(),
						detailAddress: $("#detailAddress").val(),
						zipCode: $("#zipCode").val(),
						officeId: officeId,
			    	};
					$.get(url, param, function(res) {
						$("#saveBtn").attr("class","btn btn_primary");
						$("#saveBtn").removeAttr("disabled");
						parent.location.reload();
						layer.alert('成功新增地址!', {icon: 1,title: "提示",yes:function(){
							var index = parent.layer.getFrameIndex(window.name); 
							parent.layer.close(index);
						}});
					});
				}else{
					layer.alert('请选择地区!', {
					    skin: 'layui-layer-lan'
					    ,closeBtn: 0
					    ,anim: 4 //动画类型
					  });
				}
			}
		};
	</script>
</body>
</html>

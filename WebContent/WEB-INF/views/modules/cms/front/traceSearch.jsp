<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>溯源搜索</title>
<meta name="decorator" content="cms_default_no_menu"/>
<link rel="stylesheet" href="${ctxNo}/static/cms/css/s.css">
<link rel="stylesheet" href="${ctxNo}/static/cms/css/layout.css">
<script type="text/javascript" src="${ctxStatic}/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/zn/js/jquery.qrcode.min.js"></script>
<style type="text/css"></style>
</head>
<body class="bg">
<div class="f_l maxbox searchbg">
  <div class="f_l maxbox searchbg_top">&nbsp;</div>
  <div class="search_main">
    <div class="f_l main_l">
      <div class="f_l main_l_search">
        <b>产品溯源</b>
        <input id="traceCode" name="traceCode" type="text" value="10002900012018114201812" placeholder="请输入产品身份编码">
        <input name="" type="button" value="查询" onclick="traceSearch();">
      </div>
      <div class="f_l" id="code1" style="width:180px;height:180px;margin-top:50px;margin-left:194px;">
      	<span class="code1" style="width:180px;height:180px;"></span>
      </div>
    </div>
    <div class="f_l main_show">
      <iframe name="show" src="" frameborder="0" width="480" height="100%"></iframe>
    </div>
   
  </div>
</div>
<script type="text/javascript">
function traceSearch() {
	var traceCode = $("#traceCode").val();
	if(!traceCode) {
		alert("请输入产品身份编码")
	} else {
		$.ajax({
			url: "${ctxNo}/api/traceCode/findTraceCode",
			data:"code=" + traceCode,
			type: "GET",
			dataType: 'json',
			success: function(data){
				$("#code1").html('');
				if(data.code == 0) {
					$("iframe").attr('src',"${ctxNo}/v1.html?traceCode=" + traceCode);
					$("#code1").html('<span class="code1" style="width:180px;height:180px;"></span>');
					var src = "http://trace.sureserve.cn/v.html?traceCode=" + traceCode;
					jQuery('.code1').qrcode({
						render: "canvas", 
						width: 180,
						height: 180,
						correctLevel: 0,
						text: src
					});
				} else {
					alert("该产品不存在");
				}
			}
		});
	}
}

</script>
</body>
</html>
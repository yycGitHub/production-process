<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>标签打印</title>
	<script type="text/javascript" src="${ctxStatic}/jquery/jquery-1.9.1.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/zn/js/users.js"></script>
	<script type="text/javascript" src="${ctxStatic}/zn/js/stringBuilder.js"></script>
	<script type="text/javascript" src="${ctxStatic}/zn/js/jquery.qrcode.min.js"></script>
	<style type="text/css">
		html,body
		 {margin: 0;           
		  padding: 0;
		}  
		.w75{width:75px}
		.w80{width:80px}
		.w245{width:245px}
		.h25{height:0.25cm}
		.h1{height:0.01cm}
		.h70{height:68px}
		.h90{height:90px}
		.font-3 {
			font-family: "黑体";
			font-size: 8px;
			-webkit-transform:scale(0.78);
			padding-top:2px;
		}
		
		.font-33 {
			font-family: "黑体";
			font-size: 8px;
			-webkit-transform:scale(0.78);
		}
		
		.font-4 {
			font-family: "黑体";
			font-size: 10px;
			-webkit-transform:scale(0.88);
		}
		
		.font-8 {
			-webkit-transform:scale(0.95);
		}
		
		.font-35 {
			font-family: "黑体";
			font-size: 10px;
			padding-top:1px;
		}
		
		.font-335 {
			font-family: "黑体";
			font-size: 10px;
		}
		
		.font-45 {
			font-family: "黑体";
			font-size: 11px;
			padding-left: 10px;
		}
		
	</style>
	<script type="text/javascript">
		$(document).ready(function() {
			getData();
		});
		
		function setButtonDisabled(){
			id = "import";
			$("#"+id).attr("class","min_btn btn_default upload_btn mouse_arrow");
			$("#"+id).attr("disabled","disabled");
		}
		
		function getData() {
			var applyId = '${applyId}';
			var token = '${token}';
			userInfo.saveToken(token);
			var serialNumberStart = '${serialNumberStart}';
			var serialNumberEnd = '${serialNumberEnd}';
			var pageno = 1;
	    	var pagesize = 10000;
	    	var param = {
	    		applyId: applyId,
	    		token: token,
	    		serialNumberStart: serialNumberStart,
	    		serialNumberEnd: serialNumberEnd,
	    		pageno: pageno,
	    		pagesize: pagesize
	    	};
	    	var url = "/sureserve-admin-dev/api/common/codelist";
	    	//var url = "/api/common/codelist";
		    $.get(url, param, function(res) {
		    	if(res.code == 0) {
					var data = res.bodyData.list;
					var applyContent = [];
					var url1 = "/sureserve-admin-dev/api/common/traceLableContentList?id="+applyId;
					//var url1 = "/api/common/traceLableContentList?id="+applyId;
					$.get(url1, {}, function(res) {
						if(res.code == 0) {
							applyContent = res.bodyData;
							dayin_new(data,applyContent);
							var param = {
								applyId: applyId,
								startSerialNumber: serialNumberStart,
								endSerialNumber: serialNumberEnd
							}
							saveTracePrintRecord(param);
						} else {
							alert("查询内容失败");
						}
					});
				} else {
					alert("查询内容失败");
				}
			});
		}
		
		var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
		
		function dayin_new(data,applyContent){
			var sb = new StringBuilder();
			for(var i=0; i<data.length; i++) {
				var traceCode = data[i].traceCode;
				var src = "http://sh.sureserve.cn/traceInfo.html?traceCode=" + traceCode;
				var fontname1 = applyContent[0].applyElementContent;
				var fontname2 = applyContent[1].applyElementContent;
				var fontname3 = applyContent[2].applyElementContent;
				var fontname4 = applyContent[3].applyElementContent;
				var fontname5 = applyContent[4].applyElementContent;
				var fontname6 = "身份编码:" + traceCode;
				
				//判断是否IE浏览器
				//设置打印内容
				if (userAgent.indexOf("Trident") > -1 || userAgent.indexOf("MSIE") > -1) {
					setContent_ie(sb,src,traceCode,fontname1,fontname2,fontname3,fontname4,fontname5,fontname6);
				}else {
					setContent_chorm(sb,src,traceCode,fontname1,fontname2,fontname3,fontname4,fontname5,fontname6);
				}
			}
			$("body").html(sb.toString());
			for(var i=0; i<data.length; i++) {
				var traceCode = data[i].traceCode;
				var src = "http://sh.sureserve.cn/v.html?traceCode=" + traceCode;
				if (userAgent.indexOf("Trident") > -1 || userAgent.indexOf("MSIE") > -1) {
					setCode(traceCode,src,75);
				}else {
					setCode(traceCode,src,78);
				}
			}
			window.print(); // 调用打印方法
		}
		
		//设置二维码
		function setCode(traceCode,src,len){
			jQuery('.code'+traceCode).qrcode({
				render: "canvas", 
				width: len,
				height: len,
				correctLevel: 0,
				text: src
			});
		}
		
		//设置打印内容
		function setContent_chorm(sb,src,traceCode,fontname1,fontname2,fontname3,fontname4,fontname5,fontname6){
			sb.append("<div style='width:10cm;height:3.8cm;page-break-after:always;'>");
			sb.append("<div style='width:10cm;height:0.98cm;'></div>");
			sb.append("<div style='width:10cm;height:2.7cm;padding-left:0.9cm'>");
			sb.append("<table width='100%' height='2.7cm' border='0' cellspacing='0' cellpadding='0'>");
			sb.append("<tr><td colspan='2' class='h1'></tr>");
			sb.append("<tr><td class='font-8 w80' rowspan='8' align='center'>");
			sb.append("<div class='" + ("code" + traceCode) +"'></div>");
			sb.append("<div class='font-3'>湖南12396</div><div class='font-33'>科技扶贫兴农</div></td>");
			sb.append("<td class='font-4 h25'></td></tr>");
			sb.append("<tr><td class='font-4 h25'>"+ fontname1 +"</td></tr>");
			sb.append("<tr><td class='font-4 h25'>"+ fontname2 +"</td></tr>");
			sb.append("<tr><td class='font-4 h25'>"+ fontname3 +"</td></tr>");
			sb.append("<tr><td class='font-4 h25'>"+ fontname4 +"</td></tr>");
			sb.append("<tr><td class='font-4 h25'>"+ fontname5 +"</td></tr>");
			sb.append("<tr><td class='font-4 h25'>"+ fontname6 +"</td></tr>");
			sb.append("<tr><td class='font-4 h25'></td></tr>");
			sb.append("<tr><td colspan='2' class='h1'></tr>");
			sb.append("</table></div>");
			sb.append("</div>");
			return sb.toString(" ");
		}
		
		//设置打印内容
		function setContent_ie(sb,src,traceCode,fontname1,fontname2,fontname3,fontname4,fontname5,fontname6){
			sb.append("<div style='width:9cm;height:3.8cm;page-break-after:always;transform:scale(0.95);'>");
			sb.append("<div style='width:9cm;height:0.90cm;'></div>");
			sb.append("<div style='width:9cm;height:2.7cm;padding-left:0.54cm'>");
			sb.append("<table width='100%' height='2.7cm' border='0' cellspacing='0' cellpadding='0'>");
			sb.append("<tr><td colspan='2' class='h1'></tr>");
			sb.append("<tr><td class='w75' rowspan='8' align='center'>");
			sb.append("<div class='" + ("code" + traceCode) +"'></div>");
			sb.append("<div class='font-335'>湖南12396</div><div class='font-335'>科技扶贫兴农</div></td>");
			sb.append("<td class='font-45 h25'></td></tr>");
			sb.append("<tr><td class='font-45 h25'>"+ fontname1 +"</td></tr>");
			sb.append("<tr><td class='font-45 h25'>"+ fontname2 +"</td></tr>");
			sb.append("<tr><td class='font-45 h25'>"+ fontname3 +"</td></tr>");
			sb.append("<tr><td class='font-45 h25'>"+ fontname4 +"</td></tr>");
			sb.append("<tr><td class='font-45 h25'>"+ fontname5 +"</td></tr>");
			sb.append("<tr><td class='font-45 h25'>"+ fontname6 +"</td></tr>");
			sb.append("<tr><td class='font-45 h25'></td></tr>");
			sb.append("<tr><td colspan='2' class='h1'></tr>");
			sb.append("</table></div>");
			sb.append("</div>");
			return sb.toString(" ");
		}
		
		function saveTracePrintRecord(param) {
			var url = "/sureserve-admin-dev/api/tracePrintRecord/saveTracePrintRecord";
			$.post(url, param, function(res) {
				if(res.code == 0) {
					if(res.code == 0) {
					} else {
						alert("保存标签记录失败");
					}
					window.opener=null;
					window.close();
				} 
			});
		}
	</script>
	
</head>
<body>
	
</body>
</html>

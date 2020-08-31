<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>评论审核</title>
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
		.tdOne{width: 100px;}
		.tdTwo{width: 150px;}
	</style>
</head>
<body>
	<div class="step-content" align="center" style="width:100%;height:100%;">
      <div class="step-list" style="width:90%;overflow: hidden;margin-top: 30px;">
	 	<form:form id="auditForm" action="" method="post" class="form-horizontal">
			<input id="token" type="hidden" value="${token}" />
			<input id="commentId" type="hidden" value="${commentId}" />
			<table class="tab" cellpadding="0" cellspacing="0">
				<tr>
					<td align="right" class="tdOne"><span class="red font18 martop">&lowast;&nbsp;</span>审核结果:</td>
					<td align="left" class="tdTwo">
						<div id="wrap">
    						<input type="radio" name="auditFlag" value="2" checked/>通过&nbsp;&nbsp;
    						<input type="radio" name="auditFlag" value="3" />拒绝
						</div>
					</td>
				</tr>
			</table>
		</form:form> 
		<div class="tab_btn_show">
			<input id="saveBtn" class="btn btn_primary" type="button" onclick="saveAudit()" value="保存"/>
		</div>
	  </div>
    </div>
	<script type="text/javascript" src="${ctxStatic}/jquery/jquery-1.9.1.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/zn/js/layui/layui.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jquery-validation/1.11.1/jquery.validate.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jquery-validation/1.11.1/jquery.validate.method.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/layer/layer.js"></script>
	
	<script type="text/javascript">
		$(document).ready(function() {
		    
		});
		function saveAudit(){
			var token = $("#token").val();
			var commentId = $("#commentId").val();
			var auditFlag = $('input:radio:checked').val();
			if($("#auditForm").valid()){
				$("#saveBtn").attr("class","btn btn_default mouse_arrow");
				$("#saveBtn").attr("disabled","disabled");
				
				var url = '${pageContext.request.contextPath}/api/pcIndex/pc_auditSave';
				var param = {
					token: token,
					commentId: commentId,
					auditFlag: auditFlag,
		    	};
				$.get(url, param, function(res) {
					$("#saveBtn").attr("class","btn btn_primary");
					$("#saveBtn").removeAttr("disabled");
					parent.location.reload();
					layer.alert('成功审核评论!', {icon: 1,title: "提示",yes:function(){
						var index = parent.layer.getFrameIndex(window.name); 
						parent.layer.close(index);
					}});
				});
			}
		}
	</script>
</body>
</html>
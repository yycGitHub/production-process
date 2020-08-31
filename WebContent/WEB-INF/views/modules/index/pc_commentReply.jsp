<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>评论回复</title>
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
		.tdTwo{width: 300px;}
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
					<td align="right" class="tdOne"><span class="red font18 martop">&lowast;&nbsp;</span>回复内容:</td>
					<td align="left" class="tdTwo">
    					<textarea id="commentReply" name="commentReply" style="height: 150px;width: 280px;"></textarea>
					</td>
				</tr>
			</table>
		</form:form> 
		<div class="tab_btn_show">
			<input id="saveBtn" class="btn btn_primary" type="button" onclick="saveReply()" value="保存"/>
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
		function saveReply(){
			var token = $("#token").val();
			var commentId = $("#commentId").val();
			var commentReply = $("#commentReply").val();
			if(commentReply.replace(/(^\s*)/g,"").length > 0){
				$("#saveBtn").attr("class","btn btn_default mouse_arrow");
				$("#saveBtn").attr("disabled","disabled");
				var url = '${pageContext.request.contextPath}/api/pcIndex/pc_saveCommentReply';
				var param = {
					token: token,
					commentId: commentId,
					replyContent: commentReply,
		    	};
				$.get(url, param, function(res) {
					$("#saveBtn").attr("class","btn btn_primary");
					$("#saveBtn").removeAttr("disabled");
					parent.location.reload();
					layer.alert('成功回复评论!', {icon: 1,title: "提示",yes:function(){
						var index = parent.layer.getFrameIndex(window.name); 
						parent.layer.close(index);
					}});
				});
			}else{
				layer.alert('请输入评论内容!', {
				    skin: 'layui-layer-lan'
				    ,closeBtn: 0
				    ,anim: 4 //动画类型
				  });
			}
		}
	</script>
</body>
</html>
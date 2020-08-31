<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<link href="${ctxStatic}/jquery-validation/1.11.1/jquery.validate.min.css" type="text/css" rel="stylesheet" />
<script src="${ctxStatic}/jquery-validation/1.11.1/jquery.validate.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-validation/1.11.1/jquery.validate.method.min.js" type="text/javascript"></script>
<style type="text/css">.reply{border:1px solid #ddd;background:#fefefe;margin:10px;}</style>
<script type="text/javascript">
	$(document).ready(function() {
		commentForm("#commentForm0");
	});
	function commentForm(form){
		$(form).validate({
			messages: {
				content: {required: "请填写评论内容"}
			},
			submitHandler: function(form){
			    $.post($(form).attr("action"), $(form).serialize(), function(data){
			    	data = eval("("+data+")");
			    	alert(data.message);
			    	if (data.result==1){
			    		page(1);
			    	}
			    });
			},
			errorContainer: form + " .messageBox",
			errorPlacement: function(error, element) {
				if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
					error.appendTo(element.parent().parent());
				} else {
					error.insertAfter(element);
				}
			}
		});
	}
	function comment(id){
		if ($("#commentForm"+id).html()==""){
			$(".commentForm").hide(500,function(){$(this).html("");});
			$("#commentForm"+id).html($("#commentFormTpl").html()).show(500);
			$("#commentForm"+id+" input[name='replyId']").val(id);
			commentForm("#commentForm"+id + " form");
		}else{
			$("#commentForm"+id).hide(500,function(){$(this).html("");});
		}
	}
</script>
      <div class="fl show martop20">
        <h2>评论</h2>
        <div class="fl maxbox comment">
          <c:forEach items="${page.list}" var="comment">
          <ul>
            <li class="green font16">${comment.createBy.name}</li>
            <li class="font12"><fmt:formatDate value="${comment.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></li>
            <li class="txt">${comment.content}</li>
            <li><a href="javascript:comment(${comment.id})"><i class="iconfont">&#xe625;</i> 回复</a></li>
            <li><div id="commentForm${comment.id}" class="commentForm hide"></div></li>
          </ul>
          </c:forEach>
          <c:if test="${fn:length(page.list) eq 0}">
			<ul>
				<li>暂时还没有人评论！</li>
			</ul>
		  </c:if>
        </div>
      </div>
      <h2>发表评论</h2>
      <div class="fl show martop20" id="commentFormTpl">
        <div class="fl maxbox">
        	<form id="commentForm0" action="${ctx}/comment" method="post" class="form-horizontal">
        		<input type="hidden" name="category.id" value="${comment.category.id}"/>
				<input type="hidden" name="contentId" value="${comment.contentId}"/>
				<input type="hidden" name="title" value="${comment.title}"/>
				<input type="hidden" name="replyId"/>
          		<textarea class="w_98 h100" name="content" cols="" rows=""></textarea>
          		<p align="center"><input name="" class="btn bg_green martop" type="submit" value="发表评论"></p>
          </form>
        </div>
      </div>
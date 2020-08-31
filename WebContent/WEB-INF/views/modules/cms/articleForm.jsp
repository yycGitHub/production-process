<%@page import="java.util.Date"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>文章管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript" charset="utf-8" src="${ctxStatic}/ueditor/ueditor.config.js?time=<%=new Date().getTime()%>"></script>
	<!-- 编辑器源码文件 -->
	<script type="text/javascript" charset="utf-8" src="${ctxStatic}/ueditor/ueditor.all.js?time=<%=new Date().getTime()%>"></script>
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/baidu/webuploader/webuploader.css">
	<script type="text/javascript" src="${ctxStatic}/baidu/webuploader/webuploader.min.js"></script>
	<script type="text/javascript">
		var BASE_URL = "${pageContext.request.contextPath}";
	</script>
	<script type="text/javascript" charset="utf-8" src="${ctxStatic}/baidu/uploadcommon.js?time=<%=new Date().getTime()%>"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			var ue = UE.getEditor('content',{ 
			    initialFrameWidth : 	100 + "%", 
			    initialFrameHeight: 350 
			});
			$("#title").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
                    if ($("#category.id").val()==""){
                        $("#category.name").focus();
                        top.$.jBox.tip('请选择归属栏目','warning');
                    }else if (ue.getContent()==""){
                        top.$.jBox.tip('请填写正文','warning');
                    }else{
                        form.submit();
                    }
				}
			});
		});
	</script>
</head>
<body>
<div class="miandody">
<div class="con">
    <div class="con_nav">
	<ul class="fl">
	        <li><a href="${ctx}/cms/article?category.id=${article.category.id}">文章列表</a></li>
	        <shiro:hasPermission name="cms:gxcms0102:edit">
	        <li><a class="cur"  href="<c:url value='${fns:getAdminPath()}/cms/article/form?id=${article.id}&category.id=${article.category.id}'><c:param name='category.name' value='${article.category.name}'/></c:url>">文章<shiro:hasPermission name="cms:article:edit">${not empty article.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="cms:article:edit">查看</shiro:lacksPermission></a></li>
			</shiro:hasPermission>
			
      	</ul>
      	<ul class="fr con_btn">
			<li><input id="btnCancel" class="btn btn_success min_btn"
				type="button" value="返回" onclick="history.go(-1)" /></li>
		</ul>
	</div>
 	<div class="con">
	<form:form id="inputForm" modelAttribute="article" action="${ctx}/cms/article/save" method="post" class="form-horizontal">	
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<div class="tab_door">
		<div class="tab_door_con">
		<table class="tab" cellpadding="0" cellspacing="0">
		<tbody>
		<tr>
			<td align="right" class="w100"><span class="red font18 martop">&lowast;&nbsp;</span>归属栏目: </td>
			<td colspan="2" align="left">
				 <tags:treeselect id="category" name="category.id" value="${article.category.id}" labelName="category.name" labelValue="${article.category.name}"
			title="栏目" url="/cms/category/treeData" module="article" selectScopeModule="true" notAllowSelectRoot="false" notAllowSelectParent="true" cssClass="input_txt required"/>&nbsp;
			</td>
		</tr>
		<tr>
			<td align="right"><span class="red font18 martop">&lowast;&nbsp;</span>标题: </td>
			<td colspan="2" align="left">
			<form:input path="title" htmlEscape="false" maxlength="200" class="input_txt w400 required"/>
			</td>
		</tr>
				
		<tr>
			<td align="right"><span class="red font18 martop">&lowast;&nbsp;</span>权重: </td>
			<td colspan="2" align="left">
				<form:input path="weight" htmlEscape="false" maxlength="200" class="input_txt required digits"/>&nbsp;
			<span>
				<input id="weightTop" type="checkbox" onclick="$('#weight').val(this.checked?'999':'0')"><label for="weightTop">置顶</label>
			</span>
			</td>
		</tr>
		
		<tr>
			<td align="right"><span class="red font18 martop">&lowast;&nbsp;</span>正文: </td>
			<td colspan="2" align="left">
				<textarea id="content" name="content" class="required">${article.content}</textarea>
			</td>
		</tr>
		<tr>
			<td align="right">来源: </td>
			<td colspan="2" align="left">
				<form:input path="copyfrom" htmlEscape="false" maxlength="200" class="input_txt"/>
			</td>
		</tr>
		
		<tr>
            <td align="right">图片：</td>
            <td align="left" class="w400">
            	<form:hidden path="image"/>
				<div id="ls">
					<img id="image" src="${article.image}" width='100px;height:100px;'>
				</div>
	        </td>
	        <td>
	            <div class="btns">
		        	<div id="picker"  onclick="addPicButton();"></div>
		    	</div>
	        </td>
	    </tr>
	    
	    <tr>
            <td align="right">附件：</td>
            <td align="left" class="w400">
               	<div id="hiddenFilesF">
				</div>
				<div id="lsF">
					<c:set var="fileList" value="${fep:getFiles(article.id,1,'picButton_one')}"/>
					<c:forEach items="${fileList}" var="fileInfo" varStatus="status">
               			<div id="${status.index}_picButton_one"><span>${fileInfo.fileName}</span>&nbsp;&nbsp;
	               			<span class="state">已上传</span>
	               			<a onclick="return delUploadedFile('${status.index}_picButton_one','${fileInfo.id}');" 
	               			href="javascript:;">删除</a>
               			</div>
               		</c:forEach>
				</div>
            </td>
            <td>
               	<div class="btns">
		        <div id="pickerF"  onclick="addPicButtonF();"></div>
		    	</div>
             </td>
        </tr>
		<tags:expandPropertyTable targetId="${article.category.id}" fromTableId="${article.id}" fromTableName="article"/>
		</tbody>
		</table>
		</div>
		<div class="tab_btn_show">
			<shiro:hasPermission name="cms:article:edit">
				<input id="btnSubmit" class="btn btn_primary" type="submit" value="保 存" />&nbsp;
			</shiro:hasPermission>
		</div>
		</div>
	</form:form>
	</div>
</div>
</div>
<script type="text/javascript">	
		//有几个上传按钮，就绑定几个
		uploader.addButton({
		    id: '#picker',//按钮ID
		    innerHTML: '选择文件'//按钮名称
		});
		
		uploader.addButton({
		    id: '#pickerF',//按钮ID
		    innerHTML: '选择文件'//按钮名称
		});
		
		function addPicButton(){
			oneFile = true;
			//设置上传文件显示列表
			$list = $('#ls');
			$image = $('#image');
			//设置上传子目录
			uploader.options.formData.moduleFilePath="articlefiles";
/* 			uploader.option('accept',{
				title: 'Images',
		        extensions: 'gif,jpg,jpeg,bmp,png',
		        mimeTypes: 'image/*'
			}); */
		}
		
		function addPicButtonF(){
			oneFile = false;
			fieldMark = 'picButton_one';
			uploader.option('formData',{"fieldMark":fieldMark});
			//设置上传文件显示列表
			$list = $('#lsF');
			$hiddenFiles = $('#hiddenFilesF');
			//设置上传子目录
			uploader.options.formData.moduleFilePath="articlefiles";
		}
	</script>
</body>
</html>
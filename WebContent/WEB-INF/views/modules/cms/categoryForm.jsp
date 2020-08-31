<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>栏目管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate();
		});
	</script>
</head>
<body>
    <div class="miandody">
	<div class="con">
	<form:form id="inputForm" modelAttribute="category" action="${ctx}/cms/category/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<div class="list_edit">
		<dl>
        <dt>归属机构:</dt>
        <dd>
        	 <tags:treeselect id="office" name="office.id" value="${category.office.id}" labelName="office.name" labelValue="${category.office.name}"
					title="机构" url="/sys/office/treeData" cssClass="required input_txt w400"/>
        </dd>
        </dl>
        <dl>
        <dt>上级栏目:</dt>
        <dd>
        	<tags:treeselect id="category" name="parent.id" value="${category.parent.id}" labelName="parent.name" labelValue="${category.parent.name}"
					title="栏目" url="/cms/category/treeData" extId="${category.id}" cssClass="required input_txt w400"/>
        </dd>
        </dl>
        <dl>
        <dt>栏目模型:</dt>
        <dd>
        	<form:select path="module" class="drop_down gray w200">
					<form:option value="" label="公共模型"/>
					<form:options items="${fns:getDictList('cms_module')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
        </dd>
        </dl>
        
        <dl>
        <dt>栏目名称:</dt>
        <dd>
        	<form:input path="name" htmlEscape="false" maxlength="50" class="required input_txt w400"/>
        </dd>
        </dl>
        
        <dl>
        <dt>栏目图片:</dt>
        <dd>
        	<form:hidden path="image" htmlEscape="false" maxlength="255" class="input-xlarge"/>
        </dd>
        </dl>
        
        <dl>
        <dt>链接:</dt>
        <dd>
        	<form:input path="href" htmlEscape="false" maxlength="200" class="input_txt w400"/>
				<span class="help-inline">栏目超链接地址，优先级“高”</span>
        </dd>
        </dl>
        
        <dl>
        <dt>目标:</dt>
        <dd>
        	<form:input path="target" htmlEscape="false" maxlength="200" class="input_txt w400"/>
				<span class="help-inline">栏目超链接打开的目标窗口，新窗口打开，请填写：“_blank”</span>
        </dd>
        </dl>
        
        <dl>
        <dt>描述:</dt>
        <dd>
        	<form:textarea path="description" htmlEscape="false" rows="4" maxlength="200" class="area h50 w400"/>
        </dd>
        </dl>
        
        <dl>
        <dt>关键字:</dt>
        <dd>
        	<form:input path="keywords" htmlEscape="false" maxlength="200" class="input_txt w400"/>
				<span class="help-inline">填写描述及关键字，有助于搜索引擎优化</span>
        </dd>
        </dl>
        
        <dl>
        <dt>排序:</dt>
        <dd>
        	<form:input path="sort" htmlEscape="false" maxlength="11" class="required digits input_txt w400"/>
				<span class="help-inline">栏目的排列次序</span>
        </dd>
        </dl>
        
        <dl>
        <dt>在导航中显示:</dt>
        <dd>
        	<form:radiobuttons path="inMenu" items="${fns:getDictList('show_hide')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required"/>
        	<span class="help-inline">是否在导航中显示该栏目</span>
        </dd>
        </dl>
        
        <dl>
        <dt>在分类页中显示列表:</dt>
        <dd>
        	<form:radiobuttons path="inList" items="${fns:getDictList('show_hide')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required"/>
        	<span class="help-inline">是否在分类页中显示该栏目的文章列表</span>
        </dd>
        </dl>
        <dl>
        <dt>是否需要审核:</dt>
        <dd>
        	<form:radiobuttons path="isAudit" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
        </dd>
        </dl>
        <dl>
	        <dt>是否允许评论:</dt>
	        <dd>
	        	<form:radiobuttons path="allowComment" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
	        </dd>
        </dl>
        <dl>
	        <dt>评论是否需要审核:</dt>
	        <dd>
	        	<form:radiobuttons path="isCommentAudit" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
	        </dd>
        </dl>
        <dl>
        <dt>自定义列表视图:</dt>
        	<dd>
        		<form:select path="customListView" class="drop_down gray w200">
                    <form:option value="" label="默认视图"/>
                    <form:options items="${listViewList}" htmlEscape="false"/>
                </form:select>
        	</dd>
        </dl>
        
        <dl>
        <dt>自定义内容视图:</dt>
        	<dd>
        		<form:select path="customContentView" class="drop_down gray w200">
                    <form:option value="" label="默认视图"/>
                    <form:options items="${contentViewList}" htmlEscape="false"/>
                </form:select>	
        	</dd>
        </dl>
        <dl>
        <dt>&nbsp;</dt>
        <dd>
        	<shiro:hasPermission name="cms:category:edit">
        		<input id="btnSubmit" class="btn btn_primary" type="submit" value="保 存"/>&nbsp;
        	</shiro:hasPermission>
			<input id="btnCancel" class="btn btn_primary" type="button" value="返 回" onclick="history.go(-1)"/>
		</dd>
      </dl>
	</div>
	</form:form>
	</div>
	</div>
</body>
</html>
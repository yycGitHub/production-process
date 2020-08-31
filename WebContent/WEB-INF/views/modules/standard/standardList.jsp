<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>表单管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>
	<style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
	<script src="${ctxStatic}/common/pony.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(document).ready(function() {		
			$("#btnDelete").click(function(){
			   	if(Pn.checkedCount('ids')<=0) {
			   	   $.jBox.alert("请选择!","提示");
			       return;
			  	}
				top.$.jBox.confirm("确认要删除吗？","系统提示",function(v,h,f){
					if(v == "ok"){
						 $("#contentTableForm").attr("action", "${ctx}/standard/delete"); 
						 $("#contentTableForm").submit();
					}
				},{buttonsFocus:1});
			});
		});
		
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/standard/").submit();
	    	return false;
	    }
		
		function clearInput(){
			$(".search dt :text").val("");
			$(".search dt select").val("");
		}
	</script>
</head>
<body>
	<div class="miandody">
	<div class="crumb">
		<i class="iconfont">&#xe60d;</i><strong>表单管理</strong><span
			class="songti">&gt;</span><span>表单列表</span>
		<b class="fr">
			<a class="min_btn btn_secondary marright" id="btnDelete">删除</a>
		</b>
		<b class="fr">
			<a href="${ctx}/standard/form" class="min_btn btn_secondary marright">新增</a>
		</b>
	</div>
	<div class="con">
		<form:form id="searchForm" modelAttribute="standard" action="${ctx}/standard/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
			<div class="search">
				<dl>
					<dt>
						<span>
							<form:input path="name" htmlEscape="false"
								maxlength="50" cssClass="input_txt w260" placeholder="名称" />
						</span>
					</dt>
					<dd>
			          <span><input id="btnSubmit" class="btn btn_primary" type="submit" value="查询" onclick="return page();"/></span>
			          <span><input class="btn btn_default" name="" type="button" value="清除" onclick="clearInput()"></span>
			        </dd>
				</dl>
			</div>
		</form:form>
		<tags:message content="${message}"/>
		<div class="tabcon">
		<form id="contentTableForm" name="contentTableForm" action="" method="post">
			<table class="tab" id="contentTable" cellpadding="0" cellspacing="0">
				<thead>
					<tr>
						<td><input type="checkbox"  onclick="Pn.checkbox('ids',this.checked)"/></td>
						<td><nobr class="sort-column name">名称</nobr></td>
						<td><nobr>操作</nobr></td>
					</tr>
				</thead>
				<tbody>
						<c:forEach items="${page.list}" var="standard">
							<tr>
								<td align="center"><input type="checkbox" name="ids" value="${standard.id}" /></td>
								<td><a href="${ctx}/standard/form?standardId=${standard.id}">${standard.name}</a></td>
								<td>
									<a href="${ctx}/standard/form?standardId=${standard.id}">修改</a>
									<a href="${ctx}/standard/delete?ids=${standard.id}"
											onclick="return confirmx('确认要删除该用户吗？', this.href)">删除</a>
									<a href="${ctx}/standard/standardItemList?stId=${standard.id}" >参数项管理</a>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				<tfoot>
				<tr>
					<td colspan="40">
						<div class="fr">
							<div class="page">${page}</div>
						</div>
					</td>
				</tr>
				</tfoot>
			</table>
			</form>
		</div>
	</div>
</div>
</body>
</html>
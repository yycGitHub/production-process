<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:getConfig('productName')}</title> 
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/common/pony.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(document).ready(function() {	
			$("#btnExport").click(function(){
				location.href = "${ctx}/sys/menu/form?id=${menu.id}&rebackId=${menu.id}";
			});
			$("#btnImport").click(function(){
				location.href = "${ctx}/sys/menu/form?parent.id=${menu.id}&rebackId=${menu.id}";
			});
			$("#btnDelete").click(function(){
			   	if(Pn.checkedCount('ids')<=0) {
			   	   $.jBox.alert("请选择要删除的菜单!","提示");
			       return;
			  	}
				top.$.jBox.confirm("确认要删除菜单吗？","系统提示",function(v,h,f){
					if(v == "ok"){
						 $("#contentTableForm").attr("action", "${ctx}/sys/menu/delete"); 
						 $("#contentTableForm").submit();
					}
				},{buttonsFocus:1});
			});
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
	</script>
</head>
<body>
<div class="miandody">
  <div class="con">
    <div class="con_nav">
      <ul class="fr con_btn">
        <li><input class="btn btn_secondary min_btn" id="btnExport" name="" type="button" value="编辑本级"></li>
        <li><input class="btn btn_secondary min_btn" id="btnImport" name="" type="button" value="添加下级"></li>
      </ul>
    </div>
    <form:form id="searchForm" modelAttribute="menu" action="${ctx}/sys/menu/list" method="post" class="breadcrumb form-search">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
	<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
	<input id="id" name="id" type="hidden" value="${menu.id}"/>
    <div class="search">
      <dl>
        <dt>
          <span class="drop_down_triangle">
          <form:select path="delFlag" class="drop_down gray w200">
			  <form:options items="${fns:getDictList('sys_user_act')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
		  </form:select>
          </span>
          <span><input class="btn btn_primary" id="btnSubmit" name="" type="submit" value="查询"></span>
          <span><input class="btn btn_default" id="btnDelete" name="" type="button" value="删除"></span>
        </dt>
      </dl>
    </div>
    </form:form>
    <div class="clearfix"><p class="martop">&nbsp;</p></div>
    <form id="contentTableForm" name="contentTableForm" action="" method="post">
    <div class="tabcon">
      <table class="tab" cellpadding="0" cellspacing="0">
        <thead>
          <tr>
			<td><input type="checkbox"  onclick="Pn.checkbox('ids',this.checked)"/></td>
			<td>菜单名称</td><td>链接</td><td>排序</td><td>可见</td><td>权限标识</td>
			<shiro:hasPermission name="sys:menu:edit"><td>操作</td></shiro:hasPermission>
          </tr>
        </thead>
        <tbody>
		<c:forEach items="${page.list}" var="menuTemp">
			<tr>
				<td align="center"><input type="checkbox" name="ids" value="${menuTemp.id}"/></td>
				<td><a href="${ctx}/sys/menu/form?id=${menuTemp.id}&rebackId=${menuTemp.id}">${menuTemp.name}</a></td>
				<td>${menuTemp.href}</td>
				<td>${menu.sort}</td>
				<td>${menu.isShow eq '1'?'显示':'隐藏'}</td>
				<td>${menu.permission}</td>
				<shiro:hasPermission name="sys:menu:edit"><td>
					<c:if test="${menuTemp.delFlag == '0'}">
    					<a href="${ctx}/sys/menu/form?id=${menuTemp.id}&rebackId=${menuTemp.id}">修改</a>
						<a href="${ctx}/sys/menu/delete?id=${menuTemp.id}&rebackId=${menuTemp.id}" 
						onclick="return confirmx('要删除该菜单及所有子菜单项吗？', this.href)">删除</a>
					</c:if>
					<c:if test="${menuTemp.delFlag == '1'}">
						<a href="${ctx}/sys/menu/rebackOffice?ids=${menuTemp.id}" 
						onclick="return confirmx('确认要恢复吗？', this.href)">恢复</a>
					</c:if>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
        </tbody>
        <tfoot>
        <tr>
            <td colspan="22">
              <div class="fr">
                <div class="page">
                	${page}
                </div>
              </div>
            </td>
        </tfoot>
      </table>
    </div>
    </form>
  </div>
</div>
</body>
</html>
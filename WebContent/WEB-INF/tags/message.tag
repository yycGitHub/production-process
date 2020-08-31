<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ attribute name="content" type="java.lang.String" required="true" description="消息内容"%>
<%@ attribute name="type" type="java.lang.String" description="消息类型：danger、success、warning、info"%>
<c:if test="${not empty content}">
	<c:if test="${not empty type}">
		<c:set var="ctype" value="${type}"/>
	</c:if>
	<c:if test="${empty type}">
		<c:set var="ctype" value="${fn:indexOf(content,'失败') eq -1?'success':'danger'}"/>
	</c:if>
	<div class="alert alert-${ctype}">
		<button type="button" class="close" data-dismiss="alert">
			<i class="ace-icon fa fa-times"></i>
		</button>

		<strong>
			<c:if test="${ctype == 'danger'}">
				<i class="ace-icon fa fa-times"></i>
			</c:if>
			<c:if test="${ctype == 'success'}">
				<i class="ace-icon fa fa-check"></i>
			</c:if>
		</strong>
		${content}
	</div>
</c:if>
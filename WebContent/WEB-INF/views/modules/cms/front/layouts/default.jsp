<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=yes">
<meta name="renderer" content="webkit">
<title>${fns:getConfig('productName')}</title>
<%@include file="/WEB-INF/views/modules/cms/front/include/head.jsp" %>
<sitemesh:head/>
</head>
<body class="bg">
<%@include file="/WEB-INF/views/modules/cms/front/include/top.jsp" %>
<sitemesh:body/>
<%@include file="/WEB-INF/views/modules/cms/front/include/bottom.jsp" %>
</body>
</html>
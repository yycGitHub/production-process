<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<script type="text/javascript">
	var appId = '${appid}';
	var fromurl=location.href+"?redirectUrl="+"${redirectUrl}";  
	var url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId + "&redirect_uri=" + fromurl + "&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
	location.href= url;
</script>
</head>
<body>
	${erro}
</body>
</html>
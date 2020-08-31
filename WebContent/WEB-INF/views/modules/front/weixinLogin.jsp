<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">

    <head>

        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>平台账户绑定微信</title>

        <!-- CSS -->
        <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Roboto:400,100,300,500">
        <link rel="stylesheet" href="${ctxStatic}/front/index/assets/bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" href="${ctxStatic}/front/index/assets/font-awesome/css/font-awesome.min.css">
		<link rel="stylesheet" href="${ctxStatic}/front/index/assets/css/form-elements.css">
        <link rel="stylesheet" href="${ctxStatic}/front/index/assets/css/style.css">

        <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
            <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
            <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
        <![endif]-->

        <!-- Favicon and touch icons -->
        <link rel="shortcut icon" href="assets/ico/favicon.png">
        <link rel="apple-touch-icon-precomposed" sizes="144x144" href="${ctxStatic}/front/index/assets/ico/apple-touch-icon-144-precomposed.png">
        <link rel="apple-touch-icon-precomposed" sizes="114x114" href="${ctxStatic}/front/index/assets/ico/apple-touch-icon-114-precomposed.png">
        <link rel="apple-touch-icon-precomposed" sizes="72x72" href="${ctxStatic}/front/index/assets/ico/apple-touch-icon-72-precomposed.png">
        <link rel="apple-touch-icon-precomposed" href="${ctxStatic}/front/index/assets/ico/apple-touch-icon-57-precomposed.png">
		<script>
			var ctxStatic = '${ctxStatic}';
		</script>
    </head>

    <body>

        <!-- Top content -->
        <div class="top-content">
        	
            <div class="inner-bg">
                <div class="container">
                    <div class="row">
                        <div class="col-sm-6 col-sm-offset-3 form-box">
                        	<div class="form-top">
                        		<div class="form-top-left">
                        			<h3>平台账户绑定微信</h3>
                            		<p>输入平台用户名和密码:</p>
                        		</div>
                        		<div class="form-top-right">
                        			<i class="fa fa-key"></i>
                        		</div>
                            </div>
                            <div class="form-bottom">
			                    <form role="form" action="${ctxFront}/login" method="post" class="login-form">
			                    	<input type="hidden" name="redirectUrl" value="${redirectUrl}"></input>
			                    	<input type="hidden" name="openId" value="${openId }"></input>
			                     	<div class="form-group">
			                    		<label class="sr-only" for="username">用户名</label>
			                        	<input type="text" name="username" placeholder="用户名" class="form-username form-control" id="form-username">
			                        </div>
			                        <div class="form-group">
			                        	<label class="sr-only" for="password">密码</label>
			                        	<input type="password" name="password" placeholder="密码" class="form-password form-control" id="form-password">
			                        </div>
			                        <button type="submit" class="btn">绑定</button>
			                    </form>
		                    </div>
                        </div>
                    </div>
                
                </div>
            </div>
            
        </div>


        <!-- Javascript -->
        <script src="${ctxStatic}/front/index/assets/js/jquery-1.11.1.min.js"></script>
        <script src="${ctxStatic}/front/index/assets/bootstrap/js/bootstrap.min.js"></script>
        <script src="${ctxStatic}/front/index/assets/js/jquery.backstretch.min.js"></script>
        <script src="${ctxStatic}/front/index/assets/js/scripts.js"></script>
        
        <!--[if lt IE 10]>
            <script src="${ctxStatic}/patrol/index/assets/js/placeholder.js"></script>
        <![endif]-->

    </body>

</html>
//用户登录信息
var userInfo = new UserInfo();

function UserInfo() {
	var saveName = "trace-loginInfo";
	var _self = this;
	
	this.logoOut = function(info) {
		localStorage.setItem("trace-loginInfo", "");
		localStorage.setItem("trace-token", "");
	};


	this.saveInfo = function(info) {
		localStorage.setItem("trace-loginInfo", JSON.stringify(info));
	};

	//获取保存的登录信息
	this.getInfo = function() {
		var str = localStorage.getItem("trace-loginInfo") || "";
		var strJ = "";
		if(str) {
			try {
				strJ = JSON.parse(str);
			} catch(e) {
				huiJsonLog("解析登录信息错误");
			};
		}
		return strJ;
	};

	//是否登录
	this.isLogin = function() {
		if(loginInfo = null) {
			this.getInfo();
		}

		if(typeof(loginInfo) != "undefined" && loginInfo != null) {
			return true;
		}

		return false;
	};

	//获取loginID
	this.getLoginId = function() {
		var str = this.getInfo();
		//}
		var id = "";
		if(str && str.loginID) {
			id = str.loginID ? str.loginID : "";
		}
		return id;
	};
	
	this.saveToken = function(token){
		localStorage.setItem("trace-token", token);
	}
	
	//获取用户token
	this.token = function(){
		var str = localStorage.getItem("trace-token") || "";
		return str.replace(/\s+/g,"");  ;
	}
	//获取用户loginName
	this.loginName = function(){
		var str = localStorage.getItem("loginName") || "";
		return str;
	}
	//获取用户操作系统  1 为ios系统， 0为ios以外系统
	this.ios = function(){
		var loginName = localStorage.getItem("loginName") || "";
		var ios = localStorage.getItem(loginName+"ios");
		return ios;
	}
}

//打开用户登录页面,登录后跳转
function loginWithRedirect(web) {
	window.localStorage.setItem("redirect", web);
	hui.open("login.html", {}, true, {});
}

//打开需要校验用户的网页
function openHtmlWithLogin(html) {
	if(IsNull(userInfo.getUserId())) {
		loginWithRedirect(html);
	} else {
		hui.open(html, {}, true, {});
	}
}
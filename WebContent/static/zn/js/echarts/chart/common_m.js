/**
 * JS 公用方法
 */


//时间格式化
Date.prototype.Format = function (fmt) { 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}
/*demo:    
var time1 = new Date().Format("yyyy-MM-dd");
var time2 = new Date().Format("yyyy-MM-dd HH:mm:ss");  
* */


//字符串去掉左右空格
 String.prototype.trim=function(){ 
return this.replace(/(^\s*)|(\s*$)/g, ""); 
} 

//判断字符串或对象是否为空
 function isNull (mixed_var) {  
        var key;  
       
        if (mixed_var === "" || mixed_var === 0 || mixed_var === "0" ||
        mixed_var === null || mixed_var === false || typeof mixed_var === 'undefined') {
            return true;  
        }  
       
        if (typeof mixed_var == 'object') {  
            for (key in mixed_var) {  
                return false;  
            }  
            return true;  
        }  
       
        return false;  
    }  
    
 //获取当前时间  （格式 :2017-06-08 15:30:27）
 function getNowFormatDate() {
	    var date = new Date();
	    var seperator1 = "-";
	    var seperator2 = ":";
	    var month = date.getMonth() + 1;
	    var strDate = date.getDate();
	    if (month >= 1 && month <= 9) {
	        month = "0" + month;
	    }
	    if (strDate >= 0 && strDate <= 9) {
	        strDate = "0" + strDate;
	    }
	    var hours= date.getHours();
	    if (hours >= 0 && hours <= 9) {
	    	hours = "0" + hours;
	    }
	    var minute = date.getMinutes();
	    if (minute >= 0 && minute <= 9) {
	    	minute = "0" + minute;
	    }
	    
	    var seconds = date.getSeconds();
	    if (seconds >= 0 && seconds <= 9) {
	    	seconds = "0" + seconds;
	    }
	    
	    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
	            + " " + hours + seperator2 + minute
	            + seperator2 + seconds;
	    return currentdate;
	}


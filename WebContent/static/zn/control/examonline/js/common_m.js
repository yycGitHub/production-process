/**
 * JS 公用方法
 */
//时间戳转时间
function formatDateTime(inputTime) {    
  	    var date = new Date(inputTime);  
  	    var y = date.getFullYear();    
  	    var m = date.getMonth() + 1;    
  	    m = m < 10 ? ('0' + m) : m;    
  	    var d = date.getDate();    
  	    d = d < 10 ? ('0' + d) : d;    
  	    var h = date.getHours();  
  	    h = h < 10 ? ('0' + h) : h;  
  	    var minute = date.getMinutes();  
  	    var second = date.getSeconds();  
  	    minute = minute < 10 ? ('0' + minute) : minute;    
  	    second = second < 10 ? ('0' + second) : second;   
  	    return y + '-' + m + '-' + d+' '+h+':'+minute;    
  	};  

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
 
	var tmpHTML="{"+
	"    \"id\":\"fe8d59a6efa546219da02bcbd774e8de\","+
	"    \"name\":\"地理试卷A卷\","+
	"    \"status\":0,"+
	"    \"starttime\":1498717875000,"+
	"    \"endtime\":1501309877000,"+
	"    \"duration\":10,"+
	"    \"totalscore\":20,"+
	"    \"passscore\":12,"+
	"    \"ordertype\":1,"+
	"    \"papertype\":0,"+
	"    \"remark\":\"地理试卷A卷1\","+
	"    \"sections\":["+
	"        {"+
	"            \"name\":\"选择题\","+
	"            \"remark\":\"选择题\","+
	"            \"id\":\"1\","+
	"            \"timquestions\":["+
	"                {"+
	"                    \"tmid\":\"3e0f9e2a8c75468081e84b366668088f\","+
	"                    \"questions\":["+
	"                        {"+
	"                            \"id\":\"638097859b2743e7ad8635e79eefb992\","+
	"                            \"type\":\"4\","+
	"                            \"content\":\"<p>sdfdfsdfds_sdfsdfsdfsdf[BlankArea1]</p> \","+
	"                            \"score\":2,"+
	"                            \"ext\":\"<p>sdfdsfsdfsdfsdfsd</p> \","+
	"                            \"blanks\":["+
	"                                {"+
	"                                    \"id\":1,"+
	"                                    \"name\":\"BLANK1\","+
	"                                    \"value\":\"2222\""+
	"                                }"+
	"                            ],"+
	"                            \"complex\":false"+
	"                        }"+
	"                    ]"+
	"                },"+
	"                {"+
	"                    \"tmid\":\"0271e8986f5f495895e7348f26163f25\","+
	"                    \"questions\":["+
	"                        {"+
	"                            \"id\":\"6cef2d5c990e4bb9aaf74e08086ffbe3\","+
	"                            \"type\":\"1\","+
	"                            \"content\":\"<p>222222222222</p> \","+
	"                            \"key\":\"A\","+
	"                            \"score\":2,"+
	"                            \"ext\":\"<p>23423423423</p> \","+
	"                            \"options\":["+
	"                                {"+
	"                                    \"alisa\":\"A\","+
	"                                    \"text\":\"1\""+
	"                                },"+
	"                                {"+
	"                                    \"alisa\":\"B\","+
	"                                    \"text\":\"2\""+
	"                                },"+
	"                                {"+
	"                                    \"alisa\":\"C\","+
	"                                    \"text\":\"3\""+
	"                                },"+
	"                                {"+
	"                                    \"alisa\":\"D\","+
	"                                    \"text\":\"4\""+
	"                                }"+
	"                            ]"+
	"                        }"+
	"                    ]"+
	"                },"+
	"                {"+
	"                    \"tmid\":\"2a64b13c1d0a4761a0a628a722a87275\","+
	"                    \"questions\":["+
	"                        {"+
	"                            \"id\":\"b7f2c046613f4b26bca8e1e21592eb84\","+
	"                            \"type\":\"1\","+
	"                            \"content\":\"<p>111111111111</p> \","+
	"                            \"key\":\"A\","+
	"                            \"score\":2,"+
	"                            \"ext\":\"<p>sdfdfdsfds</p> \","+
	"                            \"options\":["+
	"                                {"+
	"                                    \"alisa\":\"A\","+
	"                                    \"text\":\"1\""+
	"                                },"+
	"                                {"+
	"                                    \"alisa\":\"B\","+
	"                                    \"text\":\"2\""+
	"                                },"+
	"                                {"+
	"                                    \"alisa\":\"C\","+
	"                                    \"text\":\"3\""+
	"                                },"+
	"                                {"+
	"                                    \"alisa\":\"D\","+
	"                                    \"text\":\"4\""+
	"                                }"+
	"                            ]"+
	"                        }"+
	"                    ]"+
	"                },"+
	"                {"+
	"                    \"tmid\":\"00634931925f4561b256162cb33d031b\","+
	"                    \"questions\":["+
	"                        {"+
	"                            \"id\":\"9d6fb7a533e3412d84fcdccc977286d6\","+
	"                            \"type\":\"1\","+
	"                            \"content\":\"<p>sdfdsfds</p> \","+
	"                            \"key\":\"A\","+
	"                            \"score\":2,"+
	"                            \"ext\":\"<p>sdfdsfsd</p> \","+
	"                            \"options\":["+
	"                                {"+
	"                                    \"alisa\":\"A\","+
	"                                    \"text\":\"aa\""+
	"                                },"+
	"                                {"+
	"                                    \"alisa\":\"B\","+
	"                                    \"text\":\"bb\""+
	"                                },"+
	"                                {"+
	"                                    \"alisa\":\"C\","+
	"                                    \"text\":\"cc\""+
	"                                },"+
	"                                {"+
	"                                    \"alisa\":\"D\","+
	"                                    \"text\":\"dd\""+
	"                                }"+
	"                            ]"+
	"                        }"+
	"                    ]"+
	"                },"+
	"                {"+
	"                    \"tmid\":\"96a4d0eff3524e239723f530c4ca29a0\","+
	"                    \"questions\":["+
	"                        {"+
	"                            \"id\":\"fc653f8c847c4bfa9d9cd5fa539f4dee\","+
	"                            \"type\":\"1\","+
	"                            \"content\":\"<p>sdfsdfsdfsd</p> \","+
	"                            \"key\":\"A\","+
	"                            \"score\":2,"+
	"                            \"ext\":\"<p>sdfsdfsdf</p> \","+
	"                            \"options\":["+
	"                                {"+
	"                                    \"alisa\":\"A\","+
	"                                    \"text\":\"a\""+
	"                                },"+
	"                                {"+
	"                                    \"alisa\":\"B\","+
	"                                    \"text\":\"b\""+
	"                                },"+
	"                                {"+
	"                                    \"alisa\":\"C\","+
	"                                    \"text\":\"c\""+
	"                                },"+
	"                                {"+
	"                                    \"alisa\":\"D\","+
	"                                    \"text\":\"d\""+
	"                                }"+
	"                            ]"+
	"                        }"+
	"                    ]"+
	"                },"+
	"                {"+
	"                    \"tmid\":\"6c50716508334126900342447ae698e1\","+
	"                    \"questions\":["+
	"                        {"+
	"                            \"id\":\"59157c0984954813b5b62832b2c7dc72\","+
	"                            \"type\":\"1\","+
	"                            \"content\":\"<p>dsfdf</p> \","+
	"                            \"key\":\"A\","+
	"                            \"score\":2,"+
	"                            \"ext\":\"<p>sdfdsfds</p> \","+
	"                            \"options\":["+
	"                                {"+
	"                                    \"alisa\":\"A\","+
	"                                    \"text\":\"sdf\""+
	"                                },"+
	"                                {"+
	"                                    \"alisa\":\"B\","+
	"                                    \"text\":\"sdf\""+
	"                                },"+
	"                                {"+
	"                                    \"alisa\":\"C\","+
	"                                    \"text\":\"sdfsd\""+
	"                                },"+
	"                                {"+
	"                                    \"alisa\":\"D\","+
	"                                    \"text\":\"fds\""+
	"                                }"+
	"                            ]"+
	"                        }"+
	"                    ]"+
	"                },"+
	"                {"+
	"                    \"tmid\":\"45a1243acd574fb1a17b6f6cd07329e4\","+
	"                    \"tmcontext\":\"bbbbbbbbbbbbbbbbbbbbbbbbbbbbbb\","+
	"                    \"questions\":["+
	"                        {"+
	"                            \"id\":\"285cc704989a4eb98a7f975956e8531f\","+
	"                            \"type\":\"3\","+
	"                            \"content\":\"<p>sdfsdf</p> \","+
	"                            \"key\":\"Y\","+
	"                            \"score\":2,"+
	"                            \"ext\":\"<p>sdfsdfdsfdsfsdf</p> \""+
	"                        },"+
	"                        {"+
	"                            \"id\":\"a92ca401fd4a4e048a9c61a1682ba1b4\","+
	"                            \"type\":\"1\","+
	"                            \"content\":\"<p>sdfdsfsd</p> \","+
	"                            \"key\":\"A\","+
	"                            \"score\":2,"+
	"                            \"ext\":\"<p>sdfsdfsdfds</p> \","+
	"                            \"options\":["+
	"                                {"+
	"                                    \"alisa\":\"A\","+
	"                                    \"text\":\"sdf\""+
	"                                },"+
	"                                {"+
	"                                    \"alisa\":\"B\","+
	"                                    \"text\":\"sdf\""+
	"                                },"+
	"                                {"+
	"                                    \"alisa\":\"C\","+
	"                                    \"text\":\"sdf\""+
	"                                },"+
	"                                {"+
	"                                    \"alisa\":\"D\","+
	"                                    \"text\":\"sdfsd\""+
	"                                }"+
	"                            ]"+
	"                        }"+
	"                    ]"+
	"                },"+
	"                {"+
	"                    \"tmid\":\"22fbcab2197f402ea8f31979343e2b0a\","+
	"                    \"questions\":["+
	"                        {"+
	"                            \"id\":\"5ba6efd91be148efaf31f8c113ba2cd0\","+
	"                            \"type\":\"3\","+
	"                            \"content\":\"<p>dsfsdsdf</p> \","+
	"                            \"key\":\"Y\","+
	"                            \"score\":2,"+
	"                            \"ext\":\"<p>sdfsdfsd</p> \""+
	"                        }"+
	"                    ]"+
	"                },"+
	"                {"+
	"                    \"tmid\":\"43b791606c7c4b3088d5c22b225158dd\","+
	"                    \"questions\":["+
	"                        {"+
	"                            \"id\":\"ad5fe6c47da9420f810824bc4c9e7fe5\","+
	"                            \"type\":\"1\","+
	"                            \"content\":\"<p>sdfsdf</p> \","+
	"                            \"key\":\"A\","+
	"                            \"score\":2,"+
	"                            \"ext\":\"<p>sdfsdfdsfds</p> \","+
	"                            \"options\":["+
	"                                {"+
	"                                    \"alisa\":\"A\","+
	"                                    \"text\":\"sdf\""+
	"                                },"+
	"                                {"+
	"                                    \"alisa\":\"B\","+
	"                                    \"text\":\"sdf\""+
	"                                },"+
	"                                {"+
	"                                    \"alisa\":\"C\","+
	"                                    \"text\":\"sdf\""+
	"                                },"+
	"                                {"+
	"                                    \"alisa\":\"D\","+
	"                                    \"text\":\"fsdf\""+
	"                                }"+
	"                            ]"+
	"                        }"+
	"                    ]"+
	"                }"+
	"            ],"+
	"            \"rnum\":0,"+
	"            \"rtype\":0,"+
	"            \"rlevel\":0,"+
	"            \"rscore\":0"+
	"        }"+
	"    ],"+
	"    \"showKey\":true,"+
	"    \"showMode\":1"+
	"}";
	
	
	var tmp2JSON= "{"+
	"  \"id\" : \"74b7067fc5034a5992ea99aea2df195d\","+
	"  \"name\" : \"测试选题试卷\","+
	"  \"status\" : 0,"+
	"  \"starttime\" : 1499050717000,"+
	"  \"endtime\" : 1499223518000,"+
	"  \"duration\" : 10,"+
	"  \"totalscore\" : 8,"+
	"  \"passscore\" : 5,"+
	"  \"ordertype\" : 1,"+
	"  \"papertype\" : 0,"+
	"  \"remark\" : \"测试选题试卷\","+
	"  \"sections\" : [ {"+
	"    \"name\" : \"选择判断填空多选\","+
	"    \"remark\" : \"选择判断填空多选\","+
	"    \"id\" : \"1\","+
	"    \"timquestions\" : [ {"+
	"      \"tmid\" : \"f4389298d9934df492c461388276cbc1\","+
	"      \"questions\" : [ {"+
	"        \"id\" : \"0067327fed244136b3b2950749c69d18\","+
	"        \"type\" : \"2\","+
	"        \"content\" : \"<p>asdasd</p>\/\/r\/\/n\","+
	"        \"key\" : \"AB\","+
	"        \"score\" : 2,"+
	"        \"ext\" : \"<p>lkjkj</p>\/\/r\/\/n\","+
	"        \"options\" : [ {"+
	"          \"alisa\" : \"A\","+
	"          \"text\" : \"a\""+
	"        }, {"+
	"          \"alisa\" : \"B\","+
	"          \"text\" : \"b\""+
	"        }, {"+
	"          \"alisa\" : \"C\","+
	"          \"text\" : \"c\""+
	"        }, {"+
	"          \"alisa\" : \"D\","+
	"          \"text\" : \"d\""+
	"        } ]"+
	"      } ]"+
	"    }, {"+
	"      \"tmid\" : \"3e0f9e2a8c75468081e84b366668088f\","+
	"      \"questions\" : [ {"+
	"        \"id\" : \"638097859b2743e7ad8635e79eefb992\","+
	"        \"type\" : \"4\","+
	"        \"content\" : \"<p>sdfdfsdfds_sdfsdfsdfsdf[BlankArea1]</p>\/\/r\/\/n\","+
	"        \"score\" : 2,"+
	"        \"ext\" : \"<p>sdfdsfsdfsdfsdfsd</p>\/\/r\/\/n\","+
	"        \"blanks\" : [ {"+
	"          \"id\" : 1,"+
	"          \"name\" : \"BLANK1\","+
	"          \"value\" : \"2222\""+
	"        } ],"+
	"        \"complex\" : false"+
	"      } ]"+
	"    }, {"+
	"      \"tmid\" : \"0271e8986f5f495895e7348f26163f25\","+
	"      \"questions\" : [ {"+
	"        \"id\" : \"6cef2d5c990e4bb9aaf74e08086ffbe3\","+
	"        \"type\" : \"1\","+
	"        \"content\" : \"<p>222222222222</p>\/\/r\/\/n\","+
	"        \"key\" : \"A\","+
	"        \"score\" : 2,"+
	"        \"ext\" : \"<p>23423423423</p>\/\/r\/\/n\","+
	"        \"options\" : [ {"+
	"          \"alisa\" : \"A\","+
	"          \"text\" : \"1\""+
	"        }, {"+
	"          \"alisa\" : \"B\","+
	"          \"text\" : \"2\""+
	"        }, {"+
	"          \"alisa\" : \"C\","+
	"          \"text\" : \"3\""+
	"        }, {"+
	"          \"alisa\" : \"D\","+
	"          \"text\" : \"4\""+
	"        } ]"+
	"      } ]"+
	"    }, {"+
	"      \"tmid\" : \"22fbcab2197f402ea8f31979343e2b0a\","+
	"      \"questions\" : [ {"+
	"        \"id\" : \"5ba6efd91be148efaf31f8c113ba2cd0\","+
	"        \"type\" : \"3\","+
	"        \"content\" : \"<p>dsfsdsdf</p>\/\/r\/\/n\","+
	"        \"key\" : \"Y\","+
	"        \"score\" : 2,"+
	"        \"ext\" : \"<p>sdfsdfsd</p>\/\/r\/\/n\""+
	"      } ]"+
	"    } ],"+
	"    \"rnum\" : 0,"+
	"    \"rtype\" : 0,"+
	"    \"rlevel\" : 0,"+
	"    \"rscore\" : 0"+
	"  } ],"+
	"  \"showKey\" : true,"+
	"  \"showMode\" : 1"+
	"}";


/*
 * IE Alert! jQuery plugin
 * version 1
 * author: David Nemes http://nmsdvid.com
 * http://nmsdvid.com/iealert/
 */

(function($){
$(document).on('click', "#goon", function(){
//$("#goon").live("click", function(){
		$("#ie-alert-overlay").hide();	
		$("#ie-alert-panel").hide();						  
});
function initialize($obj, support, title, text){

		var panel = "<span>"+ title +"</span>"
				  + "<p> "+ text +"</p>"
			      + "<div class='browser'>"
			      + "<ul>"
			      + "<li><a class='chrome' href='https://www.baidu.com/link?url=0wkXRaHlYFXdoW4l5EeSKjjtOBSXAi15f_OVRlAhr_U8vA2FUGd06a7ONrScyozKXAlhGY-vJ6NZb_w6MK058bEOqPBgobeJgNyGAlZKHEC&wd=&eqid=9d35d25f0000cfd00000000659b8a55a' target='_blank'></a></li>"
//			      + "<li><a class='firefox' href='http://www.mozilla.org/en-US/firefox/new/' target='_blank'></a></li>"
//			      + "<li><a class='ie9' href='http://windows.microsoft.com/en-US/internet-explorer/downloads/ie/' target='_blank'></a></li>"
//			      + "<li><a class='safari' href='http://www.apple.com/safari/download/' target='_blank'></a></li>"
//			      + "<li><a class='opera' href='http://www.opera.com/download/' target='_blank'></a></li>"
			      + "<ul>"
			      + "</div>"; 

		var overlay = $("<div id='ie-alert-overlay'></div>");
		var iepanel = $("<div id='ie-alert-panel'>"+ panel +"</div>");

		var docHeight = $(document).height();

		overlay.css("height", docHeight + "px");



			     
		
		if (support === "ie8") { 			// shows the alert msg in IE8, IE7, IE6
		
//			if ($.browser.msie  && parseInt($.browser.version, 10) < 9) {
//				
//				$obj.prepend(iepanel);
//				$obj.prepend(overlay);
//				
//			}

			if(navigator.userAgent.indexOf("MSIE")>0){   
				if(navigator.userAgent.indexOf("MSIE 6.0")>0 || navigator.userAgent.indexOf("MSIE 7.0")>0){   
					$obj.prepend(iepanel);
					$obj.prepend(overlay);
				}  
				if(navigator.userAgent.indexOf("MSIE 9.0")>0 && !window.innerWidth){   
					$obj.prepend(iepanel);
					$obj.prepend(overlay);
				}   
				if(navigator.userAgent.indexOf("MSIE 6.0")>0){   
					$("#ie-alert-panel").css("background-position","-626px -116px");
					$obj.css("margin","0"); 
				}   
			} 

//			if ($.browser.msie  && parseInt($.browser.version, 10) === 6) {
//
//				
//				$("#ie-alert-panel").css("background-position","-626px -116px");
//				$obj.css("margin","0");
//  
//			}
			
			
		} else if (support === "ie7") { 	// shows the alert msg in IE7, IE6
			if(navigator.userAgent.indexOf("MSIE")>0){   
				if(navigator.userAgent.indexOf("MSIE 6.0")>0 || navigator.userAgent.indexOf("MSIE 7.0")>0){   
					$obj.prepend(iepanel);
					$obj.prepend(overlay);
				} 
				if(navigator.userAgent.indexOf("MSIE 6.0")>0){   
					$("#ie-alert-panel").css("background-position","-626px -116px");
					$obj.css("margin","0"); 
				}   
			}

//			if ($.browser.msie  && parseInt($.browser.version, 10) < 8) {
//				
//				$obj.prepend(iepanel);
//				$obj.prepend(overlay);
//			}
			
//			if ($.browser.msie  && parseInt($.browser.version, 10) === 6) {
//				
//				$("#ie-alert-panel").css("background-position","-626px -116px");
//				$obj.css("margin","0");
//  
//			}
			
		} else if (support === "ie6") { 	// shows the alert msg only in IE6
			
//			if ($.browser.msie  && parseInt($.browser.version, 10) < 7) {
//				
//				$obj.prepend(iepanel);
//				$obj.prepend(overlay);
//				
//  				$("#ie-alert-panel").css("background-position","-626px -116px");
//				$obj.css("margin","0");
//				
//			}
			if(navigator.userAgent.indexOf("MSIE 6.0")>0){   
				$obj.prepend(iepanel);
				$obj.prepend(overlay);
				
				$("#ie-alert-panel").css("background-position","-626px -116px");
				$obj.css("margin","0"); 
			}   
		}

}; //end initialize function


	$.fn.iealert = function(options){
		var defaults = { 
			support: "ie7",  // ie8 (ie6,ie7,ie8), ie7 (ie6,ie7), ie6 (ie6)
			title: "\u4F60\u77E5\u9053\u4F60\u7684Internet Explorer\u662F\u8FC7\u65F6\u4E86\u5417?", // title text
			text: "为了得到我们网站最好的体验效果,我们建议您下载谷歌浏览器进行访问.<br /><br /><h1 id='goon' style='font-size:20px;cursor:pointer;'>>>>\u7EE7\u7EED\u8BBF\u95EE</h1>"
		};
		
		
		var option = $.extend(defaults, options);

		
		

			return this.each(function(){
				if ( navigator.userAgent.indexOf("MSIE")>0) {
					var $this = $(this);  
					initialize($this, option.support, option.title, option.text);
				} //if ie	
			});		       
	
	};
})(jQuery);

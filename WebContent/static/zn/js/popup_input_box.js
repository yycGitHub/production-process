(function($){
$.fn.bgIframe = $.fn.bgiframe = function(s) {
	// This is only for IE6
	if ( $.browser.msie && /6.0/.test(navigator.userAgent) ) {
		s = $.extend({
			top     : 'auto', // auto == .currentStyle.borderTopWidth
			left    : 'auto', // auto == .currentStyle.borderLeftWidth
			width   : 'auto', // auto == offsetWidth
			height  : 'auto', // auto == offsetHeight
			opacity : true,
			src     : 'javascript:false;'
		}, s || {});
		var prop = function(n){return n&&n.constructor==Number?n+'px':n;},
		    html = '<iframe class="bgiframe"frameborder="0"tabindex="-1"src="'+s.src+'"'+
		               'style="display:block;position:absolute;z-index:-1;'+
			               (s.opacity !== false?'filter:Alpha(Opacity=\'0\');':'')+
					       'top:'+(s.top=='auto'?'expression(((parseInt(this.parentNode.currentStyle.borderTopWidth)||0)*-1)+\'px\')':prop(s.top))+';'+
					       'left:'+(s.left=='auto'?'expression(((parseInt(this.parentNode.currentStyle.borderLeftWidth)||0)*-1)+\'px\')':prop(s.left))+';'+
					       'width:'+(s.width=='auto'?'expression(this.parentNode.offsetWidth+\'px\')':prop(s.width))+';'+
					       'height:'+(s.height=='auto'?'expression(this.parentNode.offsetHeight+\'px\')':prop(s.height))+';'+
					'"/>';
		return this.each(function() {
			if ( $('> iframe.bgiframe', this).length == 0 )
				this.insertBefore( document.createElement(html), this.firstChild );
		});
	}
	return this;
};

})(jQuery);

jQuery.fn.selectCity = function(targetId) {
	var _seft = this;
	var targetId = $(targetId);

	this.click(function(){
		var A_top = $(this).offset().top + $(this).outerHeight(true);  //  1
		var A_left =  $(this).offset().left;
		targetId.bgiframe();
		//targetId.show().css({"position":"absolute","top":A_top+"px" ,"left":A_left+"px"});原有文件
		targetId.show().css({"position":"fixed","bottom":0+"px" ,"left":0+"px"});
	});

	targetId.find(".popup_title_close").click(function(){
		targetId.hide();
	});

	targetId.find(".popup_con :checkbox").click(function(){		
		targetId.find(":checkbox").attr("checked",false);
		$(this).attr("checked",true);	
		_seft.val( $(this).val() );
		targetId.hide();
	});

	$(document).click(function(event){
		if(event.target.id!=_seft.selector.substring(1)){
			targetId.hide();	
		}
	});

	targetId.click(function(e){
		e.stopPropagation(); //  2
	});

    return this;
}
 
$(function(){
	//test1:
	$("#popup_input").selectCity("#popup_input_box");
	//test2：
	$("#popup_input2").selectCity("#popup_input_box2");
	$("#popup_input3").selectCity("#popup_input_box3");
	$("#popup_input4").selectCity("#popup_input_box4");
	$("#popup_input5").selectCity("#popup_input_box5");
	$("#popup_input6").selectCity("#popup_input_box6");
	$("#popup_input7").selectCity("#popup_input_box7");
	$("#popup_input8").selectCity("#popup_input_box8");
});
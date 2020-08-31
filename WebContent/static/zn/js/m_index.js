//全屏图片轮播
$(document).ready(function(){

	$(".banner").hover(function(){
		$(this).find(".banner_pre,.banner_next").stop(true, true).fadeTo("show", 1)
	},function(){
		$(this).find(".banner_pre,.banner_next").fadeOut()
	});
	
	$(".banner").slide({
		titCell: ".banner_nav a ",
		mainCell: ".banner_con",
		delayTime: 500,
		interTime: 3500,
		prevCell:".banner_pre",
		nextCell:".banner_next",
		effect: "fold",
		autoPlay: true,
		trigger: "click",
		startFun:function(i){
			$(".banner_info").eq(i).find("h3").css("display","block").fadeTo(1000,1);
			$(".banner_info").eq(i).find(".text").css("display","block").fadeTo(1000,1);
		}
	});

});
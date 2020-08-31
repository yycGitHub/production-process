//左侧菜单显示与隐藏
$(function(){
$(".menu dt").click(function(){
		$(this).next(".menu dd").slideToggle(500).siblings(".menu dd").slideUp(500);// 修改数字控制速度， slideUp(500)控制卷起速度
	});
});

//滑动门效果,对应样式slide_door
function slide_door_nav(m,n){
var tli=document.getElementById("slide_door_nav"+m).getElementsByTagName("li");
var mli=document.getElementById("slide_door_con"+m).getElementsByTagName("ul");
for(i=0;i<tli.length;i++){
   tli[i].className=i==n?"cur":"";
   mli[i].style.display=i==n?"block":"none";
}
}

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
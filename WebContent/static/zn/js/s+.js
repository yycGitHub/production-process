$(function(){
	$('.fancybox').fancybox();
});

//一级菜单更多处理
$(function(){
	$("#menu_more").hover(function(){
		$("#menu_more dd").show();
		$("#menu_more dt a").addClass("more_btn");
	},function(){
		$("#menu_more dd").hide();
		$("#menu_more dt a").removeClass("more_btn");
	});
});

//左侧菜单显示与隐藏
$(function(){
	$(".menu dt").click(function(){
		$(this).next(".menu dd").slideToggle(500).siblings(".menu dd").slideUp(500);// 修改数字控制速度， slideUp(500)控制卷起速度
		//计算左侧菜单展开后的最大高度
		var o = document.getElementById("leftMenu");
		var h = o.offsetHeight; //高度
		var h1 = h - $(".menu dt").length*41 - 30;
		$(this).next(".menu dd").css('max-height', h1+'px');
	});
	
	//鼠标点击当前背景显示
	$(".menu dd a").click(function(){
		$(this).addClass("cur");
		$(this).siblings("dd a").removeClass("cur");		
	});
	
	//高级检索显示与隐藏效果
	$(".show_hide_btn").each(function(i){
	   $(this).click(function(){
			if($(this).text() =="收起"){
				$(".show_hide").hide();
				$(this).text("高级检索");
			}else if($(this).text() =="高级检索"){
				$(".show_hide").show();
				$(this).text("收起");
			}			
	   });
	});
	
});

//滑动门效果,对应样式slide_door--使用于我的预约、每周预约情况
function slide_door_nav(m,n){
  var tli=document.getElementById("slide_door_nav"+m).getElementsByTagName("li");
  var mli=document.getElementById("slide_door_con"+m).getElementsByTagName("ul");
  for(i=0;i<tli.length;i++){
    tli[i].className=i==n?"cur":"";
    mli[i].style.display=i==n?"block":"none";
  }
}

//滑动门效果,对应样式tab_door--适用于一页多表格
function tab_door_nav(m,n){
  var tli=document.getElementById("tab_door_nav"+m).getElementsByTagName("li");
  var mli=document.getElementById("tab_door_con"+m).getElementsByTagName("ul");
  for(i=0;i<tli.length;i++){
    tli[i].className=i==n?"cur":"";
    mli[i].style.display=i==n?"block":"none";
  }
}

//点击箭头内容左右滚动
$(function(){
	var con_nav_show = $('.con_nav_show');
	(function(){
		var ul       = $("ul", con_nav_show);  
        var li       = $("li", ul);  
        var liSize   = li.size();
        var liWidth  = li.width() + 0;
        var width    = liWidth * liSize;
		ul.css('width',width);
	})();
	$('.last,.next').click(function(){
		$("div[class]", $(this).parent()).scrollPlug({direction:this.rel,step:10});
		return false;
	});		   
});

(function($){
	$.fn.scrollPlug = function(o){
        o = $.extend({  
            speed:      parseInt($(this).attr('speed')) || 30, // 滚动速度  
            step:       parseInt($(this).attr('step')) || 1, // 滚动步长  
            direction:  $(this).attr('direction') || 'up', // 滚动方向  
            pause:      parseInt($(this).attr('pause')) || 1000, // 停顿时长
			heightVle:  '',
			widthVle:   ''
        }, o || {});
		var dIndex = jQuery.inArray(o.direction, ['right', 'down']); 
		if (dIndex > -1) {  
            o.direction = ['left', 'up'][dIndex];  
            o.step = -o.step;  
        };
		var mid; 
		var ki          = 0;
        var div         = $(this);
        var divWidth    = div.innerWidth();
        var divHeight   = div.innerHeight();  
        var ul          = $("ul", div);  
        var li          = $("li", ul);  
        var liSize      = li.size();  
        var liWidth     = o.widthVle  = li.width();
        var liHeight    = o.heightVle = li.height();
        var width       = liWidth * liSize;  
        var height      = liHeight * liSize;
		clearInterval(mid);	
		var scrollHandle = function() {// 滚动   
            if(o.direction == 'left'){  
                var l = div.scrollLeft(); 
				div.scrollLeft(l + o.step);
				ki += Math.abs(o.step);
                if(ki >= liWidth) _stop();  
            }else{
				var t = div.scrollTop();
				ki += Math.abs(o.step);
				div.scrollTop(t + o.step);
                if(ki >= liHeight) _stop();
            }; 
        };
		var _stop = function(){
			ki = 0;
			clearInterval(mid);	
		};
		mid = setInterval(scrollHandle, o.speed); 		
	};	  
})(jQuery);


//表格隔行变色,鼠标经过变色
$(function(){
	$(".setcolor").tableUI();
	//点击一行选中checkbox
	$(".keeprow tr").click(function() {
		$(this).addClass("clickcss");
		$(this).siblings(".keeprow tr").removeClass("clickcss");

		//var hasSelected = $(this).hasClass("keeprow");
		//$(this)[hasSelected ? "removeClass" : "addClass"]("keeprow").find(":checkbox").prop("checked", !hasSelected);
	})
	 $(".keeprow tr").hover(function () {
		 $(this).addClass('overcss');}, function(){ $(this).removeClass('overcss'); }).click(  
       	function(e){  
          	if(
					$(e.srcElement || e.target).attr("type") != "radio"){  
                  $(this).find(":radio").click(); 
              }  
       	});  
          $(".keeprow input[type='radio']").click(function () {  
          	$(this).parent().parent().addClass('clickcss')  
              .siblings().removeClass('clickcss')  
              .end();  
	 });  
});
(function($){
	$.fn.tableUI = function(options){
		var defaults = {
			evenRowClass:"evenrow",
			oddRowClass:"oddrow",
			activeRowClass:"activerow",
			clickRowClass:"clickrow"
		}
		var options = $.extend(defaults, options);
		this.each(function(){
			var thisTable = $(this);
			
			$(thisTable).find("tbody tr:even").addClass(options.evenRowClass);
			$(thisTable).find("tbody tr:odd").addClass(options.oddRowClass);
			$(thisTable).find("tbody tr").bind("mouseover",function(){
				$(this).removeClass(options.clickRowClass).addClass(options.activeRowClass);
			});
			$(thisTable).find("tbody tr").bind("mouseout",function(){
				$(this).removeClass(options.clickRowClass).removeClass(options.activeRowClass);
			});
			$(thisTable).find("tbody tr").bind("click",function(){
				$(this).addClass(options.clickRowClass);
			});
		});
	};
})(jQuery);
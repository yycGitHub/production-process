$(function(){
	//风格选择滚动
	var blw=$(".roll_main li").width();
	//获取单个子元素所需宽度
	var liArr = $(".roll_main ul").children("li");
	//获取子元素数量
	var mysw = $(".rollbox").width();
	//获取子元素所在区域宽度
	var mus = parseInt(mysw/blw);
	//计算出需要显示的子元素的数量
	var length = liArr.length-mus;
	//计算子元素可移动次数（被隐藏的子元素数量）
	var i=0
	$(".arrow_right").click(function(){
	  i++
	  //点击i加1
	  if(i<length){
		  $(this).prev(".roll_main").css("left",-(blw*i));
		  //子元素集合向左移动，距离为子元素的宽度乘以i。
	  }else{
		  i=length;
		  $(this).prev(".roll_main").css("left",-(blw*length));
		  //超出可移动范围后点击不再移动。最后几个隐藏的元素显示时i数值固定位已经移走的子元素数量。
	  }
	});
	$(".arrow_left").click(function(){
	  i--
	  //点击i减1
	  if(i>=0){
		 $(this).next(".roll_main").css("left",-(blw*i));
		 //子元素集合向右移动，距离为子元素的宽度乘以i。
	  }else{
		 i=0;
		 $(this).next(".roll_main").css("left",0);
		 //超出可移动范围后点击不再移动。最前几个子元素被显示时i为0。
	  }
	});
	
});

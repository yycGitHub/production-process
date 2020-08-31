$(document).ready(function(){

	var tb = $("#row");
	
	//添加行
	$("#row_add").click(function(){
		var hideTr = $("#row_temp",tb).children().first();
		var newTr = hideTr.clone().show();
		$("#row_show",tb).append(newTr);
	});
	
	//保存行
	$(".row_keep",tb).die('click').live('click',function(){
			var tr = $(this).parent().parent();
		$("input[type='text']",tr).each(function(i,el){
			el = $(el);
			el.parent().text(el.val());
			el.remove();
		});
		$(".row_keep",tr).hide();
		$(".row_changes",tr).show();
	});
	
	//修改行
	$(".row_changes",tb).die('click').live('click',function(){
		var tr = $(this).parent().parent();
		$("td:not('.operate')",tr).each(function(i,el){
			el = $(el);
			var html = "<input value='"+el.text()+"' type='text'>";
			el.html(html);
		});
		$(".row_changes",tr).hide();
		$(".row_keep",tr).show();
	});
	
	//删除行
	$(".row_del",tb).die('click').live('click',function(){
		$(this).parent().parent().remove();
	});
	
	//根据关键字查询行
	$("#row_search").click(function(){
		 var keyword = $("#row_keyword").val();
		 var tby = $("#row_show",tb);
		if(!!!keyword){
			tby.children().show();
		}else{
			tby.children().hide();
			$("td:contains('"+keyword+"')",tby).each(function(i,el){
				$(el).parent().show();
			});
		}
		
		
	});
	
});
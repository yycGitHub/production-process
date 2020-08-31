var addTabs = function (options) {
  $("#menu li").removeClass("active");
  $("#menu_li_"+options.id).addClass("active");
  $("#menu_li_"+options.id).parent().parent();
  $("#menu_li_"+options.id).parents(" .topMenu").addClass("active");
  id = "tab_" + options.id;
  $(".nav-tabs .active").removeClass("active");
  $(".tab-content .active").removeClass("active");
  //如果TAB不存在，创建一个新的TAB
 if (!$("#" + id)[0]) {
  //固定TAB中IFRAME高度
  mainHeight = $(document.body).height() - 125;
  //创建新TAB的title
  title = '<li role="presentation" id="tab_' + id + '"><a href="#' + id + '" aria-controls="' + id + '" role="tab" data-toggle="tab">' + options.title;
  //是否允许关闭
  if (options.close) {
   title += ' <i class="glyphicon glyphicon-remove-sign" tabclose="' + id + '"></i>';
  }
  title += '</a></li>';
  //是否指定TAB内容
  if (options.content) {
   content = '<div role="tabpanel" class="tab-pane" id="' + id + '">' + options.content + '</div>';
  } else {//没有内容，使用IFRAME打开链接
   content = '<div role="tabpanel" class="tab-pane" id="' + id + '"><iframe src="' + options.url + '" width="100%" height="' + mainHeight +
     '" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="yes" allowtransparency="yes"></iframe></div>';
  }
  //加入TABS
  $(".nav-tabs").append(title);
  $(".tab-content").append(content);
 }
 //激活TAB
 $("#tab_" + id).addClass('active');
 $("#" + id).addClass("active");
 var lasttab_left = $("#tab_" + id).position().left;
 var lasttab_offleft = $("#tab_" + id).offset().left;
 var winWidth = $("#navbar").width();
 var thisTab = $("#tab_" + id).width();
 var menuwidth = $("#menu").width();
 var btnwidth = $(".leftbackward").width();
 var tabwidth = $(".nav-tabs").width();
 var toRight = lasttab_offleft - winWidth;
 var toLeftOffset = lasttab_offleft - menuwidth - btnwidth;
 var tabsNavLeft = $(".nav-tabs").position().left;
 var tabsNavOffsetLeft = $(".nav-tabs").offset().left;

var animateLeft = 0;
 if(toLeftOffset < 0){
   animateLeft= toLeftOffset * -1 + tabsNavLeft ;
   $(".nav-tabs").animate({left: (animateLeft) + 'px'});
   return;
 }
 if( (toRight < (thisTab+btnwidth) && toRight > (-btnwidth-thisTab))){
   animateLeft = tabsNavLeft - (thisTab + toRight) - btnwidth;
   $(".nav-tabs").animate({left: (animateLeft) + 'px'});
   return;
 }
};
var closeTab = function (id) {
 //如果关闭的是当前激活的TAB，激活他的前一个TAB
 if ($(".nav-tabs li.active").attr('id') == "tab_" + id) {
  $("#tab_" + id).prev().addClass('active');
  $("#" + id).prev().addClass('active');
 }
 //关闭TAB
 $("#tab_" + id).remove();
 $("#" + id).remove();
};
$(function () {
 mainHeight = $(document.body).height() - 45;
 $('.main-left,.main-right').height(mainHeight);
 $("[addtabs]").click(function () {
  addTabs({ id: $(this).attr("id"), title: $(this).attr('title'), close: true });
 });

 $(".nav-tabs").on("click", "[tabclose]", function (e) {
  id = $(this).attr("tabclose");
  closeTab(id);
 });
});

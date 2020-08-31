// 布局宽度设置
$(window).resize(function(){
  $(".menu_frame").height($(document.body).height() - 108);
  $(".mian_frame").width($(document.body).width() - 220);
  $(".mian_frame").height($(document.body).height() - 108);
  $(".con").width($(".miandody").width() - 10);
  $(".con").height($(".miandody").height() - 51);
  $(".tab").width($(".con").width() - 27);
  $(".tab_ie").width($(document.body).width() - 27);
  $(".timeline_con").width($(".con").width() - 250);
  $(".main_left_frame").height($(document.body).height() - 40);
  $(".main_right_frame").width($(document.body).width() - 350);
  $(".main_right_frame").height($(document.body).height() - 0);
});   
$(window).resize();


//实现IE浏览器兼容placeholder效果
$(function(){
if(!placeholderSupport()){   // 判断浏览器是否支持 placeholder
    $('[placeholder]').focus(function() {
        var input = $(this);
        if (input.val() == input.attr('placeholder')) {
            input.val('');
            input.removeClass('placeholder');
        }
    }).blur(function() {
        var input = $(this);
        if (input.val() == '' || input.val() == input.attr('placeholder')) {
            input.addClass('placeholder');
            input.val(input.attr('placeholder'));
        }
    }).blur();
};
})
function placeholderSupport() {
    return 'placeholder' in document.createElement('input');
}



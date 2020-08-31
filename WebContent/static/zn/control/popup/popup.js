// 默认基本弹出窗口
$('.popup_btn').on('click', function(){
  parent.layer.open({
  type: 2,
  title: '显示标题',
  maxmin: true,
  shadeClose: true, //点击遮罩关闭层
  area : ['800px' , '520px'],
  content: 'booking_ok.html'
  });
});
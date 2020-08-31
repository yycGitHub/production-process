// JavaScript Document
!function(){
	laydate.skin('default');//切换皮肤，请查看skins下面皮肤库
	laydate({elem: '#datetime'});//绑定元素
}();

//日期范围限定在昨天到明天
laydate({
    elem: '#limit',
    min: laydate.now(-1), //-1代表昨天，-2代表前天，以此类推
    max: laydate.now(+1) //+1代表明天，+2代表后天，以此类推
});
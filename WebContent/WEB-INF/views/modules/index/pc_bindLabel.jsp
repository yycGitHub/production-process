<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>标签绑定</title>
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/zn/css/base.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/zn/css/s+.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/pcindex/css/s.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/pcindex/css/base.css">
	<%-- <link rel="stylesheet" type="text/css" href="${ctxStatic}/pcindex/css/choice.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/pcindex/css/style.css"> --%>
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/layer/skin/layer.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/hui/hui.css" />

	<style type="text/css">
		html, body {
		    width: 100%;
		    height: 100%;
		    overflow:hidden;
		}
		.b_line{margin-bottom:5px;}
		.pic{width:30px;}
		.min{margin-right: 5px;}
		.max{margin-right: 4px;}
	</style>
</head>
<body>
	<div class="step-content" align="center" style="width:100%;height:100%;overflow-y:scroll;">
      <div id="data" class="step-list" style="width:98%;margin-top: 10px;">
	  	<div id="echartsDiv" style="height:150px;width: 100%;padding-top: 20px;"></div>
	  	<div  style="height:20px;width: 100%;"></div>
		<div style="height:20px;width: 100%;margin-bottom: 40px;"><p align="center" >说明：点击图表中未绑定区间进行选中，也可手动输入值绑定</p></div>
		<div class="list-txt" id="form1">
			<ul id="listUl">
				<li id="first_li" class="b_line">
					<input class="input_txt text-style" name="minName" id="min" type="tel"  placeholder="起始值">
					<input class="input_txt text-style" name="maxName" id="max" type="tel"  placeholder="结束值">
					<img  id="firstBtn" src="${ctxStatic}/images/add_value.png" alt="" />
				</li>
			</ul>
			<div  style="height:20px;width: 100%;"></div>
			<ul>
				<li>
					<div style="height:50px;width: 100%;">
						<p align="center"><input class="btn btn_primary" name="" style="width: 100px;" id="submit" type="button" value="绑定"></p>
					</div>
				</li>
			</ul>
		</div>
	  </div>
	  <div id="dataNull" style="height:20px;width: 100%;margin-bottom: 40px;margin-top:200px">
	  	<span align="center" >该产品暂无可绑定标签，请先点击</span>
	  	<a onclick="applyLabel();" style="color:#46bef2 ">申请标签</a>
	  	<span align="center" >后再来绑定！</span>
	  </div>
    </div>
	<script type="text/javascript" src="${ctxStatic}/jquery/jquery-1.9.1.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/hui/hui.js"></script>
	<script type="text/javascript" src="${ctxStatic}/zn/js/layui/layui.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jquery-validation/1.11.1/jquery.validate.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jquery-validation/1.11.1/jquery.validate.method.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/layer/layer.js"></script>
	<script type="text/javascript" src="${ctxStatic}/echarts/echarts.min.js"></script>
	
	<script type="text/javascript">
	var globalMin;
	var globalMax;
	var jhdData = [];
	var chart = echarts.init(document.getElementById('echartsDiv'));
	var productId = '${productId}';
	var batchId = '${batchId}';
	var token = '${token}';
	
	$(document).ready(function() {
		var data = JSON.parse('${dataList}')
		document.getElementById("data").style.display="none";//隐藏
		document.getElementById("dataNull").style.display="";//显示
		
		if(data.bodyData && data.bodyData.length>0) {
			document.getElementById("dataNull").style.display="none";//隐藏
			document.getElementById("data").style.display="";//显示
			globalMin = parseInt(data.bodyData[0].min);
			globalMax = parseInt(data.bodyData[data.bodyData.length-1].max);
			setChartData(data.bodyData);
		}
		
	    var data2 ='${qjjhm}'
		if(data2 && data2.length>0) {
			var da = data2.split(",");
			initData(da);
		}
	});
	
	function initData(jhdData){
		var ul=document.getElementById("listUl");
		var li=document.getElementById("ybd");
		if(li && li!=null){
			li.remove();
		}
		for(var i=0;i<jhdData.length;i++){
			var a = jhdData[i];
			if(a.indexOf("-")>-1){
				var start = a.split("-")[0];
				var end = a.split("-")[1];
				var li = document.createElement('li');
				li.id="ybd";
				var input_min = document.createElement('input');
				var input_max = document.createElement('input');
				var img = document.createElement('img');
				li.className = 'b_line';
				input_min.className = 'input_txt text-style min';
				input_min.name='minName';
				input_min.id='min'+i+1;
				input_min.type='tel';
				input_min.placeholder='起始值';
				input_min.value=start;
				input_max.className = 'input_txt text-style max';
				input_max.name='maxName';
				input_max.id='max'+i+1;
				input_max.type='tel';
				input_max.placeholder='结束值';
				input_max.value=end;
				img.className = 'pic';
				img.setAttribute("onclick","deleteMe(this)");
				img.setAttribute("src","${ctxStatic}/images/delete.png");
				img.setAttribute("alt","");
				li.appendChild(input_min);
				li.appendChild(input_max);
				li.appendChild(img);
				ul.appendChild(li);
			}else{
				var li = document.createElement('li');
				li.id="ybd";
				var input_min = document.createElement('input');
				var input_max = document.createElement('input');
				var img = document.createElement('img');
				li.className = 'b_line';
				input_min.className = 'input_txt text-style min';
				input_min.name='minName';
				input_min.id='min'+i+1;
				input_min.type='tel';
				input_min.placeholder='起始值';
				input_min.value=a;
				input_max.className = 'input_txt text-style max';
				input_max.name='maxName';
				input_max.id='max'+1+1;
				input_max.type='tel';
				input_max.placeholder='结束值';
				input_max.value=a;
				img.className = 'pic';
				img.setAttribute("onclick","deleteMe(this)");
				img.setAttribute("src","${ctxStatic}/images/delete.png");
				img.setAttribute("alt","");
				li.appendChild(input_min);
				li.appendChild(input_max);
				li.appendChild(img);
				ul.appendChild(li);
			}
		}
		
	}
	
	
	function setChartData(cdata){
		 var lableData = [];
		 //2种颜色
		 var colors = ['#46bef2','#899acc','#86cb71'];
	     // 2种状态
	     var state = ['未绑定','已绑定(可编辑)','已绑定(不可编辑)'];
	     
	     var min_x;
		 for (var i = 0; i < cdata.length; i++) {
			 var color,name;
			 if(i == 0){
			 	min_x = cdata[i].min;
			 }
			 switch(cdata[i].actFlag)
			 {
			 case 0:
			   	name='未绑定',color = colors[0];
			   	break;
			 case 1:
			 	name='已绑定(可编辑)',color = colors[1];
			    break;
			 case 2:
			 	name='已绑定(不可编辑)',color = colors[2];
			    break;
			 default:
				 name='已绑定',color = colors[1];
			 }
			 lableData.push(
			 		{
			 	    	itemStyle: { normal: { color: color } },
		        		name: name,
		            	value: [ 0, cdata[i].min, cdata[i].max ]
  					}
			 	);
		    }
		
	        // echart配置
	        var opt = {
	            color: colors,
	            tooltip: {
	        
	                formatter: function (params) {
	                    return params.name + ':' + params.value[1] + '~' + params.value[2];
	                }
	            },
	            legend: {
	                data: state,
	                bottom: '1%',
	                selectedMode: false, // 图例设为不可点击 
	                textStyle: {
	                    color: '#000'
	                },
	                selected: {aaa: true, ccc: true}, // 图例选中状态表
	                tooltip: { // 图例的 tooltip 配置
	                    show: true
	                }
	            },
	            dataZoom : [{
			        show : true,
			        filterMode: 'weakFilter',
			        y: 100,
			        height: 20,
			        backgroundColor: 'rgba(187,255,255,0.5)',//最下面底色
			        dataBackgroundColor: '#eee',
			        fillerColor: 'rgba(138,131,120,0.6)',//拖动条颜色
			        handleColor: 'rgba(128,43,16,0.8)',
			        handleIcon: 'M10.7,11.9H9.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4h1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7v-1.2h6.6z M13.3,22H6.7v-1.2h6.6z M13.3,19.6H6.7v-1.2h6.6z', // jshint ignore:line
			        handleSize: 20,
			        handleStyle: {
			            shadowBlur: 6,
			            shadowOffsetX: 1,
			            shadowOffsetY: 2,
			            shadowColor: '#aaa'
			        },
			    }],
	            grid: {
	                left: '1%',
	                right: '5%',
	                top: '20%',
	                bottom: '40%',
	                containLabel: true
	            },
	            xAxis: {
	                min: 1,
	                scale: true,
	                axisLabel: {
	                    formatter: function (val) {
	                        return Math.max(0, val);
	                    }
	                }
	            },
	            yAxis: {
	                data: ['标签'],
	                show:false
	            },
	            series: [
	                // 用空bar来显示四个图例
	                {name: state[0], type: 'bar', data: []},
	                {name: state[1], type: 'bar', data: []}, 
	                {name: state[2], type: 'bar', data: []},
	                {
	                    type: 'custom',
	                    label: {
	    					show: true, //开启显示 true
	    					position: 'insideRight',//在上方显示right insideRight
	    					textStyle: { //数值样式
					    		    color: '#FFFFFF',
					    			fontSize: 12,
					    			fontWeight: 900
   					}
   					
					},
                   renderItem: function (params, api) {
                       var categoryIndex = api.value(0);
                       var start = api.coord([api.value(1), categoryIndex]);
                       var end = api.coord([api.value(2), categoryIndex]);
                       if(end[0] == start[0]){
                       	 start[0] = start[0]-1;
                       	 end[0] = end[0] +1;
                       }
                       var height = 50;
                       return {
                           type: 'rect',
                           shape: echarts.graphic.clipRectByRect({
                               x: start[0],
                               y: start[1] - height / 2,
                               width: end[0] - start[0],
                               height: height
                           }, {
                               x: params.coordSys.x,
                               y: params.coordSys.y,
                               width: params.coordSys.width,
                               height: params.coordSys.height
                           }),
                           style: api.style()
                       };
                   },
                   encode: {
                       x: [1, 2],
                       y: 0
                   },
              		data: lableData
	            }
	        ]
	    };
	    chart.setOption(opt);
	}
	
	chart.on('click',function(params){
		console.log(params);
		var  name = params.data.name;
		var valueArr = params.data.value;
		if(name =='已绑定(可编辑)' || name =='已绑定(不可编辑)'){
			hui.toast("所选区间已绑定！");
			hui('#min').val('');
			hui('#max').val('');
			return false;
		}else{
			var min = valueArr[1];
			var max = valueArr[2];
			hui('#min').val(min);
			hui('#max').val(max);
		}
	});
	
	//动态添加输入框
	hui('#firstBtn').click(function() {
		var linume = $("#listUl li").length;
		var ul=document.getElementById("listUl");
		//ul.append(popContent);
		var li = document.createElement('li');
		var input_min = document.createElement('input');
		var input_max = document.createElement('input');
		var img = document.createElement('img');
		li.className = 'b_line';
		input_min.className = 'input_txt text-style min';
		input_min.name='minName';
		input_min.id='min'+linume;
		input_min.type='tel';
		input_min.placeholder='起始值';
		input_max.className = 'input_txt text-style max';
		input_max.name='maxName';
		input_max.id='max'+linume;
		input_max.type='tel';
		input_max.placeholder='结束值';
		img.className = 'pic';
		img.setAttribute("onclick","deleteMe(this)");
		img.setAttribute("src","${ctxStatic}/images/delete.png");
		img.setAttribute("alt","");
		li.appendChild(input_min);
		li.appendChild(input_max);
		li.appendChild(img);
		ul.appendChild(li);
	})
	
	hui('#submit').click(function() {
		var paramString="";
		var minArr =  document.getElementsByName('minName');
		var maxArr = document.getElementsByName("maxName");
		
		for(var i = 0 ;i< minArr.length; i++){
			var min =  minArr[i].value;
			var max = maxArr[i].value;
			if(min != '' && max != ''){
				var re = /^[1-9]\d*$/; //判断字符串是否为数字 //判断正整数 //^[1-9]\d*$/ 
				if (!re.test(min)) {
			 		hui.toast("起始值应为正整数");
　　　　           		return false;
　　   			}
				if (!re.test(max)) {
			 		hui.toast("结束值应为正整数");
　　　　           		return false;
　　   			}
				var minInt = parseInt(min);
				var maxInt = parseInt(max);
				
				if(typeof(minInt)=="number" && typeof(maxInt)=="number"){
					if(minInt > maxInt){
						hui.toast("起始值不能大于结束值");
						return false;
					}
					paramString+= minInt + '_' + maxInt + ',';
				}
				
			}
		}
		
		if(paramString.length==0){
			hui.toast("请输入起始值或者结束值");
			return false;
		}else{
			paramString = paramString.substr(0,paramString.length-1);
		}
		

	    layer.confirm('您确认要绑定吗？', {
		   btn: ['确定','取消'] //按钮
		}, function(){
			actCode(paramString);
		}, function(){
		  
		});
	});
	
	function actCode(paramString){
		var param = {
			token:token,
			batchId: batchId,
			productId:productId,
			paramString: paramString
		};
		var url ='${pageContext.request.contextPath}/api/pcLabel/activationTraceCode';
		$.get(url, param, function(res){
			/* document.location.reload(); */
			layer.alert('绑定成功!', {icon: 1,title: "提示",yes:function(){
				parent.$("#handle").val('4');
				var index = parent.layer.getFrameIndex(window.name); 
				parent.layer.close(index);
			}});
		});
	}	
	//删除输入框
	function deleteMe (self) {
		var ul=document.getElementById("listUl");
		var li = self.parentElement;
		ul.removeChild(li);
	};
	
	//申请标签
    function applyLabel(){
    	layer.open({
	        type: 2,
	        title: '申请标签',
	        shadeClose: false,
	        area: ['110%', '110%'],
	        scrollbar: false, // 父页面 滚动条 禁止
	        content: '${pageContext.request.contextPath}/api/pcLabel/applyPage?token=${token}&productId='+productId,
	        end:function (){
	        	localStorage.clear();
	        	location.reload();
	        	var handle = $("#handle").val();
		        if ( handle && handle == '3' ) {
		        	location.reload();
		        }
	        }
	    });
    }
	</script>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>标签申请</title>
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/zn/css/base.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/zn/css/s+.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/pcindex/css/s.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/pcindex/css/base.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/layer/skin/layer.css">
	<script type="text/javascript" src="${ctxStatic}/jquery/jquery-1.9.1.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/pcindex/js/sureserve.js"></script>
	<script type="text/javascript" src="${ctxStatic}/layer/layer.js"></script>
	<script type="text/javascript" src="${ctxStatic}/echarts/echarts.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/echarts/china.js"></script>
	<script type="text/javascript" src="${ctxStatic}/echarts/xinjiang.js"  charset="utf-8"></script>
</head>
<body>
	<div class="divclass" style="height: 100%">
	    <div align="right">
		    <select id = "select" onchange="changeTest()">
		    	<option value = "">所有产品</option>
		    	<c:forEach items="${productList}" var="li">
		    		<option value = "${li.id}">${li.productName}</option>
		    	</c:forEach>
			</select>
		</div> 
		<div id="echartsDiv" style="height:95%;width: 95%;"></div>
	</div>
<script type="text/javascript">
var token = '${token}';
var chart = echarts.init(document.getElementById('echartsDiv'));
var value = '${map.data}';
if(value){
	var maxNum = '${map.maxNum}';
	var array = new Array();
	var arr = value.split('},');
	for(var i=0;i<arr.length;i++){
		if(arr[i].indexOf('}')==-1){
			arr[i] = arr[i]+"}";
		}
		var json = JSON.parse(arr[i]);
		array.push(json);
	}
	var option = {
		    title : {
		    	text: '全国扫码区域图',
		        subtext: '实时数据',
		        left: 'center'
		    },
		    tooltip : {
		        trigger: 'item'
		    },
		    legend: {
		        orient: 'vertical',
		        left: 'left',
		        data:['']
		    },
		    visualMap: {
		        min: 1,
		        max: maxNum,
		        left: 'left',
		        top: 'bottom',
		        text:['高','低'],           // 文本，默认为数值文本
		        calculable : false,
			    inRange: {
			             color: ['lightskyblue','gray', '#0a3049']
			        }
		    },
		    toolbox: {
		        show: true,
		        orient : 'vertical',
		        left: 'right',
		        top: 'center',
		        feature : {
		            mark : {show: true},
		            dataView : {show: true, readOnly: false},
		            restore : {show: true},
		            saveAsImage : {show: true}
		        }
		    },
		    series : [
		        {
		            name: '',
		            type: 'map',
		            mapType: 'china',
		            roam: false,
		            label: {
		                normal: {
		                    show: false
		                },
		                emphasis: {
		                    show: true
		                }
		            },
		            data:array
		        }
		    ]
		};
		chart.setOption(option);
}



//下拉框选择产品
function changeTest(){
	var productId = document.getElementById("select").value;
	var param = {
		token:token,
		productId:productId
	};
		var url ='${pageContext.request.contextPath}/api/pcIndex/getchina';
		$.get(url,param,function(res){
			if(res.code == 0) {
				var value = res.bodyData.data;
				if(value){
					var maxNum = res.bodyData.maxNum;
					var array = new Array();
					var arr = value.split('},');
					for(var i=0;i<arr.length;i++){
						if(arr[i].indexOf('}')==-1){
							arr[i] = arr[i]+"}";
						}
						var json = JSON.parse(arr[i]);
						array.push(json);
					}
					option = {
					    title : {
					        text: '全国扫码区域图',
					        subtext: '实时数据',
					        left: 'center'
					    },
					    tooltip : {
					        trigger: 'item'
					    },
					    legend: {
					        orient: 'vertical',
					        left: 'left',
					        data:['']
					    },
					    visualMap: {
					        min: 1,
					        max: maxNum,
					        left: 'left',
					        top: 'bottom',
					        text:['高','低'],  
					        calculable : false,
					        inRange: {
			                	color: ['lightskyblue','gray', '#0a3049']
			            	}
					    },
					    toolbox: {
					        show: true,
					        orient : 'vertical',
					        left: 'right',
					        top: 'center',
					        feature : {
					            mark : {show: true},
					            dataView : {show: true, readOnly: false},
					            restore : {show: true},
					            saveAsImage : {show: true}
					        }
					    },
					    series : [
					        {
					            name: '',
					            type: 'map',
					            mapType: 'china',
					            roam: false,
					            label: {
					                normal: {
					                    show: false
					                },
					                emphasis: {
					                    show: true
					                }
					            },
					            data:array
					        }
					    ]
					};
					chart.setOption(option);
				}
			}
		});
}
</script>
</body>
</html>
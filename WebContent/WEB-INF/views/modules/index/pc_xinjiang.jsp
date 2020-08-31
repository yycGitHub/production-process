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
		<div id="main" style="height:95%;width: 95%;"></div>
	</div>
<script type="text/javascript">
var token = '${token}';
//container 为div的id 
var dom = document.getElementById("main");
//得到echarts的实例对象
var myChart = echarts.init(dom);
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
	initEcharts();
	function initEcharts() {
		  //关键是配置项
		var option = {
				title : {
				    	text: '新疆扫码区域图',
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
		    	series: [{
		      			name: '',
		      			//series[i]-map:系列列表。每个系列通过 type 决定自己的图表类型,此处是地图类型
		      			type: 'map',
		      			mapType: '新疆',
		      			//地图区域的多边形 图形样式，有 normal 和 emphasis 两个状态
		      			itemStyle: {
		        			//normal 是图形在默认状态下的样式；
		        			normal: {
		          				show: true,
		          				areaColor: "#CECECE",
		          				borderColor: "#FCFCFC",
		          				borderWidth: "1"
		        			},
		        		//emphasis 是图形在高亮状态下的样式，比如在鼠标悬浮或者图例联动高亮时。
		        			emphasis: {
		          				show: true,
		          				areaColor: "#C8A5DF",
		        			}
		      			},
		      			//图形上的文本标签，可用于说明图形的一些数据信息
		      			label: {
		        			normal: {
		          				show: true
		        			},
		        			emphasis: {
		          				show: true
		        			}
		      			},
		      			data:array
		    	}]
		  };
		  //使用刚指定的配置项和数据显示图表。
		  myChart.setOption(option);
		}
}


//下拉框选择产品
function changeTest(){
	var productId = document.getElementById("select").value;
	var param = {
		token:token,
		productId:productId
	};
	var url ='${pageContext.request.contextPath}/api/pcIndex/getxinjiang';
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
					option2 = {
					    title : {
					        text: '新疆扫码区域图',
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
					            mapType: '新疆',
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
					myChart.setOption(option2);
				}
			}
		});
}
//定义全国省份的数组
/* var provinces = ['hami', 'shuozhou', 'wulumuqi', 'changji', 'aletai', 'kelamayi', 'tacheng', 'shihezi', 'kuitun', 'bole', 'yinning', 'akesu', 'hetian', 'atushi', 'keshi', 'kuerle'];
var provincesText = ['哈密市', '吐鲁番市', '乌鲁木齐市', '昌吉市', '阿勒泰市', '克拉玛依市', '塔城市', '石河子市', '奎屯市', '博乐市', '尹宁市', '阿克苏市', '和田市', '阿图什市', '喀什市', '库尔勒市'	];
myChart.on('click', function(param) {
  //console.log(param);
  //遍历取到provincesText 中的下标  去拿到对应的省js
  for (var i = 0; i < provincesText.length; i++) {
    if (param.name == provincesText[i]) {
      //显示对应省份的方法
      showProvince(provincesText[i],provinces[i]);
      break;
    }
  }
});
//展示对应的省
function showProvince(pText,pName) {
  loadBdScript('$' + pName + 'JS', 'js/province/' + pName + '.js', function() {
    //初始化echarts
    initEcharts(pText);
  });
} */
//加载对应的JS
/* function loadBdScript(scriptId, url, callback) {
  var script = document.createElement("script")
  script.type = "text/javascript";
  if (script.readyState) { //IE  
    script.onreadystatechange = function() {
      if (script.readyState == "loaded" || script.readyState == "complete") {
        script.onreadystatechange = null;
        callback();
      }
    };
  } else { //Others  
    script.onload = function() {
      callback();
    };
  }
  script.src = url;
  script.id = scriptId;
  document.getElementsByTagName("head")[0].appendChild(script);
}; */
</script>
</body>
</html>

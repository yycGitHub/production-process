<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="renderer" content="webkit">
	<title>首页</title>
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/pcindex/css/s.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/pcindex/css/base.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/pcindex/css/layout.css">
	<script type="text/javascript" src="${ctxStatic}/jquery/jquery-1.9.1.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/pcindex/js/sureserve.js"></script>
	<script type="text/javascript" src="${ctxStatic}/layer/layer.js"></script>
	<script type="text/javascript" src="${ctxStatic}/echarts/echarts.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/echarts/china.js"></script>
	<style type="text/css">
	/*产品名称*/	
	.gray{font-size: 3px}
	
	/*评论图片*/	
	.commentPic{
		width: 30%;
		padding-left: 3%;		
	}
	
	/*回复内容： 华文黑体  1874CD */
	.reply{color: #AF874D;font-family:STHeiti;}
	
	/*全部列表加载更多*/	
	.js-load-more{color: #104E8B;}
	
	/*好评列表加载更多*/	
	.js-load-more2{color: #104E8B;}
	
	/*差评列表加载更多*/	
	.js-load-more3{color: #104E8B;}
	
	/*回复按钮*/	
	.replybtn{display:inline-block;padding:7px 10px;line-height:1;background:#f39b01;color:#fff;border-radius:2px;}
	</style>
	<script type="text/javascript">
		var token = '${token}';
		var allcount = '${allcount}';
		var highcount = '${highcount}';
		var lowcount = '${lowcount}';
		var counter = 0; /*全部列表计数器*/
		var pageNo = 1;
		var pageSize =5;
		var counter2 = 0; /*好评列表计数器*/
		var pageNo2 = 1;
		var pageSize2 =5;
		var counter3 = 0; /*差评列表计数器*/
		var pageNo3 = 1;
		var pageSize3 =5;
		$(document).ready(function() {
			commentlist(pageNo,pageSize);
			highScoreList(pageNo2,pageSize2);
			lowScoreList(pageNo3,pageSize3);
			 /*监听全部列表加载更多*/
		    $(document).on('click', '.js-load-more', function () {
		    	pageNo++;
		    	commentlist(pageNo,pageSize);
		    });
		    /*监听好评列表加载更多*/
		    $(document).on('click', '.js-load-more2', function () {
		    	pageNo2++;
		    	highScoreList(pageNo2,pageSize2);
		    });
		    /*监听差评列表加载更多*/
		    $(document).on('click', '.js-load-more3', function () {
		    	pageNo3++;
		    	lowScoreList(pageNo3,pageSize3);
		    });
		});
		 /*全部列表*/
		function commentlist(pageNo,pageSize){
			var param = {
					token:token,
					officeId:'${officeId}',
					pageNo:pageNo,
					pageSize:pageSize
			    };
	 			var url ='${pageContext.request.contextPath}/api/pcIndex/commentlist';
	 			$.get(url,param,function(res){
	 				var result = '';
	 				var imgurl = "${ctxStatic}/images/wx.jpeg"
	 				if(res.code == 0) {
	 					counter++;
	 					$('.js-load-more').remove();
			 			for(var i = 0; i < res.bodyData.list.length; i++){
			 				var id = "li"+(counter*5+i);
			 				result+= '<li id=\"'+ id +'\"><dt>'
			 				+ '<p>';
			 				if(!res.bodyData.list[i].headImg){
			 					result+= '<img class="head" src="${ctxStatic}/images/logo_yuanxiang.png">';
			 				}else{
			 					result+= '<img class="head" src=\"'+ res.bodyData.list[i].headImg +'\">';
			 				}
			 				if(!res.bodyData.list[i].nickname){
			 					result+= '<b>' + "游客" + '</b>';
			 				}else{
			 					result+= '<b>' + res.bodyData.list[i].nickname + '</b>';
			 				}
			 				if(res.bodyData.list[i].score){
			 					result+= '<em class=\"'+ 'stars ' + 'stars'+ res.bodyData.list[i].score +'\"></em>';
			 				}
				            + '</p>';
			 				if(res.bodyData.list[i].content){
				            	result+= '<p>' + res.bodyData.list[i].content + '</p>';
				            }
				            if(res.bodyData.list[i].commentPicUrlList.length > 0){//判断图片列表是否有值
				            	result+= '<p>';
				            	for(var j = 0; j < res.bodyData.list[i].commentPicUrlList.length; j++){
				            		if(res.bodyData.list[i].commentPicUrlList[j].substring(22).length > 0){//去掉路径前缀 http://sh.sureserve.cn 判断是否为空
				            			result+= '<img class="commentPic" onclick="enlarge(this)" src=\"'+ res.bodyData.list[i].commentPicUrlList[j] +'\" onerror=this.src=\"'+ "${ctxStatic}/images/default.png" +'\">';
				            		}
				            	}
				            	result+= '</p>';
				            }
				            if(res.bodyData.list[i].replyContent){//判断评论是否有回复内容
			            		 result+= '<p class="reply">管理员回复：'+res.bodyData.list[i].replyContent+'</p>';
			            	}
				            result+= '<p>'
				            + '<span class="f_l gray">';
				            if(res.bodyData.list[i].productName){
				            	result+= '产品名称：'+ res.bodyData.list[i].productName;
				            }
				            if(res.bodyData.list[i].batchCode){
				            	result+= '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;批次号：' + res.bodyData.list[i].batchCode;
				            }
				            result+= '</span>'
				            + '</p>'
				            + '<p>';
				            if(res.bodyData.list[i].creatTime){
				            	result+= '<span class="f_l gray">' + res.bodyData.list[i].creatTime + '</span>';
				            }
				            if(res.bodyData.list[i].auditFlag == 1){//判断评论是否是未审核
				            	result+= '<a class="f_r btn" onclick="audit(\''+ res.bodyData.list[i].id +'\')">审核</a> ';
				            }
				            if(res.bodyData.list[i].auditFlag == 2){
				            	if(!res.bodyData.list[i].replyContent){//判断评论是否有回复内容
				            		 result+= '<a class="f_r replybtn" onclick="reply(\''+ res.bodyData.list[i].id +'\')">回复</a>';
				            	}
					        }
				            result+= '</p>'
				 			+ '</li>';
			 			}
			 			result+= '<dl class="js-load-more" style="padding-bottom:8px;cursor:pointer;"><p align="center">加载更多</p></dl>';
				 		$('#ulid').append(result);
				 		/*隐藏more*/
		                if (pageSize*pageNo >= allcount) {//判断是否显示加载更多             
		                    $('.js-load-more').remove();
		                }
	 				}
	 			});
		}
		/*放大图片*/
		function enlarge(){
			/* alert("放大图片"); */
		}
		/*评论审核弹窗*/
		function audit(id){
			layer.open({
    	        type: 2,
    	        title: '评论审核',
    	        shadeClose: false,
    	        area: ['40%', '30%'],
    	        scrollbar: false, // 父页面 滚动条 禁止
    	        content: '${pageContext.request.contextPath}/api/pcIndex/commentAudit?token=${token}&commentId='+id,
    	    });
		}
		/*评论回复弹窗*/
		function reply(id){
			layer.open({
    	        type: 2,
    	        title: '评论回复',
    	        shadeClose: false,
    	        area: ['70%', '45%'],
    	        scrollbar: false, // 父页面 滚动条 禁止
    	        content: '${pageContext.request.contextPath}/api/pcIndex/commentReply?token=${token}&commentId='+id,
    	    });
		}
		 /*好评列表*/
		function highScoreList(pageNo2,pageSize2){
			var param = {
					token:token,
					officeId:'${officeId}',
					pageNo:pageNo2,
					pageSize:pageSize2
			    };
			var url ='${pageContext.request.contextPath}/api/pcIndex/commentHighScorelist';
 			$.get(url,param,function(res){
 				var result2 = '';
 				var imgurl = "${ctxStatic}/images/wx.jpeg"
 					if(res.code == 0) {
 						counter2++;
	 					$('.js-load-more2').remove();
			 			for(var i = 0; i < res.bodyData.list.length; i++){
			 				var id = "li"+(counter2*5+i);
			 				result2+= '<li id=\"'+ id +'\"><dt>'
			 				+ '<p>';
			 				if(!res.bodyData.list[i].headImg){
			 					result2+= '<img class="head" src="${ctxStatic}/images/logo_yuanxiang.png">';
			 				}else{
			 					result2+= '<img class="head" src=\"'+ res.bodyData.list[i].headImg +'\">';
			 				}
			 				if(!res.bodyData.list[i].nickname){
			 					result2+= '<b>' + "游客" + '</b>';
			 				}else{
			 					result2+= '<b>' + res.bodyData.list[i].nickname + '</b>';
			 				}
			 				if(res.bodyData.list[i].score){
			 					result2+= '<em class=\"'+ 'stars ' + 'stars'+ res.bodyData.list[i].score +'\"></em>';
			 				}
				            + '</p>';
			 				if(res.bodyData.list[i].content){
				            	result2+= '<p>' + res.bodyData.list[i].content + '</p>';
				            }
				            if(res.bodyData.list[i].commentPicUrlList.length > 0){//判断图片列表是否有值
				            	result2+= '<p>';
				            	for(var j = 0; j < res.bodyData.list[i].commentPicUrlList.length; j++){
				            		if(res.bodyData.list[i].commentPicUrlList[j].substring(22).length > 0){//去掉路径前缀 http://sh.sureserve.cn 判断是否为空
				            			result2+= '<img class="commentPic" onclick="enlarge(this)" src=\"'+ res.bodyData.list[i].commentPicUrlList[j] +'\" onerror=this.src=\"'+ "${ctxStatic}/images/default.png" +'\">';
				            		}
				            	}
				            	result2+= '</p>';
				            }
				            if(res.bodyData.list[i].replyContent){//判断评论是否有回复内容
			            		 result2+= '<p class="reply">管理员回复：'+res.bodyData.list[i].replyContent+'</p>';
			            	}
				            result2+= '<p>'
				            + '<span class="f_l gray">';
				            if(res.bodyData.list[i].productName){
				            	result2+= '产品名称：'+ res.bodyData.list[i].productName;
				            }
				            if(res.bodyData.list[i].batchCode){
				            	result2+= '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;批次号：' + res.bodyData.list[i].batchCode;
				            }
				            result2+= '</span>'
				            + '</p>'
				            + '<p>';
				            if(res.bodyData.list[i].creatTime){
				            	result2+= '<span class="f_l gray">' + res.bodyData.list[i].creatTime + '</span>';
				            }
				            if(res.bodyData.list[i].auditFlag == 1){//判断评论是否是未审核
				            	result2+= '<a class="f_r btn" onclick="audit(\''+ res.bodyData.list[i].id +'\')">审核</a> ';
				            }
				            if(res.bodyData.list[i].auditFlag == 2){
				            	if(!res.bodyData.list[i].replyContent){//判断评论是否有回复内容
				            		 result2+= '<a class="f_r replybtn" onclick="reply(\''+ res.bodyData.list[i].id +'\')">回复</a>';
				            	}
					        }
				            result2+= '</p>'
				 			+ '</li>';
			 			}
			 			result2+= '<dl class="js-load-more2" style="padding-bottom:8px;cursor:pointer;"><p align="center">加载更多</p></dl>';
			 		$('#ulid2').append(result2);
			 		/*隐藏more*/
	                if (pageSize2*pageNo2 >= highcount) {                
	                    $('.js-load-more2').remove();
	                }
	 				}
 			});
		}
		 /*差评列表*/
		function lowScoreList(pageNo3,pageSize3){
			var param = {
					token:token,
					officeId:'${officeId}',
					pageNo:pageNo3,
					pageSize:pageSize3
			    };
			var url ='${pageContext.request.contextPath}/api/pcIndex/commentLowScorelist';
 			$.get(url,param,function(res){
 				var result3 = '';
 				var imgurl = "${ctxStatic}/images/wx.jpeg"
 					if(res.code == 0) {
 						counter3++;
	 					$('.js-load-more3').remove();
	 					if(res.bodyData.list){
	 						for(var i = 0; i < res.bodyData.list.length; i++){
				 				var id = "li"+(counter3*5+i);
				 				result3+= '<li id=\"'+ id +'\"><dt>'
				 				+ '<p>';
				 				if(!res.bodyData.list[i].headImg){
				 					result3+= '<img class="head" src="${ctxStatic}/images/logo_yuanxiang.png">';
				 				}else{
				 					result3+= '<img class="head" src=\"'+ res.bodyData.list[i].headImg +'\">';
				 				}
				 				if(!res.bodyData.list[i].nickname){
				 					result3+= '<b>' + "游客" + '</b>';
				 				}else{
				 					result3+= '<b>' + res.bodyData.list[i].nickname + '</b>';
				 				}
				 				if(res.bodyData.list[i].score){
				 					result3+= '<em class=\"'+ 'stars ' + 'stars'+ res.bodyData.list[i].score +'\"></em>';
				 				}
					            + '</p>';
				 				if(res.bodyData.list[i].content){
					            	result3+= '<p>' + res.bodyData.list[i].content + '</p>';
					            }
					            if(res.bodyData.list[i].commentPicUrlList.length > 0){//判断图片列表是否有值
					            	result3+= '<p>';
					            	for(var j = 0; j < res.bodyData.list[i].commentPicUrlList.length; j++){
					            		if(res.bodyData.list[i].commentPicUrlList[j].substring(22).length > 0){//去掉路径前缀 http://sh.sureserve.cn 判断是否为空
					            			result3+= '<img class="commentPic" onclick="enlarge(this)" src=\"'+ res.bodyData.list[i].commentPicUrlList[j] +'\" onerror=this.src=\"'+ "${ctxStatic}/images/default.png" +'\">';
					            		}
					            	}
					            	result3+= '</p>';
					            }
					            if(res.bodyData.list[i].replyContent){//判断评论是否有回复内容
				            		 result3+= '<p class="reply">管理员回复：'+res.bodyData.list[i].replyContent+'</p>';
				            	}
					            result3+= '<p>'
					            + '<span class="f_l gray">';
					            if(res.bodyData.list[i].productName){
					            	result3+= '产品名称：'+ res.bodyData.list[i].productName;
					            }
					            if(res.bodyData.list[i].batchCode){
					            	result3+= '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;批次号：' + res.bodyData.list[i].batchCode;
					            }
					            result3+= '</span>'
					            + '</p>'
					            + '<p>';
					            if(res.bodyData.list[i].creatTime){
					            	result3+= '<span class="f_l gray">' + res.bodyData.list[i].creatTime + '</span>';
					            }
					            if(res.bodyData.list[i].auditFlag == 1){//判断评论是否是未审核
					            	result3+= '<a class="f_r btn" onclick="audit(\''+ res.bodyData.list[i].id +'\')">审核</a> ';
					            }
					            if(res.bodyData.list[i].auditFlag == 2){
					            	if(!res.bodyData.list[i].replyContent){//判断评论是否有回复内容
					            		 result3+= '<a class="f_r replybtn" onclick="reply(\''+ res.bodyData.list[i].id +'\')">回复</a>';
					            	}
						        }
					            result3+= '</p>'
					 			+ '</li>';
				 			}
	 						result3+= '<dl class="js-load-more3" style="padding-bottom:8px;cursor:pointer;"><p align="center">加载更多</p></dl>';
					 		$('#ulid3').append(result3);
					 		/*隐藏more*/
			                if (pageSize3*pageNo3 >= lowcount) {                
			                    $('.js-load-more3').remove();
			                }
	 					}
	 				}
 			});
		}
		 
		/*显示全部评论*/
		function switch1(){
			document.getElementById("comment2").style.display="none";//隐藏
			document.getElementById("comment3").style.display="none";//隐藏
			document.getElementById("comment").style.display="";//显示
			document.getElementById("a_cur2").setAttribute("class", "");
			document.getElementById("a_cur3").setAttribute("class", "");
			document.getElementById("a_cur1").setAttribute("class", "cur");
		}
		/*显示好评评论*/
		function switch2(){
			document.getElementById("comment").style.display="none";//隐藏
			document.getElementById("comment3").style.display="none";//隐藏
			document.getElementById("comment2").style.display="";//显示
			document.getElementById("a_cur3").setAttribute("class", "");
			document.getElementById("a_cur1").setAttribute("class", "");
			document.getElementById("a_cur2").setAttribute("class", "cur");
		}
		/*显示差评评论*/
		function switch3(){
			document.getElementById("comment").style.display="none";//隐藏
			document.getElementById("comment2").style.display="none";//隐藏
			document.getElementById("comment3").style.display="";//显示
			document.getElementById("a_cur2").setAttribute("class", "");
			document.getElementById("a_cur1").setAttribute("class", "");
			document.getElementById("a_cur3").setAttribute("class", "cur");
		}
		
	</script>
</head>
<body>
	<div class="f_l maxbox mainbody">
	  <h2><b>数据展示</b></h2>
	  <div class="f_l maxbox header">
	    <dl>
	      <dt><img src="${ctxStatic}/pcindex/images/icon02.png"></dt>
	      <dd>
	        <em>总标签数</em>
	        <b>${num}</b>
	      </dd>
	    </dl>
	    <dl>
	      <dt><img src="${ctxStatic}/pcindex/images/icon03.png"></dt>
	      <dd>
	        <em>本周扫码数</em>
	        <b>${traceInfoReport.weekTraceCount}</b>
	      </dd>
	    </dl>
	    <dl>
	      <dt><img src="${ctxStatic}/pcindex/images/icon04.png"></dt>
	      <dd>
	        <em>本月扫码数</em>
	        <b>${traceInfoReport.monthTraceCount}</b>
	      </dd>
	    </dl>
	    <dl>
	      <dt><img src="${ctxStatic}/pcindex/images/icon05.png"></dt>
	      <dd>
	        <em>总扫码数</em>
	        <b>${traceInfoReport.traceCount}</b>
	      </dd>
	    </dl>
	  </div>
	  <div class="f_l analyse">
	  	<div align="right">
		    <select id = "select2" onchange="changeTest2()">
		    	<option value = "">所有产品</option>
		    	<c:forEach items="${productList}" var="li">
		    		<option value = "${li.id}">${li.productName}</option>
		    	</c:forEach>
			</select>
		</div>
	  	<div id="echartsDiv2" style="height:90%;width: 100%;"></div>
	  </div>
	  <div class="f_l maxbox footer">
	    <div class="f_l map">
	    	<iframe class="framing" name="show" width="80%" src="${pageContext.request.contextPath}/api/pcIndex/region?token=${token}&type=china&officeId=${officeId}" frameborder="0" allowtransparency="true"></iframe>
	    </div>
	    <div class="f_r slide">
	      <h3><b>用户留言</b></h3>
	      <div class="slidenav">
	        <ul>
	          <li><a id="a_cur1" class="cur" onclick="switch1()"><b>全部</b>${allcount}条评论</a></li>
	          <li><a id="a_cur2" class="" onclick="switch2()"><b>力荐</b>${highcount}条评论</a></li>
	          <li><a id="a_cur3" class="" onclick="switch3()"><b>差评</b>${lowcount}条评论</a></li>
	        </ul>
	      </div>
	      <div class="comment" id="comment">
	        <ul id ="ulid">
	        </ul>
	      </div>
	      
	      <div class="comment" id="comment2">
	        <ul id ="ulid2">
	        </ul>
	      </div>
	      
	      <div class="comment" id="comment3">
	        <ul id ="ulid3">
	        </ul>
	      </div>
	    </div>
	  </div>
	</div>
	<script>
		var chart2 = echarts.init(document.getElementById('echartsDiv2'));
		var valuemonth = '${map2.month}';
		var valuesaoma = '${map2.saoma}';
		var valuecomment = '${map2.comment}';
		var saomamax = '${map2.saomamax}';
		var commentmax = '${map2.commentmax}';
		var sm = 0;
		if(saomamax>4){
			sm = parseInt(saomamax/4)+1;
			saomamax = sm*4;
		}else{
			sm = 1;
			saomamax = 4;
		}
		var cm = 0;
		if(commentmax>4){
			cm = parseInt(commentmax/4)+1;
			commentmax = cm*4;
		}else{
			cm = 1;
			commentmax = 4;
		}
		var option2 = {
			title : {
	 	 		text: '溯源趋势分析',
	 	 		left: 'center'
	 	 	},
	 	 	grid: {
	 	 		x : 60,
	 	 		x2 : 50
	 	 	},
		    tooltip: {
		        trigger: 'axis',
		        axisPointer: {
		            type: 'cross',
		            crossStyle: {
		                color: '#999'
		            }
		        }
		    },
		    toolbox: {
		        feature: {
		            dataView: {show: true, readOnly: false},
		            magicType: {show: true, type: ['line', 'bar']},
		            restore: {show: true},
		            saveAsImage: {show: true}
		        }
		    },
		    legend: {
		 	 	y: 'bottom',
		        data:['扫码量','留言量']
		    },
		    
		    xAxis: [
		        {
		            type: 'category',
		            data: JSON.parse(valuemonth),
		            axisPointer: {
		                type: 'shadow'
		            }
		        }
		    ],
		    yAxis: [
		        {
		            type: 'value',
		            name: '扫码量(次)',
		            min: 0,
		            max: saomamax,
		            interval: sm,
		            axisLabel: {
		                formatter: '{value}'
		            }
		        },
		        {
		            type: 'value',
		            name: '留言量(条)',
		            min: 0,
		            max: commentmax,
		            interval: cm,
		            axisLabel: {
		                formatter: '{value}'
		            }
		        }
		    ],
		    series: [
		        {
		            name:'扫码量',
		            type:'bar',
		            data:JSON.parse(valuesaoma),
		            itemStyle:{
	                    normal:{
	                        color:'#27af59'
	                    }
	                }
		        },
		        {
		            name:'留言量',
		            type:'line',
		            yAxisIndex: 1,
		            data:JSON.parse(valuecomment),
		            itemStyle:{
	                    normal:{
	                        color:'#6eaaee'
	                    }
	                }
		        }
		    ]
		};
		chart2.setOption(option2);
		
		
		
		//下拉框选择产品
		function changeTest2(){
			var productId = document.getElementById("select2").value;
			var param = {
				token:token,
				officeId:'${officeId}',
				productId:productId
			};
	 		var url ='${pageContext.request.contextPath}/api/pcIndex/getBarData';
	 		$.get(url,param,function(res){
	 			if(res.code == 0) {
	 				var value = res.bodyData;
	 				if(value){
	 					var valuemonth = res.bodyData.month;
	 					var valuesaoma = res.bodyData.saoma;
	 					var valuecomment = res.bodyData.comment;
	 					var saomamax = res.bodyData.saomamax;
	 					var commentmax = res.bodyData.commentmax;
	 					console.log(res.bodyData.commentmax);
	 					var sm = 0;
	 					if(saomamax>4){
	 						sm = parseInt(saomamax/4)+1;
	 					}else{
	 						sm = 1;
	 						saomamax = 4;
	 					}
	 					var cm = 0;
	 					if(commentmax>4){
	 						cm = parseInt(commentmax/4)+1;
	 					}else{
	 						cm = 1;
	 						commentmax = 4;
	 					}
	 					var option2 = {
 							title : {
 					 	 		text: '溯源趋势分析',
 					 	 		left: 'center'
 					 	 	},
 					 	 	grid: {
 					 	 		x : 60,
 					 	 		x2 : 50
 					 	 	},
	 					    tooltip: {
	 					        trigger: 'axis',
	 					        axisPointer: {
	 					            type: 'cross',
	 					            crossStyle: {
	 					                color: '#999'
	 					            }
	 					        }
	 					    },
	 					    toolbox: {
	 					        feature: {
	 					            dataView: {show: true, readOnly: false},
	 					            magicType: {show: true, type: ['line', 'bar']},
	 					            restore: {show: true},
	 					            saveAsImage: {show: true}
	 					        }
	 					    },
	 					    legend: {
	 					 	 	y: 'bottom',
	 					        data:['扫码量','留言量']
	 					    },
	 					    
	 					    xAxis: [
	 					        {
	 					            type: 'category',
	 					            data: valuemonth,
	 					            axisPointer: {
	 					                type: 'shadow'
	 					            }
	 					        }
	 					    ],
	 					    yAxis: [
	 					        {
	 					            type: 'value',
	 					            name: '扫码量',
	 					            min: 0,
	 					            max: saomamax,
	 					            interval: sm,
	 					            axisLabel: {
	 					                formatter: '{value} 次'
	 					            }
	 					        },
	 					        {
	 					            type: 'value',
	 					            name: '留言量',
	 					            min: 0,
	 					            max: commentmax,
	 					            interval: cm,
	 					            axisLabel: {
	 					                formatter: '{value} 条'
	 					            }
	 					        }
	 					    ],
	 					    series: [
	 					        {
	 					            name:'扫码量',
	 					            type:'bar',
	 					            data:valuesaoma,
	 					            itemStyle:{
	 				                    normal:{
	 				                        color:'#27af59'
	 				                    }
	 				                }
	 					        },
	 					        
	 					        {
	 					            name:'留言量',
	 					            type:'line',
	 					            yAxisIndex: 1,
	 					            data:valuecomment,
	 					            itemStyle:{
	 				                    normal:{
	 				                        color:'#6eaaee'
	 				                    }
	 				                }
	 					        }
	 					    ]
	 					};
 	 					chart2.setOption(option2);
	 				}
	 			}
	 		});
		}
	</script>
</body>
</html>

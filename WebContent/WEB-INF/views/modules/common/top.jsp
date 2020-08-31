<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<div class="fl max_w top">
  <dl class="wrap">
    <dt class="fl">
      <span class="marright">客服热线：0731-83094555</span>
      <span class="marleft">服务时间：09:00-18:00</span>
    </dt>
<!--     <dd class="fr">
      <a class="fl" href="">登录</a>
      <i class="iconfont">&#xe647;</i>
      <a class="fl" href="">注册</a>
    </dd> -->
  </dl>
</div>
<div class="fl max_w header">
  <dl class="wrap">
    <dt class="fl"><a href=""><img src="${ctxStatic}/xcl/images/logo_bak.png" alt="" /></a></dt>
<!--     <dd class="fr search">
      <input class="fl search_txt" name="" type="text" placeholder="请输入搜索关键字">
      <input class="search_btn" name="" type="button">
      <i class="iconfont">&#xe86e;</i>
    </dd> -->
  </dl>
</div>
<div class="fl max_w menu">
  <ul class="wrap">
    <li class="submenu"><a href="http://www.htxincailiao.com/">首页</a></li>
    <li class="submenu">
      <a href="http://xcl.htxincailiao.com/f/product">展示中心</a>
      <ul>
        <li><a href="http://xcl.htxincailiao.com/f/product">新材院</a></li>
        <li><a href="http://xcl.htxincailiao.com/f/product">入驻单位</a></li>
      </ul>
    </li>
    <li class="submenu">
      <a href="http://www.htxincailiao.com/zxzx.view">资讯中心</a>
      <ul>
        <li><a href="http://www.htxincailiao.com/zxzx.view">成功案列</a></li>
        <li><a href="http://www.htxincailiao.com/hyzx_js.view">行业资讯-技术</a></li>
        <li><a href="http://www.htxincailiao.com/hyzx_sc.view">行业资讯-市场</a></li>
        <li><a href="http://www.htxincailiao.com/xcyzx.view">新材院咨询</a></li>
      </ul>
    </li>
    <li class="submenu">
      <a href="${ctxFront}/list-3.html">项目孵化</a>
      <ul>
      	<c:forEach items="${fnc:getCategoryList(3, 80, '')}" var="category">
			<li><a href="${category.url}" >${fns:abbr(category.name,28)}</a></li>
		</c:forEach>
      </ul>
    </li>
    <li class="submenu">
    	<a href="http://xcl.htxincailiao.com/f/meeting/list">会议活动</a>
    </li>
    <li class="submenu">
      <a href="http://xcl.htxincailiao.com/f/list-17.html">产业联盟</a>
      <ul>
        <c:forEach items="${fnc:getCategoryList(17, 80, '')}" var="category">
			<li><a href="${category.url}" >${fns:abbr(category.name,28)}</a></li>
		</c:forEach>
      </ul>
    </li>
    <li class="submenu">
      <a href="http://www.htxincailiao.com/kycg.view">研究院介绍</a>
      <ul>
        <li><a href="http://www.htxincailiao.com/yjyjs.view?LB=YSJS">企业简介</a></li>
        <li><a href="http://www.htxincailiao.com/kycg.view">科研成果</a></li>
        <li><a href="http://www.htxincailiao.com/zjrc.view">专家人才</a></li>
        <li><a href="http://www.htxincailiao.com/yjyjs.view?LB=LXWM">联系我们</a></li>
      </ul>
    </li>
    <li class="submenu">
      <a href="http://www.htxincailiao.com/cpyc.view">诚聘英才</a>
    </li>
  </ul>
</div>
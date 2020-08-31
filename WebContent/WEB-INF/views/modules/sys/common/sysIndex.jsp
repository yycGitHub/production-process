<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:getConfig('productName')}</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/assets/css/font-awesome.min.css" />
	<script type="text/javascript">
		$(document).ready(function() {
			bootbox.setDefaults("locale","zh_CN");
		});
	</script>
</head>
<body class="no-skin">
		<div id="navbar" class="navbar navbar-default ace-save-state">
			<div class="navbar-container ace-save-state" id="navbar-container">
				<button type="button" class="navbar-toggle menu-toggler pull-left" id="menu-toggler" data-target="#sidebar">
					<span class="sr-only">Toggle sidebar</span>

					<span class="icon-bar"></span>

					<span class="icon-bar"></span>

					<span class="icon-bar"></span>
				</button>

				<div class="navbar-header pull-left">
					<a href="${ctx}" class="navbar-brand">
						<small>
							<i class="fa fa-leaf"></i>
							溯源管理平台
						</small>
					</a>
				</div>

				<div class="navbar-buttons navbar-header pull-right" role="navigation">
					<ul class="nav ace-nav">
						<li class="grey dropdown-modal">
							<a data-toggle="dropdown" class="dropdown-toggle" href="#">
								<i class="ace-icon fa fa-tasks"></i>
								<span class="badge badge-grey">4</span>
							</a>

							<ul class="dropdown-menu-right dropdown-navbar dropdown-menu dropdown-caret dropdown-close">
								<li class="dropdown-header">
									<i class="ace-icon fa fa-check"></i>
									4 Tasks to complete
								</li>

								<li class="dropdown-content">
									<ul class="dropdown-menu dropdown-navbar">
										<li>
											<a href="#">
												<div class="clearfix">
													<span class="pull-left">Software Update</span>
													<span class="pull-right">65%</span>
												</div>

												<div class="progress progress-mini">
													<div style="width:65%" class="progress-bar"></div>
												</div>
											</a>
										</li>

										<li>
											<a href="#">
												<div class="clearfix">
													<span class="pull-left">Hardware Upgrade</span>
													<span class="pull-right">35%</span>
												</div>

												<div class="progress progress-mini">
													<div style="width:35%" class="progress-bar progress-bar-danger"></div>
												</div>
											</a>
										</li>

										<li>
											<a href="#">
												<div class="clearfix">
													<span class="pull-left">Unit Testing</span>
													<span class="pull-right">15%</span>
												</div>

												<div class="progress progress-mini">
													<div style="width:15%" class="progress-bar progress-bar-warning"></div>
												</div>
											</a>
										</li>

										<li>
											<a href="#">
												<div class="clearfix">
													<span class="pull-left">Bug Fixes</span>
													<span class="pull-right">90%</span>
												</div>

												<div class="progress progress-mini progress-striped active">
													<div style="width:90%" class="progress-bar progress-bar-success"></div>
												</div>
											</a>
										</li>
									</ul>
								</li>

								<li class="dropdown-footer">
									<a href="#">
										See tasks with details
										<i class="ace-icon fa fa-arrow-right"></i>
									</a>
								</li>
							</ul>
						</li>

						<li class="purple dropdown-modal">
							<a data-toggle="dropdown" class="dropdown-toggle" href="#">
								<i class="ace-icon fa fa-bell icon-animated-bell"></i>
								<span class="badge badge-important">8</span>
							</a>

							<ul class="dropdown-menu-right dropdown-navbar navbar-pink dropdown-menu dropdown-caret dropdown-close">
								<li class="dropdown-header">
									<i class="ace-icon fa fa-exclamation-triangle"></i>
									8 Notifications
								</li>

								<li class="dropdown-content">
									<ul class="dropdown-menu dropdown-navbar navbar-pink">
										<li>
											<a href="#">
												<div class="clearfix">
													<span class="pull-left">
														<i class="btn btn-xs no-hover btn-pink fa fa-comment"></i>
														New Comments
													</span>
													<span class="pull-right badge badge-info">+12</span>
												</div>
											</a>
										</li>

										<li>
											<a href="#">
												<i class="btn btn-xs btn-primary fa fa-user"></i>
												Bob just signed up as an editor ...
											</a>
										</li>

										<li>
											<a href="#">
												<div class="clearfix">
													<span class="pull-left">
														<i class="btn btn-xs no-hover btn-success fa fa-shopping-cart"></i>
														New Orders
													</span>
													<span class="pull-right badge badge-success">+8</span>
												</div>
											</a>
										</li>

										<li>
											<a href="#">
												<div class="clearfix">
													<span class="pull-left">
														<i class="btn btn-xs no-hover btn-info fa fa-twitter"></i>
														Followers
													</span>
													<span class="pull-right badge badge-info">+11</span>
												</div>
											</a>
										</li>
									</ul>
								</li>

								<li class="dropdown-footer">
									<a href="#">
										See all notifications
										<i class="ace-icon fa fa-arrow-right"></i>
									</a>
								</li>
							</ul>
						</li>

						<li class="green dropdown-modal">
							<a data-toggle="dropdown" class="dropdown-toggle" href="#">
								<i class="ace-icon fa fa-envelope icon-animated-vertical"></i>
								<span class="badge badge-success">5</span>
							</a>

							<ul class="dropdown-menu-right dropdown-navbar dropdown-menu dropdown-caret dropdown-close">
								<li class="dropdown-header">
									<i class="ace-icon fa fa-envelope-o"></i>
									5 Messages
								</li>

								<li class="dropdown-content">
									<ul class="dropdown-menu dropdown-navbar">
										<li>
											<a href="#" class="clearfix">
												<img src="${ctxStatic}/assets/images/avatars/avatar.png" class="msg-photo" alt="Alex's Avatar" />
												<span class="msg-body">
													<span class="msg-title">
														<span class="blue">Alex:</span>
														Ciao sociis natoque penatibus et auctor ...
													</span>

													<span class="msg-time">
														<i class="ace-icon fa fa-clock-o"></i>
														<span>a moment ago</span>
													</span>
												</span>
											</a>
										</li>

										<li>
											<a href="#" class="clearfix">
												<img src="${ctxStatic}/assets/images/avatars/avatar3.png" class="msg-photo" alt="Susan's Avatar" />
												<span class="msg-body">
													<span class="msg-title">
														<span class="blue">Susan:</span>
														Vestibulum id ligula porta felis euismod ...
													</span>

													<span class="msg-time">
														<i class="ace-icon fa fa-clock-o"></i>
														<span>20 minutes ago</span>
													</span>
												</span>
											</a>
										</li>

										<li>
											<a href="#" class="clearfix">
												<img src="${ctxStatic}/assets/images/avatars/avatar4.png" class="msg-photo" alt="Bob's Avatar" />
												<span class="msg-body">
													<span class="msg-title">
														<span class="blue">Bob:</span>
														Nullam quis risus eget urna mollis ornare ...
													</span>

													<span class="msg-time">
														<i class="ace-icon fa fa-clock-o"></i>
														<span>3:15 pm</span>
													</span>
												</span>
											</a>
										</li>

										<li>
											<a href="#" class="clearfix">
												<img src="${ctxStatic}/assets/images/avatars/avatar2.png" class="msg-photo" alt="Kate's Avatar" />
												<span class="msg-body">
													<span class="msg-title">
														<span class="blue">Kate:</span>
														Ciao sociis natoque eget urna mollis ornare ...
													</span>

													<span class="msg-time">
														<i class="ace-icon fa fa-clock-o"></i>
														<span>1:33 pm</span>
													</span>
												</span>
											</a>
										</li>

										<li>
											<a href="#" class="clearfix">
												<img src="${ctxStatic}/assets/images/avatars/avatar5.png" class="msg-photo" alt="Fred's Avatar" />
												<span class="msg-body">
													<span class="msg-title">
														<span class="blue">Fred:</span>
														Vestibulum id penatibus et auctor  ...
													</span>

													<span class="msg-time">
														<i class="ace-icon fa fa-clock-o"></i>
														<span>10:09 am</span>
													</span>
												</span>
											</a>
										</li>
									</ul>
								</li>

								<li class="dropdown-footer">
									<a href="inbox.html">
										See all messages
										<i class="ace-icon fa fa-arrow-right"></i>
									</a>
								</li>
							</ul>
						</li>

						<li class="light-blue dropdown-modal">
							<a data-toggle="dropdown" href="#" class="dropdown-toggle">
								<img class="nav-user-photo" src="${ctxStatic}/assets/images/avatars/user.jpg" alt="Jason's Photo" />
								<span class="user-info">
									<small>欢迎,</small>
									<c:set var="user" value="${fns:getUser()}"/>
									${user.name}
								</span>

								<i class="ace-icon fa fa-caret-down"></i>
							</a>

							<ul class="user-menu dropdown-menu-right dropdown-menu dropdown-yellow dropdown-caret dropdown-close">
<!-- 								<li> -->
<!-- 									<a href="#"> -->
<!-- 										<i class="ace-icon fa fa-cog"></i> -->
<!-- 										Settings -->
<!-- 									</a> -->
<!-- 								</li> -->
								<li>
									<a href="#" onclick="javascript:$('#main-tab').aceaddtab({ title: '个人信息', url: '${ctx}/sys/index' })">
										<i class="ace-icon fa fa-user"></i>
										个人信息
									</a>
								</li>

								<li class="divider"></li>

								<li>
									<a href="${ctx}/logout">
										<i class="ace-icon fa fa-power-off"></i>
										退出登录
									</a>
								</li>
							</ul>
						</li>
					</ul>
				</div>
			</div><!-- /.navbar-container -->
		</div>
		<div class="main-container ace-save-state" id="main-container">
			<script type="text/javascript">
					try{ace.settings.loadState('main-container')}catch(e){}
			</script>
			<div id="sidebar" class="sidebar responsive ace-save-state">
				<script type="text/javascript">
					try{ace.settings.loadState('sidebar')}catch(e){}
 				</script>
				<div class="sidebar-shortcuts" id="sidebar-shortcuts">
					<div class="sidebar-shortcuts-large" id="sidebar-shortcuts-large">
						<button class="btn btn-success">
							<i class="ace-icon fa fa-signal"></i>
						</button>
				
						<button class="btn btn-info">
							<i class="ace-icon fa fa-pencil"></i>
						</button>
				
						<button class="btn btn-warning">
							<i class="ace-icon fa fa-users"></i>
						</button>
				
						<button class="btn btn-danger">
							<i class="ace-icon fa fa-cogs"></i>
						</button>
					</div>
				
					<div class="sidebar-shortcuts-mini" id="sidebar-shortcuts-mini">
						<span class="btn btn-success"></span>
				
						<span class="btn btn-info"></span>
				
						<span class="btn btn-warning"></span>
				
						<span class="btn btn-danger"></span>
					</div>
				</div>
				
				<ul class="nav nav-list" id="menu">
					<c:set var="menuList" value="${fns:getMenuList()}"/>
					<c:forEach items="${menuList}" var="menu" varStatus="idxStatus">
					<c:if test="${menu.parent.id eq '1' && menu.isShow eq '1'}">
					<li id="menu_li_${menu.id}" class="topMenu">	
						<c:if test="${empty menu.href}">
							<a href="#" class="dropdown-toggle">
								<i name="iconId" class="menu-icon fa ${menu.icon}"></i>
								<span class="menu-text"> ${menu.name} </span>
								<b class="arrow fa fa-angle-down"></b>
							</a>
						</c:if>
						<c:if test="${not empty menu.href}">
							<a href="#" onclick="$('#main-tab').aceaddtab({id:'${menu.id}',pname:'${menu.name}',icon:'${menu.icon}', title: '${menu.name}',url: '${fn:indexOf(menu.href, '://') eq -1 ? ctx : ''}${menu.href}?menuId=${menu.id}'});">
								<i class="menu-icon fa fa-desktop"></i>
								<span class="menu-text"> ${menu.name} </span>
							</a>
						</c:if>
						<b class="arrow"></b>
						<ul class="submenu">
						<c:forEach items="${menuList}" var="menuChild">
						<c:set var="parFlag" value="${fn:indexOf(menuChild.href, '://') eq -1 ? '&' : '?'}"/>
						<c:if test="${menuChild.parent.id eq menu.id&&menuChild.isShow eq '1'}">
							<li id="menu_li_${menuChild.id}" class="">
								<c:if test="${empty menuChild.href}">
									<a href="#" class="dropdown-toggle">
										<i class="menu-icon fa fa-caret-right"></i>
										${menuChild.name}
										<b class="arrow fa fa-angle-down"></b>
									</a>
								</c:if>
								<c:if test="${not empty menuChild.href}">
									<a href="#" onclick="$('#main-tab').aceaddtab({id:'${menuChild.id}',pname:'${menu.name}',name:'${menuChild.name}',icon:'${menu.icon}', title: '${menuChild.name}',url: '${fn:indexOf(menuChild.href, '://') eq -1 ? ctx : ''}${menuChild.href}${parFlag}menuId=${menuChild.id}'});">
									<i class="menu-icon fa fa-caret-right"></i>
									<span class="menu-text"> ${menuChild.name} </span>
									</a>
								</c:if>
								<b class="arrow"></b>
								<ul class="submenu">
								<c:forEach items="${menuList}" var="menuTreeLevel">
								<c:if test="${menuTreeLevel.parent.id eq menuChild.id&&menuTreeLevel.isShow eq '1'}">
									<li id="menu_li_${menuTreeLevel.id}" class="">
										<c:if test="${empty menuTreeLevel.href}">
											<a href="#" class="dropdown-toggle">
												<i class="menu-icon fa fa-caret-right"></i>
												${menuTreeLevel.name}
												<b class="arrow fa fa-angle-down"></b>
											</a>
										</c:if>
										<c:if test="${not empty menuTreeLevel.href}">
											<a href="#" onclick="$('#main-tab').aceaddtab({id:'${menuTreeLevel.id}',pname:'${menu.name}',name:'${menuChild.name}',sname:'${menuTreeLevel.name}',icon:'${menu.icon}', title: '${menuTreeLevel.name}', url: '${fn:indexOf(menuTreeLevel.href, '://') eq -1 ? ctx : ''}${menuTreeLevel.href}?menuId=${menuTreeLevel.id}' })">
<%-- 											<a href= "javascript:loadPage('${fn:indexOf(menuTreeLevel.href, '://') eq -1 ? ctx : ''}${menuTreeLevel.href}?menuId=${menuTreeLevel.id}','#page-content');"> --%>
											<i class="menu-icon fa fa-caret-right"></i>
											<span class="menu-text"> ${menuTreeLevel.name} </span>
											</a>
										</c:if>
										<b class="arrow"></b>
									</li>
								</c:if>
								</c:forEach>
								</ul>
							</li>
						</c:if>
						</c:forEach>
						</ul>
					</li>	
					</c:if>
					</c:forEach>
				</ul>
				
				<div class="sidebar-toggle sidebar-collapse" id="sidebar-collapse">
					<i id="sidebar-toggle-icon" class="ace-icon fa fa-angle-double-left ace-save-state" data-icon1="ace-icon fa fa-angle-double-left" data-icon2="ace-icon fa fa-angle-double-right"></i>
				</div>
			</div>
			
			<div class="main-content">
				<div class="main-content-inner">
					<div class="breadcrumbs ace-save-state" id="breadcrumbs">
					<div id="main-tab"></div>
					</div>
				</div>
			</div>
			
<!-- 			<div class="main-content"> -->
<!-- 			<div class="main-content-inner"> -->
<!-- 				<div class="page-content" id="page-content"> -->
<!-- 					<div class="row"> -->
<!-- 						<div class="col-xs-12"> -->
<!-- 							PAGE CONTENT BEGINS -->
<!-- 							<ul class="nav-my-tab"> -->
<!-- 			                    <li class="leftbackward"><a href="javascript:viod(0)" > -->
<!-- 			                        <i class="icon-backward"></i> -->
<!-- 			                        </a> -->
<!-- 			                    </li> -->
<!-- 			                    <li class="middletab"> -->
<!-- 									<ul class="nav nav-tabs" role="tablist"> -->
<!-- 												<li class="active"><a href="#Index" role="tab" data-toggle="tab">首页</a></li> -->
<!-- 									</ul> -->
<!-- 			                    </li> -->
<!-- 			                    <li class="rightforward"><a href="javascript:viod(0)"> -->
<!-- 			                            <i class="icon-forward"></i></a> -->
<!-- 			                     </li> -->
<!-- 			                </ul> -->

<!-- 		      				<div class="tab-content"> -->
<!-- 		       					<div role="tabpanel" class="tab-pane active" id="Index"></div> -->
<!-- 		      				</div> -->

<!-- 							PAGE CONTENT ENDS -->
<!-- 						</div>/.col -->
<!-- 					</div>/.row -->
<!-- 				</div>/.page-content -->
<!-- 		        /.page-content -->
<!-- 			</div> -->
<!-- 			</div> -->
<!-- 			<div class="footer"> -->
<!-- 				<div class="footer-inner"> -->
<!-- 					<div class="footer-content"> -->
<!-- 						<span class="bigger-120"> -->
<!-- 							<span class="blue bolder">SureServe</span> -->
<!-- 						</span> -->
	
<!-- 						&nbsp; &nbsp; -->
<!-- 						<span class="action-buttons"> -->
<!-- 							<a href="http://www.sureserve.cn/"> -->
<!-- 								<i class="ace-icon fa fa-twitter-square light-blue bigger-150"></i> -->
<!-- 							</a> -->
	
<!-- 							<a href="http://www.sureserve.cn/"> -->
<!-- 								<i class="ace-icon fa fa-facebook-square text-primary bigger-150"></i> -->
<!-- 							</a> -->
	
<!-- 							<a href="http://www.sureserve.cn/"> -->
<!-- 								<i class="ace-icon fa fa-rss-square orange bigger-150"></i> -->
<!-- 							</a> -->
<!-- 						</span> -->
<!-- 					</div> -->
<!-- 				</div> -->
<!-- 			</div> -->

			<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
				<i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
			</a>
		</div><!-- /.main-container -->
		
		<script type="text/javascript">
		jQuery(function($) {
				$("#main-tab").aceaddtab({ title: "个人信息", url: "${ctx}/sys/index" });
			
				$('#recent-box [data-rel="tooltip"]').tooltip({placement: tooltip_placement});
				function tooltip_placement(context, source) {
					var $source = $(source);
					var $parent = $source.closest('.tab-content')
					var off1 = $parent.offset();
					var w1 = $parent.width();
			
					var off2 = $source.offset();
					//var w2 = $source.width();
			
					if( parseInt(off2.left) < parseInt(off1.left) + parseInt(w1 / 2) ) return 'right';
					return 'left';
				}
			
			
				$('.dialogs,.comments').ace_scroll({
					size: 300
			    });
				
				
				//Android's default browser somehow is confused when tapping on label which will lead to dragging the task
				//so disable dragging when clicking on label
				var agent = navigator.userAgent.toLowerCase();
				if(ace.vars['touch'] && ace.vars['android']) {
				  $('#tasks').on('touchstart', function(e){
					var li = $(e.target).closest('#tasks li');
					if(li.length == 0)return;
					var label = li.find('label.inline').get(0);
					if(label == e.target || $.contains(label, e.target)) e.stopImmediatePropagation() ;
				  });
				}
			
				$('#tasks').sortable({
					opacity:0.8,
					revert:true,
					forceHelperSize:true,
					placeholder: 'draggable-placeholder',
					forcePlaceholderSize:true,
					tolerance:'pointer',
					stop: function( event, ui ) {
						//just for Chrome!!!! so that dropdowns on items don't appear below other items after being moved
						$(ui.item).css('z-index', 'auto');
					}
					}
				);
				$('#tasks').disableSelection();
				$('#tasks input:checkbox').removeAttr('checked').on('click', function(){
					if(this.checked) $(this).closest('li').addClass('selected');
					else $(this).closest('li').removeClass('selected');
				});
			
			
				//show the dropdowns on top or bottom depending on window height and menu position
				$('#task-tab .dropdown-hover').on('mouseenter', function(e) {
					var offset = $(this).offset();
			
					var $w = $(window)
					if (offset.top > $w.scrollTop() + $w.innerHeight() - 100) 
						$(this).addClass('dropup');
					else $(this).removeClass('dropup');
				});
			
			})
		</script>
</body>
</html>
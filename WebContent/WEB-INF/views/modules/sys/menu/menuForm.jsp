<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>菜单编辑</title>
	<meta name="decorator" content="default"/>
	<!-- sureserve 图标选中样式   add by ludang 2018-08-02 -->
	<style>
		.active {
		    text-decoration: none;
		    background-color: #4F99C6;
		    color: #FFF;
		    margin: auto;
		    font-weight: 400;
		}
	</style>
	<script type="text/javascript">
	$(document).ready(function(){
		$("#name").focus();
		$("#inputForm").validate({
			errorElement: 'div',
			errorClass: 'help-block',
			focusInvalid: false,
			ignore: "",
			rules: {
				name: {
					required: true
				},
				parentName: {
					required: true
				}
			},
			highlight: function (e) {
				$(e).closest('.form-group').removeClass('has-info').addClass('has-error');
			},
			success: function (e) {
				$(e).closest('.form-group').removeClass('has-error');//.addClass('has-info');
				$(e).remove();
			},
			errorPlacement: function (error, element) {
				if(element.is('input[type=checkbox]') || element.is('input[type=radio]')) {
					var controls = element.closest('div[class*="col-"]');
					if(controls.find(':checkbox,:radio').length > 1) controls.append(error);
					else error.insertAfter(element.nextAll('.lbl:eq(0)').eq(0));
				}
				else if(element.is('.select2')) {
					error.insertAfter(element.siblings('[class*="select2-container"]:eq(0)'));
				}
				else if(element.is('.chosen-select')) {
					error.insertAfter(element.siblings('[class*="chosen-container"]:eq(0)'));
				}
				else error.insertAfter(element.parent());
			},
			submitHandler: function(form){
				form.submit();
			},
			invalidHandler: function (form) {
			}
		});
	});
	</script>
</head>
	<div id="menuDiv">
	<form id="inputForm" class="form-horizontal" action="${ctx}/sys/menu/save" method="post">
		<input type="text" name="id" hidden="true" value="${menu.id}"/>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 上级菜单 </label>
			<div class="col-sm-9">
				<div class="clearfix">
					<tags:treeselect id="menu" name="parent.id" value="${menu.parent.id}" labelName="parent.name" labelValue="${menu.parent.name}"
					title="菜单" url="/sys/menu/treeData" extId="${menu.id}" cssClass="col-xs-10 col-sm-5"/>
				</div>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 菜单名称 </label>
	
			<div class="col-sm-9">
				<div class="clearfix">
					<input type="text" name="name" class="col-xs-10 col-sm-5" value="${menu.name}"/>
				</div>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 链接 </label>
	
			<div class="col-sm-9">
				<input type="text" name="href" class="col-xs-10 col-sm-5" value="${menu.href}"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 图标 </label>
	
			<div class="col-sm-9">
				<input type="text" name="icon" id="menuIcon" class="col-xs-10 col-sm-5" readonly="true" value="${menu.icon}"/>
				<div class="input-group-btn">
					<button type="button" id="icon-select-dialog" class="btn btn-primary btn-sm">
						<i class="ace-icon fa fa-search icon-on-right bigger-110"></i>
					</button>
				</div>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 排序 </label>
	
			<div class="col-sm-9">
				<input type="text" name="sort" class="col-xs-10 col-sm-5" value="${menu.sort}"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 可见 </label>
	
			<div class="col-sm-9">
					<div class="radio">
					<c:if test="${menu.isShow != '0'}">
						<label>
							
							<input name="isShow" type="radio" class="ace" value="1" checked="checked" />
							
							<span class="lbl"> 显示</span>
						</label>
						<label>
							<input name="isShow" type="radio" class="ace" value="0"/>
							
							<span class="lbl"> 隐藏</span>
						</label>
					</c:if>
					<c:if test="${menu.isShow == '0'}">
						<label>
							
							<input name="isShow" type="radio" class="ace" value="1" />
							
							<span class="lbl"> 显示</span>
						</label>
						<label>
							<input name="isShow" type="radio" class="ace" value="0" checked="checked" />
							
							<span class="lbl"> 隐藏</span>
						</label>
					</c:if>
					</div>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 权限标识 </label>
	
			<div class="col-sm-9">
				<input type="text" name="permission" class="col-xs-10 col-sm-5" value="${menu.permission}"/>
			</div>
		</div>
		<div class="clearfix form-actions">
			<div class="col-md-offset-3 col-md-9">
				<button class="btn btn-info" type="submit">
					<i class="ace-icon fa fa-check bigger-110"></i>
					保存
				</button>
	
				&nbsp; &nbsp; &nbsp;
				<button class="btn" type="reset">
					<i class="ace-icon fa fa-undo bigger-110"></i>
					重置
				</button>
			</div>
		</div>
	</form>
	</div>
	<div id="dialog-ico" class="hide">
	<input type="hidden" id="icon" value="${menu.icon}" />
		<div class="row">
			<div class="col-sm-3">
				<script type="text/javascript">
					$("#icons li").click(function(){
			    		$("#icons li").removeClass("active");
			    		$(this).addClass("active");
			    		$("#icon").val($(this).text());
			    	});
			    	$("#icons li").each(function(){
			    		if ($(this).text()=="${menu.icon}"){
			    			$(this).click();
			    		}
			    	});
			    	$("#icons li").dblclick(function(){
			    	});
				</script>
				<ul class="list-unstyled" id="icons">
					<li><i class="ace-icon fa fa-adjust"></i>fa-adjust</li>
					<li><i class="ace-icon fa fa-asterisk"></i>fa-asterisk</li>
					<li><i class="ace-icon fa fa-ban"></i>fa-ban</li>
					<li><i class="ace-icon fa fa-bar-chart-o"></i>fa-bar-chart-o</li>
					<li><i class="ace-icon fa fa-barcode"></i>fa-barcode</li>
					<li><i class="ace-icon fa fa-flask"></i>fa-flask</li>
					<li><i class="ace-icon fa fa-beer"></i>fa-beer</li>
					<li><i class="ace-icon fa fa-bell-o"></i>fa-bell-o</li>
					<li><i class="ace-icon fa fa-bell"></i>fa-bell</li>
					<li><i class="ace-icon fa fa-bolt"></i>fa-bolt</li>
					<li><i class="ace-icon fa fa-book"></i>fa-book</li>
					<li><i class="ace-icon fa fa-bookmark"></i>fa-bookmark</li>
					<li><i class="ace-icon fa fa-bookmark-o"></i>fa-bookmark-o</li>
					<li><i class="ace-icon fa fa-briefcase"></i>fa-briefcase</li>
					<li><i class="ace-icon fa fa-bullhorn"></i>fa-bullhorn</li>
					<li><i class="ace-icon fa fa-calendar"></i>fa-calendar</li>
					<li><i class="ace-icon fa fa-camera"></i>fa-camera</li>
					<li><i class="ace-icon fa fa-camera-retro"></i>fa-camera-retro</li>
					<li><i class="ace-icon fa fa-certificate"></i>fa-certificate</li>
				</ul>
			</div>
			<div class="col-sm-3">
				<ul class="list-unstyled" id="icons"><li><i class="ace-icon fa fa-check-square-o"></i>fa-check-square-o</li>
					<li><i class="ace-icon fa fa-square-o"></i>fa-square-o</li>
					<li><i class="ace-icon fa fa-circle"></i>fa-circle</li>
					<li><i class="ace-icon fa fa-circle-o"></i>fa-circle-o</li>
					<li><i class="ace-icon fa fa-cloud"></i>fa-cloud</li>
					<li><i class="ace-icon fa fa-cloud-download"></i>fa-cloud-download</li>
					<li><i class="ace-icon fa fa-cloud-upload"></i>fa-cloud-upload</li>
					<li><i class="ace-icon fa fa-coffee"></i>fa-coffee</li>
					<li><i class="ace-icon fa fa-cog"></i>fa-cog</li>
					<li><i class="ace-icon fa fa-cogs"></i>fa-cogs</li>
					<li><i class="ace-icon fa fa-comment"></i>fa-comment</li>
					<li><i class="ace-icon fa fa-comment-o"></i>fa-comment-o</li>
					<li><i class="ace-icon fa fa-comments"></i>fa-comments</li>
					<li><i class="ace-icon fa fa-comments-o"></i>fa-comments-o</li>
					<li><i class="ace-icon fa fa-credit-card"></i>fa-credit-card</li>
					<li><i class="ace-icon fa fa-tachometer"></i>fa-tachometer</li>
					<li><i class="ace-icon fa fa-desktop"></i>fa-desktop</li>
					<li><i class="ace-icon fa fa-arrow-circle-o-down"></i>fa-arrow-circle-o-down</li>
					<li><i class="ace-icon fa fa-download"></i>fa-download</li>
				</ul>
			</div>
			<div class="col-sm-3">
				<ul class="list-unstyled" id="icons">
					<li><i class="ace-icon fa fa-pencil-square-o"></i>fa-pencil-square-o</li>
					<li><i class="ace-icon fa fa-envelope"></i>fa-envelope</li>
					<li><i class="ace-icon fa fa-envelope-o"></i>fa-envelope-o</li>
					<li><i class="ace-icon fa fa-exchange"></i>fa-exchange</li>
					<li><i class="ace-icon fa fa-exclamation-circle"></i>fa-exclamation-circle</li>
					<li><i class="ace-icon fa fa-external-link"></i>fa-external-link</li>
					<li><i class="ace-icon fa fa-eye-slash"></i>fa-eye-slash</li>
					<li><i class="ace-icon fa fa-eye"></i>fa-eye</li>
					<li><i class="ace-icon fa fa-video-camera"></i>fa-video-camera</li>
					<li><i class="ace-icon fa fa-fighter-jet"></i>fa-fighter-jet</li>
					<li><i class="ace-icon fa fa-film"></i>fa-film</li>
					<li><i class="ace-icon fa fa-filter"></i>fa-filter</li>
					<li><i class="ace-icon fa fa-fire"></i>fa-fire</li>
					<li><i class="ace-icon fa fa-flag"></i>fa-flag</li>
					<li><i class="ace-icon fa fa-folder"></i>fa-folder</li>
					<li><i class="ace-icon fa fa-folder-open"></i>fa-folder-open</li>
					<li><i class="ace-icon fa fa-folder-o"></i>fa-folder-o</li>
					<li><i class="ace-icon fa fa-folder-open-o"></i>fa-folder-open-o</li>
					<li><i class="ace-icon fa fa-cutlery"></i>fa-cutlery</li>
				</ul>
			</div>
			<div class="col-sm-3">
				<ul class="list-unstyled" id="icons">
					<li><i class="ace-icon fa fa-heart-o"></i>fa-heart-o</li>
					<li><i class="ace-icon fa fa-home"></i>fa-home</li>
					<li><i class="ace-icon fa fa-inbox"></i>fa-inbox</li>
					<li><i class="ace-icon fa fa-info-circle"></i>fa-info-circle</li>
					<li><i class="ace-icon fa fa-key"></i>fa-key</li>
					<li><i class="ace-icon fa fa-leaf"></i>fa-leaf</li>
					<li><i class="ace-icon fa fa-laptop"></i>fa-laptop</li>
					<li><i class="ace-icon fa fa-gavel"></i>fa-gavel</li>
					<li><i class="ace-icon fa fa-lemon-o"></i>fa-lemon-o</li>
					<li><i class="ace-icon fa fa-lightbulb-o"></i>fa-lightbulb-o</li>
					<li><i class="ace-icon fa fa-lock"></i>fa-lock</li>
					<li><i class="ace-icon fa fa-unlock"></i>fa-unlock</li>
					<li><i class="ace-icon fa fa-glass"></i>fa-glass</li>
					<li><i class="ace-icon fa fa-globe"></i>fa-globe</li>
					<li><i class="ace-icon fa fa-users"></i>fa-users</li>
					<li><i class="ace-icon fa fa-hdd-o"></i>fa-hdd-o</li>
					<li><i class="ace-icon fa fa-headphones"></i>fa-headphones</li>
					<li><i class="ace-icon fa fa-heart"></i>fa-heart</li>
				</ul>
			</div>
		</div>
	</div><!-- #dialog-ico -->
	<script type="text/javascript">
		jQuery(function($) {		
			$( "#icon-select-dialog" ).on('click', function(e) {
				 	bootbox.dialog({ 
					message: $( "#dialog-ico").html(),
					title: "选择菜单图标 ",
					className: "my-modal",
			        buttons:             
			        {
			            "success" :
			             {
			                "label" : "<i class='icon-ok'></i> 确定",
			                "className" : "btn-sm btn-success",
			                "callback": function() {
			                  var selectIcon = $("#icon").val();
							  $("#menuIcon").val(selectIcon);
			                }
			            },
			        	"cancel": {
				            "label" : "<i class='icon-info'></i> 取消",
				            "className" : "btn-sm btn-danger"
			            }
			        }
				});
			});
		});
	</script>
</html>
var lastsel;
var list = [];
$(function() {

	loadData();

	$("#find_btn").click(function() {
		$('[placeholder]').each(function() {
			var input = $(this);
			if (input.val() == input.attr('placeholder')) {
				input.val('');
			}
		})
		var delFlag = $("#delFlag").val();
		var id = $("#id").val();
		postData = {
			'delFlag' : delFlag,
			'id' : id
		}
		$("#officetable").jqGrid('setGridParam', {
			url : "getOfficeList",
			postData : postData, // 发送数据
			page : 1
		}).trigger("reloadGrid"); // 重新载入
		initData();
		$('[placeholder]').each(function() {
			var input = $(this);
			if (input.val() == '') {
				input.val(input.attr('placeholder'));
			}
		})
	});

});

function deleteMul() {
	var idstring = '';
	var ids = $("#officetable").jqGrid("getGridParam", "selarrrow");
	if (ids.length == 0) {
		bootbox.alert("请选择要删除的机构!");
		return;
	} else {
		for (var i = 0; i < ids.length; i++) {
			var rowData = $("#officetable").jqGrid("getRowData", ids[i]);
			idstring += rowData.id + ',';
		}
		idstring = idstring.substr(0, idstring.length - 1);
	}

	var id = $("#id").val();
	postData = {
		'ids' : idstring,
		'rebackId' : id
	}

	bootbox.confirm({
		size : "small",
		message : "你确定删除所选机构吗?",
		callback : function(result) {
			if (result) {
				$.ajax({
					url : 'deleteMul',
					data : postData,
					type : "POST",
					dataType : 'json',
					beforeSend : function() {
						// 在异步提交前要做的操作
					},
					success : function(data) {
						$("#officetable").jqGrid('setGridParam').trigger(
								"reloadGrid"); // 重新载入
						initData();
					}
				});
			}
			return;
		}
	});
}

function pageInit() {
	var delFlag = $("#delFlag").val();
	var id = $("#id").val();
	postData = {
		'delFlag' : delFlag,
		'id' : id
	}

	jQuery("#officetable")
			.jqGrid(
					{
						url : 'getOfficeList',
						postData : postData, // 发送数据
						datatype : "json",
						mtype : 'POST',
						colNames : [ '机构id', '机构状态', '机构名称', '归属区域', '机构编码',
								'机构类型', '备注', '操作' ],
						colModel : [ {
							name : 'id',
							index : 'id',
							align : "center",
							editable : false,
							hidden : true
						}, {
							name : 'delFlag',
							index : 'delFlag',
							align : "center",
							editable : false,
							hidden : true
						}, {
							name : 'name',
							index : 'name',
							width : 200,
							align : "center",
							editable : false
						}, {
							name : 'areaName',
							index : 'areaName',
							width : 200,
							align : "center",
							editable : false
						}, {
							name : 'code',
							index : 'code',
							width : 120,
							align : "center",
							editable : false
						}, {
							name : 'typeName',
							index : 'type',
							align : "center",
							editable : false,
							width : 150
						}, {
							name : 'remarks',
							index : 'remarks',
							align : "center",
							editable : false,
							width : 200
						}, {
							name : 'act',
							index : 'act',
							align : "center",
							width : 120,
							sortable : false
						} ],
						rowNum : 20,
						rowList : [ 10, 20, 30, 50, 100 ],
						pager : '#officepage',
						autowidth : true,
						height : 450,
						sortname : 'id',
						viewrecords : true,
						sortorder : "desc",
						multiselect : true,
						jsonReader : {
							root : "list",
							page : "pageNo",
							total : "pageTotal",
							records : "count"
						},
						prmNames : {
							'page' : 'pageNo',
							'rows' : 'pageSize'
						},
						onSelectRow : function(id) {
							if (lastsel && id !== lastsel) {
								// 保存行数据
								loadList();
							}

						},
						gridComplete : function() {
							var ids = jQuery("#officetable").jqGrid(
									"getDataIDs");
							for (var i = 0; i < ids.length; i++) {
								var cl = ids[i];
								var rowData = $("#officetable").jqGrid(
										"getRowData", ids[i]);
								if (rowData.delFlag == '0') {
									html = '<a onclick="update(\''
											+ rowData.id
											+ '\',\''
											+ id
											+ '\');">'
											+ '<span class="green"><i class="ace-icon fa fa-pencil bigger-130"></i></span>&nbsp;</a>'
											+ '<a onclick="deleteById(\''
											+ rowData.id
											+ '\',\''
											+ id
											+ '\');">'
											+ '<span class="red"><i class="ace-icon fa fa-trash-o bigger-130"></i></span>&nbsp;</a>';
								} else {
									html = '<a onclick="rebackOffice(\''
											+ rowData.id
											+ '\',\''
											+ id
											+ '\');">'
											+ '<span class="green"><i class="ace-icon fa fa-pencil bigger-130"></i>恢复</span></a>';
								}
								jQuery("#officetable").jqGrid("setRowData",
										ids[i], {
											act : html
										});
							}
						}
					});
}

function loadData() {
	pageInit();
}

function clearInput() {
	$(".search :text").val("");
	$(".search select").val("");
	$('[placeholder]').each(function() {
		var input = $(this);
		input.val(input.attr('placeholder'));
	})
}

function initData() {
	list = [];
	lastsel = 0;
}
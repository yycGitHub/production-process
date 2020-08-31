var rootPatch = getRootPath_dc();
var WEBUPLOADER_URL = rootPatch+"/static/baidu/webuploader";

var $list;
var $image;
var $hiddenFiles;
var oneFile = true;
var fieldMark;
var uploader = WebUploader.create({
	auto : true,
	swf : WEBUPLOADER_URL + '/Uploader.swf',
	server : BASE_URL+'/a/common/fileUpload',
	resize : false,
	fileVal : "Filedata"
});

uploader.on('fileQueued', function(file) {
	if(oneFile){
		$list.html('<div id="' + file.id+"_"+fieldMark+ '"><span>' + file.name
				+ '</span>&nbsp;&nbsp;<span class="state">等待上传...</span>'
				+ '</div>');
	}else{
		$list.append('<div id="' + file.id+"_"+fieldMark + '"><span>' + file.name
				+ '</span>&nbsp;&nbsp;<span class="state">等待上传...</span>'
				+ '</div>');
	}
});

uploader.on('uploadProgress', function(file, percentage) {
	var $li = $('#' + file.id+"_"+fieldMark);
	$li.find('span.state').text('上传中:' + percentage.toFixed(2) * 100 + '%');
});

uploader.on('uploadSuccess', function(file, response) {
	if(oneFile){
		$image.val(rootPatch+"/"+response.absolutePath);
		$('#' + file.id+"_"+fieldMark).append("<img src=\""+rootPatch+"/"+response.absolutePath+"\" width='100px;height:100px;'>");
		$('#' + file.id+"_"+fieldMark).find('span.state').text('已上传');
		$('#' + file.id+"_"+fieldMark).find('span.state').after('<a onclick="return delUploadedFile(\'' 
				+ file.id+"\',\'"+ file.id+"_"+fieldMark + '\',\''+ response.id + '\');" href="javascript:;" >删除</a>');
	}else{
		$('#' + file.id+"_"+fieldMark).find('span.state').text('已上传');
		$('#' + file.id+"_"+fieldMark).find('span.state').after('<a onclick="return delUploadedFile(\'' 
				+ file.id+"\',\'"+ file.id+"_"+fieldMark + '\',\''+ response.id + '\');" href="javascript:;" >删除</a>');
		$hiddenFiles.append("<div id='" + response.id + "'><input type='hidden' name='fileIds' value='" + response.id + "' />"
				+"<input type='hidden' name='fileNames' value='" + response.fileName+ "' />"
				+"<input type='hidden' name='filePaths' value='" + response.absolutePath + "' /></div>");
	}
});

uploader.on('uploadError', function(file) {
	$('#' + file.id+"_"+fieldMark).find('span.state').text('上传出错');
});

function getRootPath_dc() {
    return window.location.protocol + '//' + window.location.host;
}

//初始化fileinput控件（第一次初始化）
function delUploadedFile(fileid,Nodeid,tid) {   
	var url = BASE_URL+"/a/common/delete";
	$.ajax({
		url : url,
		type : "POST",
		data : {id : tid},
		success : function(data){
			if(data=="success"){
				alert("附件删除成功");
				uploader.removeFile(fileid,true);
				var div = document.getElementById(Nodeid);
			    while(div.hasChildNodes()) //当div下还存在子节点时 循环继续
			    {
			        div.removeChild(div.firstChild);
			    }
			}else{
				alert("附件删除失败");
			}
		}
	})
    
}
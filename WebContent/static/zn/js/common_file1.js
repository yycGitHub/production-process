var isIE_handleFiles1 = /msie/i.test(navigator.userAgent) && !window.opera;
//target = this
//filetypes = ".jpg.png.gif"; 文件類型 ,如为空则默认文件类型为.doc.docx.ppt.pptx.xls.xlsx.pdf.wpt.bmp.jpg.jpeg.gif
//filemaxsize  =1024  kb单位;  文件大小，如为空则默认为2M
function common_handleFiles1(target, filetypes, filemaxsize,mkid,sxid,type) {
	var fileSize = 0;
	var filepath = target.value;
	//设置默认大小2M;
	if (filemaxsize == '' || filemaxsize == null || filemaxsize == undefined) {
		filemaxsize = 1024 * 2;
	}
	//设置默认文件格式;
	if (filetypes == '' || filetypes == null|| filetypes == undefined) 	
	{
		filetypes = ".doc.docx.ppt.pptx.xls.xlsx.pdf.wpt.bmp.jpg.jpeg.gif.txt.png";
	}
	if (filepath) {
		if (isIE_handleFiles1 && !target.files) {
			var filePath = target.value;
			var three = filePath.split(".");
			var last = three[three.length - 1].toLowerCase();
			var rs = filetypes.indexOf(last);
			if (rs < 0) {
				alert(filePath + ". 不接受此文件类型！");
				target.value = "";
				return false;
			}
			var fileSystem = new ActiveXObject("Scripting.FileSystemObject");
			if (!fileSystem.FileExists(filePath)) {
				alert("附件不存在，请重新输入！");
				return false;
			}
			var file = fileSystem.GetFile(filePath);
			fileSize = file.Size;

			var size = fileSize / 1024;
			if (size > filemaxsize) {
				alert("附件大小不能大于" + filemaxsize / 1024 + "M！");
				target.value = "";
				return false;
			}
			if (size <= 0) {
				alert("附件大小不能为0M！");
				target.value = "";
				return false;
			}
		}else{
			var fujian = target.files;
			var result = "";
			var len = fujian.length;
			if (len > 0) {
				for (var i = 0; i < len; i++) {

					var three = fujian[i].name.split(".");
					var last = three[three.length - 1].toLowerCase();
					var rs = filetypes.indexOf(last);
					if (rs < 0) {
						alert(fujian[i].name + ". 不接受此文件类型！");
						target.value = "";
						return false;
					}
					var size = fujian[i].size / 1024;
					if (size > filemaxsize) {
						alert(fujian[i].name + ". 附件大小不能大于" + filemaxsize / 1024
								+ "M！");
						target.value = "";
						return false;
					}
					if (size <= 0) {
						alert(fujian[i].name + ". 附件大小不能为0M！");
						target.value = "";
						return false;
					}
					result += fujian[i].name + ", ";
				}
				var objUrl = getObjectURL(fujian[0]) ;//获取文件信息  
				if (objUrl) {  
					uploadSxFile(objUrl,mkid,sxid,type);
				}
			}
		}
	}
}

function uploadSxFile(objUrl,mkid,sxid,type){  
	var picFile = document.getElementById("file"+sxid); 
	var files = picFile.files;
	if(files.length==0){
		alert("请先上传产品图片");
		return;
	}
	var formData = new FormData($("#sxForm")[0]);  //重点：要用这种方法接收表单的参数  
    $.ajax({  
        //url : "/sureserve-admin-dev/api/pcIndex/uploadImage?sxid="+sxid,
        url : "/api/pcIndex/uploadImage?sxid="+sxid,
        type : 'POST',  
        data : formData,  
        // 告诉jQuery不要去处理发送的数据  
        processData : false,                   
        // 告诉jQuery不要去设置Content-Type请求头  
        contentType : false,  
        async : false,  
        success : function(data) {  
        	if(data.indexOf("|")>-1){
        		var arr = data.split("|");
    			$("#pic"+mkid+'_'+sxid).attr("src", objUrl);
    			$("#"+mkid+'_'+sxid+'_'+type).val(arr[1]);
        	}else{
        		alert("上传失败");
        		$("#pic"+mkid+'_'+sxid).attr("src", '');
        		$("#"+mkid+'_'+sxid+'_'+type).val('');
        	}
        }  
    });  
}  

function getObjectURL(file) {  
	var url = null;   
	if (window.createObjectURL!=undefined) {  
	    url = window.createObjectURL(file) ;  
	} else if (window.URL!=undefined) { // mozilla(firefox)  
	    url = window.URL.createObjectURL(file) ;  
	} else if (window.webkitURL!=undefined) { // webkit or chrome  
	    url = window.webkitURL.createObjectURL(file) ;  
	}  
	return url ;  
}  

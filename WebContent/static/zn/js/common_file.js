var isIE_handleFiles = /msie/i.test(navigator.userAgent) && !window.opera;
//target = this
//filetypes = ".jpg.png.gif"; 文件類型 ,如为空则默认文件类型为.doc.docx.ppt.pptx.xls.xlsx.pdf.wpt.bmp.jpg.jpeg.gif
//filemaxsize  =1024  kb单位;  文件大小，如为空则默认为2M
function common_handleFiles(target, filetypes, filemaxsize) {
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

		if (isIE_handleFiles && !target.files) {
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
				result = result.substring(0, result.length - 2);
				$("#filename").text(result);
			}
		}
	}
	
}

//预览图片
function look_image(url){
	layer.open({
	   type: 2,
	   title: "图片预览",
	   shadeClose: false,
	   shade: 0.8,
	   area: ['80%','93%'],
	   content: url
	}); 
}

//预览多个图片
function look_imagelist(ctx,images,thisimage,index){
	url = ctx+'/diligentmanagement/gXZZ0101/lookImage?dz='+thisimage+'&index='+index+'&images='+images;
	layer.open({
	   type: 2,
	   title: "多图片预览",
	   shadeClose: false,
	   shade: 0.8,
	   area: ['80%','93%'],
	   content: url
	}); 
}

//预览
function lookFile(basePath,ctx,fileName,dz){
	if(fileName.toLowerCase().indexOf(".bmp")>-1 || fileName.toLowerCase().indexOf(".jpg")>-1 
			|| fileName.toLowerCase().indexOf(".jpeg")>-1 || fileName.toLowerCase().indexOf(".gif")>-1
			|| fileName.toLowerCase().indexOf(".png")>-1){
		look_image(ctx+'/diligentmanagement/gXZZ0101/lookImage?dz='+dz);
	}else if(fileName.toLowerCase().indexOf(".pdf")>-1){
		var url = basePath+dz;
		window.open(url,"_blank")
	}else{
		alert("该文件不支持预览");
	}
}
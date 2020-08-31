<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<%@ attribute name="targetId" type="java.lang.String" required="true" description="目标Id"%>
<%@ attribute name="fromTableId" type="java.lang.String" required="false" description="来源表数据ID"%>
<%@ attribute name="fromTableName" type="java.lang.String" required="false" description="来源表名"%>
<c:set var="valuesMap" value="${fep:getItemValues(fromTableId,fromTableName)}"/>
<input type="hidden" name="epListId" value="${valuesMap['epListId']}"/>
<c:forEach 
	items="${fep:getExpandProperty(targetId)}" 
	var="standardItem">
	<c:set var="key" value="${standardItem.id}"/>
	<c:if test="${standardItem.itemType == '0' }">
		<tr>
			<td align="right">
				<c:if test="${standardItem.isRequired}">
					<span class="red font18 martop">&lowast;&nbsp;</span>
				</c:if>
				${standardItem.name}:
			</td>
			<td colspan="2" align="left">
				<input id="${standardItem.id}" name="ep_${standardItem.keyName}" htmlEscape="false" 
		         	class="input_txt w400 <c:if test="${standardItem.isRequired}">required</c:if>"
		         	value="${valuesMap[key]}"/>
			</td>
		</tr>
	</c:if>
	<c:if test="${standardItem.itemType == '1' }">
		<tr>
			<td align="right">
				<c:if test="${standardItem.isRequired}">
					<span class="red font18 martop">&lowast;&nbsp;</span>
				</c:if>
				${standardItem.name}:
			</td>
			<td colspan="2" align="left">
				<select id="${standardItem.id}" name="ep_${standardItem.keyName}" class="drop_down gray w200">
					<c:forEach items="${standardItem.standardItemValues}" var="itemValue">
						<option value="${itemValue.value}">${itemValue.name}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
	</c:if>
	<c:if test="${standardItem.itemType == '3' }">
		<tr>
			<td align="right">
				<c:if test="${standardItem.isRequired}">
					<span class="red font18 martop">&lowast;&nbsp;</span>
				</c:if>
				${standardItem.name}:
			</td>
			<td colspan="2" align="left">
				<fmt:parseDate value="${valuesMap[key]}" var="dateF" pattern="yyyy-MM-dd HH:mm:ss" type="both"></fmt:parseDate>
				<input id="${standardItem.id}" name="ep_${standardItem.keyName}"
				 	value="<fmt:formatDate value="${dateF}" pattern="yyyy-MM-dd HH:mm:ss"/>" 
				 	class="input_txt w400 <c:if test="${standardItem.isRequired}">required</c:if>" 
					onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" 
				    datatype="*/^\s*$|^\d{4}\-\d{1,2}\-\d{1,2}$/" errormsg="请选择正确的日期" sucmsg=" " nullmsg="请填写信息！"/>
			</td>
		</tr>
	</c:if>
	<c:if test="${standardItem.itemType == '5' }">
		<tr>
			<td align="right">
				<c:if test="${standardItem.isRequired}">
					<span class="red font18 martop">&lowast;&nbsp;</span>
				</c:if>
				${standardItem.name}:
			</td>
			<td colspan="2" align="left">
				<input type="hidden" id="${standardItem.id}" name="ep_${standardItem.keyName}" value="${valuesMap[key]}">
				<span>${valuesMap[key]}</span>
			</td>
		</tr>
	</c:if>
	<c:if test="${standardItem.itemType == '7' }">
		<tr>
			<td align="right">
				<c:if test="${standardItem.isRequired}">
					<span class="red font18 martop">&lowast;&nbsp;</span>
				</c:if>
				${standardItem.name}:
			</td>
			<td colspan="2" align="left">
				<textarea name="remarks" htmlEscape="false" class="area w_90 h50" id="${standardItem.id}" name="ep_${standardItem.keyName}">${valuesMap[key]}</textarea>
			</td>
		</tr>
	</c:if>
	<c:if test="${standardItem.itemType == '8' }">
		<script type="text/javascript">
				$(document).ready(function() {
				var ue = UE.getEditor('${standardItem.id}',{ 
				    initialFrameWidth : 	100 + "%", 
				    initialFrameHeight: 350 
				});
		</script>
		<tr>
			<td align="right">
				<c:if test="${standardItem.isRequired}">
					<span class="red font18 martop">&lowast;&nbsp;</span>
				</c:if>
				${standardItem.name}:
			</td>
			<td colspan="2" align="left">
				<textarea name="remarks" htmlEscape="false" class="<c:if test="${standardItem.isRequired}">required</c:if>" 
					id="${standardItem.id}" name="ep_${standardItem.keyName}">${valuesMap[key]}</textarea>
			</td>
		</tr>
	</c:if>
	<c:if test="${standardItem.itemType == '9' }">
	<c:set var="fileList" value="${fep:getFiles(fromTableId,1,standardItem.id)}"/>
	    <tr>
            <td align="right">${standardItem.name}：</td>
            <td align="left" class="w400">
               	<div id="hiddenFiles_${standardItem.id}">
				</div>
				<div id="ls_${standardItem.id}">
					<c:forEach items="${fileList}" var="fileInfo" varStatus="status">
               			<div id="${status.index}_${standardItem.id}">
               				<span>${fileInfo.fileName}</span>&nbsp;&nbsp;
	               			<span class="state">已上传</span>
	               			<a onclick="return delUploadedFile('${status.index}_${standardItem.id}','${fileInfo.id}');" 
	               			href="javascript:;">删除</a>
               			</div>
               		</c:forEach>
				</div>
            </td>
            <td>
               	<div class="btns">
		        <div id="picker_${standardItem.id}"  onclick="addPicButton_${standardItem.id}();"></div>
		    	</div>
             </td>
        </tr>
	    <script type="text/javascript">
			//有几个上传按钮，就绑定几个
			uploader.addButton({
			    id: '#picker_${standardItem.id}',//按钮ID
			    innerHTML: '选择文件'//按钮名称
			});
			
			function addPicButton_${standardItem.id}(){
				oneFile = false;
				fieldMark = '${standardItem.id}';
				uploader.option('formData',{"fieldMark":fieldMark});
				//设置上传文件显示列表
				$list = $('#ls_${standardItem.id}');
				$hiddenFiles = $('#hiddenFiles_${standardItem.id}');
				//设置上传子目录
				uploader.options.formData.moduleFilePath="vacationfiles";
			}
	    </script>
	</c:if>
</c:forEach>
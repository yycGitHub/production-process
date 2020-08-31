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
	<!-- input输入框 -->
	<c:if test="${standardItem.itemType == '0' }">
		<dl>
			<dt>${standardItem.name}:</dt>
			<dd>
		         <input id="${standardItem.id}" name="ep_${standardItem.keyName}" htmlEscape="false" 
		         	class="input_txt w400 <c:if test="${standardItem.isRequired}">required</c:if>"
		         	value="${valuesMap[key]}"/>
			</dd>
		</dl>
	</c:if>
	<!-- select下拉选择框 -->
	<c:if test="${standardItem.itemType == '1' }">
		<dl>
			<dt>${standardItem.name}:</dt>
			<dd>
		         <select id="${standardItem.id}" name="ep_${standardItem.keyName}" class="drop_down gray w200">
					<c:forEach items="${standardItem.standardItemValues}" var="itemValue">
						<option value="${itemValue.value}">${itemValue.name}</option>
					</c:forEach>
				</select>
			</dd>
		</dl>
	</c:if>
	<!-- 日期选择 -->
	<c:if test="${standardItem.itemType == '3' }">
		<dl>
			<dt>${standardItem.name}:</dt>
			<dd>
				<fmt:parseDate value="${valuesMap[key]}" var="dateF" pattern="yyyy-MM-dd HH:mm:ss" type="both"></fmt:parseDate>
				<input id="${standardItem.id}" name="ep_${standardItem.keyName}"
				 	value="<fmt:formatDate value="${dateF}" 
					pattern="yyyy-MM-dd HH:mm:ss"/>" class="input_txt w400 <c:if test="${standardItem.isRequired}">required</c:if>" 
					onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" 
				    datatype="*/^\s*$|^\d{4}\-\d{1,2}\-\d{1,2}$/" errormsg="请选择正确的日期" sucmsg=" " nullmsg="请填写信息！"/>
			</dd>
		</dl>
	</c:if>
	<!-- 描述项 -->
	<c:if test="${standardItem.itemType == '5' }">
		<dl>
			<dt>${standardItem.name}:</dt>
			<dd>
		         <input type="hidden" id="${standardItem.id}" name="ep_${standardItem.keyName}" htmlEscape="false" value="${valuesMap[key]}"/>
		         <span>${valuesMap[key]}</span>	
			</dd>
		</dl>
	</c:if>
	<c:if test="${standardItem.itemType == '7' }">
		<dl>
			<dt>${standardItem.name}:</dt>
			<dd>
				<textarea name="remarks" htmlEscape="false" class="area w_90 h50" id="${standardItem.id}" name="ep_${standardItem.keyName}">${valuesMap[key]}</textarea>
			</dd>
		</dl>
	</c:if>
	<c:if test="${standardItem.itemType == '8' }">
		<script type="text/javascript">
				$(document).ready(function() {
				var ue = UE.getEditor('${standardItem.id}',{ 
				    initialFrameWidth : 	100 + "%", 
				    initialFrameHeight: 350 
				});
		</script>
		<dl>
			<dt>${standardItem.name}:</dt>
			<dd>
		         <textarea name="remarks" htmlEscape="false" class="<c:if test="${standardItem.isRequired}">required</c:if>" 
					id="${standardItem.id}" name="ep_${standardItem.keyName}">${valuesMap[key]}</textarea>
			</dd>
		</dl>
	</c:if>
	<c:if test="${standardItem.itemType == '9' }">
	    <dl>
			<dt>${standardItem.name}:</dt>
			<dd>
		         <table>
		         	<tr>
			            <td align="left" class="w400">
			            	<input type="hidden" name="ep_${standardItem.keyName}" value="${valuesMap[key]}">
							<div id="ls">
								<img id="${standardItem.id}" src="${valuesMap[key]}" width='100px;height:100px;'>
							</div>
				        </td>
				        <td>
				            <div class="btns">
					        	<div id="picker"  onclick="addPicButton();"></div>
					    	</div>
				        </td>
				    </tr>
		         </table>
			</dd>
		</dl>
	</c:if>
</c:forEach>
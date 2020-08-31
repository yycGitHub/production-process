<#assign columnList = sourceTable.sourceColumnList>
<#assign importList = sourceTable.sourceImportList>
package ${packageName}.${moduleName}.entity${subModuleName};

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.PrePersist;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


import com.surekam.common.persistence.XGXTEntity;
import com.surekam.common.utils.IdGen;

<#list importList as import>
	<#if (import !="")>
import ${import};
	</#if>
</#list>

/**
 * ${functionName}Entity
 * @author ${classAuthor}
 * @version ${classVersion}
 */
@Entity
@Table(name = "${sourceTable.tableName}")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ${ClassName} extends XGXTEntity<${ClassName}> {
	
	private static final long serialVersionUID = 1L;
	<#list columnList as column>
	private ${column.javaClass} ${column.instance};//${column.logicalName!""}
	</#list>

	public ${ClassName}() {
		super();
	}
	
<#list columnList as column>
		<#if column.instance == sourceTable.idKey>
	@PrePersist
	public void prePersist(){
		this.${column.instance} = IdGen.uuid();
	}
		
	@Id
		</#if>
		<#if column.physicalInstance?ends_with('_ID')>
	@Column(name="${column.physicalInstance}")
		</#if>
		<#if column.physicalName == "CLOB">
  	@Lob
  	@Basic(fetch = FetchType.EAGER)
  	@Column(columnDefinition="CLOB", nullable=true)
		</#if>
		<#if column.physicalName == "BLOB">
	@Transient	
	public String get${column.instance?cap_first}String() {
		if(this.${column.instance} == null){
			return null;
		}
		return new String(this.${column.instance});
	}
		
	@Lob
  	@Basic(fetch = FetchType.EAGER)
  	@Column(columnDefinition="BLOB", nullable=true)
		</#if>
	public ${column.javaClass} get${column.instance?cap_first}() {
		return this.${column.instance};
	}
	public void set${column.instance?cap_first}(${column.javaClass} ${column.instance}) {
		this.${column.instance} = ${column.instance};
	}
	
</#list>	
}



package com.surekam.modules.sys.utils;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.surekam.modules.sys.entity.Role;

/**
 * 用于定义各种角色的ID 除非必须 否则尽量不要在代码中加入此角色判断
 * @author ligm
 * @version 2017-03-29
 */
public class StaticStringUtils {

	//请假管理   班长  
	public static final String STUDENT_VACATION_BZ = "a91d748b2fdc4df7b15ae1ba3a71c48e";
	//请假管理   班导师  
	public static final String STUDENT_VACATION_BDS = "a609c34f4be84d7bae1953c01cd25b68";
	//请假管理   系主任 
	public static final String STUDENT_VACATION_XZR = "68f49045cf6c40d6bb272b3722320a18";
	//请假管理   副院长  
	public static final String STUDENT_VACATION_FYZ = "ad2f12ba37e74a9390e974825dccf6c3";
	//学工部请假管理负责人
	public static final String STUDENT_VACATION_XGB = "ecdf94cf522d439caa48eb4c3b1b3470";
	//请假管理   年级级长 
	public static final String STUDENT_VACATION_NJJZ = "1f1b34d261f943ee849bd20310ec39dc";
	
	//吐鲁番
	public static final String AGRO_TULUFAN = "433620addfd144028f9d1afaabfe8299";
	//望城
	public static final String AGRO_WANGCHENG = "a2bfcffa042646a98360ef86343de977";
	//浏阳
	public static final String AGRO_LIUYANG = "f76a6516f42647699fa1c7ceba53a511";
	//南县
	public static final String AGRO_NANXIAN = "ddc5d5315cb049b6875906f2d3a2915d";
	
	//基地负责人
	public static final String AGRO_FZR = "391192d4f9634982858634f12de44275";
	//农户
	public static final String AGRO_NH = "1852c8e247744ff184e8c162eff44f4c";
	//专家
	public static final String AGRO_ZJ = "7b18d965552a4a2286a35dd94fed0b2a";
	
	//待办任务流程划分tab页，每页对应检索的流程定义关键字
	public static final String DBRW_TYPE1 = "plan_report";
	public static final String DBRW_TYPE2 = "plan_publish";
	public static final String DBRW_TYPE3 = "plan_check";
	public static final String DBRW_TYPE4 = "plan_modify";
	
	
	/**
	 * 检查是当前用户否含有某个角色
	 * @param roleId
	 * @return
	 */
	public static boolean hasTheRoleByRoleId(String roleId,List<Role> roleList){
		for (Iterator iterator = roleList.iterator(); iterator.hasNext();) {
			Role role = (Role) iterator.next();
			if(StringUtils.isNotBlank(role.getId()) && roleId.equals(role.getId())){
				return true;
			}
		}
		return false;
	}
	
}

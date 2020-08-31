package com.surekam.common.utils;

/**
 * <p>
 * 异常枚举类
 * </p>
 * 
 * @author 张刚学
 * @date 2017年4月30日 下午10:17:25
 */
public enum ResultEnum {

	SUCCESS(0, "操作成功"), 
	OPERATION_FAILED(-1, "操作失败"), 
	DEMO_MODE_FAILED(-1, "演示模式，不允许操作！"),
	ERROR_VALIDATE(2, "数据验证失败！"),
	TOKEN_IS_NULL(100001, "token值为空"), 
	TOKEN_IS_INVALID(100002, "token无效"), 
	TOKEN_IS_EXPIRED(100002, "token已过期"), 
	INVITATIONCODE_IS_INVALID(100003, "无效的邀请码"),
	LOGIN_FAILED(1, "工号或密码错误!"), 
	SYSTEM_ERROR(2, "系统错误,请联系管理员!"), 
	REGISTER_CONFIRM_PASSWORD_ERROR(3, "确认密码不一致"), 
	SYSTEM_USER_EXIST(505, "登录名已存在"), 
	SYSTEM_USER_PASSWORD_LENGTH(506, "密码长度不能小于6位数"), 
	SYSTEM_USER_ROLE(507, "请选择角色"), 
	REGISTER_PHONE_EXSIST(4, "手机号码已注册"), 
	REGISTER_VALIDATION_CODE_ERROR(5, "验证码错误"), 
	GET_VALIDATION_CODE_ERROR(8, "验证码发送失败"), 
	REGISTER_PHONE_NOT_EXSIST(6, "手机号码未注册"), 
	AUTH_FAILED(400, "认证失败"), 
	BAD_REQUEST(401, "非法请求"), 
	BAD_REQ_PARAM(402, "非法请求，参数无效"), 
	NOT_FOUND(404, "未找到资源"), 
	INTERNAL_ERROR(500, "服务器内部错误"), 
	AUTH_RES_NULL(501, "权限未分配相关资源"),
	DATA_EXIST(601, "提交的数据已存在"), 
	DATA_NOT_EXIST(602, "请求的数据不存在"), 
	NULL_EXCEPTION(603, "数据空指针异常"),
	DATA_COUNT_ZERO(604, "请求的数据暂时未录入"),
	LOGIN_AUDIT(102, "账号注册成功,等待公司管理员审核后方可登录!"),
	
	INTERFACE_FAILED(1004, "访问接口错误"),

	// 机构管理
	INSERT_OFFICE_FAILED(100101, ""), 
	DELETE_OFFICE_FAILED(200102, "删除机构失败, 不允许删除顶级机构或编号空"), 
	UPDATE_OFFICE_FAILED(200103, "修改角色错误"), 
	ROLE_NAME_DUPLICATED(200104, "角色名称重复"), 
	INSERT_ROLE_AUTH_FAILED(200105, "角色分配权限失败"), 
	DELETE_OFFICE_FAILED_SON(200106, "删除公司失败,请先删除子公司"), 
	DELETE_OFFICE_FAILED_PRODUCT(200107, "删除公司失败,请先该公司的有关产品"), 
	UPDATE_OFFICE_PARENT_ID(200108, "所属企业不能选择下级"),
	// 标签管理
	APLLY_NUM_TOO_LGRGE(200201, "标签申请数量太大"),

	// 主题附件文件类型错误提示
	ATTACH_TYPE_ERROR(300001, "只支持zip和rar格式的压缩文件上传"),

	// 系统管理
	SYSTEM_NO_AUTH_USER(400001, "无权查看该用户信息！"),

	// 溯源产品管理
	PRODUCT_BATCH_EXIST(500001, "产品存在批次数据，不能删除"),

	// 溯源产品管理
	PRODUCT_LABEL_EXIST(500002, "该批次存在已绑定的标签数据，不能删除"),

	// 溯源产品管理
	PRODUCT_MODEL_DATA_ERROR(500003, "模块数据的ID和排序未正确传入参数，无法保存模块排序"),

	// 溯源产品管理
	PRODUCT_MODEL_NOT_EXIST_ERROR(500004, "该批次没有配置任何模块，数据有误"),

	// 溯源产品管理
	PRODUCT_NAME_EXIST(500005, "产品名称已存在，不能新增"),

	// 溯源产品管理
	OTHER_PRODUCT_NAME_EXIST(500015, "产品名称已被占用，不能修改"),

	// 溯源企业维护
	COMPANY_NAME_EXIST(600013, "该企业名称已被占用，不能保存"),

	// 溯源产品管理
	PRODUCT_LABEL_PRINTED(500006, "标签已被打印，标签申请数据不能被删除"),

	// 溯源产品管理
	THEME_NAME_EXIST(500007, "主题名称已存在，不能新增"),

	// 溯源产品管理
	MODEL_NAME_EXIST(500008, "模块名称已存在，不能新增"),

	// 溯源码激活
	ACT_TRACE_CODE_ERROR(900000, "激活溯源码失败"),

	// 溯源产品管理
	THEME_LABEL_AUDIT(500009, "标签审核未通过不能打印"),

	// 溯源产品管理
	THEME_LABEL_CANCEL(500010, "已打印的标签不能作废"),

	// 溯源标签申请
	THEME_LABEL_EXIST(600009, "部分标签号存在其他批次中"),

	// 溯源标签申请
	THEME_LABEL_NOTEXIST(600011, "不在绑定标签号范围内"),

	// 溯源标签申请
	THEME_LABEL_NULL(600010, "请输入起始值或者结束值"),

	CODE_NULL(600014, "未查询到该食品溯码信息"),

	// 选择的公司下没有标签
	COMPANY_LABEL_NULL(600012, "选择的公司下没有标签"),

	// 微信接口调用失败
	WX_GET_NULL(1000001, "获取失败"),

	// 基地名称重复
	BASE_NAME_EXIST(500009, "基地名称已存在，不能新增"),

	// 批次编码重复
	BATCH_CODE_EXIST(500010, "批次编码已存在，不能新增"),
	
	BATCH_DELETE_ERROR(500011, "该批次执行过农事任务，不能删除"),

	// 溯源设备维护t_agro_sensor_setup
	SENSOR_NAME_EXIST(700013, "该设备名称已被占用，不能保存"),
	//品种种类
	PRODUCT_CATEGORY_NULL(1100001, "获取失败"), 
	PRODUCT_CATEGORY_ERROR(1100002, "系统错误"), 
	PRODUCT_CATEGORY_CODE(1100003, "选择错误，请选择品种。"),

	//邀请失败
	INVITE_ERROR(1100005,"邀请失败"),
	
	//退出失败
	EXIT_ERROR(1100006,"邀请失败"),
	
	// 生长周期时间
	CYCLE_BEGIN_DAY(800001,"开始时间错误"),
	CYCLE_END_DAY(800002,"结束时间错误"),
	CYCLE_NAME(800003, "生长周期名称已存在，不能新增。"),
	
	// 作业标准数据存在
	TASK_DATA_EXISTS(900001,"请先删除作业记录，再删除该记录。"),
	TASK_DATA_NAME(900001,"标准名称已存在，不能新增。"),
	
	// 参数名称
	PARAM_DATA_EXISTS(910001,"参数名称已存在，不能新增。"),
	
	//任务错误
	TASK_LIST_ERROR(920000, "任务列表查询错误。"),
	TASK_SAVE_ERROR(920001, "任务保存失败。"),

	TASK_DELETE_LIVE_ERROR(920002,"存在已执行的任务，不能删除。"),
	
	USER_PHONE(50001, "农户未填写手机号，无法开通短信功能。"),

	USER_MAIL(50002, "农户未填写邮箱地址，无法开通邮箱功能。");
	
	
	/** 错误码 */
	private Integer code;
	/** 错误信息 */
	private String message;

	ResultEnum(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	/**
	 * @return the code
	 */
	public Integer getCode() {
		return code;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
}

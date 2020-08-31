package com.surekam.common.utils;

/** 
 * <p>设置返回数据的工具</p>
 * @author 张刚学 
 * @date 2017年4月30日 下午4:53:11 
 */
public class ResultUtil {

	public static <T> ResultBean<T> success(T bodyData) {
    	ResultBean<T> result = new ResultBean<T>();
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMessage(ResultEnum.SUCCESS.getMessage());
        result.setBodyData(bodyData);
        return result;
    }
    
	@SuppressWarnings("rawtypes")
	public static ResultBean success() {
        return success(null);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> ResultBean<T> error(Integer code, String message) {
    	ResultBean result = new ResultBean();
        result.setCode(code);
        result.setMessage(message);
        result.setBodyData(null);
        return result;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <T> ResultBean<T> error(Integer code, String message,T bodyData) {
    	ResultBean result = new ResultBean();
    	result.setCode(code);
    	result.setMessage(message);
    	result.setBodyData(bodyData);
    	return result;
    }
}

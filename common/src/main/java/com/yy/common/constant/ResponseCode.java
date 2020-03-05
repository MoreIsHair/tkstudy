package com.yy.common.constant;

/**
 * API响应码
 */
public enum ResponseCode {

	SUCCESS(1000, "成功。"),

	FAIL(1001, "失败。"),

	NOMAL(0, "Network anomaly。"),

	SYS_OR_NET_ERROR(100001, "网络故障或系统出错。"),

	DB_ERROR(101001, "数据库错误。"),

	UNKNOWN_ERROR(102001, "不可预知错误。"),

	DB_EXCEPTION(103001, "数据异常，请稍后重试。"),

	REQUEST_PARAM_EXCEPTION(103002, "请求参数异常。"),

	RESPONSE_PARAM_EXCEPTION(103003, "响应参数异常。"),

	//内部异常formFieldinstance
	INTERNAL_EXCEPTION(1004001, "内部异常。"),

	NO_SUCH_REQUEST_HANDLING_METHOD_EXCEPTION(1004002, "内部异常！！！"),

	//http请求方法不支持
	HTTPREQUEST_METHOD_NOTSUPPORTED_EXCEPTION(1004003, "method not support!!!"),

	//http媒体类型不支持
	HTTP_MEDIATYPE_NOTSUPPORTED_EXCEPTION(1004004, "media type not support!!!"),

	//客户端无法支持的媒体类型
	HTTP_MEDIATYPE_NOTACCEPTABLE_EXCEPTION(1004005, "media type not acceptable!!!"),

	//缺少请求路径的变量
	MISSING_PATH_VARIABL_EEXCEPTION(1004006, "miss request part!!!"),

	//缺少必须请求参数
	MISSING_SERVLET_REQUEST_PARAMETER_EXCEPTION(1004007, "missing request params!!!"),

	//请求参数无法绑定
	SERVLETREQUEST_BINDING_EXCEPTION(1004008, "request bind error!!!"),

	//转换出错
	CONVERSION_NOTSUPPORTED_EXCEPTION(1004009, "conversion not supported!!!"),

	//类型不匹配
	TYPEM_ISMATCH_EXCEPTION(1004010, "type mismatch!!!"),

	//消息不可读
	HTTP_MESSAGE_NOTREADABLE_EXCEPTION(1004011, "message is not readable!!!"),

	//消息不可写
	HTTP_MESSAGE_NOTWRITABLE_EXCEPTION(1004012, "message is not writable!!!"),

	//方法参数验证不正确
	METHOD_ARGUMENT_NOTVALID_EXCEPTION(1004013, "method argument not valid!!!"),

	MISSING_SERVLET_REQUESTPART_EXCEPTION(1004014, "缺少请求体"),

	//绑定异常
	BIND_EXCEPTION(1004015, "bind error!!!"),

	//禁止修改其值或其他项的值与其冲突
	REPEAT_SUBMIT(1004016, "重复提交"),

	//dubbo异常
	INTERNAL_DUBBO_EXCEPTION(1004017, "网络异常"),

	//SQL异常
	INTERNAL_SQL_EXCEPTION(1004018, "内部错误！！！"),

	//Shiro无权限
	SHIRO_UNAUTHORIZEDEXCEPTION_EXCEPTION(1004019, "无权限"),

	//Shiro token无效
	SHIRO_TOKEN_EXCEPTION(1004021, "Token无效"),

	//Shiro未登录
	SHIRO_UNAUTHENTICATEDEXCEPTION_EXCEPTION(1004020, "未登录"),

	//Shiro未登录
	BIZ_DATA_SHOR_EXCEPTION(1004022, "缺少数据");


    private Integer code;

    private String msg;

    ResponseCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

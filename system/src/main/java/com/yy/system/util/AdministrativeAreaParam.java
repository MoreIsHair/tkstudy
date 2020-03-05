package com.yy.system.util;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * @author YY
 * @date 2019/11/8
 * @description 省市区结构
 */
@Data
public class AdministrativeAreaParam {
    /**
     * 名称
     */
    @JSONField(name = "name")
    private String name;

    /**
     * code
     */
    @JSONField(name = "code")
    private Integer code;

    /**
     * 子集
     */
    @JSONField(name = "child")
    private List<AdministrativeAreaParam> child;
    /**
     * 子集数组(容错鲜香信息为一个字符串数组,而不是统一的对象格式)
     */
    @JSONField(name = "childArray")
    private List<String> childArray;
}

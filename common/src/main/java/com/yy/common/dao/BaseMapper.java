package com.yy.common.dao;

import com.yy.common.dao.po.BasePo;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface BaseMapper<PO extends BasePo> extends Mapper<PO>, MySqlMapper<PO> {
}

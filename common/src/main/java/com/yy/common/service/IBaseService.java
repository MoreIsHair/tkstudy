package com.yy.common.service;

import com.github.pagehelper.PageInfo;
import com.yy.common.exception.BizException;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

public interface IBaseService<BO> {
    /**
     * 添加
     * @param bo
     */
    void add(BO bo) throws BizException;

    /**
     * 根据id获取
     * @param id
     */
    BO get(Long id) throws BizException;

    /**
     * 更新
     * @param bo
     */
    void updateById(BO bo) throws BizException;

    /**
     * 删除
     * @param id
     */
    void delete(Long id) throws BizException;

    /**
     * 删除
     * @param bo
     */
    void delete(BO bo) throws BizException;

    /**
     * 根据条件查询列表
     * @param bo
     * @return
     * @throws BizException
     */
    List<BO> select(BO bo) throws BizException;

    /**
     * 拓展查询
     * @param example
     * @return
     * @throws BizException
     */
    List<BO> selectByExample(Example example) throws BizException;

    /**
     *  分页查询
     * @param bo
     */
    PageInfo<BO> pageSelect(BO bo, Integer pageSize, Integer pageNum) throws BizException;

    /**
     *  拓展分页查询
     * @param example
     */
    PageInfo<BO> pageSelectByExample(Example example, Integer pageSize, Integer pageNum) throws BizException;
}

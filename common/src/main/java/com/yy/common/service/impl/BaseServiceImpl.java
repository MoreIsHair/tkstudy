package com.yy.common.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yy.common.dao.BaseMapper;
import com.yy.common.dao.bo.BaseBo;
import com.yy.common.dao.po.BasePo;
import com.yy.common.exception.AssertUtils;
import com.yy.common.exception.BizException;
import com.yy.common.reflection.Reflections;
import com.yy.common.service.IBaseService;
import com.yy.common.utils.IDWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

public class BaseServiceImpl<Mapper extends BaseMapper,BO extends BaseBo,PO extends BasePo> implements IBaseService<BO>, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(BaseServiceImpl.class);

    @Autowired
    private ConfigurableApplicationContext ctx;

    protected Mapper mapper;

    @Override
    @Transactional
    public void add(BO bo) throws BizException {
        AssertUtils.notNull(bo,"插入BO对象不能为空！！！");
        bo.setId(IDWorker.getId());
        mapper.insert(boToPO(bo));
    }

    @Override
    public BO get(Long id) throws BizException {
        AssertUtils.notNull(id,"主键Id不能为空！！！");
        PO po = (PO) mapper.selectByPrimaryKey(id);
        return po == null? null : poToBO(po);
    }

    @Override
    @Transactional
    public void updateById(BO bo) throws BizException {
        AssertUtils.notNull(bo,"更新BO对象不能为空！！！");
        mapper.updateByPrimaryKeySelective(boToPO(bo));
    }

    @Override
    @Transactional
    public void delete(Long id) throws BizException {
        AssertUtils.notNull(id,"主键Id不能为空！！！");
        mapper.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional
    public void delete(BO bo) throws BizException {
        AssertUtils.notNull(bo,"删除BO对象不能为空！！！");
        mapper.delete(boToPO(bo));
    }

    @Override
    public List<BO> select(BO bo) throws BizException {
        AssertUtils.notNull(bo,"查询BO对象不能为空！！！");
        PO po = boToPO(bo);
        List<PO> poList = mapper.select(po);

        if (poList != null) {
            List<BO> boList = new ArrayList<>();
            poList.forEach((p) -> boList.add(poToBO(p)));
            return boList;
        }
        return null;
    }

    @Override
    public List<BO> selectByExample(Example example) throws BizException {
        AssertUtils.notNull(example,"查询Example对象不能为空！！！");
        List<PO> poList = mapper.selectByExample(example);

        if (poList != null) {
            List<BO> boList = new ArrayList<>();
            poList.forEach((p) -> boList.add(poToBO(p)));
            return boList;
        }
        return null;
    }

    @Override
    public PageInfo<BO> pageSelect(BO bo, Integer pageSize, Integer pageNum) throws BizException {
        AssertUtils.notNull(bo,"查询BO对象不能为空！！！");
        PageInfo<PO> pageInfo = PageHelper.startPage(pageNum==null?1:pageNum,pageSize==null?20:pageSize).doSelectPageInfo(()->mapper.select(boToPO(bo)));
        PageInfo<BO> boPageInfo = new PageInfo<>(new ArrayList<BO>());
        BeanUtils.copyProperties(pageInfo,boPageInfo,"list");
        if (pageInfo.getSize()>0) {
            pageInfo.getList().forEach((p)->boPageInfo.getList().add(poToBO(p)));
        }
        return boPageInfo;
    }

    @Override
    public PageInfo<BO> pageSelectByExample(Example example, Integer pageSize, Integer pageNum) throws BizException {
        AssertUtils.notNull(example,"查询Example对象不能为空！！！");
        PageInfo<PO> pageInfo = PageHelper.startPage(pageNum==null?1:pageNum,pageSize==null?20:pageSize).doSelectPageInfo(()->mapper.selectByExample(example));
        PageInfo<BO> boPageInfo = new PageInfo<>(new ArrayList<>());
        BeanUtils.copyProperties(pageInfo,boPageInfo,"list");
        if (pageInfo.getSize()>0) {
            pageInfo.getList().forEach((p)->boPageInfo.getList().add(poToBO(p)));
        }
        return boPageInfo;
    }

    private Class<Mapper> getMapperClazz() {
        return Reflections.getSuperClassGenricType(getClass(), 0);
    }


    private Class<BO> getBoClazz() {
        return Reflections.getSuperClassGenricType(getClass(), 1);
    }

    private Class<PO> getPoClazz() {
        return Reflections.getSuperClassGenricType(getClass(), 2);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        mapper = ctx.getBean(getMapperClazz());
    }

    /**
     * BO 转 PO
     * @param bo
     * @return
     */
    protected PO boToPO(BO bo) {
        if (bo == null) {
            return null;
        }
        PO po = null;
        try {
            po = getPoClazz().newInstance();
            BeanUtils.copyProperties(bo,po);
        } catch (Exception e) {
            logger.error("创建PO异常：", e);
        }
        return po;
    }

    /**
     * PO 转 BO
     * @param po
     * @return
     */
    protected BO poToBO(PO po) {
        if (po == null) {
            return null;
        }
        BO bo = null;
        try {
            bo = getBoClazz().newInstance();
            BeanUtils.copyProperties(po,bo);
        } catch (Exception e) {
            logger.error("创建PO异常：", e);
        }
        return bo;
    }

}

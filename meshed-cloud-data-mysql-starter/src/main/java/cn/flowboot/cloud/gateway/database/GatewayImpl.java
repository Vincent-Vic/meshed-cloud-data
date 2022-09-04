package cn.meshed.cloud.gateway.database;

import cn.meshed.cloud.gateway.IGateway;
import cn.meshed.cloud.utils.CopyUtils;
import com.alibaba.cola.dto.PageQuery;
import com.alibaba.cola.dto.PageResponse;
import com.alibaba.cola.exception.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * <h1>网关通用实现</h1>
 * M：Mapper C: 命令数据（新增或修改数据）,Q（查询参数）,V（返回数据【列表】）,VI（返回数据【项】）,I （id类型）
 * @author Vincent Vic
 * @version 1.0
 */

public abstract class GatewayImpl<M,C,Q extends PageQuery,V,VI> implements IGateway<C,Q,V,VI>{

    /**
     * 默认批次提交数量
     */
    public final static int DEFAULT_BATCH_SIZE = 1000;

    protected Log log = LogFactory.getLog(getClass());

    public Class<M> currentModelClass() {
        return (Class<M>) ReflectionKit.getSuperClassGenericType(this.getClass(), GatewayImpl.class, 0);
    }

    public Class<V> currentModelVoClass() {
        return (Class<V>) ReflectionKit.getSuperClassGenericType(this.getClass(), GatewayImpl.class, 3);
    }

    public Class<VI> currentModelVoItemClass() {
        return (Class<VI>) ReflectionKit.getSuperClassGenericType(getClass(),GatewayImpl.class, 4);
    }

    @Autowired
    private BaseMapper<M> baseMapper;




    /**
     * <h2>查询</h2>
     *
     * @param qry 查询参数
     * @return
     */
    @Override
    public PageResponse<V> searchPageList(Q qry) {
        Assert.notNull(qry,"查询参数不能为空");
        LambdaQueryWrapper<M> wrapper = new LambdaQueryWrapper<>();
        handlePageWrapper(wrapper,qry);

        Assert.notNull(wrapper,"处理后条件包装类不能为");
        Assert.notNull(qry,"处理后查询参数不能为空");

        Page<M> page = PageHelper.startPage(qry.getPageIndex(),
                qry.getPageSize(), qry.getOrderBy());
        List<M> mList = baseMapper.selectList(wrapper);
        List<V> vList = CopyUtils
                .copyListProperties(mList, currentModelVoClass());
        return PageResponse.of(vList,(int)page.getTotal(), page.getPageSize(), page.getPageNum());
    }

    /**
     * 处理查询条件
     *
     * @param wrapper 查询包装类
     * @param qry 查询对象
     */
    public abstract void handlePageWrapper(LambdaQueryWrapper<M> wrapper, Q qry);

    /**
     * 根据ID查询一个元素
     *
     * @param id
     * @return
     */
    @Override
    public VI getOneById(Serializable id) {
        M m = baseMapper.selectById(id);
        return CopyUtils.copy(m,currentModelVoItemClass());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VI save(C data) {
        Assert.notNull(data,"保存数据不能为空");
        //拷贝
        M model = CopyUtils.copy(data, currentModelClass());
        //处理数据
        handleSave(model,data);
        //处理后NPE处理
        Assert.notNull(model,"存储对象不能为空");
        //新增
        int insert = baseMapper.insert(model);
        Assert.isTrue(insert > 0,"新增失败");
        return CopyUtils.copy(model,currentModelVoItemClass());
    }

    /**
     * 保存数据处理
     * @param model
     * @param cmd 传递的参数
     */
    public abstract void handleSave(M model,C cmd);

    /**
     * 批量保存
     *
     * @param datas 保存列表
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<VI> batchSave(Collection<C> datas) {
        //拷贝
        List<M> list = CopyUtils.copyListProperties(datas, currentModelClass());
        handleBatchSave(list,datas);
        //处理后NPE处理
        if (CollectionUtils.isEmpty(list)){
            return Collections.emptyList();
        }
        boolean flag = saveBatch(list, DEFAULT_BATCH_SIZE);
        Assert.isTrue(flag,"批量新增失败");

        return CopyUtils.copyListProperties(list,currentModelVoItemClass());
    }

    /**
     * 批量新增数据处理
     * @param models 数据库对应数据
     * @param cmds 传递的参数
     */
    public void handleBatchSave(Collection<M> models,Collection<C> cmds){

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VI update(C cmd) {
        Assert.notNull(cmd,"更新数据不能为空");
        //拷贝
        M model = CopyUtils.copy(cmd, currentModelClass());
        //处理数据
        handleUpadte(model,cmd);
        //处理后NPE处理
        Assert.notNull(model,"存储对象不能为空");
        //新增
        int update = baseMapper.updateById(model);
        Assert.isTrue(update > 0,"更新失败");
        return CopyUtils.copy(model,currentModelVoItemClass());
    }

    /**
     * 更新数据处理
     * @param model 数据库对应数据
     * @param cmd 传递的参数
     */
    public abstract void handleUpadte(M model,C cmd);


    /**
     * 批量保存
     *
     * @param datas 保存列表
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<VI> batchUpdateById(Collection<C> datas) {
        if (CollectionUtils.isEmpty(datas)){
            return Collections.emptyList();
        }
        //拷贝
        List<M> list = CopyUtils.copyListProperties(datas, currentModelClass());
        handleBatchUpadte(list,datas);
        if (CollectionUtils.isEmpty(list)){
            return Collections.emptyList();
        }
        boolean flag = updateBatchById(list, DEFAULT_BATCH_SIZE);
        Assert.isTrue(flag,"批量更新失败");

        return CopyUtils.copyListProperties(list,currentModelVoItemClass());
    }


    /**
     * 批量更新数据处理
     * @param models 数据库对应数据
     * @param cmds 传递的参数
     */
    public void handleBatchUpadte(Collection<M> models,Collection<C> cmds){

    }

    /**
     * 删除
     *
     * @param id id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Serializable id) {
        Assert.notNull(id,"ID 不能为空");
        int delete = baseMapper.deleteById(id);
        Assert.isTrue(delete > 0,"删除失败");
    }

    /**
     * 批量删除
     *
     * @param ids ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteByIds(Collection<Serializable> ids) {
        Assert.isTrue(CollectionUtils.isEmpty(ids),"ID列表不能为空");
        int delete = baseMapper.deleteBatchIds(ids);
        Assert.isTrue(delete > 0,"删除失败");
    }

    public BaseMapper<M> getBaseMapper() {
        return baseMapper;
    }

    /**
     * 返回LambdaQueryWrapper
     * @return
     * @param <T>
     */
    public <T> LambdaQueryWrapper<T> getLqw(){
        return new LambdaQueryWrapper<T>();
    }

    /**
     * 部分代码为了不冗余，直接搬第三方代码
     * {@link com.baomidou.mybatisplus.extension.service.impl.ServiceImpl}
     */

    /**
     * 批量插入
     *
     * @param entityList ignore
     * @param batchSize  ignore
     * @return ignore
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatch(Collection<M> entityList, int batchSize) {
        String sqlStatement = getSqlStatement(SqlMethod.INSERT_ONE);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }


    @Transactional(rollbackFor = Exception.class)
    public boolean updateBatchById(Collection<M> entityList, int batchSize) {
        String sqlStatement = getSqlStatement(SqlMethod.UPDATE_BY_ID);
        return executeBatch(entityList, batchSize, (sqlSession, entity) -> {
            MapperMethod.ParamMap<M> param = new MapperMethod.ParamMap<>();
            param.put(Constants.ENTITY, entity);
            sqlSession.update(sqlStatement, param);
        });
    }

    /**
     * 获取mapperStatementId
     *
     * @param sqlMethod 方法名
     * @return 命名id
     * @since 3.4.0
     */
    protected String getSqlStatement(SqlMethod sqlMethod) {
        return SqlHelper.getSqlStatement(currentModelClass(), sqlMethod);
    }

    /**
     * 执行批量操作
     *
     * @param list      数据集合
     * @param batchSize 批量大小
     * @param consumer  执行方法
     * @param <E>       泛型
     * @return 操作结果
     * @since 3.3.1
     */
    protected <E> boolean executeBatch(Collection<E> list, int batchSize, BiConsumer<SqlSession, E> consumer) {
        return SqlHelper.executeBatch(currentModelClass(), this.log, list, batchSize, consumer);
    }
}

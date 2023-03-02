package cn.meshed.cloud.mysql;

import cn.meshed.cloud.entity.BaseEntity;
import cn.meshed.cloud.context.SecurityContext;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * <h1>基本实体自动填充类</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@Slf4j
public class BaseEntityMetaObjectHandler implements MetaObjectHandler {

    // todo 获取用户信息

    /**
     *  新增填充 BaseEntity 数据
     *
     * @param metaObject Meta Object
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        if (metaObject.getOriginalObject() instanceof BaseEntity) {
            log.debug("start insert fill ......");
            this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
            this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
            this.strictInsertFill(metaObject, "createBy", String.class, getCurrentOperator());
            this.strictInsertFill(metaObject, "updateBy", String.class, getCurrentOperator());
            log.debug("end insert fill ......");
        }
    }


    /**
     * 更新填充
     *
     * @param metaObject Meta Object
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        if (metaObject.getOriginalObject() instanceof BaseEntity) {
            log.debug("start update fill ......");
            this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
            this.strictInsertFill(metaObject, "updateBy", String.class, getCurrentOperator());
            log.debug("end update fill ......");
        }
    }

    private String getCurrentOperator() {
        return SecurityContext.getOperatorString();
    }
}

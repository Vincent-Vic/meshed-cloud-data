package cn.meshed.cloud.mysql;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.context.annotation.Bean;

/**
 * <h1>基本实体自动填充类 自动配置</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@ConditionalOnResource(resources = {"classpath:mapper"})
@ConditionalOnClass({SqlSessionFactory.class})
public class BaseEntityMetaObjectHandlerAutoConfiguration {

    /**
     * 创建bean
     *
     * @return
     */
    @Bean
    public BaseEntityMetaObjectHandler baseEntityMetaObjectHandler(){
        return new BaseEntityMetaObjectHandler();
    }
}

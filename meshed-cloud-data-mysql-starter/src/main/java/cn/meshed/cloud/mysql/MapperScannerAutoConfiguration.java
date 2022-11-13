package cn.meshed.cloud.mysql;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.context.annotation.Bean;

/**
 * <h1></h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@ConditionalOnResource(resources = {"classpath:mapper"})
@ConditionalOnClass({SqlSessionFactory.class})
public class MapperScannerAutoConfiguration {

    @Bean(name = "MapperScannerConfigurerForFramework")
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        // 设置mybatis mapper接口目录
        mapperScannerConfigurer.setBasePackage("cn.meshed.cloud.*.gatewayimpl.database.mapper");
        return mapperScannerConfigurer;
    }

}

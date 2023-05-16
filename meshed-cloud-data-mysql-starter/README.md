# meshed-cloud-data-mysql-starter

推送制品库
```shell
mvn clean install org.apache.maven.plugins:maven-deploy-plugin:2.8:deploy -DskipTests
```

## MySql数据库

EasyMapper 提供 批量新增/修改功能
BaseEntity 实现类自动拥有新增/更新是注入操作用户信息，未登入会抛出异常
```xml
<dependency>
    <groupId>cn.meshed.cloud</groupId>
    <artifactId>meshed-cloud-data-mysql-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## Redis数据库

```xml
<dependency>
    <groupId>cn.meshed.cloud</groupId>
    <artifactId>meshed-cloud-data-redis-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## 数据流

提供@EventContext注解或继承cn.meshed.cloud.cqrs.EventExecute，将从设置操作人信息上下文信息，未登入产生警告信息

提供@EventInject注解，将上下文信息注入安全事件对象

提供StreamBridgeSender包装SCS的StreamBridge，将注入操作对象信息

> 上述均需要满足事件对象继承于SecurityEvent才有效

```xml
<dependency>
    <groupId>cn.meshed.cloud</groupId>
    <artifactId>meshed-cloud-stream-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## DubboRPC

内含有生产者，消费者过滤器对服务的验签，签名传递功能
```xml
<dependency>
    <groupId>cn.meshed.cloud</groupId>
    <artifactId>meshed-cloud-dubbo-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## Nacos依赖

Nacos 配置依赖，一般在Start中引用start依赖中包含
```xml
<dependency>
    <groupId>cn.meshed.cloud</groupId>
    <artifactId>meshed-cloud-nacos-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

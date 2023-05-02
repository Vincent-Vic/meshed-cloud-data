package cn.meshed.cloud.stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;

/**
 * <h1></h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */

@ConditionalOnProperty(name = {"spring.rabbitmq.host"})
public class StreamBridgeSenderAutoConfiguration {

    @Autowired
    private StreamBridge streamBridge;

    @Bean
    public StreamBridgeSender streamBridgeSender(){
        return new StreamBridgeSender(streamBridge);
    }
    @Bean
    public EventContextAspect eventContextAspect(){
        return new EventContextAspect();
    }
    @Bean
    public EventInjectAspect eventInjectAspect(){
        return new EventInjectAspect();
    }
}

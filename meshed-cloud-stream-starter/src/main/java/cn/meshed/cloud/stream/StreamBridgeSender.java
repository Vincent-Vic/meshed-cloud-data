package cn.meshed.cloud.stream;

import cn.meshed.cloud.dto.Event;
import cn.meshed.cloud.context.SecurityContext;
import cn.meshed.cloud.dto.SecurityEvent;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.lang.Nullable;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import java.util.UUID;

/**
 * <h1></h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
public class StreamBridgeSender {

    private final StreamBridge streamBridge;

    public StreamBridgeSender(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public StreamBridge getStreamBridge() {
        return streamBridge;
    }

    /**
     * <h2>发送MQ消息</h2>
     *
     * @param bindingName 绑定名称
     * @param data        数据
     * @return {@link Boolean}
     */
    public boolean send(String bindingName, Object data) {
        preHandleEventData(data);
        return streamBridge.send(bindingName, data);
    }

    /**
     * <h2>发送MQ消息</h2>
     *
     * @param bindingName       绑定名称
     * @param data              数据
     * @param outputContentType 输出内容类型
     * @return {@link Boolean}
     */
    public boolean send(String bindingName, Object data, MimeType outputContentType) {
        preHandleEventData(data);
        return streamBridge.send(bindingName, null, data, outputContentType);
    }

    /**
     * <h2>发送MQ消息</h2>
     *
     * @param bindingName 绑定名称
     * @param binderName  binder名称
     * @param data        数据
     * @return {@link Boolean}
     */
    public boolean send(String bindingName, @Nullable String binderName, Object data) {
        preHandleEventData(data);
        return streamBridge.send(bindingName, binderName, data, MimeTypeUtils.APPLICATION_JSON);
    }


    /**
     * <h2>发送MQ消息</h2>
     *
     * @param bindingName       绑定名称
     * @param binderName        binder名称
     * @param data              数据
     * @param outputContentType 输出内容类型
     * @return {@link Boolean}
     */
    public boolean send(String bindingName, @Nullable String binderName, Object data, MimeType outputContentType) {
        preHandleEventData(data);
        return streamBridge.send(bindingName, binderName, data, outputContentType);
    }

    /**
     * <h2>前置处理数据</h2>
     *
     * @param data 事件数据
     */
    private void preHandleEventData(Object data) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        /**
         * 判断是否是标准事件
         * 标准事件需要用户登入
         */
        if (data instanceof SecurityEvent) {
            ((SecurityEvent) data).setOperator(SecurityContext.getOperator());
            if (((SecurityEvent) data).getUuid() == null){
                ((SecurityEvent) data).setUuid(uuid);
            }
        } else if (data instanceof Event && ((Event) data).getUuid() == null) {
            ((Event) data).setUuid(uuid);
        }

    }

}

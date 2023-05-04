package cn.meshed.cloud.dubbo.filter;






import cn.meshed.cloud.context.SecurityContext;
import cn.meshed.cloud.dubbo.constant.Constant;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;

/**
 * <h1>消费者安全过滤器</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@Activate(group = {Constant.CONSUMER})
public class SecurityConsumerFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String sign = SecurityContext.getSign();
        if (StringUtils.isBlank(sign)) {
            sign = RpcContext.getClientAttachment().getAttachment(Constant.SIGN);
        }
        invocation.getAttachments().put(Constant.SIGN, sign);
        return invoker.invoke(invocation);
    }
}

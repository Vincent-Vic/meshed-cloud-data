package cn.meshed.cloud.dubbo.filter;

import cn.meshed.cloud.context.SecurityContext;
import cn.meshed.cloud.dto.Operator;
import cn.meshed.cloud.dubbo.constant.Constant;
import cn.meshed.cloud.security.AccessTokenService;
import com.alibaba.cola.domain.ApplicationContextHelper;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;

import java.util.Collections;
import java.util.Set;

/**
 * <h1>提供者安全过滤器</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@Slf4j
@Activate(group = {Constant.PROVIDER})
public class SecurityProviderFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String sign = RpcContext.getServerAttachment().getAttachment(Constant.SIGN);
        if (StringUtils.isBlank(sign)) {
            return invoker.invoke(invocation);
        }
        try {
            AccessTokenService accessTokenService = ApplicationContextHelper.getBean(AccessTokenService.class);
            if (accessTokenService != null) {
                //安全校验
                String data = accessTokenService.verifyToken(sign);
                //设置上下文
                SecurityContext.setSign(sign);
                SecurityContext.setOperator(parsingOperator(data));
            }
            return invoker.invoke(invocation);
        } finally {
            SecurityContext.clear();
        }
    }

    public Operator parsingOperator(String data) {
        JSONObject jsonObject = JSONObject.parseObject(data);
        //目前仅存在有用户信息和无用户信息两种签名
        if (jsonObject.size() == 0) {
            return null;
        }
        Operator operator = new Operator(jsonObject.getString("id"), jsonObject.getString("realName"));
        Set<String> grantedAuthority = getSet(jsonObject, "grantedAuthority");
        Set<String> grantedRole = getSet(jsonObject, "grantedRole");

        operator.setAccess(grantedAuthority);
        operator.setRoles(grantedRole);
        log.debug("操作用户: {}| {} ", operator, JSONObject.toJSONString(operator));
        return operator;
    }

    public Set<String> getSet(JSONObject jsonObject, String key) {
        String str = jsonObject.getString(key);
        if (StringUtils.isNotBlank(str)) {
            return JSONObject.parseObject(str, Set.class);
        }
        return Collections.emptySet();
    }
}
package cn.meshed.cloud.gateway.database;

import com.alibaba.cola.dto.PageQuery;

/**
 * <h1>单一网关实现</h1>
 * <h2>单一指Model和VO相同，数据单一</h2>
 * @author Vincent Vic
 * @version 1.0
 */
public abstract class SingleGatewayImpl<M,C,Q extends PageQuery,I extends Comparable> extends GatewayImpl<M,C,Q,M,M>{
}

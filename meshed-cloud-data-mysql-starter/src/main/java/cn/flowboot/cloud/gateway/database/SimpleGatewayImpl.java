package cn.meshed.cloud.gateway.database;

import com.alibaba.cola.dto.PageQuery;

/**
 * <h1>简单网关实现</h1>
 * <h2>简单Modle和Vo</h2>
 * @author Vincent Vic
 * @version 1.0
 */
public abstract class SimpleGatewayImpl<M,C,Q extends PageQuery,V,I extends Comparable> extends GatewayImpl<M,C,Q,V,V>{
}

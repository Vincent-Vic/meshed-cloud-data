package cn.meshed.cloud.utils;

import cn.meshed.cloud.utils.CopyUtils;
import com.alibaba.cola.dto.PageResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import java.util.List;
import java.util.function.Supplier;

/**
 * <h1>分页简化工具</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
public class PageUtils {

    public static <E>  PageResponse<E> of(List<E> list, Page<Object> page){
        return PageResponse.of(list, Math.toIntExact(page.getTotal()), page.getPageSize(), page.getPageNum());
    }

    public static <E,V>  PageResponse<V> of(List<E> list, Page<Object> page, Supplier<V> target){
        List<V> vos = CopyUtils.copyListProperties(list, target);
        return PageResponse.of(vos, Math.toIntExact(page.getTotal()), page.getPageSize(), page.getPageNum());
    }

    public static <E> Page<E> startPage(int pageNum, int pageSize) {
        return PageHelper.startPage(pageNum, pageSize);
    }
}

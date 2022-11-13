package cn.meshed.cloud.mysql;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.injector.methods.AlwaysUpdateSomeColumnById;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import java.util.List;

/**
 * <h1></h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */

public class EasySqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
//        methodList.add(new InsertBatch(i -> i.getFieldFill() != FieldFill.UPDATE));
//        methodList.add(new AlwaysUpdateSomeColumnById(i -> i.getFieldFill() != FieldFill.UPDATE));
        methodList.add(new InsertBatchMethod());
        methodList.add(new UpdateBatchByIdMethod());
        return methodList;
    }
}
package cn.meshed.cloud.mysql;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;

import java.util.function.Predicate;

/**
 * <h1></h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
public class InsertBatch extends InsertBatchSomeColumn {

    public InsertBatch() {
    }

    public InsertBatch(Predicate<TableFieldInfo> predicate) {
        super(predicate);
    }
}

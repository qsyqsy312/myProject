package com.test.util;


import com.test.model.User;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;

@Component
public class UserShardingAlgorithm implements PreciseShardingAlgorithm<Date> {



    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Date> shardingValue) {
        Date date = shardingValue.getValue();
        DateTimeFormatter yyyyMM = DateTimeFormat.forPattern("yyyyMM");
        String dateStr = new DateTime(date).toString(yyyyMM);


        String tableName = User.TABLE_NAME + "_" + dateStr;
        if (availableTargetNames.contains(tableName)) {
            return tableName;
        }

        throw new RuntimeException("分表错误！表名称不存在！");
    }


}

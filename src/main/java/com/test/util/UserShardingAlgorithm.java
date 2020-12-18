package com.test.util;


import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import com.test.model.User;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Component
public class UserShardingAlgorithm implements PreciseShardingAlgorithm<Date>, RangeShardingAlgorithm<Date> {


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


    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<Date> shardingValue) {
        List<String> tableNames = Lists.newArrayList();
        Range<Date> valueRange = shardingValue.getValueRange();
        if (valueRange.hasLowerBound() && valueRange.hasUpperBound()) {
            DateTime startTime = new DateTime(valueRange.lowerEndpoint());
            DateTime endTime = new DateTime(valueRange.upperEndpoint());
            if (Period.fieldDifference(startTime.toLocalDateTime(), endTime.toLocalDateTime()).getMonths() > 1) {
                throw new RuntimeException("查询日期不得超过两个月");
            }
            while (startTime.compareTo(endTime)<=0){
                tableNames.add(User.TABLE_NAME+"_"+startTime.toString("yyyyMM"));
                startTime = startTime.plusMonths(1);
            }
        }

        if (!tableNames.isEmpty()) {
            return tableNames;
        }
        throw new RuntimeException("分表错误！表名称不存在！");
    }
}

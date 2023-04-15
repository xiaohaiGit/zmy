package com.zmy.zmyserver.modle;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AirsAnalyseParams {
    private int categorize;
    private int interval;

    private String timeRange;

    private Date startDate;
    private Date endDate;

    public void processTimeRange() {

        if (!StringUtils.hasLength(timeRange)) {
            return;
        }

        String[] split = timeRange.split("-");
        DateTime parse1 = DateUtil.parse(split[0] + " 00:00:00");
        startDate = parse1.toJdkDate();

        DateTime parse2 = DateUtil.parse(split[1] + " 23:59:59");
        endDate = parse2.toJdkDate();

    }


}

package com.ytpay.systemwebmagic.task;

import com.ytpay.systemwebmagic.processor.meituan.com.MeiShiDetailProcessor;
import com.ytpay.systemwebmagic.processor.meituan.com.MeiShiDetailProcessorSecond;
import com.ytpay.systemwebmagic.processor.meituan.com.MeiShiDetailProcessorThird;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author ws
 * @date 2020/7/8
 */
@Component
public class MeiTuanTask {

    Logger log = LoggerFactory.getLogger(MeiTuanTask.class);

    @Autowired
    MeiShiDetailProcessor meiShiDetailProcessor;

    @Autowired
    MeiShiDetailProcessorSecond meiShiDetailProcessorSecond;

    @Autowired
    MeiShiDetailProcessorThird meiShiDetailProcessorThird;

    int count = 1;

//    @Scheduled(cron = "* * * * * ?")
    public void catchMeiTuan() {
        if (count % 3 == 1) {
            log.info("meiShiDetailProcessor start");
            meiShiDetailProcessor.startCatchAll();
        } else if (count % 3 == 2) {
            log.info("meiShiDetailProcessorSecond start");
            meiShiDetailProcessorSecond.startCatchAll();
        } else {
            log.info("meiShiDetailProcessorThird start");
            meiShiDetailProcessorThird.startCatchAll();
        }
        count++;
    }

}

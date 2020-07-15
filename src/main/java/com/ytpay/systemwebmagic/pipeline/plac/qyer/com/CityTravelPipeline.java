package com.ytpay.systemwebmagic.pipeline.plac.qyer.com;

import com.ytpay.systemwebmagic.model.plac.qyer.com.CrawlerTravelDetail;
import org.apache.commons.collections.CollectionUtils;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

/**
 * @author ws
 * @date 2020/7/1
 */
public class CityTravelPipeline implements Pipeline {

    @Override
    public void process(ResultItems resultItems, Task task) {

        List<CrawlerTravelDetail> list = resultItems.get("list");
        if (CollectionUtils.isNotEmpty(list)) {
            //保存到数据库
        }

    }

}

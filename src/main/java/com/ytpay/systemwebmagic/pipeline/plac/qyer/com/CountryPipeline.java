package com.ytpay.systemwebmagic.pipeline.plac.qyer.com;

import com.ytpay.systemwebmagic.model.plac.qyer.com.QyCity;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

/**
 * @author ws
 * @date 2020/7/1
 */
@Component
public class CountryPipeline implements Pipeline {

    @Override
    public void process(ResultItems resultItems, Task task) {
        List<QyCity> list = resultItems.get("countryList");
        if (CollectionUtils.isNotEmpty(list)) {
            //保存到数据库
        }

    }

}

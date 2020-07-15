package com.ytpay.systemwebmagic.processor.plac.qyer.com;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.ytpay.systemwebmagic.model.plac.qyer.com.CrawlerTravelDetail;
import org.assertj.core.util.Lists;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.HttpRequestBody;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.JsonPathSelector;
import us.codecraft.webmagic.utils.HttpConstant;

import java.util.List;
import java.util.Map;

/**
 * @author ws
 * @date 2020/7/1
 */
public class CityTravelPageProcessor implements PageProcessor {

    private Site site = Site.me()
            .setDomain("place.qyer.com")
            .setSleepTime(1000)
            .setRetryTimes(3)
            .setTimeOut(30000)
            .setUserAgent("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:77.0) Gecko/20100101 Firefox/77.0");

    @Override
    public void process(Page page) {
        List<String> list = new JsonPathSelector("$.data.list").selectList(page.getRawText());

        List<CrawlerTravelDetail> poList = Lists.newArrayList();

        for (String json : list) {
            JSONObject ob = JSONObject.parseObject(json);
            CrawlerTravelDetail po = new CrawlerTravelDetail();
            po.setCnname(null != ob.get("cnname") ? ob.getString("cnname") : "");
            po.setRefId(null != ob.get("id") ? ob.getLong("id") : 0);
            po.setEnname(null != ob.get("enname") ? ob.getString("enname") : "");
            po.setGrade(null != ob.get("grade") ? ob.getString("grade") : "");
            po.setRank(null != ob.get("rank") ? ob.getInteger("rank") : null);
            po.setCommentCount(null != ob.get("commentCount") ? ob.getInteger("commentCount") : null);
            po.setUrl(null != ob.get("url") ? ob.getString("url") : "");
            po.setStatus(0);

            poList.add(po);
        }

        page.putField("list", poList);
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        String url = "https://place.qyer.com/poi.php";
        Map<String, Object> paraMap = Maps.newHashMap();
        paraMap.put("action", "list_json");
        paraMap.put("type", "city");
        paraMap.put("page", 1);
        paraMap.put("pid", 50);
        paraMap.put("sort", 32);

        Request request = new Request();
        request.setUrl(url);
        request.setRequestBody(HttpRequestBody.form(paraMap, "UTF-8"));
        request.setMethod(HttpConstant.Method.POST);
        Spider.create(new CityTravelPageProcessor())
                .addRequest(request)
                .addPipeline(new ConsolePipeline())
                .run();
    }
}

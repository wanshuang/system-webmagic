package com.ytpay.systemwebmagic.processor.meituan.com;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.ytpay.systemwebmagic.model.meituan.com.RestaurantVo;
import com.ytpay.systemwebmagic.pipeline.meituan.com.RestaurantDetailPipeline;
import com.ytpay.systemwebmagic.pipeline.meituan.com.RestaurantPipeline;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;
import java.util.Objects;

/**
 * @author ws
 * @date 2020/7/1
 */
@Component
public class MeiShiSearchProcessor implements PageProcessor {


    @Autowired
    private RestaurantPipeline restaurantPipeline;

    //默认分页用
    private Integer limit = 32;

    private Integer offset = 0;

    //请动态调整（cookie uuid通过cookie中获取）
    private final String uuid = "65ff394b13eb4abab4cb.1594870108.1.0.0";

    private static final String HEADER_COOKIE = "_lxsdk_cuid=165b2a989d39-0b17deed584c07-75276752-1fa400-165b2a989d4c8; ci=1; rvct=1%2C332; _hc.v=e8b41556-3f9c-19ad-2e2c-5beb9be7b127.1593505769; lsu=; uuid=65ff394b13eb4abab4cb.1594870108.1.0.0; lat=39.65393; lng=116.619825; _lxsdk=165b2a989d39-0b17deed584c07-75276752-1fa400-165b2a989d4c8; _lxsdk_s=17356f01291-fe7-1e-bf5%7C%7C232";

    private String search = "麦当劳";

    //动态分页地址

    private String getSearchUrl(){
        return "https://bj.meituan.com/s/" + search + "/";
    }

    private String getLimitUrl(int offset) {
        return "https://apimobile.meituan.com/group/v4/poi/pcsearch/1?uuid="
                + uuid + "&userid=-1&limit=" + limit + "&offset=" + offset + "&cateId=-1&q=" + search;
    }

    private boolean init = false;

    //修改旧数据标识
    private boolean update = false;

    private static String URL_HTTPS = "https://bj.meituan.com";

    private static String URL_HTTPS_MEISHI_SEARCH = "https://apimobile.meituan.com/group/v4/poi/pcsearch/1?uuid=65ff394b13eb4abab4cb.1594870108.1.0.0&userid=-1&limit=32&offset=0&cateId=-1&q=麦当劳";


    private String UserAgentChrome = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36";

    private String UserAgentFireFox = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:78.0) Gecko/20100101 Firefox/78.0";

    private String UserAgentSafari = "User-Agent:Mozilla/5.0 (iPhone; CPU iPhone OS 5_0 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Version/5.1 Mobile/9A334 Safari/7534.48.3";

    private Site site = Site.me()
            .setDomain(URL_HTTPS)
            .setSleepTime(500)
            .setRetryTimes(1)
            .setTimeOut(30000)
//            .addCookie("__mta","245849403.1593505730272.1594347367396.1594870400196.27")
//            .addCookie("_hc.v","e8b41556-3f9c-19ad-2e2c-5beb9be7b127.1593505769")
//            .addCookie("_lxsdk","165b2a989d39-0b17deed584c07-75276752-1fa400-165b2a989d4c8")
//            .addCookie("_lxsdk_cuid","165b2a989d39-0b17deed584c07-75276752-1fa400-165b2a989d4c8")
//            .addCookie("_lxsdk_s","17356f01291-fe7-1e-bf5||1")
//            .addCookie("ci","1")
//            .addCookie("client-id","6d19d5dc-3b36-4909b09a-87e6931ef79b")
//            .addCookie("lat","39.65393")
//            .addCookie("lng","116.619825")
//            .addCookie("lsu","")
//            .addCookie("rvct","1,332")
//            .addCookie("uuid","65ff394b13eb4abab4cb.1594870108.1.0.0")

//            .addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
//            .addHeader("Cache-Control","max-age=0")
//            .addHeader("Host","bj.meituan.com")

            //翻页的header
            .addHeader("Accept", "*/*")
            .addHeader("Host", "apimobile.meituan.com")
            .addHeader("Origin", URL_HTTPS)

            //需要动态添加
            .addHeader("Referer", URL_HTTPS + "/s/" + search + "/")

            //通用
            .addHeader("Accept-Encoding", "gzip, deflate, br")
            .addHeader("Accept-Language", "en-US,en;q=0.5")
            .addHeader("Upgrade-Insecure-Requests", "1")
            .addHeader("Connection", "keep-alive")
            .addHeader("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:78.0) Gecko/20100101 Firefox/78.0")
            .addHeader("Cookie", HEADER_COOKIE)
            .setUserAgent(UserAgentFireFox);


    @Override
    public void process(Page page) {
        JSONObject result = (JSONObject) JSONObject.parse(page.getRawText());
        if (Objects.nonNull(result)) {
            JSONObject data = result.getJSONObject("data");

            //完成初始化分页
            if (init) {
                Integer totalCount = data.getIntValue("totalCount");
                List<String> addUrl = Lists.newArrayList();
                for (int index = offset + limit; index < totalCount; index = index + limit) {
                    addUrl.add(getLimitUrl(index));
                }
                if (CollectionUtils.isNotEmpty(addUrl)) {
                    page.addTargetRequests(addUrl);
                }
                //分页处理
                init = false;
            }

            List<RestaurantVo> restaurantList = Lists.newArrayList();

            //数据进行解析
            JSONArray array = data.getJSONArray("searchResult");
            if(!array.isEmpty()){
                for (int index = 0; index < array.size(); index++) {
                    JSONObject poi = array.getJSONObject(index);
                    if (!poi.isEmpty()) {
                        RestaurantVo restaurant = new RestaurantVo();
                        restaurant.setAdsClickUrl(poi.getString("adsClickUrl"));
                        restaurant.setHasAds(poi.getBoolean("hasAds"));
                        restaurant.setAddress(poi.getString("address"));
                        restaurant.setAvgScore(poi.getBigDecimal("avgscore"));
                        restaurant.setAllCommentNum(poi.getInteger("comments"));
                        restaurant.setAdsShowUrl(poi.getString("adsShowUrl"));
                        restaurant.setFrontImg(poi.getString("imageUrl"));
                        restaurant.setAvgPrice(poi.getInteger("avgprice"));
                        restaurant.setPoiId(poi.getInteger("id"));
                        restaurant.setTitle(poi.getString("title"));
                        restaurantList.add(restaurant);
                    }
                }
            }

            page.putField("restaurantList", restaurantList);
            page.putField("update", update);

        }
    }


    @Override
    public Site getSite() {
        return site;
    }

    public MeiShiSearchProcessor setSearch(String search) {
        this.search = search;
        return this;
    }

    public MeiShiSearchProcessor setInit(boolean init) {
        this.init = init;
        return this;
    }


    public void doSearch() {
        this.site.addHeader("Referer", getSearchUrl());
        Spider.create(new MeiShiSearchProcessor().setInit(true))
                .addUrl(getLimitUrl(offset))
                .addPipeline(restaurantPipeline)
                .addPipeline(new ConsolePipeline())
                .run();
    }

    public static void main(String args[]) {
        Spider.create(new MeiShiSearchProcessor().setInit(true))
                .addUrl(URL_HTTPS_MEISHI_SEARCH)
                .addPipeline(new ConsolePipeline())
                .run();
    }


}

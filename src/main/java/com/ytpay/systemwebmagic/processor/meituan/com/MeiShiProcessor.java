package com.ytpay.systemwebmagic.processor.meituan.com;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.ytpay.systemwebmagic.model.meituan.com.RestaurantVo;
import com.ytpay.systemwebmagic.pipeline.meituan.com.RestaurantPipeline;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

/**
 * @author ws
 * @date 2020/7/1
 */
@Component
public class MeiShiProcessor implements PageProcessor {

    private static String URL_HTTPS = "https://bj.meituan.com/meishi/";

    private static String URL_HTTP = "http://bj.meituan.com/meishi/";

    private static String URL_MEISHI = "https://bj.meituan.com/meishi/c40/pn1/";

    private static String UserAgentChrome = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36";

    private static String UserAgentFireFox = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:77.0) Gecko/20100101 Firefox/77.0";

    @Autowired
    private MeiShiProcessor meiShiProcessor;

    private boolean update = false;

    private boolean filter = true;

    private final Integer maxPage = 67;

    @Autowired
    private RestaurantPipeline restaurantPipeline;

    private Site site = Site.me()
            .setDomain(URL_HTTPS)
            .setSleepTime(1000)
            .setRetryTimes(3)
            .setTimeOut(30000)
            .setUserAgent(UserAgentFireFox);


    @Override
    public void process(Page page) {
        List<Selectable> list = page.getHtml().xpath("//script").nodes();
        for (Selectable selectable : list) {
            if (selectable.toString().indexOf("window._appState") > 0) {
                //清理标签
                String content = selectable.toString().replace("<script>", "").replace("</script>", "").replace("window._appState = ", "");
                //去掉分号;
                content = content.substring(0, content.length() - 1);
                JSONObject json = (JSONObject) JSONObject.parse(content);

                //添加所有地址信息
                if (filter) {
                    //主页所有分页
                    List<String> addMainPages = Lists.newArrayList();
                    for (int p = 1; p <= maxPage; p++) {
                        addMainPages.add(page.getUrl().toString() + "pn" + p + "/");
                    }
                    page.addTargetRequests(addMainPages);

                    JSONObject filters = json.getJSONObject("filters");
                    //获取全部地区
                    JSONArray areas = filters.getJSONArray("areas");
                    //如果此页有数据
                    if (!areas.isEmpty()) {
                        //解析区域
                        for (int index = 0; index < areas.size(); index++) {
                            JSONObject area = areas.getJSONObject(index);
                            //子区域
                            JSONArray subAreas = area.getJSONArray("subAreas");
                            if (!subAreas.isEmpty()) {
                                for (int indexSub = 0; indexSub < subAreas.size(); indexSub++) {
                                    JSONObject subArea = subAreas.getJSONObject(indexSub);
                                    String subAreaUrl = subArea.getString("url");
                                    //添加所有分页
                                    if (StringUtils.isNotBlank(subAreaUrl)) {
                                        List<String> addPages = Lists.newArrayList();
                                        for (int p = 1; p <= maxPage; p++) {
                                            if (p > 1) {
                                                addPages.add(subAreaUrl + "pn" + p + "/");
                                            } else {
                                                addPages.add(subAreaUrl);
                                            }
                                        }
                                        page.addTargetRequests(addPages);
                                    }
                                }
                            }
                        }
                    }

                    //美食分类
                    JSONArray cates = filters.getJSONArray("cates");
                    //如果此页有数据
                    if (!cates.isEmpty()) {
                        //解析美食分类
                        for (int index = 0; index < areas.size(); index++) {
                            JSONObject cate = cates.getJSONObject(index);
                            String cateUrl = cate.getString("url");
                            //添加所有分页
                            if (StringUtils.isNotBlank(cateUrl)) {
                                List<String> addPages = Lists.newArrayList();
                                for (int p = 1; p <= maxPage; p++) {
                                    if (p > 1) {
                                        addPages.add(cateUrl + "pn" + p + "/");
                                    } else {
                                        addPages.add(cateUrl);
                                    }
                                }
                                page.addTargetRequests(addPages);
                            }
                        }
                    }
                    //所有页面已经添加
                    filter = false;
                }

                //饭店信息
                JSONObject poiLists = json.getJSONObject("poiLists");
                JSONArray poiList = poiLists.getJSONArray("poiInfos");
                List<RestaurantVo> restaurantList = Lists.newArrayList();

                //如果此页有数据
                if (!poiList.isEmpty()) {
                    //解析
                    for (int index = 0; index < poiList.size(); index++) {
                        JSONObject poi = poiList.getJSONObject(index);
                        if (!poi.isEmpty()) {
                            RestaurantVo restaurant = new RestaurantVo();
                            restaurant.setAdsClickUrl(poi.getString("adsClickUrl"));
                            restaurant.setHasAds(poi.getBoolean("hasAds"));
                            restaurant.setAddress(poi.getString("address"));
                            restaurant.setAvgScore(poi.getBigDecimal("avgScore"));
                            restaurant.setAllCommentNum(poi.getInteger("allCommentNum"));
                            restaurant.setAdsShowUrl(poi.getString("adsShowUrl"));
                            restaurant.setFrontImg(poi.getString("frontImg"));
                            restaurant.setAvgPrice(poi.getInteger("avgPrice"));
                            restaurant.setPoiId(poi.getInteger("poiId"));
                            restaurant.setTitle(poi.getString("title"));
                            restaurantList.add(restaurant);
                        }
                    }
                }

                page.putField("restaurantList", restaurantList);
                page.putField("update", update);

                //翻页 页码
//                Integer pn = json.getInteger("pn");
//                String nextPage = "";
//                if(pn < maxPage){
//                    //分页
//                    //https://bj.meituan.com/meishi/b14/pn1 地区格式
//                    //https://bj.meituan.com/meishi/c17/pn1 美食分类格式
//                    if(Pattern.matches("^("+URL_HTTPS+")b\\d+/pn\\d+/$" ,page.getUrl().toString()) ||
//                            Pattern.matches("^("+URL_HTTP+")b\\d+/pn\\d+/$" ,page.getUrl().toString())){
//                        //地区分页
//                        String pageUrl = page.getUrl().toString();
//                        nextPage = pageUrl.substring(0, pageUrl.indexOf("/pn")) + "/pn" + (pn + 1) + "/";
//                    }else if(Pattern.matches("^("+URL_HTTPS+")c\\d+/pn\\d+/$" ,page.getUrl().toString()) ||
//                            Pattern.matches("^("+URL_HTTP+")c\\d+/pn\\d+/$" ,page.getUrl().toString())){
//                        //美食分类分页
//                        String pageUrl = page.getUrl().toString();
//                        nextPage = pageUrl.substring(0, pageUrl.indexOf("/pn")) + "/pn" + (pn + 1) + "/";
//
//                    }else{
//                        nextPage = URL_HTTPS + "pn" + (pn + 1) + "/";
//                    }
//                    page.putField("restaurantList", restaurantList);
//                    page.putField("update", update);
//                    page.addTargetRequest(nextPage);
//                }
            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public void startCatch(Boolean update) {
        Spider.create(meiShiProcessor.setUpdate(update != null ? update : false))
                .addUrl(URL_HTTPS)
                .addPipeline(restaurantPipeline)
                .addPipeline(new ConsolePipeline())
                .run();
    }

    public static void main(String args[]) {
        Spider.create(new MeiShiProcessor())
                .addUrl(URL_HTTPS)
                .addPipeline(new RestaurantPipeline())
                .addPipeline(new ConsolePipeline())
                .run();
    }

    public MeiShiProcessor setUpdate(boolean update) {
        this.update = update;
        return this;
    }
}

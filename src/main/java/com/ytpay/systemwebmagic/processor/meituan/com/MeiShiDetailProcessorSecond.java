package com.ytpay.systemwebmagic.processor.meituan.com;

import com.alibaba.fastjson.JSONObject;
import com.ytpay.systemwebmagic.data.entity.MeiTuanRestaurant;
import com.ytpay.systemwebmagic.data.repository.MeiTuanRestaurantRepository;
import com.ytpay.systemwebmagic.model.meituan.com.RestaurantDetailVo;
import com.ytpay.systemwebmagic.pipeline.meituan.com.RestaurantDetailPipeline;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;
import java.util.Random;

/**
 * @author ws
 * @date 2020/7/1
 */
@Component
public class MeiShiDetailProcessorSecond implements PageProcessor {


    private static String URL_HTTPS = "https://bj.meituan.com/meishi/";

    private static String URL_HTTPS_MEISHI_DETAIL = "https://bj.meituan.com/meishi/160174985/";

    private boolean init = true;

    @Autowired
    private MeiShiDetailProcessorSecond meiShiDetailProcessor;

    @Autowired
    private RestaurantDetailPipeline restaurantDetailPipeline;

    @Autowired
    private MeiTuanRestaurantRepository meiTuanRestaurantRepository;

    private Random random = new Random();

    private String UserAgentChrome = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36";

    private String UserAgentFireFox = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:77.0) Gecko/20100101 Firefox/77.3";

    private String UserAgentSafari = "User-Agent:Mozilla/5.0 (iPhone; CPU iPhone OS 5_0 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Version/5.1 Mobile/9A334 Safari/7534.48.3";

    private Site site = Site.me()
            .setDomain(URL_HTTPS)
            .setSleepTime(500)
            .setRetryTimes(1)
            .setTimeOut(30000)
            .setUserAgent(UserAgentChrome);


    @Override
    public void process(Page page) {
        if (init) {
            List<MeiTuanRestaurant> restaurants = meiTuanRestaurantRepository.findAllByPoiIdIsNotNullAndPhoneIsNull();
            for (int x = restaurants.size() - 1; x > 0; x--) {
                MeiTuanRestaurant res = restaurants.get(x);
                String url = URL_HTTPS + res.getPoiId() + "/";
                page.addTargetRequest(url);
            }
//            restaurants.forEach(res -> {
//                String url = URL_HTTPS + res.getPoiId() + "/";
//                page.addTargetRequest(url);
//            });
            init = false;
        } else {
            List<Selectable> list = page.getHtml().xpath("//script").nodes();
            for (Selectable selectable : list) {
                //解析内容范例 https://bj.meituan.com/meishi/97479834/
                if (selectable.toString().indexOf("window._appState") > 0 &&
                        selectable.toString().indexOf("detailInfo") > 0) {
                    //清理标签
                    String content = selectable.toString().replace("<script>", "").replace("</script>", "").replace("window._appState = ", "");
                    //去掉分号;
                    content = content.substring(0, content.length() - 1);
                    JSONObject json = (JSONObject) JSONObject.parse(content);
                    //解析 店铺详情页
                    JSONObject detailInfo = json.getJSONObject("detailInfo");
                    if (ObjectUtils.isNotEmpty(detailInfo)) {
                        RestaurantDetailVo detailVo = new RestaurantDetailVo();
                        detailVo.setIsMeishi(detailInfo.getBoolean("isMeishi"));
                        detailVo.setBrandName(detailInfo.getString("brandName"));
                        detailVo.setAddress(detailInfo.getString("address"));
                        detailVo.setHasFoodSafeInfo(detailInfo.getBoolean("hasFoodSafeInfo"));
                        detailVo.setAvgPrice(detailInfo.getInteger("avgPrice"));
                        detailVo.setLatitude(detailInfo.getBigDecimal("latitude"));
                        detailVo.setShowStatus(detailInfo.getInteger("showStatus"));
                        detailVo.setAvgScore(detailInfo.getBigDecimal("avgScore"));
                        detailVo.setPhone(detailInfo.getString("phone"));
                        detailVo.setBrandId(detailInfo.getInteger("brandId"));
                        detailVo.setName(detailInfo.getString("name"));
                        detailVo.setPoiId(detailInfo.getInteger("poiId"));
                        detailVo.setOpenTime(detailInfo.getString("openTime"));
                        detailVo.setLongitude(detailInfo.getBigDecimal("longitude"));
                        page.putField("detailVo", detailVo);
                    }
                }
                //解析内容范例 https://bj.meituan.com/meishi/52277537/
                else if (selectable.toString().indexOf("window.AppData") > 0 &&
                        selectable.toString().indexOf("poiInfo") > 0) {
                    //清理标签
                    String content = selectable.toString().replace("<script>", "").replace("</script>", "").replace("window.AppData = ", "");
                    //去掉分号;
                    content = content.substring(0, content.length() - 1);
                    JSONObject json = (JSONObject) JSONObject.parse(content);
                    //解析 店铺详情页
                    JSONObject poiInfo = json.getJSONObject("poiInfo");
                    if (ObjectUtils.isNotEmpty(poiInfo)) {
                        RestaurantDetailVo detailVo = new RestaurantDetailVo();
                        detailVo.setBrandName(poiInfo.getString("brandName"));
                        detailVo.setAddress(poiInfo.getString("address"));
                        detailVo.setAvgPrice(poiInfo.getInteger("avgPrice"));
                        detailVo.setLatitude(poiInfo.getBigDecimal("lat"));
                        detailVo.setPhone(poiInfo.getString("phone"));
                        detailVo.setBrandId(poiInfo.getInteger("brandId"));
                        detailVo.setName(poiInfo.getString("name"));
                        detailVo.setPoiId(poiInfo.getInteger("id"));
                        detailVo.setOpenTime(poiInfo.getString("openTime"));
                        detailVo.setLongitude(poiInfo.getBigDecimal("lng"));
                        page.putField("detailVo", detailVo);
                    }
                }
            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public void startCatchAll() {
        Spider.create(meiShiDetailProcessor.setInit(true))
                .addUrl(URL_HTTPS)
                .addPipeline(restaurantDetailPipeline)
                .addPipeline(new ConsolePipeline())
                .run();
    }

    public void startCatchById(Integer poiId) {
        String url = URL_HTTPS + poiId + "/";
        Spider.create(meiShiDetailProcessor.setInit(false))
                .addUrl(url)
                .addPipeline(restaurantDetailPipeline)
                .addPipeline(new ConsolePipeline())
                .run();
    }

    public static void main(String args[]) {
        Spider.create(new MeiShiDetailProcessorSecond().setInit(false))
                .addUrl(URL_HTTPS_MEISHI_DETAIL)
                .addPipeline(new RestaurantDetailPipeline())
                .addPipeline(new ConsolePipeline())
                .run();
    }

    public MeiShiDetailProcessorSecond setInit(boolean init) {
        this.init = init;
        return this;
    }

}

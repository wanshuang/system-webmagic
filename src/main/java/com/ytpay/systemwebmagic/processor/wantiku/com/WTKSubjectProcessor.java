package com.ytpay.systemwebmagic.processor.wantiku.com;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.ytpay.systemwebmagic.model.wantiku.com.SubjectVo;
import com.ytpay.systemwebmagic.pipeline.wantiku.com.SubjectParentPipeline;
import com.ytpay.systemwebmagic.pipeline.wantiku.com.SubjectPipeline;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

/**
 * @author ws
 * @date 2020/7/1
 */
@Component
public class WTKSubjectProcessor implements PageProcessor {

//    https://api.566.com/api/User/UserSubjectNew?SubjectLevel=0&time=16012891635087

    private static String URL_HTTPS = "https://api.566.com/api/User/UserSubjectNew";

    private static String URL_HTTP = "http://api.566.com/api/User/UserSubjectNew";

    private static String HOST = "api.566.com";

    private static String UserAgentIOSFox = "wantiku/4.4.7 (iPhone; iOS 14.0; Scale/3.00)";

    private static String COOKIE = "ASP.NET_SessionId=ftdxenfhcmejbitu5yfhz4gh; __jsluid_s=bb6d61d6fbff62690daca22d74b1b0dc";

    private static Integer MSGCODE_SUCCESS = 1;

    @Autowired
    private WTKSubjectProcessor wtkSubjectProcessor;

    @Autowired
    private SubjectPipeline subjectPipeline;

    private Site site = null;

    private Integer subjectParentId;

    private Integer subjectParentLevel;

    @Override
    public void process(Page page) {
        String rawText = page.getRawText();
        if (StringUtils.isNotBlank(rawText)) {
            JSONObject responseObj = JSONObject.parseObject(rawText);
            System.out.println(responseObj.toJSONString());
            if (MSGCODE_SUCCESS == responseObj.getInteger("MsgCode")) {
                List<SubjectVo> subjects = Lists.newArrayList();
                //项目集合
                JSONArray SubjectEntities = responseObj.getJSONArray("SubjectEntities");
                System.out.println("projects:" + SubjectEntities.toJSONString());
                if (SubjectEntities != null && !SubjectEntities.isEmpty()) {
                    for (int indexS = 0; indexS < SubjectEntities.size(); indexS++) {
                        JSONObject subjectObject = SubjectEntities.getJSONObject(indexS);
                        SubjectVo subjectVo = JSONObject.parseObject(subjectObject.toJSONString(), SubjectVo.class);
                        subjectVo.setSubjectParentId(this.subjectParentId);
                        subjectVo.setSubjectParentLevel(this.subjectParentLevel);
                        subjects.add(subjectVo);
                    }
                }
                page.putField("subjectList", subjects);
            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public void startCatch(Integer subjectParentId, Integer subjectLevel) {
        String domain = URL_HTTPS + "?SubjectLevel=" + subjectLevel + "&time=" + System.currentTimeMillis();
        setSite(subjectParentId, subjectLevel, domain);
        Spider.create(wtkSubjectProcessor)
                .addUrl(domain)
                .addPipeline(subjectPipeline)
                .addPipeline(new ConsolePipeline())
                .run();

    }

    private void setSite(Integer subjectParentId, Integer subjectLevel, String domain) {
        this.subjectParentId = subjectParentId;
        this.subjectParentLevel = subjectLevel;
        this.site = Site.me()
                .setDomain(domain)
                .setSleepTime(500)
                .setRetryTimes(1)
                .setTimeOut(30000)
                //翻页的header
                .addHeader("Accept", "*/*")
                .addHeader("Host", HOST)
                .addHeader("Origin", URL_HTTPS)
                .addHeader("SubjectParentID", subjectParentId + "")
                .addHeader("SubjectLevel", subjectLevel + "")


                //通用
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .addHeader("Accept-Language", "zh-Hans-US;q=1, en-US;q=0.9, zh-Hant-US;q=0.8")
                .addHeader("Connection", "keep-alive")
                .addHeader("Cookie", COOKIE)
                .setUserAgent(UserAgentIOSFox);
    }

    public static void main(String args[]) {
//        subjectParentId = 515;
//        String domain = URL_HTTPS + "?SubjectLevel=" + 0 + "&time=" + System.currentTimeMillis();
//        site = Site.me()
//                .setDomain(domain)
//                .setSleepTime(500)
//                .setRetryTimes(1)
//                .setTimeOut(30000)
//                //翻页的header
//                .addHeader("Accept", "*/*")
//                .addHeader("Host", HOST)
//                .addHeader("Origin", URL_HTTPS)
////                .addHeader("Token", "20200923175247-41c309f977f592cede27b4dd301f59d6")
//                .addHeader("SubjectParentID", "515")
//                .addHeader("SubjectLevel", "1")
//
//                //通用
//                .addHeader("Accept-Encoding", "gzip, deflate, br")
//                .addHeader("Accept-Language", "zh-Hans-US;q=1, en-US;q=0.9, zh-Hant-US;q=0.8")
//                .addHeader("Connection", "keep-alive")
//                .addHeader("Cookie", COOKIE)
//                .setUserAgent(UserAgentIOSFox);

        //需要设置site
        Spider.create(new WTKSubjectProcessor())
                .addUrl(URL_HTTPS + System.currentTimeMillis())
                .addPipeline(new SubjectParentPipeline())
                .addPipeline(new ConsolePipeline())
                .run();
    }

}

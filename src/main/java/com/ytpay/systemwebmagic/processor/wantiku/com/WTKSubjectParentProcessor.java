package com.ytpay.systemwebmagic.processor.wantiku.com;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.ytpay.systemwebmagic.model.wantiku.com.SubjectParentVo;
import com.ytpay.systemwebmagic.pipeline.wantiku.com.SubjectParentPipeline;
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
public class WTKSubjectParentProcessor implements PageProcessor {

    private static String URL_HTTPS = "https://api.566.com/api/ExamSubject/GetAllSubjects?time=";

    private static String URL_HTTP = "http://api.566.com/api/ExamSubject/GetAllSubjects?time=";

    private static String HOST = "api.566.com";

    private static String UserAgentIOSFox = "wantiku/4.4.7 (iPhone; iOS 14.0; Scale/3.00)";

    private static String COOKIE = "ASP.NET_SessionId=ftdxenfhcmejbitu5yfhz4gh; __jsluid_s=bb6d61d6fbff62690daca22d74b1b0dc";

    private static Integer MSGCODE_SUCCESS = 1;

    private static Integer USER_ID = 32449786;

    @Autowired
    private WTKSubjectParentProcessor wtkSubjectParentProcessor;

    @Autowired
    private SubjectParentPipeline subjectParentPipeline;

    private Site site = Site.me()
            .setDomain(URL_HTTPS + System.currentTimeMillis())
            .setSleepTime(500)
            .setRetryTimes(1)
            .setTimeOut(30000)
            //翻页的header
            .addHeader("Accept", "*/*")
            .addHeader("Host", HOST)
            .addHeader("Origin", URL_HTTPS)

            //通用
            .addHeader("Accept-Encoding", "gzip, deflate, br")
            .addHeader("Accept-Language", "zh-Hans-US;q=1, en-US;q=0.9, zh-Hant-US;q=0.8")
            .addHeader("Connection", "keep-alive")
            .addHeader("Cookie", COOKIE)
            .setUserAgent(UserAgentIOSFox);


    @Override
    public void process(Page page) {
        String rawText = page.getRawText();
        if (StringUtils.isNotBlank(rawText)) {
            JSONObject responseObj = JSONObject.parseObject(rawText);
            System.out.println(responseObj.toJSONString());
            if (MSGCODE_SUCCESS == responseObj.getInteger("MsgCode")) {
                List<SubjectParentVo> subjectParents = Lists.newArrayList();
                //项目大类
                JSONArray projects = responseObj.getJSONArray("SubjectEntities");
                System.out.println("projects:" + projects.toJSONString());
                if (projects != null && !projects.isEmpty()) {
                    for (int indexP = 0; indexP < projects.size(); indexP++) {
                        JSONObject project = projects.getJSONObject(indexP);
                        JSONArray subjectEntities = project.getJSONArray("SubjectEntities");
                        System.out.println("subjects:" + subjectEntities.toJSONString());
                        if (subjectEntities != null && !subjectEntities.isEmpty()) {
                            for (int indexS = 0; indexS < subjectEntities.size(); indexS++) {
                                JSONObject entity = subjectEntities.getJSONObject(indexS);
                                SubjectParentVo subjectParentVo = JSONObject.parseObject(entity.toJSONString(), SubjectParentVo.class);
                                subjectParents.add(subjectParentVo);
                            }
                        }
                    }
                }
                page.putField("subjectParentList", subjectParents);
            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public void startCatch() {
        Spider.create(wtkSubjectParentProcessor)
                .addUrl(URL_HTTPS + System.currentTimeMillis())
                .addPipeline(subjectParentPipeline)
                .addPipeline(new ConsolePipeline())
                .run();
    }

    public static void main(String args[]) {
        Spider.create(new WTKSubjectParentProcessor())
                .addUrl(URL_HTTPS + System.currentTimeMillis())
                .addPipeline(new SubjectParentPipeline())
                .addPipeline(new ConsolePipeline())
                .run();
    }

}

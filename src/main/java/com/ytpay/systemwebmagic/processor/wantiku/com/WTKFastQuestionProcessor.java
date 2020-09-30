package com.ytpay.systemwebmagic.processor.wantiku.com;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.ytpay.systemwebmagic.data.entity.wantiku.com.Subject;
import com.ytpay.systemwebmagic.model.wantiku.com.QuestionVo;
import com.ytpay.systemwebmagic.pipeline.wantiku.com.PagerQuestionPipeline;
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
import java.util.Random;

/**
 * @author ws
 * @date 2020/7/1
 */
@Component
public class WTKFastQuestionProcessor implements PageProcessor {

//  https://api.566.com/APP/exam8/Tiku/FastIntelligentPaper/513/32449786/GetFast/_apple/68e3247ed6bef19e1c04cd6611a5921f

    private static String URL_HTTPS = "https://api.566.com/APP/exam8/Tiku/FastIntelligentPaper";

    private static String URL_HTTP = "http://api.566.com/APP/exam8/Tiku/FastIntelligentPaper";

    private static String HOST = "api.566.com";

    private static String UserAgentIOSFox = "wantiku/4.4.7 (iPhone; iOS 14.0; Scale/3.00)";

    private static String COOKIE = "ASP.NET_SessionId=ftdxenfhcmejbitu5yfhz4gh; __jsluid_s=bb6d61d6fbff62690daca22d74b1b0dc";

    private static Integer MSGCODE_SUCCESS = 1;

    private static Integer USER_ID = 32449786;

    private static Random RANDOM_USER_ID = new Random();

    private static String URL_TOKEN = "68e3247ed6bef19e1c04cd6611a5921f";

    @Autowired
    private WTKFastQuestionProcessor wtkSubjectProcessor;

    @Autowired
    private SubjectPipeline subjectPipeline;

    private static Site site;

    private Subject subject;

    @Override
    public void process(Page page) {
        String rawText = page.getRawText();
        if (StringUtils.isNotBlank(rawText)) {
            JSONObject responseObj = JSONObject.parseObject(rawText);
            System.out.println(responseObj.toJSONString());
            //paper信息
            JSONObject fastIntelligentPaperResult = responseObj.getJSONObject("FastIntelligentPaperResult");
            if (MSGCODE_SUCCESS == fastIntelligentPaperResult.getInteger("ErrorCode")) {
                JSONObject pageEntity = fastIntelligentPaperResult.getJSONObject("PaperEntity");
                JSONArray questionsBasicEntityList = pageEntity.getJSONArray("TKQuestionsBasicEntityList");
                if (questionsBasicEntityList != null && questionsBasicEntityList.size() > 0) {
                    //题集
                    JSONObject questionsBasicEntity = questionsBasicEntityList.getJSONObject(0);
                    JSONArray questionList = questionsBasicEntity.getJSONArray("QuestionsEntityList");
                    List<QuestionVo> questionVos = Lists.newArrayList();
                    for (int questionIndex = 0; questionIndex < questionList.size(); questionIndex++) {
                        //题信息
                        JSONObject questionEntity = questionList.getJSONObject(questionIndex);
                        QuestionVo questionVo = JSONObject.parseObject(questionEntity.toJSONString(), QuestionVo.class);
                        questionVos.add(questionVo);
                    }
                    page.putField("questionList", questionVos);
                }
            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public void startCatch(Subject subject) {
        Integer userId = RANDOM_USER_ID.nextInt(USER_ID);
        String domain = URL_HTTPS + "/" + subject.getSubjectId() + "/" + userId + "/" + URL_TOKEN;
        setSite(subject, userId, domain);
        Spider.create(wtkSubjectProcessor)
                .addUrl(domain)
                .addPipeline(subjectPipeline)
                .addPipeline(new ConsolePipeline())
                .run();

    }

    private void setSite(Subject subject, Integer userId, String domain) {

        this.site = Site.me()
                .setDomain(domain)
                .setSleepTime(500)
                .setRetryTimes(1)
                .setTimeOut(30000)
                //翻页的header
                .addHeader("Accept", "*/*")
                .addHeader("Host", HOST)
                .addHeader("Origin", URL_HTTPS)
                .addHeader("SubjectId", subject.getSubjectId() + "")
                .addHeader("UserId", userId + "")


                //通用
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .addHeader("Accept-Language", "zh-Hans-US;q=1, en-US;q=0.9, zh-Hant-US;q=0.8")
                .addHeader("Connection", "keep-alive")
                .addHeader("Cookie", COOKIE)
                .setUserAgent(UserAgentIOSFox);
    }

    public static void main(String args[]) {
        String domain = "https://api.566.com/APP/exam8/Tiku/FastIntelligentPaper/513/32449786/GetFast/_apple/68e3247ed6bef19e1c04cd6611a5921f";
        site = Site.me()
                .setDomain(domain)
                .setSleepTime(500)
                .setRetryTimes(1)
                .setTimeOut(30000)
                //翻页的header
                .addHeader("Accept", "*/*")
                .addHeader("Host", HOST)
                .addHeader("Origin", URL_HTTPS)
                .addHeader("SubjectId", "513")
                .addHeader("UserId", "32449786")

                //通用
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .addHeader("Accept-Language", "zh-Hans-US;q=1, en-US;q=0.9, zh-Hant-US;q=0.8")
                .addHeader("Connection", "keep-alive")
                .addHeader("Cookie", COOKIE)
                .setUserAgent(UserAgentIOSFox);

        //需要设置site
        Spider.create(new WTKFastQuestionProcessor())
                .addUrl(domain)
                .addPipeline(new PagerQuestionPipeline())
                .addPipeline(new ConsolePipeline())
                .run();
    }

}

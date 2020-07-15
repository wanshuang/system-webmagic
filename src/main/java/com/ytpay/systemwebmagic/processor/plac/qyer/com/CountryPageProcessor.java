package com.ytpay.systemwebmagic.processor.plac.qyer.com;

import com.ytpay.systemwebmagic.model.plac.qyer.com.QyCity;
import com.ytpay.systemwebmagic.pipeline.plac.qyer.com.CountryPipeline;
import org.assertj.core.util.Lists;
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
public class CountryPageProcessor implements PageProcessor {

    private Site site = Site.me()
            .setDomain("place.qyer.com")
            .setSleepTime(1000)
            .setUserAgent("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:77.0) Gecko/20100101 Firefox/77.0");

    @Override
    public void process(Page page) {

        List<Selectable> list = page.getHtml().xpath("//div[@class='place-home-card4']//a").nodes();

        List<QyCity> countryList = Lists.newArrayList();

        for (Selectable selectable : list) {

            Selectable urlsele = selectable.xpath("//a/@href");
            Selectable namesele = selectable.xpath("//a//span/text()");
            Selectable nameEnsele = selectable.xpath("//a//em/text()");
            if (null != urlsele && null != namesele && null != nameEnsele) {
                String url = urlsele.toString();
                String name = namesele.toString().trim();
                String nameEn = nameEnsele.toString().trim();

                String code = url.split("/")[3];
                QyCity country = new QyCity();
                country.setCode(code);
                country.setNameEn(nameEn);
                ;
                country.setName(name);
                country.setUrl(url);
                countryList.add(country);
            }

        }

        page.putField("countryList", countryList);
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        String url = "https://place.qyer.com";
        Spider.create(new CountryPageProcessor())
                .addUrl(url)
                .addPipeline(new ConsolePipeline())
                .addPipeline(new CountryPipeline())
                .run();
    }

}

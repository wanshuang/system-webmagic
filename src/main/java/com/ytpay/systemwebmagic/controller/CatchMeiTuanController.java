package com.ytpay.systemwebmagic.controller;

import com.ytpay.systemwebmagic.processor.meituan.com.MeiShiDetailProcessor;
import com.ytpay.systemwebmagic.processor.meituan.com.MeiShiProcessor;
import com.ytpay.systemwebmagic.processor.meituan.com.MeiShiSearchProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ws
 * @date 2020/7/2
 */
@RestController
@RequestMapping("/catch/meituan")
public class CatchMeiTuanController {

    @Autowired
    MeiShiProcessor meiShiProcessor;

    @Autowired
    MeiShiDetailProcessor meiShiDetailProcessor;

    @Autowired
    MeiShiSearchProcessor meiShiSearchProcessor;

    @RequestMapping(value = "/meishi", method = RequestMethod.GET)
    public String catchMeiShi(@RequestParam(value = "update", required = false) Boolean update) {
        meiShiProcessor.startCatch(update);
        return "success";
    }


    @RequestMapping(value = "/meishi/detail/all", method = RequestMethod.GET)
    public String catchMeiShiDetail() {
        meiShiDetailProcessor.startCatchAll();
        return "success";
    }

    @RequestMapping(value = "/meishi/detail", method = RequestMethod.GET)
    public String catchMeiShiDetailById(@RequestParam(value = "poiId") Integer poiId) {
        meiShiDetailProcessor.startCatchById(poiId);
        return "success";
    }

    @RequestMapping(value = "/meishi/search", method = RequestMethod.GET)
    public String catchMeiShiDetailById(@RequestParam(value = "search") String search) {
        meiShiSearchProcessor.setSearch(search).doSearch();
        return "success";
    }
}

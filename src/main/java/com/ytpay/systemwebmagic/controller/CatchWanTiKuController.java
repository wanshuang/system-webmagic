package com.ytpay.systemwebmagic.controller;

import com.ytpay.systemwebmagic.processor.wantiku.com.WTKSubjectParentProcessor;
import com.ytpay.systemwebmagic.service.wantiku.com.WTKCatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ws
 * @date 2020/7/2
 */
@RestController
@RequestMapping("/catch/wtk")
public class CatchWanTiKuController {

    @Autowired
    WTKSubjectParentProcessor wTKSubjectParentProcessor;

    @Autowired
    WTKCatchService wtkCatchService;

    @RequestMapping(value = "/subject/parent/all", method = RequestMethod.GET)
    public String catchSubjectParent() {
        wTKSubjectParentProcessor.startCatch();
        return "success";
    }

    @RequestMapping(value = "/subject/all", method = RequestMethod.GET)
    public String catchSubject() {
        wtkCatchService.subjectBatchCatch();
        return "success";
    }

    @RequestMapping(value = "/question/all", method = RequestMethod.GET)
    public String catchQuestion() {
        wtkCatchService.questionBatchCatch();
        return "success";
    }

}

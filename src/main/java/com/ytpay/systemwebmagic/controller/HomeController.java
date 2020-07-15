package com.ytpay.systemwebmagic.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ws
 * @date 2020/6/28
 */
@RestController
public class HomeController {

    @RequestMapping("/home")
    public String hello() {
        return "Hello webmagic";
    }

}

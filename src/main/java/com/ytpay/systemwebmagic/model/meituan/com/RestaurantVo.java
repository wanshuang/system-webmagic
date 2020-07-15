package com.ytpay.systemwebmagic.model.meituan.com;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ws
 * @date 2020/7/2
 */
@Data
public class RestaurantVo {

    /**
     * {"status":0,"data":{"status":0}}
     * 广告信息
     */
    private String adsClickUrl;

    /**
     * 是否有广告
     */
    private Boolean hasAds;

    /**
     * 地址
     */
    private String address;

    /**
     * 平均分
     */
    private BigDecimal avgScore;

    /**
     * 所有评论数
     */
    private Integer allCommentNum;

    /**
     * 广告展现地址
     */
    private String adsShowUrl;

    /**
     * 图片地址
     */
    private String frontImg;

    /**
     * 平均消费
     */
    private Integer avgPrice;

    /**
     * 饭店id
     */
    private Integer poiId;

    /**
     * 抬头标题
     */
    private String title;
}

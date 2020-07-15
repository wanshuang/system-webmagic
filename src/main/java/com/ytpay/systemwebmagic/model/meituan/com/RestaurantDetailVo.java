package com.ytpay.systemwebmagic.model.meituan.com;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ws
 * @date 2020/7/6
 */
@Data
public class RestaurantDetailVo {

    /**
     * 是否是美食
     */
    private Boolean isMeishi;

    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 地址
     */
    private String address;

    /**
     * 食品安全档案
     */
    private Boolean hasFoodSafeInfo;

    /**
     * 平均消费
     */
    private Integer avgPrice;

    /**
     * 纬度
     */
    private BigDecimal latitude;


    /**
     * 显示状态
     */
    private Integer showStatus;

    /**
     * 平均分
     */
    private BigDecimal avgScore;

    /**
     * 联系电话
     */
    private String phone;


    /**
     * 品牌ID
     */
    private Integer brandId;

    /**
     * 名称
     */
    private String name;

    /**
     * 唯一ID
     */
    private Integer poiId;

    /**
     * 营业时间
     */
    private String openTime;

    /**
     * 经度
     */
    private BigDecimal longitude;


}

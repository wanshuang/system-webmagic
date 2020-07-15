package com.ytpay.systemwebmagic.data.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * @author ws
 * @date 2020/7/3
 */
@Entity
@Table(name = "t_meituan_restaurant")
@Accessors(chain = true)
@Data
public class MeiTuanRestaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    /**
     * 地址
     */
    private String address;

    /**
     * 平均分
     */
    private Double avgScore;

    /**
     * 所有评论数
     */
    private Long allCommentNum;

    /**
     * 图片地址
     */
    private String frontImg;

    /**
     * 平均消费
     */
    private Double avgPrice;


    /**
     * 美团美食id
     */
    private Long poiId;

    /**
     * 抬头标题店铺名称
     */
    private String title;

    private Long createTime;

    private Long updateTime;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 食品安全档案
     */
    private Boolean hasFoodSafeInfo;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 营业时间
     */
    private String openTime;


}

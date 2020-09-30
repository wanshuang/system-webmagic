package com.ytpay.systemwebmagic.model.wantiku.com;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ws
 * @date 2020/9/30
 */
@Data
public class QuestionStatisticVo {

    Integer questionId;

    Integer questionStatisticId;

    Integer totalCount;

    BigDecimal rightRatio;

    Integer wrongCount;

    Integer askCount;

    String errorItem;

}

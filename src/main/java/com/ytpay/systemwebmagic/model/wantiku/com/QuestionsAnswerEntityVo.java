package com.ytpay.systemwebmagic.model.wantiku.com;

import lombok.Data;

import java.util.List;

/**
 * @author ws
 * @date 2020/9/30
 */
@Data
public class
QuestionsAnswerEntityVo {

    String vId;

    String videoSrc;

    //用, 分割
    List<String> formatImage;

    //用, 分割 正确答案
    List<String> answerArray;

    String formatContent;

}

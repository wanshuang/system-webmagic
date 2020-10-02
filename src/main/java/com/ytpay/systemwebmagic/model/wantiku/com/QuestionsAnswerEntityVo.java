package com.ytpay.systemwebmagic.model.wantiku.com;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author ws
 * @date 2020/9/30
 */
@Data
@ToString
public class
QuestionsAnswerEntityVo {

    String vId;

    String videoSrc;

    //用, 分割
    List<String> formatImages;

    //用, 分割 正确答案
    List<String> answerArray;

    String formatContent;

}

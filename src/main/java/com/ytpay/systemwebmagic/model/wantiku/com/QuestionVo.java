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
public class QuestionVo {

    Integer subjectId;

    Integer subjectParentId;

    Integer realQuestionId;

    Integer realPaperId;

    Integer paperId;

    Long questionId;

    Integer isAudio;

    Integer buyState;

    Integer orderNumber;

    Integer realOrderNumber;

    List<AnswerVo> questionContentKeyValue;

    String audioUrl;

    QuestionStatisticVo questionStatisticsEntity;

    String realPaperName;

    String formatContent;

    QuestionsAnswerEntityVo questionsAnswerEntity;

}

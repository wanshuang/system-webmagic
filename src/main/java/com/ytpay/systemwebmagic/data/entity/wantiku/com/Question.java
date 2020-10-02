package com.ytpay.systemwebmagic.data.entity.wantiku.com;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author ws
 * @date 2020/9/28
 */
@Entity
@Table(name = "t_wantiku_question")
@Accessors(chain = true)
@Data
@ToString
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    Integer subjectId;

    Integer subjectParentId;

    //TKQuestionsBasicEntityList
    Integer realQuestionId;

    Integer realPaperId;

    Integer paperId;

    Long questionId;

    Integer isAudio;

    Integer buyState;

    Integer orderNumber;

    Integer realOrderNumber;

    String audioUrl;

    String realPaperName;

    String formatContent;

    //questionStatisticsEntity
    Integer questionStatisticId;

    Integer totalCount;

    BigDecimal rightRatio;

    Integer wrongCount;

    Integer askCount;

    String errorItem;

    //questionsAnswerEntity
    String vId;

    String videoSrc;

    //用, 分割 图片信息
    String answerFormatImage;

    //用, 分割 正确答案
    String answerArray;

    String answerFormatContent;

}

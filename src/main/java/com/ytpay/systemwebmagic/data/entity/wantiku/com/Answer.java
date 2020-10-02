package com.ytpay.systemwebmagic.data.entity.wantiku.com;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * @author ws
 * @date 2020/9/28
 */
@Entity
@Table(name = "t_wantiku_answer")
@Accessors(chain = true)
@Data
@ToString
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    Long questionId;

    Integer realQuestionId;

    String answerKey;

    String answerValue;

}

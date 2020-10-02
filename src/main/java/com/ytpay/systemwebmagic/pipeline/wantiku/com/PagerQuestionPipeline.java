package com.ytpay.systemwebmagic.pipeline.wantiku.com;

import com.ytpay.systemwebmagic.data.entity.wantiku.com.Answer;
import com.ytpay.systemwebmagic.data.entity.wantiku.com.Question;
import com.ytpay.systemwebmagic.data.repository.wantiku.com.AnswerRepository;
import com.ytpay.systemwebmagic.data.repository.wantiku.com.QuestionRepository;
import com.ytpay.systemwebmagic.model.wantiku.com.AnswerVo;
import com.ytpay.systemwebmagic.model.wantiku.com.QuestionVo;
import org.apache.commons.collections.CollectionUtils;
import org.assertj.core.util.Lists;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

/**
 * @author ws
 * @date 2020/7/2
 */
@Component
public class PagerQuestionPipeline implements Pipeline {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Override
    public void process(ResultItems resultItems, Task task) {
        List<QuestionVo> list = resultItems.get("questionList");
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(qvo -> {
                Question questionBase = questionRepository.findByRealQuestionId(qvo.getRealQuestionId());
                if (questionBase == null) {
                    //question
                    Question question = new Question();

                    //项目信息
                    question.setSubjectId(qvo.getSubjectId());
                    question.setSubjectParentId(qvo.getSubjectParentId());

                    //题目信息
                    question.setRealQuestionId(qvo.getRealQuestionId());
                    question.setRealPaperId(qvo.getRealPaperId());
                    question.setPaperId(qvo.getPaperId());
                    question.setQuestionId(qvo.getQuestionId());
                    question.setBuyState(qvo.getBuyState());
                    question.setOrderNumber(qvo.getOrderNumber());
                    question.setRealOrderNumber(qvo.getRealOrderNumber());
                    question.setRealPaperName(qvo.getRealPaperName());
                    question.setFormatContent(qvo.getFormatContent());

                    //statis 统计信息
                    question.setQuestionStatisticId(qvo.getQuestionStatisticsEntity().getQuestionStatisticId());
                    question.setTotalCount(qvo.getQuestionStatisticsEntity().getTotalCount());
                    question.setRightRatio(qvo.getQuestionStatisticsEntity().getRightRatio());
                    question.setWrongCount(qvo.getQuestionStatisticsEntity().getWrongCount());
                    question.setAskCount(qvo.getQuestionStatisticsEntity().getAskCount());
                    question.setErrorItem(qvo.getQuestionStatisticsEntity().getErrorItem());

                    //音频信息
                    question.setIsAudio(qvo.getIsAudio());
                    question.setAudioUrl(qvo.getAudioUrl());

                    //正确答案 以及 针对的答案信息
                    if (qvo.getQuestionsAnswerEntity() != null) {
                        question.setVId(qvo.getQuestionsAnswerEntity().getVId());
                        question.setVideoSrc(qvo.getQuestionsAnswerEntity().getVideoSrc());
                        question.setAnswerFormatContent(qvo.getQuestionsAnswerEntity().getFormatContent());
                        List<String> imagers = qvo.getQuestionsAnswerEntity().getFormatImages();
                        List<String> answers = qvo.getQuestionsAnswerEntity().getAnswerArray();
                        if (CollectionUtils.isNotEmpty(imagers)) {
                            question.setAnswerFormatImage(StringUtil.join(imagers, ","));
                        }
                        if (CollectionUtils.isNotEmpty(answers)) {
                            question.setAnswerArray(StringUtil.join(answers, ","));
                        }
                    }

                    //所有答案信息
                    List<AnswerVo> answerVos = qvo.getQuestionContentKeyValue();
                    if (CollectionUtils.isNotEmpty(answerVos)) {
                        List<Answer> answers = Lists.newArrayList();
                        answerVos.forEach(vo -> {
                            Answer answer = new Answer();
                            answer.setAnswerKey(vo.getKey());
                            answer.setAnswerValue(vo.getValue());
                            answer.setQuestionId(qvo.getQuestionId());
                            answer.setRealQuestionId(question.getRealQuestionId());
                            answers.add(answer);
                        });
                        System.out.println("add answer:" + answers.toString());
                        answerRepository.saveAll(answers);
                    }
                    System.out.println("add question:" + question.toString());
                    questionRepository.save(question);
                }
            });
        }
    }
}

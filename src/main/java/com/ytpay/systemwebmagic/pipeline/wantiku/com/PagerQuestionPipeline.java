package com.ytpay.systemwebmagic.pipeline.wantiku.com;

import com.ytpay.systemwebmagic.data.repository.wantiku.com.AnswerRepository;
import com.ytpay.systemwebmagic.data.repository.wantiku.com.QuestionRepository;
import com.ytpay.systemwebmagic.model.wantiku.com.QuestionVo;
import org.apache.commons.collections.CollectionUtils;
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
            list.forEach(questionVo -> {

            });
        }
    }
}

package com.ytpay.systemwebmagic.pipeline.wantiku.com;

import com.ytpay.systemwebmagic.data.entity.Question;
import com.ytpay.systemwebmagic.data.entity.Subject;
import com.ytpay.systemwebmagic.data.repository.AnswerRepository;
import com.ytpay.systemwebmagic.data.repository.QuestionRepository;
import com.ytpay.systemwebmagic.data.repository.SubjectRepository;
import com.ytpay.systemwebmagic.model.wantiku.com.QuestionVo;
import com.ytpay.systemwebmagic.model.wantiku.com.SubjectVo;
import org.apache.commons.collections.CollectionUtils;
import org.assertj.core.util.Lists;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;
import java.util.stream.Collectors;

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

        }
    }
}

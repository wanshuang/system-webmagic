package com.ytpay.systemwebmagic.service.wantiku.com;

import com.ytpay.systemwebmagic.data.entity.wantiku.com.Subject;
import com.ytpay.systemwebmagic.data.entity.wantiku.com.SubjectParent;
import com.ytpay.systemwebmagic.data.repository.wantiku.com.SubjectParentRepository;
import com.ytpay.systemwebmagic.data.repository.wantiku.com.SubjectRepository;
import com.ytpay.systemwebmagic.processor.wantiku.com.WTKFastQuestionProcessor;
import com.ytpay.systemwebmagic.processor.wantiku.com.WTKSubjectProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author ws
 * @date 2020/9/29
 */
@Service
public class WTKCatchService {

    @Autowired
    WTKSubjectProcessor wtkSubjectProcessor;

    @Autowired
    SubjectParentRepository subjectParentRepository;

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    WTKFastQuestionProcessor wtkFastQuestionProcessor;

    private static final Integer RANDOM_CATCH_COUNT = 10;

    public void subjectBatchCatch() {
        List<SubjectParent> subjectParentList = subjectParentRepository.findAll();
        subjectParentList.forEach(subjectParent -> {
            wtkSubjectProcessor.startCatch(subjectParent.getSubjectParentId(), subjectParent.getSubjectLevel());
        });
    }

    public void questionBatchCatch() {
        List<Subject> subjectList = subjectRepository.findAll();
        subjectList.forEach(subject -> {
            //每个subject抓去X遍
            for (int catchCount = 0; catchCount < RANDOM_CATCH_COUNT; catchCount++) {
                wtkFastQuestionProcessor.startCatch(subject);
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

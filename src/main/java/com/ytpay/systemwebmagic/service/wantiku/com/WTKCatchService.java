package com.ytpay.systemwebmagic.service.wantiku.com;

import com.ytpay.systemwebmagic.data.entity.wantiku.com.SubjectParent;
import com.ytpay.systemwebmagic.data.repository.wantiku.com.SubjectParentRepository;
import com.ytpay.systemwebmagic.processor.wantiku.com.WTKSubjectProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void subjectBatchCatch(){
        List<SubjectParent> subjectParentList = subjectParentRepository.findAll();
        subjectParentList.forEach(subjectParent -> {
            wtkSubjectProcessor.startCatch(subjectParent.getSubjectParentId(), subjectParent.getSubjectLevel());
        });
    }

}

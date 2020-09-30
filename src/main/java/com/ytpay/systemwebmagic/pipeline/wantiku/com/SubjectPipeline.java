package com.ytpay.systemwebmagic.pipeline.wantiku.com;

import com.ytpay.systemwebmagic.data.entity.wantiku.com.Subject;
import com.ytpay.systemwebmagic.data.repository.wantiku.com.SubjectRepository;
import com.ytpay.systemwebmagic.model.wantiku.com.SubjectVo;
import org.apache.commons.collections.CollectionUtils;
import org.assertj.core.util.Lists;
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
public class SubjectPipeline implements Pipeline {

    @Autowired
    SubjectRepository subjectRepository;

    @Override
    public void process(ResultItems resultItems, Task task) {
        List<SubjectVo> list = resultItems.get("subjectList");
        if (CollectionUtils.isNotEmpty(list)) {
            //过滤条件 过滤爬取数据
            List<SubjectVo> subjectParentVos = list.stream().filter(vo ->
                    (vo.getSubjectId() != null &&
                            subjectRepository.findBySubjectId(vo.getSubjectId()) == null)
            ).collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(subjectParentVos)) {
                List<Subject> addAll = Lists.newArrayList();
                //对数据进行操作
                subjectParentVos.forEach(vo -> {
                    Subject subject = new Subject();
                    subject.setSubjectParentId(vo.getSubjectParentId());
                    subject.setSubjectParentLevel(vo.getSubjectParentLevel());
                    subject.setSubjectId(vo.getSubjectId());
                    subject.setIsOption(vo.getIsOption());
                    subject.setIsSelect(vo.getIsSelect());
                    subject.setOrderNumber(vo.getOrderNumber());
                    subject.setSubjectLevel(vo.getSubjectLevel());
                    subject.setSubjectName(vo.getSubjectName());
                    addAll.add(subject);
                });
                if (CollectionUtils.isNotEmpty(addAll)) {
                    System.out.println(addAll);
                    subjectRepository.saveAll(addAll);
                }
            }
        }
    }
}

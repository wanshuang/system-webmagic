package com.ytpay.systemwebmagic.pipeline.wantiku.com;

import com.ytpay.systemwebmagic.data.entity.MeiTuanRestaurant;
import com.ytpay.systemwebmagic.data.entity.SubjectParent;
import com.ytpay.systemwebmagic.data.repository.MeiTuanRestaurantRepository;
import com.ytpay.systemwebmagic.data.repository.SubjectParentRepository;
import com.ytpay.systemwebmagic.model.meituan.com.RestaurantVo;
import com.ytpay.systemwebmagic.model.wantiku.com.SubjectParentVo;
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
public class SubjectParentPipeline implements Pipeline {

    @Autowired
    SubjectParentRepository subjectParentRepository;

    @Override
    public void process(ResultItems resultItems, Task task) {
        List<SubjectParentVo> list = resultItems.get("subjectParentList");

        if (CollectionUtils.isNotEmpty(list)) {
            //过滤条件 过滤爬取数据
            List<SubjectParentVo> subjectParentVos = list.stream().filter(vo ->
                    (vo.getSubjectParentId() != null &&
                            subjectParentRepository.findBySubjectParentId(vo.getSubjectParentId()) == null)
            ).collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(subjectParentVos)) {
                List<SubjectParent> addAll = Lists.newArrayList();
                //对数据进行操作
                subjectParentVos.forEach(vo -> {
                    SubjectParent subjectParent = new SubjectParent();
                    subjectParent.setSubjectParentId(vo.getSubjectParentId());
                    subjectParent.setHasDown(vo.getHasDown());
                    subjectParent.setIsNeed(vo.getIsNeed());
                    subjectParent.setSearchNames(vo.getSearchNames());
                    subjectParent.setSubjectLevel(vo.getSubjectLevel());
                    subjectParent.setSubjectLevel2(vo.getSubjectLevel2());
                    subjectParent.setSubjectName(vo.getSubjectName());
                    addAll.add(subjectParent);
                });
                if (CollectionUtils.isNotEmpty(addAll)) {
                    System.out.println(addAll);
                    subjectParentRepository.saveAll(addAll);
                }
            }
        }
    }
}

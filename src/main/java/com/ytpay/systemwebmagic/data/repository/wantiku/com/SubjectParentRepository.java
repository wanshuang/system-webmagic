package com.ytpay.systemwebmagic.data.repository.wantiku.com;

import com.ytpay.systemwebmagic.data.entity.wantiku.com.SubjectParent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ws
 * @date 2020/7/3
 */
@Repository
public interface SubjectParentRepository extends JpaRepository<SubjectParent, Long> {

    SubjectParent findBySubjectParentId(Integer subjectParentId);

}

package com.ytpay.systemwebmagic.data.repository;

import com.ytpay.systemwebmagic.data.entity.Subject;
import com.ytpay.systemwebmagic.data.entity.SubjectParent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ws
 * @date 2020/7/3
 */
@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    Subject findBySubjectId(Integer subjectId);

}

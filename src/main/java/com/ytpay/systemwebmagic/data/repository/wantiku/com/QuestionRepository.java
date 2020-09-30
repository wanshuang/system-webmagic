package com.ytpay.systemwebmagic.data.repository.wantiku.com;

import com.ytpay.systemwebmagic.data.entity.wantiku.com.Question;
import com.ytpay.systemwebmagic.data.entity.wantiku.com.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ws
 * @date 2020/7/3
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    Subject findByQuestionId(Integer questionId);

}

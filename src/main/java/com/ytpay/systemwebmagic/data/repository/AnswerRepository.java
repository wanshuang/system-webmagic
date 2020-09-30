package com.ytpay.systemwebmagic.data.repository;

import com.ytpay.systemwebmagic.data.entity.Answer;
import com.ytpay.systemwebmagic.data.entity.Question;
import com.ytpay.systemwebmagic.data.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ws
 * @date 2020/7/3
 */
@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    Subject findByQuestionId(Integer questionId);

}

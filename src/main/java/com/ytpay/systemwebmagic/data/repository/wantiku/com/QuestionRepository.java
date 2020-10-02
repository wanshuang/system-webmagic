package com.ytpay.systemwebmagic.data.repository.wantiku.com;

import com.ytpay.systemwebmagic.data.entity.wantiku.com.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ws
 * @date 2020/7/3
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    Question findByRealQuestionId(Integer realQuestionId);

}

package com.ytpay.systemwebmagic.model.wantiku.com;

import lombok.Data;

/**
 * @author ws
 * @date 2020/9/28
 */
@Data
public class SubjectVo {

    Integer subjectParentId;

    Integer subjectParentLevel;

    Integer subjectId;

    String subjectName;

    Integer isOption;

    Integer isSelect;

    Integer subjectLevel;

    Integer orderNumber;

}

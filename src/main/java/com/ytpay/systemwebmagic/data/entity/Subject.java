package com.ytpay.systemwebmagic.data.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * @author ws
 * @date 2020/9/28
 */
@Entity
@Table(name = "t_wantiku_subject")
@Accessors(chain = true)
@Data
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    Integer subjectId;

    String subjectName;

    Integer isOption;

    Integer isSelect;

    Integer subjectLevel;

    Integer orderNumber;

    Integer subjectParentId;

    Integer subjectParentLevel;
}

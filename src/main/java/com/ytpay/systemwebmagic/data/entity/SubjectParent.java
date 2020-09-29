package com.ytpay.systemwebmagic.data.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * @author ws
 * @date 2020/9/28
 */
@Entity
@Table(name = "t_wantiku_subject_parent")
@Accessors(chain = true)
@Data
public class SubjectParent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    Integer subjectParentId;

    Integer subjectLevelId;

    Integer subjectLevel2;

    String subjectName;

    Integer hasDown;

    Integer subjectLevel;

    Integer isNeed;

    String SearchNames;

}

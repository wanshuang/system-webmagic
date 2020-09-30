package com.ytpay.systemwebmagic.data.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * @author ws
 * @date 2020/9/28
 */
@Entity
@Table(name = "t_wantiku_answer")
@Accessors(chain = true)
@Data
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

}

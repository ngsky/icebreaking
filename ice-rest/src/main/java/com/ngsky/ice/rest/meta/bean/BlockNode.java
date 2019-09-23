package com.ngsky.ice.rest.meta.bean;

import lombok.Data;

import javax.persistence.*;

/**
 * <dl>
 * <dt>BlockNode</dt>
 * <dd>Description:</dd>
 * <dd>CreateDate: 2019/9/21 下午7:05</dd>
 * </dl>
 *
 * @author ngsky
 */
@Data
@Entity
@Table(name = "ice_bnode")
public class BlockNode {
    /**
     * 数据块编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "bnum")
    private String bnum;
    /**
     * 数据块列表
     */
    @Column(name = "blocks")
    private String blocks;
    /**
     * 段编号(1,2,3...)
     */
    @Column(name = "segnum")
    private int segnum;
}

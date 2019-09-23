package com.ngsky.ice.rest.meta.bean;

import lombok.Data;

import javax.persistence.*;

/**
 * <dl>
 * <dt>FileNode</dt>
 * <dd>Description: 目录项</dd>
 * <dd>CreateDate: 2019/9/21 下午7:05</dd>
 * </dl>
 *
 * @author ngsky
 */
@Data
@Entity
@Table(name = "ice_fnode")
public class FileNode {

    /**
     * 文件全名
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "name")
    private String name;
    /**
     * 上层文件唯一编号
     */
    @Column(name = "fn")
    private String fn;
    /**
     * 底层文件唯一编号
     */
    @Column(name = "inode")
    private String inode;

}

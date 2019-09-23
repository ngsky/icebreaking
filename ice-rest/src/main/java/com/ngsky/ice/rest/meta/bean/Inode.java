package com.ngsky.ice.rest.meta.bean;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * <dl>
 * <dt>Inode</dt>
 * <dd>Description:</dd>
 * <dd>CreateDate: 2019/9/21 下午7:06</dd>
 * </dl>
 *
 * @author ngsky
 */
@Data
@Entity
@Table(name = "ice_inode")
public class Inode {
    /**
     * 底层文件唯一编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "inode")
    private String inode;
    /**
     * 父级文件编号
     */
    @Column(name = "pinode")
    private String pinode;
    /**
     * 文件类型(0:文件夹,1:文件)
     */
    @Column(name = "type")
    private int type;
    /**
     * 文件大小
     */
    @Column(name = "bytes")
    private String bytes;
    /**
     * 数据块唯一编号
     */
    @Column(name = "bnum")
    private String bnum;
    /**
     * 标准权限
     */
    @Column(name = "accode")
    private int accode;
    /**
     * 访问时间
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "atime")
    private Date atime;
    /**
     * 修改内容时间
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "mtime")
    private Date mtime;
    /**
     * 修改属性时间
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "ctime")
    private Date ctime;
}

package com.ngsky.ice.rest.meta.service;

import com.ngsky.ice.rest.exception.IceException;
import com.ngsky.ice.rest.meta.bean.Inode;
import org.springframework.data.domain.Page;

/**
 * <dl>
 * <dt>InodeService</dt>
 * <dd>Description:</dd>
 * <dd>CreateDate: 2019/9/21 下午9:26</dd>
 * </dl>
 *
 * @author ngsky
 */
public interface InodeService {

    Inode find(String inode) throws IceException;

    /**
     * 缺省排序：1.type asc(dir->file) 2.mtime desc
     */
    Page<Inode> findByPinode(String pinode, int pageNo, int pageSize) throws IceException;

    /**
     * 缺省排序: 1.type asc(dir->file) 2.mtime desc
     */
    Page<Inode> findByType(String pinode, int type, int pageNo, int pageSize) throws IceException;

    /**
     * 排序：时间升序
     * @param timeType atime/mtime/ctime
     */
    Page<Inode> findByTimeAsc(String pinode, int timeType, int pageNo, int pageSize) throws IceException;

    /**
     * 排序：时间降序
     * @param timeType atime/mtime/ctime
     */
    Page<Inode> findByTimeDesc(String pinode, int timeType, int pageNo, int pageSize) throws IceException;

    /**
     * 排序：时间升序
     * @param timeType atime/mtime/ctime
     */
    Page<Inode> findByTimeAsc(String pinode, int type, int timeType, int pageNo, int pageSize) throws IceException;

    /**
     * 排序：时间升序
     * @param timeType atime/mtime/ctime
     */
    Page<Inode> findByTimeDesc(String pinode, int type, int timeType, int pageNo, int pageSize) throws IceException;
}

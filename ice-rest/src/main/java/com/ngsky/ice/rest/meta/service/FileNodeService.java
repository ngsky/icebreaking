package com.ngsky.ice.rest.meta.service;

import com.ngsky.ice.rest.exception.IceException;
import com.ngsky.ice.rest.meta.bean.FileNode;

/**
 * <dl>
 * <dt>FileNodeService</dt>
 * <dd>Description:</dd>
 * <dd>CreateDate: 2019/9/21 下午9:26</dd>
 * </dl>
 *
 * @author ngsky
 */
public interface FileNodeService {

    /**
     * @param name 文件全名称
     * @return FileNode
     */
    FileNode openByName(String name) throws IceException;

    /**
     * @param fn 文件编号
     * @return FileNode
     */
    FileNode openByFn(String fn) throws IceException;


}

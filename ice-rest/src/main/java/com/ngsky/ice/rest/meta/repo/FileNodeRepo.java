package com.ngsky.ice.rest.meta.repo;

import com.ngsky.ice.rest.exception.IceException;
import com.ngsky.ice.rest.meta.bean.FileNode;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * <dl>
 * <dt>FileNodeRepo</dt>
 * <dd>Description:</dd>
 * <dd>CreateDate: 2019/9/21 下午9:23</dd>
 * </dl>
 *
 * @author ngsky
 */
@Repository
public interface FileNodeRepo extends BaseRepo<FileNode, String> {
    /**
     * @param fn 文件编号
     * @return FileNode
     */
    @Query(value = "SELECT fno FROM FileNode fno WHERE fno.fn=:fn")
    FileNode openByFn(String fn) throws IceException;
}

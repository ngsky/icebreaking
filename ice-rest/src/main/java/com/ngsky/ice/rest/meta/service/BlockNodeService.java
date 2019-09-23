package com.ngsky.ice.rest.meta.service;

import com.ngsky.ice.rest.exception.IceException;
import com.ngsky.ice.rest.meta.bean.BlockNode;

import java.util.List;

/**
 * <dl>
 * <dt>BlockNodeService</dt>
 * <dd>Description:</dd>
 * <dd>CreateDate: 2019/9/21 下午9:26</dd>
 * </dl>
 *
 * @author ngsky
 */
public interface BlockNodeService {
    List<BlockNode> find(String bnum) throws IceException;

    BlockNode find(String bnum, String segnum) throws IceException;
}

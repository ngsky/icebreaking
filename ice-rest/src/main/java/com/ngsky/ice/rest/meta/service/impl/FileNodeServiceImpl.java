package com.ngsky.ice.rest.meta.service.impl;

import com.ngsky.ice.rest.exception.IceException;
import com.ngsky.ice.rest.meta.bean.FileNode;
import com.ngsky.ice.rest.meta.repo.FileNodeRepo;
import com.ngsky.ice.rest.meta.service.FileNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * <dl>
 * <dt>FileNodeServiceImpl</dt>
 * <dd>Description:</dd>
 * <dd>CreateDate: 2019/9/21 下午11:11</dd>
 * </dl>
 *
 * @author ngsky
 */
@Service(value = "fileNodeService")
public class FileNodeServiceImpl implements FileNodeService {

    private FileNodeRepo fileNodeRepo;

    @Autowired
    public FileNodeServiceImpl(FileNodeRepo fileNodeRepo){
        this.fileNodeRepo = fileNodeRepo;
    }

    @Override
    public FileNode openByName(String name) throws IceException {
        Optional<FileNode> opt = fileNodeRepo.findById(name);
        return opt.orElse(null);
    }

    @Override
    public FileNode openByFn(String fn) throws IceException {
        return fileNodeRepo.openByFn(fn);
    }
}

package com.ngsky.ice.rest.meta.service.impl;

import com.ngsky.ice.rest.exception.IceException;
import com.ngsky.ice.rest.meta.bean.Inode;
import com.ngsky.ice.rest.meta.repo.InodeRepo;
import com.ngsky.ice.rest.meta.service.InodeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.criteria.Predicate;

/**
 * <dl>
 * <dt>InodeServiceImpl</dt>
 * <dd>Description:</dd>
 * <dd>CreateDate: 2019/9/21 下午11:32</dd>
 * </dl>
 *
 * @author ngsky
 */
@Service(value = "inodeService")
public class InodeServiceImpl implements InodeService {

    private InodeRepo inodeRepo;

    @Autowired
    public InodeServiceImpl(InodeRepo inodeRepo) {
        this.inodeRepo = inodeRepo;
    }

    @Override
    public Inode find(String inode) throws IceException {
        Optional<Inode> opt = inodeRepo.findById(inode);
        return opt.orElse(null);
    }

    @Override
    public Page<Inode> findByPinode(String pinode, int pageNo, int pageSize) throws IceException {

        return null;
    }

    @Override
    public Page<Inode> findByType(String pinode, int type, int pageNo, int pageSize) throws IceException {
        return null;
    }

    @Override
    public Page<Inode> findByTimeAsc(String pinode, int timeType, int pageNo, int pageSize) throws IceException {
        return null;
    }

    @Override
    public Page<Inode> findByTimeDesc(String pinode, int timeType, int pageNo, int pageSize) throws IceException {
        return null;
    }

    @Override
    public Page<Inode> findByTimeAsc(String pinode, int type, int timeType, int pageNo, int pageSize) throws IceException {
        return null;
    }

    @Override
    public Page<Inode> findByTimeDesc(String pinode, int type, int timeType, int pageNo, int pageSize) throws IceException {
        return null;
    }


    private Page<Inode> find(String pinode, Integer type, String timeType, int pageNo, int pageSize) {
        if (null == pinode) return null;
        Sort.Order timeOrder = null;
        Sort sort = null;
        if (StringUtils.equals(timeType, "atime")
                || StringUtils.equals(timeType, "mtime")
                || StringUtils.equals(timeType, "ctime")) {
            timeOrder = Sort.Order.desc(timeType);
        }
        Sort.Order typeOrder = Sort.Order.asc("type");
        if (null != timeOrder) {
            sort = Sort.by(typeOrder, timeOrder);
        } else {
            sort = Sort.by(typeOrder);
        }

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Specification<Inode> speci = (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            Predicate p = criteriaBuilder.equal(root.get("pinode"), pinode);
            list.add(p);

            if(null != type && (type == 0 || type == 1)){
                Predicate p1 = criteriaBuilder.equal(root.get("type"), type);
                list.add(p1);
            }
            return criteriaBuilder.and(list.toArray(new Predicate[0]));
        };
        return inodeRepo.findAll(speci, pageable);
    }
}

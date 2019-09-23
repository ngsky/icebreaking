package com.ngsky.ice.rest.meta.service.impl;

import com.ngsky.ice.rest.exception.IceException;
import com.ngsky.ice.rest.meta.bean.FileType;
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
        return find(pinode, null, FileType.TimeType.MTIME_DESC.toString().toLowerCase(), pageNo, pageSize);
    }

    @Override
    public Page<Inode> findByType(String pinode, int type, int pageNo, int pageSize) throws IceException {
        return find(pinode, type, FileType.TimeType.MTIME_DESC.toString().toLowerCase(), pageNo, pageSize);
    }

    @Override
    public Page<Inode> findByTime(String pinode, FileType.TimeType timeType, int pageNo, int pageSize) throws IceException {
        return find(pinode, null, timeType.toString().toLowerCase(), pageNo, pageSize);
    }

    @Override
    public Page<Inode> findByTime(String pinode, int type, FileType.TimeType timeType, int pageNo, int pageSize) throws IceException {
        return find(pinode, type, timeType.toString().toLowerCase(), pageNo, pageSize);
    }

    private Page<Inode> find(String pinode, Integer type, String timeType, int pageNo, int pageSize) {
        if (null == pinode) return null;
        Sort.Order timeOrder = null;
        Sort sort = null;
        String[] times = timeType.split("_");
        if (StringUtils.equals(times[0], "atime")
                || StringUtils.equals(times[0], "mtime")
                || StringUtils.equals(times[0], "ctime")) {
            if ("asc".equals(times[1])) {
                timeOrder = Sort.Order.asc(times[0]);
            } else {
                timeOrder = Sort.Order.desc(times[0]);
            }
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

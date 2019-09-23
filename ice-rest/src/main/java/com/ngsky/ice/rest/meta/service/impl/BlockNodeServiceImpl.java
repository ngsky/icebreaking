package com.ngsky.ice.rest.meta.service.impl;

import com.ngsky.ice.rest.exception.IceException;
import com.ngsky.ice.rest.meta.bean.BlockNode;
import com.ngsky.ice.rest.meta.repo.BlockNodeRepo;
import com.ngsky.ice.rest.meta.service.BlockNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Predicate;

/**
 * @Description:
 * @Author: sunyx
 * @CreateDate: 2019/9/23 17:05
 */
@Service(value = "blockNodeService")
public class BlockNodeServiceImpl implements BlockNodeService {

    private BlockNodeRepo blockNodeRepo;

    @Autowired
    public BlockNodeServiceImpl(BlockNodeRepo blockNodeRepo){
        this.blockNodeRepo = blockNodeRepo;
    }

    @Override
    public List<BlockNode> find(String bnum) throws IceException {
        Specification<BlockNode> speci = (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            Predicate p = criteriaBuilder.equal(root.get("bnum"), bnum);
            list.add(p);
            return criteriaBuilder.and(list.toArray(new Predicate[0]));
        };
        return blockNodeRepo.findAll(speci);
    }

    @Override
    public BlockNode find(String bnum, String segnum) throws IceException {
        Specification<BlockNode> speci = (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            Predicate p0 = criteriaBuilder.equal(root.get("bnum"), bnum);
            Predicate p1 = criteriaBuilder.equal(root.get("segnum"), segnum);
            list.add(p0);
            list.add(p1);
            return criteriaBuilder.and(list.toArray(new Predicate[0]));
        };
        List<BlockNode> blockNodes = blockNodeRepo.findAll(speci);
        if (blockNodes.size() == 1) return blockNodes.get(0);
        return null;
    }
}

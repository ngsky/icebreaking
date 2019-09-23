package com.ngsky.ice.rest.meta.repo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * <dl>
 * <dt>FileNodeRepo</dt>
 * <dd>Description:</dd>
 * <dd>CreateDate: 2019/9/21 下午9:20</dd>
 * </dl>
 *
 * @author ngsky
 */
@NoRepositoryBean
public interface BaseRepo<T,ID extends Serializable> extends CrudRepository<T,ID>, JpaSpecificationExecutor<T> {
}

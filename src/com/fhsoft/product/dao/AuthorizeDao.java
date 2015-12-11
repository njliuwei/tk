package com.fhsoft.product.dao;

import org.springframework.stereotype.Repository;

import com.fhsoft.base.dao.BaseDao;
import com.fhsoft.model.Authorize;

/**
 * @ClassName:com.fhsoft.product.dao.AuthorizeDao
 * @Description:authorize表的数据库操作
 *
 * @Author:liyi
 * @Date:2015年11月17日上午9:25:34
 *
 */
@Repository
public class AuthorizeDao extends BaseDao {

	public void add(Authorize authorize) {
		String sql = "insert into authorize values (?,?,?,?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.update(sql, new Object[]{ authorize.getProductId(), authorize.getUserName(), authorize.getUserIP(), authorize.getDueDate(), authorize.getIsMember(), authorize.getCode(), authorize.getCreated(), authorize.getCreatorId(), authorize.getCreator(), authorize.getLastmodified(), authorize.getLastmodifierId(), authorize.getLastmodifier() });
	}
	
}

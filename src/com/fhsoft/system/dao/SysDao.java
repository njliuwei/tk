package com.fhsoft.system.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.fhsoft.base.dao.BaseDao;
import com.fhsoft.model.Users;
/**
 * 
 * @author hjb
 * @date 2015-11-19 下午3:54:44
 */
@Repository
public class SysDao extends BaseDao {

	public Users login (String name , String password){
		Users u=null;
		String sql=" select * from users where name=? ";
		Object obj=jdbcTemplate.queryForObject(sql, new Object[] { name }, new BeanPropertyRowMapper<Users>(Users.class));
		if(obj!=null){
			u=(Users)obj;
		}
		return u;
	}
}

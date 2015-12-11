package com.fhsoft.system.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.fhsoft.base.dao.BaseDao;
import com.fhsoft.model.Menu;

/**
 * @ClassName:com.fhsoft.system.dao.MenuDao
 * @Description:menu表的数据库操作
 * 
 * @Author:liyi
 * @Date:2015年10月20日上午11:27:51
 * 
 */
@Repository
public class MenuDao extends BaseDao {

	public List<Menu> getMenusByType(int type) {
		List<Menu> list = new ArrayList<Menu>();
		String sql = "select id, name, parent_id as parentId, url, type from menu where type = ?";
		list = jdbcTemplate.query(sql, new Object[] { type }, new BeanPropertyRowMapper<Menu>(Menu.class));
		return list;
	}
}

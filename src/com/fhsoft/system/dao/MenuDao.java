package com.fhsoft.system.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.fhsoft.base.dao.BaseDao;
import com.fhsoft.model.Menu;
import com.fhsoft.model.Users;

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

	public List<Menu> getMenusByType(int type,Users user) {
		List<Menu> list = new ArrayList<Menu>();
		if ("admin".equals(user.getName())) {
			//menus = menuDao.findByHql(" from PMenu where type = ?0 ", type);
			String sql="select * from Menu where type=? ";
			list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Menu>(Menu.class),type);
		} else {
			String sql = "select distinct a.* from menu a left join role_menu b on a.id=b.menuId left join user_role c on b.roleId=c.roleId where c.userId = ? and a.type= ?";
			list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Menu>(Menu.class),user.getId(),type);
		}
//		String sql = "select id, name, parent_id as parentId, url, type from menu where type = ? ";
//		list = jdbcTemplate.query(sql, new Object[] { type }, new BeanPropertyRowMapper<Menu>(Menu.class));
		return list;
	}
}

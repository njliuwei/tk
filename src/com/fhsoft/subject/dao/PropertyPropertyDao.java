package com.fhsoft.subject.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.fhsoft.base.bean.TreeNodeBean1;
import com.fhsoft.base.dao.BaseDao;
import com.fhsoft.model.ComboxModel;
import com.fhsoft.model.PropertyProperty;

/**
 * @ClassName:com.fhsoft.subject.dao.PropertyPropertyDao
 * @Description:property_property表的数据库操作
 *
 * @Author:liyi
 * @Date:2015年10月30日下午3:55:50
 *
 */
@Repository
public class PropertyPropertyDao extends BaseDao{

	public List<TreeNodeBean1> getTreeGrid(int pid) {
		String sql = "with subqry(id,name,comment,_parentId,level) as  "
				+"( select id, name,comment, 0 as _parentId,level from property_property where parent_id =  ?  " 
				+"union all "
				+"select a.id, a.name,a.comment, a.parent_id as _parentId,a.level from property_property a join subqry b on a.parent_id = b.id) "
				+"select c.*,case when (select count(*) from property_property  where parent_id = c.id) > 0 then 'closed' else 'open' end 'state' from subqry c;";
		List<TreeNodeBean1> list = jdbcTemplate.query(sql,new Object[]{ pid }, new BeanPropertyRowMapper<TreeNodeBean1>(TreeNodeBean1.class));
		return list;
	}

	public void add(PropertyProperty propertyProperty) {
		String sql = "insert into property_property(name,comment,parent_id,level) values(?,?,?,?)";
		jdbcTemplate.update(sql, new Object[]{ propertyProperty.getName(), propertyProperty.getComment(), propertyProperty.getParentId(), 1 });
	}

	public PropertyProperty getById(int id) {
		String sql = "select id, name, comment, parent_id as parentId, level from property_property where id = ? ";
		PropertyProperty propertyProperty = jdbcTemplate.queryForObject(sql, new Object[]{ id }, new BeanPropertyRowMapper<PropertyProperty>(PropertyProperty.class));
		return propertyProperty;
	}

	public void update(PropertyProperty propertyProperty) {
		String sql = "update property_property set name = ?, comment = ? where id = ?";
		jdbcTemplate.update(sql,new Object[]{ propertyProperty.getName(), propertyProperty.getComment(),  propertyProperty.getId() });
	}

	public void deleteTreeById(int id) {
		String sql = "delete from property_property where id = ? or parent_id = ?";
		jdbcTemplate.update(sql, new Object[]{ id , id });
	}

	public List<ComboxModel> getPropertyByParentId(int id) {
		String sql = "select id,name from property_property where parent_id = ? ";
		List<ComboxModel> list = jdbcTemplate.query(sql,new Object[]{ id }, new BeanPropertyRowMapper<ComboxModel>(ComboxModel.class));
		return list;
	}

}

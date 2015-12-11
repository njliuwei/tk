package com.fhsoft.subject.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.fhsoft.base.dao.BaseDao;
import com.fhsoft.model.PropertyProperty;

/**
 * @ClassName:com.fhsoft.subject.dao.PropertyPropertyValueDao
 * @Description:property_property关于学科属性的属性值的数据库操作
 *
 * @Author:liyi
 * @Date:2015年11月3日下午3:32:17
 *
 */
@Repository
public class PropertyPropertyValueDao extends BaseDao{

	public void add(PropertyProperty propertyProperty) {
		String sql = "insert into property_property(name,comment,parent_id,level,attach_path) values (?,?,?,?,?)";
		jdbcTemplate.update(sql, new Object[]{ propertyProperty.getName(), propertyProperty.getComment(), propertyProperty.getParentId(), 1, propertyProperty.getAttachPath()});
	}

	public PropertyProperty getById(int id) {
		String sql = "select id, name, comment, parent_id as parentId, level, attach_path as attachPath from property_property where id = ? ";
		PropertyProperty propertyProperty = jdbcTemplate.queryForObject(sql, new Object[]{ id }, new BeanPropertyRowMapper<PropertyProperty>(PropertyProperty.class));
		return propertyProperty;
	}

	public void update(PropertyProperty propertyProperty) {
		String sql = "update property_property set name = ?, comment = ?, attach_path = ? where id = ?";
		jdbcTemplate.update(sql,new Object[]{ propertyProperty.getName(), propertyProperty.getComment(), propertyProperty.getAttachPath(),  propertyProperty.getId() });
	}
	

}

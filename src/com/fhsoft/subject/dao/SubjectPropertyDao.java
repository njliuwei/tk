package com.fhsoft.subject.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.fhsoft.base.bean.Page;
import com.fhsoft.base.dao.BaseDao;
import com.fhsoft.model.SubjectProperty;

/**
 * @ClassName:com.fhsoft.subject.dao.SubjectPropertyDao
 * @Description:subject_property表关于学科属性的数据库操作
 * 
 * @Author:liyi
 * @Date:2015年10月16日上午11:04:05
 * 
 */
@Repository
public class SubjectPropertyDao extends BaseDao {

	public List<SubjectProperty> getBySubjectType() {
		String sql = "with subqry(id,name,parentId,col,type,status,level,subjectName,parentName) "+
				"as ( select a.id,a.name,a.parent_id as parentId,a.col, a.type, a.status, a.level,(select name from subject where id = a.subject_id) as subjectName ,cast('' as varchar(200)) as parentName from subject_property a where exists (select b.id from menu b where a.parent_id = b.id and b.type = 3) " +
				"union all " + 
				"select c.id,c.name,c.parent_id as parentId,c.col, c.type, c.status, c.level,(select d.name from subject d where exists(select id from subject_property where id = c.parent_id and subject_id = d.id)) as subjectName,(select name from subject_property where id = c.parent_id ) as parentName from subqry a inner join subject_property c on c.parent_id = a.id where c.level = 2 ) " +
				"select * from subqry";
		List<SubjectProperty> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<SubjectProperty>(SubjectProperty.class));
		return list;
	}
	
	public List<SubjectProperty> getBySubjectType1() {
		String sql = "select a.id, a.name, a.col, a.type, a.status, a.parent_id as parentId, a.level, a.subject_id as subjectId from subject_property a where exists (select b.id from menu b where a.parent_id = b.id and b.type = 3 )";
		List<SubjectProperty> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<SubjectProperty>(SubjectProperty.class));
		return list;
	}

	public Page getByPage(int pageNo, int pageSize, String id, String status) {
		StringBuffer sql = new StringBuffer();
		Page page = new Page();
		sql.append("select id, name, col, type, status, sort, comment from subject_property where parent_id = ?");
		if(status != null && !"全部".equals(status)){
			sql.append(" and status = ?");
			page = super.pageQuery(pageNo, pageSize, sql.toString(), SubjectProperty.class, id, status);
		}else{
			page = super.pageQuery(pageNo, pageSize, sql.toString(), SubjectProperty.class, id);
		}
		return page;
	}

	public void add(SubjectProperty subjectProperty) {
		String sql = "insert into subject_property(name,col,type,status,sort,comment,parent_id,level,is_system) values (?,?,?,?,?,?,?,?,0)";
		jdbcTemplate.update(sql, new Object[] { subjectProperty.getName(), subjectProperty.getCol(), subjectProperty.getType(), "启用", subjectProperty.getSort(), subjectProperty.getComment(), subjectProperty.getParentId(), 2 });
	}

	public SubjectProperty getById(int id) {
		String sql = "select id, name, col, type, status, sort, comment from subject_property where id = ? ";
		SubjectProperty subjectProperty = jdbcTemplate.queryForObject(sql, new Object[]{ id }, new BeanPropertyRowMapper<SubjectProperty>(SubjectProperty.class));
		return subjectProperty;
	}

	public void update(SubjectProperty subjectProperty) {
		String sql = "update subject_property set name = ?, type = ?, status = ?, sort = ?, comment = ? where id = ?";
		jdbcTemplate.update(sql, new Object[]{subjectProperty.getName(), subjectProperty.getType(), subjectProperty.getStatus(), subjectProperty.getSort(), subjectProperty.getComment(), subjectProperty.getId()});
	}

	public List<SubjectProperty> getChildrenById(int subjectPropertyId) {
		String sql = "with subqry(id,name,type,status,sort,comment,parent_id,level) as "
				+"( select id, name, type, status, sort, comment,parent_id, level from subject_property where  id = ?  " 
				+"union all "
				+"select a.id, a.name, a.type, a.status, a.sort, a.comment,a.parent_id, a.level from subject_property a join subqry b on a.parent_id = b.id) "
				+"select * from subqry;";
		List<SubjectProperty> list = jdbcTemplate.query(sql,new Object[]{ subjectPropertyId }, new BeanPropertyRowMapper<SubjectProperty>(SubjectProperty.class));
		return list;
	}
	
	//返回添加数据的主键
	public int insert(final SubjectProperty subjectProperty) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		final String sql = "insert into subject_property(name,type,status,sort,comment,parent_id,level) values (?,?,?,?,?,?,?)";
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement ps = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, subjectProperty.getName());
                ps.setString(2, subjectProperty.getType());
                ps.setString(3, subjectProperty.getStatus());
                ps.setInt(4, subjectProperty.getSort());
                ps.setString(5, subjectProperty.getComment());
                ps.setInt(6, subjectProperty.getParentId());
                ps.setInt(7, subjectProperty.getLevel());
                return ps;
			}
        }, keyHolder);
		return keyHolder.getKey().intValue();
	}

	public List<SubjectProperty> getByNameAndPid(String name, String pid, String id) {
		List<SubjectProperty> list = new ArrayList<SubjectProperty>();
		String sql = "select id, name, col, type, status, sort, comment from subject_property where name = ? and parent_id = ? ";
		if(null != id && !"".equals(id)){
			sql += " and id != ?";
			list = jdbcTemplate.query(sql,new Object[]{ name, pid, id }, new BeanPropertyRowMapper<SubjectProperty>(SubjectProperty.class));
		}else{
			list = jdbcTemplate.query(sql,new Object[]{ name, pid }, new BeanPropertyRowMapper<SubjectProperty>(SubjectProperty.class));
		}
		return list;
	}

	public int getChildrenCountByParentId(int parentId) {
		String sql = "select count(1) from subject_property where parent_id = ? and is_system = 0";
		return jdbcTemplate.queryForObject(sql,new Object[]{ parentId }, Integer.class);
	}

	public void updateCol(String col, Integer id) {
		String sql = "update subject_property set col = ? where id = ?";
		jdbcTemplate.update(sql, new Object[]{ col, id });
	}
	
	/**
	 * 
	 * @Description  根据名称（知识点、教材体系）等获得对应的树形菜单
	 * @return 
	 * @Date 2015-11-9 下午3:06:40
	 */
	public List<SubjectProperty> getMenuTreeByName(String name) {
		StringBuffer sql = new StringBuffer();
		sql.append("WITH TMP (ID,NAME,PARENTID,SUBJECTID)AS(SELECT CHILD.ID,CHILD.NAME,");
		sql.append("(SELECT TOP 1 ID FROM MENU WHERE NAME=(SELECT TOP 1 NAME FROM MENU WHERE ID=PARENT.PARENT_ID) AND TYPE='1'),PARENT.SUBJECT_ID ");
		sql.append("FROM subject_property CHILD JOIN subject_property PARENT ON CHILD.parent_id=PARENT.ID ");
		sql.append("WHERE CHILD.LEVEL='2' AND CHILD.NAME=? AND PARENT.NAME='题目' ");
		sql.append("UNION ALL ");
		sql.append("SELECT CHILD.ID,CHILD.NAME,CHILD.PARENT_ID,PARENT.SUBJECTID ");
		sql.append("FROM subject_property CHILD JOIN TMP PARENT ON CHILD.PARENT_iD=PARENT.ID) ");
		sql.append("SELECT ID,NAME,PARENTID,SUBJECTID,(select count(1) from subject_property where parent_id=tmp.id)LEVEL FROM TMP ");
		Object[] values = {name};
		List<SubjectProperty> list = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<SubjectProperty>(SubjectProperty.class),values);
		return list;
	}

	/**
	 * 
	 * @Description  教材版本词库菜单
	 * @return 
	 * @Date 2015-11-9 下午3:06:40
	 */
	public List<SubjectProperty> getMenuTreeForJcbb() {
		StringBuffer sql = new StringBuffer();
		sql.append("WITH TMP (ID,NAME,PARENTID,SUBJECTID,SUBJECTNAME)AS(SELECT CHILD.ID,cast((select name from menu where id=parent.parent_id) as varchar(200)),");
		sql.append("(SELECT ID FROM MENU WHERE NAME='教材版本词库'),PARENT.SUBJECT_ID,cast((select name from menu where id=parent.parent_id) as varchar(2000)) ");
		sql.append("FROM subject_property CHILD JOIN subject_property PARENT ON CHILD.parent_id=PARENT.ID WHERE ");
		sql.append("CHILD.LEVEL='2' AND CHILD.NAME='教材体系' AND PARENT.NAME='题目' ");
		sql.append("AND PARENT.SUBJECT_ID IN(SELECT ID FROM SUBJECT WHERE NAME LIKE '%语文%' OR NAME LIKE '%英语%') ");
		sql.append("UNION ALL SELECT CHILD.ID,CHILD.NAME,CHILD.PARENT_ID,PARENT.SUBJECTID,CAST(PARENT.SUBJECTNAME+'_'+CHILD.NAME AS VARCHAR(2000)) ");
		sql.append("FROM subject_property CHILD JOIN TMP PARENT ON CHILD.PARENT_iD=PARENT.ID) SELECT ID,NAME,PARENTID,SUBJECTID,(select ");
		sql.append("count(1) from subject_property where parent_id=tmp.id)LEVEL,SUBJECTNAME FROM TMP ");
		List<SubjectProperty> list = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<SubjectProperty>(SubjectProperty.class));
		return list;
	}

}

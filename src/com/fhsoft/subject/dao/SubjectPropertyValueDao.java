package com.fhsoft.subject.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.fhsoft.base.bean.Page;
import com.fhsoft.base.bean.TreeNodeBean;
import com.fhsoft.base.bean.TreeNodeBean1;
import com.fhsoft.base.dao.BaseDao;
import com.fhsoft.model.SubjectProperty;

/**
 * @ClassName:com.fhsoft.subject.dao.SubjectPropertyValueDao
 * @Description:subject_property表关于学科属性值的数据库操作
 *
 * @Author:liyi
 * @Date:2015年10月27日上午11:33:18
 *
 */
@Repository
public class SubjectPropertyValueDao extends BaseDao {

	public Page getByPage(int pageNo, int pageSize, String id) {
		String sql = "select id, name, sort, comment from subject_property where parent_id = ?";
		Page page = super.pageQuery(pageNo, pageSize, sql, SubjectProperty.class, id);
		return page;
	}

	public void add(SubjectProperty subjectProperty) {
		String sql = "insert into subject_property(name,sort,parent_id,comment,level) values(?,?,?,?,3) ";
		jdbcTemplate.update(sql, new Object[]{ subjectProperty.getName(), subjectProperty.getSort(), subjectProperty.getParentId(), subjectProperty.getComment() });
	}

	public SubjectProperty getById(int id) {
		String sql = "select id,name,sort,parent_id as parentId,comment from subject_property where id = ?";
		SubjectProperty subjectProperty = jdbcTemplate.queryForObject(sql, new Object[]{ id }, new BeanPropertyRowMapper<SubjectProperty>(SubjectProperty.class));
		return subjectProperty;
	}

	public void update(SubjectProperty subjectProperty) {
		String sql = "update subject_property set name = ? , sort = ?, parent_id = ? where id = ? ";
		jdbcTemplate.update(sql, new Object[]{ subjectProperty.getName(), subjectProperty.getSort(), subjectProperty.getParentId(), subjectProperty.getId() });
	}

	public void deleteById(int id) {
		String sql = "delete from subject_property where id = ? ";
		jdbcTemplate.update(sql, new Object[]{ id });
	}

	public List<TreeNodeBean1> getTreeGridByRoot(int id) {
		String sql = "with subqry(id,name,sort,_parentId) as  "
				+"( select id, name, sort, 0 as _parentId from subject_property where id =  ?  " 
				+"union all "
				+"select a.id, a.name, a.sort, a.parent_id as _parentId from subject_property a join subqry b on a.parent_id = b.id) "
				+"select c.*,case when (select count(*) from subject_property  where parent_id = c.id) > 0 then 'closed' else 'open' end 'state' from subqry c order by c.sort;";
		List<TreeNodeBean1> list = jdbcTemplate.query(sql,new Object[]{ id }, new BeanPropertyRowMapper<TreeNodeBean1>(TreeNodeBean1.class));
		return list;
	}

	public List<TreeNodeBean> getComboTree(int pid, String id) {
		StringBuffer str = new StringBuffer();
		str.append("with subqry(id,text,sort,pid) as  ");
		str.append("( select id, name as text, sort, 0 as pid from subject_property where id =  ?  " );
		str.append("union all ");
		str.append("select a.id, a.name as text,a.sort, a.parent_id as pid from subject_property a join subqry b on a.parent_id = b.id ");
		if(null != id && !"".equals(id)){
			str.append(" where a.id != ? ");
			str.append(") select c.*,case when (select count(*) from subject_property  where parent_id = c.id and id != ?) > 0 then 'closed' else 'open' end 'state' from subqry c order by c.sort;");
		}else{
			str.append(") select c.*,case when (select count(*) from subject_property  where parent_id = c.id) > 0 then 'closed' else 'open' end 'state' from subqry c order by c.sort;");
		}
		List<TreeNodeBean> list = null;
		if(null != id && !"".equals(id)){
			list = jdbcTemplate.query(str.toString(),new Object[]{ pid, id, id }, new BeanPropertyRowMapper<TreeNodeBean>(TreeNodeBean.class));
		}else{
			list = jdbcTemplate.query(str.toString(),new Object[]{ pid }, new BeanPropertyRowMapper<TreeNodeBean>(TreeNodeBean.class));
		}
		return list;
	}

	public void deleteTreeById(int id) {
		String sql = "with subqry(id,name,_parentId) as  "
				+"( select id, name, parent_id as _parentId from subject_property where id =  ?  " 
				+"union all "
				+"select a.id, a.name, a.parent_id as _parentId from subject_property a join subqry b on a.parent_id = b.id) "
				+"select * from subqry ;";
		List<TreeNodeBean1> list = jdbcTemplate.query(sql,new Object[]{ id }, new BeanPropertyRowMapper<TreeNodeBean1>(TreeNodeBean1.class));
		
	    List<Object[]> batch = new ArrayList<Object[]>();
	    for (TreeNodeBean1 bean : list) {
	      Object[] values = new Object[] { bean.getId() };
	      batch.add(values);
	    }
		String delsql = "delete from subject_property where id = ? ";
		jdbcTemplate.batchUpdate(delsql, batch);

	}

	public List<TreeNodeBean> getZsdTree(String subjectId, String type) {
		String sql = "with subqry(id,text,pid) as  "
				+"( select id, name as text, parent_id as pid from subject_property where parent_id = (select id from subject_property where subject_id = ? and name = ? ) and col = 'zsd' " 
				+"union all "
				+"select a.id, a.name as text, parent_id as pid from subject_property a join subqry b on a.parent_id = b.id) "
				+"select c.*,case when (select count(*) from subject_property  where parent_id = c.id) > 0 then 'closed' else 'open' end 'state' from subqry c;";
		List<TreeNodeBean> list = jdbcTemplate.query(sql,new Object[]{ subjectId, type }, new BeanPropertyRowMapper<TreeNodeBean>(TreeNodeBean.class));
		return list;
	}

	public List<TreeNodeBean> getZsdTreeByIds(String zsd) {
		String sql = "select a.id, a.name as text, a.parent_id as pid,'open' state from subject_property a right join (select col from dbo.f_splitSTR(?,',')) b on a.id = b.col";
		List<TreeNodeBean> list = jdbcTemplate.query(sql,new Object[]{ zsd }, new BeanPropertyRowMapper<TreeNodeBean>(TreeNodeBean.class));
		return list;
	}

	public List<SubjectProperty> getChildBySubjectCol(String subject, String col) {
		String sql = "select id,name,comment,sort from subject_property where parent_id = (select id from subject_property where parent_id = (select id from subject_property where subject_id = ? and name = '题目' ) and  col = ? ) order by sort";
		List<SubjectProperty> list = jdbcTemplate.query(sql, new Object[]{ subject, col }, new BeanPropertyRowMapper<SubjectProperty>(SubjectProperty.class));
		return list;
	}

}

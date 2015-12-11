package com.fhsoft.product.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.fhsoft.base.bean.Page;
import com.fhsoft.base.dao.BaseDao;
import com.fhsoft.model.Paper;

/**
 * @ClassName:com.fhsoft.product.dao.PaperDao
 * @Description:paper表的数据库操作
 *
 * @Author:liyi
 * @Date:2015年11月17日下午2:11:41
 *
 */
@Repository
public class PaperDao extends BaseDao {

	public Page getByPage(int pageNo, int pageSize, String subject, String searchPaperName) {
		StringBuffer sql = new StringBuffer();
		sql.append("select a.id, a.name, (select name from product where id = a.product) as productName, a.created, a.creator, a.lastmodified, a.lastmodifier from paper a where a.subject = ? ");
		if(null != searchPaperName && !"".equals(searchPaperName)){
			sql.append(" and a.name like '%");
			sql.append(searchPaperName);
			sql.append("%'");
		}
		Page page = super.pageQuery(pageNo, pageSize, sql.toString(), Paper.class, subject);
		return page;
	}

	public void delete(int id) {
		String sql = "delete from paper where id = ? ";
		jdbcTemplate.update(sql, new Object[]{ id });
	}

	public Paper getById(String id) {
		String sql = "select * from paper where id = ?";
		Paper paper = jdbcTemplate.queryForObject(sql, new Object[]{ id }, new BeanPropertyRowMapper<Paper>(Paper.class)); 
		return paper;
	}

	/**
	 * @Description:
	 * @param paper
	 * @Date:2015年12月2日上午10:22:57
	 * 
	 */
	public void save(Paper paper) {
		String sql = "insert into paper values (?,?,?,?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.update(sql, new Object[]{ paper.getName(), paper.getPaperContent(), paper.getProduct(), paper.getSubject(), paper.getExportPath1(), paper.getExportPath2(), paper.getCreated(), paper.getCreator(), paper.getCreatorId(), paper.getLastmodified(), paper.getLastmodifier(), paper.getLastmodifierId() });
	}

}

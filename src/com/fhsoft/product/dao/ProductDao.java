package com.fhsoft.product.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.fhsoft.base.bean.Page;
import com.fhsoft.base.dao.BaseDao;
import com.fhsoft.model.Product;

/**
 * @ClassName:com.fhsoft.product.dao.ProductDao
 * @Description:product表的数据库操作
 * 
 * @Author:liyi
 * @Date:2015年11月6日下午2:03:26
 * 
 */
@Repository
public class ProductDao extends BaseDao {

	public Page getByPage(int pageNo, int pageSize, String subjectId, String status) {
		StringBuffer sql = new StringBuffer();
		sql.append("select id, name, propversion, qst_count as qstCount, status, created, creator, lastmodified, lastmodifier from product where subject = ?");
		if (status != null && !"全部".equals(status)) {
			sql.append(" and status = '");
			sql.append(status);
			sql.append("'");
		}
		Page page = super.pageQuery(pageNo, pageSize, sql.toString(), Product.class, subjectId);
		return page;
	}

	public int add(final Product product) {
		final String sql = "insert into product values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement ps = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, product.getName());
                ps.setString(2, product.getPropversion());
                ps.setInt(3, product.getSubject());
                ps.setString(4, product.getZsd());
                ps.setString(5, product.getTx());
                ps.setString(6,  product.getNd());
                ps.setString(7, "未发布");
                ps.setInt(8, product.getQstCount());
                ps.setString(9, product.getCreated());
//                ps.setInt(10, product.getCreatorId());
                ps.setInt(10, 0);
                ps.setString(11, product.getCreator());
                ps.setString(12, product.getLastmodified());
//                ps.setInt(13, product.getLastmodifierId());
                ps.setInt(13, 0);
                ps.setString(14, product.getLastmodifier());
                return ps;
			}
        }, keyHolder);
		return keyHolder.getKey().intValue();
	}

	public Product getById(int id) {
		String sql = "select * from product where id = ?";
		Product product = jdbcTemplate.queryForObject(sql, new Object[]{ id }, new BeanPropertyRowMapper<Product>(Product.class));
		return product;
	}

	public void update(Product product) {
		String sql = "update product set name=?, propversion=?, zsd=?, tx=?, nd=?, qst_count=?, lastmodified=?, lastmodifier_id=?, lastmodifier=? where id = ?";
		jdbcTemplate.update(sql, new Object[]{ product.getName(), product.getPropversion(), product.getZsd(), product.getTx(), product.getNd(), product.getQstCount(), product.getLastmodified(), product.getLastmodifierId(), product.getLastmodifier(), product.getId()});
	}

	public void delete(int id) {
		String sql = "delete from product where id = ?";
		jdbcTemplate.update(sql, new Object[]{ id });
	}

	public void updateStatus(int productId) {
		String sql = "update product set status = '已发布' where id = ?";
		jdbcTemplate.update(sql, new Object[]{ productId });
	}

	public void updateQstCount(String productId) {
		String sql = "update product set qst_count = (select count(1) from product_question where product_id = ?) where id = ?";
		jdbcTemplate.update(sql, new Object[]{ productId,productId });
	}

	public List<Product> getByName(String name, String id) {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from product where name = ? ");
		if(null != id && !"".equals(id)){
			sql.append(" and id != '");
			sql.append(id);
			sql.append("'");
		}
		List<Product> list = jdbcTemplate.query(sql.toString() ,new Object[]{ name }, new BeanPropertyRowMapper<Product>(Product.class));
		return list;
	}

}

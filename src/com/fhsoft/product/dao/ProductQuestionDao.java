package com.fhsoft.product.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import com.fhsoft.base.dao.BaseDao;

/**
 * @ClassName:com.fhsoft.product.dao.ProductQuestionDao
 * @Description:product_qustion关联表的数据库操作
 * 
 * @Author:liyi
 * @Date:2015年11月9日下午4:50:36
 * 
 */
@Repository
public class ProductQuestionDao extends BaseDao {

	public void batchAdd(final int productId, final List<Integer> qstIds) {
		String sql = "insert into product_question values(?,?)";
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setInt(1, productId);
				ps.setInt(2, qstIds.get(i));
			}
			@Override
			public int getBatchSize() {
				return qstIds.size();
			}
		});
	}

	public void deleteByProductId(int productId) {
		String sql = "delete from product_question where product_id = ?";
		jdbcTemplate.update(sql, new Object[]{ productId });
	}

	public void deleteByProductAndQst(String productId, String qstId) {
		String sql = "delete from product_question where qst_id = ? and product_id = ?";
		jdbcTemplate.update(sql, new Object[]{ qstId, productId });
	}
	
	
}

package com.fhsoft.base.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import com.fhsoft.base.bean.Page;

public class BaseDao {

	@Resource(name = "jdbcTemplate")
	protected JdbcTemplate jdbcTemplate;
	
	/**
	 * @Description:分页查询
	 * @param pageNum
	 * @param pageSize
	 * @param sql
	 * @param clz
	 * @param values
	 * @return
	 * @Date:2015年10月16日下午4:20:43
	 *
	 */
	public <T> Page pageQuery(int pageNo, int pageSize, String sql, final Class<T> clz , final Object... values){
		long totalCount = this.getCount(sql, values);
		if (totalCount < 1) {
			return new Page();
		}
		List<T> res = this.getList(pageNo, pageSize, sql, clz, values);
		return new Page(pageNo, pageSize, totalCount, res);
	}
	
	/**
	 * @Description:分页查询
	 * @param pageNum
	 * @param pageSize
	 * @param sql
	 * @param clz
	 * @param values
	 * @return
	 * @Date:2015年10月16日下午4:20:43
	 *
	 */
	public <T> Page pageQuery1(int pageNo, int pageSize, String sql, final Class<T> clz , final Object... values){
		long totalCount = this.getCount1(sql, values);
		if (totalCount < 1) {
			return new Page();
		}
		List<T> res = this.getList(pageNo, pageSize, sql, clz, values);
		return new Page(pageNo, pageSize, totalCount, res);
	}

	/**
	 * @Description:查询的结果集
	 * @param pageNum
	 * @param pageSize
	 * @param sql
	 * @param clz
	 * @param values
	 * @return
	 * @Date:2015年10月16日下午4:17:58
	 *
	 */
	private <T> List<T> getList(int pageNo, int pageSize, String sql, final Class<T> clz , final Object... values) {
		sql = "select TOP " + pageSize + " tmp___1.* from  (" + sql + " ) tmp___1 WHERE tmp___1.id NOT IN (SELECT TOP " + (pageSize * (pageNo - 1)) + " tmp___2.id FROM (" + sql + ") tmp___2)";
		List<T> lst = jdbcTemplate.query(sql, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException {
				int k = 1;
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						ps.setObject(k++, values[i]);
					}
					for (int i = 0; i < values.length; i++) {
						ps.setObject(k++, values[i]);
					}
				}
			}
		}, new BeanPropertyRowMapper<T>(clz));
		return lst;
	}
	
	/**
	 * @Description:查询总记录数
	 * @param sql
	 * @return
	 * @Date:2015年10月16日下午4:17:32
	 *
	 */
	private long getCount(String sql, final Object... values){
		Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(sql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		int beginPos = sb.toString().toLowerCase(Locale.CHINA).lastIndexOf("from");
		String countQueryString = "select count (1) " + sb.toString().substring(beginPos);
		long count = jdbcTemplate.queryForObject(countQueryString,values,Long.class);
		return count;
	}
	
	/**
	 * @Description:查询总记录数
	 * @param sql
	 * @return
	 * @Date:2015年10月16日下午4:17:32
	 *
	 */
	private long getCount1(String sql, final Object... values){
		Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(sql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		int beginPos = sb.toString().toLowerCase(Locale.CHINA).indexOf("from");
		String countQueryString = "select count (1) " + sb.toString().substring(beginPos);
		long count = jdbcTemplate.queryForObject(countQueryString,values,Long.class);
		return count;
	}
	
	/**
	 * 
	 * @Description 带count语句的PAGE查询
	 * @param pageNo
	 * @param pageSize
	 * @param sql
	 * @param countSql
	 * @param clz
	 * @param values
	 * @return
	 * @Date 2015-11-11 下午4:17:51
	 */
	public <T> Page pageQuery(int pageNo, int pageSize, String sql,String countSql, final Class<T> clz , final Object... values){
		long totalCount = this.getCountWithoutProcess(countSql, values);
		if (totalCount < 1) {
			return new Page();
		}
		List<T> res = this.getList(pageNo, pageSize, sql, clz, values);
		return new Page(pageNo, pageSize, totalCount, res);
	}
	
	
	/**
	 * 
	 * @Description 含有WITH的SQL语句的分页查询
	 * @param pageNo
	 * @param pageSize
	 * @param withSqlParamsNum WITHSQL含有的占位符个数
	 * @param withSql WITHSQL
	 * @param sql SQL
	 * @param countSql 总数查询SQL
	 * @param clz
	 * @param values 参数列表
	 * @return
	 * @Date 2015-11-23 上午9:05:51
	 */
	public <T> Page pageQueryContainsWithSql(int pageNo, int pageSize,int withSqlParamsNum, String withSql, String sql,String countSql, final Class<T> clz , final Object... values){
		long totalCount = this.getCountWithoutProcess(countSql, values);
		if (totalCount < 1) {
			return new Page();
		}
		List<T> res = this.getListContainsWithSql(pageNo, pageSize, withSqlParamsNum, withSql, sql, clz, values);
		return new Page(pageNo, pageSize, totalCount, res);
	}

	/**
	 * 
	 * @Description 自带COUNT语句的COUNT查询 
	 * @param countSql
	 * @param values
	 * @return
	 * @Date 2015-11-23 上午9:12:06
	 */
	private long getCountWithoutProcess(String countSql, Object[] values) {
		long count = jdbcTemplate.queryForObject(countSql,values,Long.class);
		return count;
	}

	
	/**
	 * 
	 * @Description 
	 * @param pageNo
	 * @param pageSize
	 * @param sql
	 * @param clz
	 * @param values
	 * @return
	 * @Date 2015-11-20 上午10:22:40
	 */
	private <T> List<T> getListContainsWithSql(int pageNo, int pageSize,final int withSqlParamsNum, String withSql,String sql, final Class<T> clz , final Object... values) {
		sql = withSql + "select TOP " + pageSize + " tmp___1.* from  (" + sql + " ) tmp___1 WHERE tmp___1.id NOT IN (SELECT TOP " + (pageSize * (pageNo - 1)) + " tmp___2.id FROM (" + sql + ") tmp___2)";
		List<T> lst = jdbcTemplate.query(sql, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException {
				int k = 1;
				if (values != null) {
					for (int i = 0; i < withSqlParamsNum; i++) {
						ps.setObject(k++, values[i]);
					}
					for (int i = withSqlParamsNum; i < values.length; i++) {
						ps.setObject(k++, values[i]);
					}
					for (int i = withSqlParamsNum; i < values.length; i++) {
						ps.setObject(k++, values[i]);
					}
				}
			}
		}, new BeanPropertyRowMapper<T>(clz));
		return lst;
	}
	

	/**
	 * @Description:查询的结果集
	 * @param pageNum
	 * @param pageSize
	 * @param sql
	 * @param clz
	 * @param values
	 * @return
	 * @Date:2015年10月16日下午4:17:58
	 *
	 */
	public <T> List<T> getList(String sql, final Class<T> clz , final Object... values) {
		List<T> lst = jdbcTemplate.query(sql, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException {
				int k = 1;
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						ps.setObject(k++, values[i]);
					}
				}
			}
		}, new BeanPropertyRowMapper<T>(clz));
		return lst;
	}
}

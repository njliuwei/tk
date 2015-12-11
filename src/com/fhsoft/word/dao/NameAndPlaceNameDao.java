package com.fhsoft.word.dao;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.fhsoft.base.bean.Page;
import com.fhsoft.base.dao.BaseDao;
import com.fhsoft.model.PlaceName;
import com.fhsoft.model.SubjectProperty;
import com.fhsoft.model.Word;

/**
 * 
 * @Classname com.fhsoft.word.dao.NameAndPlaceNameDao
 * @Description 
 *
 * @author lw
 * @Date 2015-11-17 上午11:18:39
 *
 */
@Repository
public class NameAndPlaceNameDao extends BaseDao {
	/**
	 * 
	 * @Description 
	 * @param pageNo
	 * @param pageSize
	 * @param place
	 * @return
	 * @Date 2015-11-17 上午11:18:46
	 */
	public Page list(int pageNo, int pageSize,PlaceName place){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM people_and_place WHERE 1=1 ");
		List<String> params = new ArrayList<String>();
		if(StringUtils.isNotBlank(place.getName())) {
			sql.append("AND NAME=? ");
			params.add(place.getName());
		}
		if(StringUtils.isNotBlank(place.getCname())) {
			sql.append("AND SOUNDMARK=? ");
			params.add(place.getCname());
		}
		return pageQuery(pageNo, pageSize, sql.toString(),PlaceName.class,params.toArray());
	}

	/**
	 * 
	 * @Description 
	 * @param place
	 * @Date 2015-11-17 上午11:22:53
	 */
	public void addName(PlaceName place) {
		String sql = "INSERT INTO people_and_place(NAME,CNAME,TYPE,LASTMODIFIED,CREATED)VALUES(?,?,?,"
				+ "(select CONVERT(varchar, getdate(), 120 )),(select CONVERT(varchar, getdate(), 120 )))";
		jdbcTemplate.update(sql, new Object[]{place.getName(),place.getCname(),place.getType()});
	}

	/**
	 * 
	 * @Description 
	 * @param place
	 * @Date 2015-11-17 上午11:22:57
	 */
	public void updateName(PlaceName place) {
		String sql = "UPDATE people_and_place SET TYPE=?,CNAME=?,"
				+ "LASTMODIFIED=(select CONVERT(varchar, getdate(), 120 )) WHERE ID=?";
		jdbcTemplate.update(sql,new Object[]{place.getType(),place.getCname(),place.getId()});
	}

	/**
	 * 
	 * @Description 
	 * @param place
	 * @Date 2015-11-17 上午11:23:02
	 */
	public void delName(PlaceName place) {
		String sql = "DELETE people_and_place WHERE ID=?";
		String sql2 = "DELETE FROM textbook_word WHERE word_id=? and type=4";
		jdbcTemplate.update(sql,new Object[]{place.getId()});
		jdbcTemplate.update(sql2,new Object[]{place.getId()});
	}

	/**
	 * 
	 * @Description 
	 * @param list
	 * @Date 2015-11-17 上午11:23:07
	 */
	public void save(List<PlaceName> list) {
		String sql = "INSERT INTO people_and_place(NAME,CNAME,TYPE,LASTMODIFIED,CREATED)VALUES(?,?,?,"
				+ "(select CONVERT(varchar, getdate(), 120 )),(select CONVERT(varchar, getdate(), 120 )))";
		List<Object[]> args = new ArrayList<Object[]>();
		List<String> wordInfo = new ArrayList<String>();
		int[] types = new int[3];
		for(int i = 0; i < types.length; i++) {
			types[i] = Types.VARCHAR;
		}
		for(PlaceName place : list) {
			wordInfo.clear();
			wordInfo.add(place.getName());
			wordInfo.add(place.getCname());
			wordInfo.add(place.getType());
			args.add(wordInfo.toArray());
		}
		jdbcTemplate.batchUpdate(sql, args, types);
	}

	/**
	 * 
	 * @Description 
	 * @param name
	 * @return
	 * @Date 2015-11-17 上午11:23:12
	 */
	public List<Word> getWordByName(String name) {
		String sql = "SELECT * FROM people_and_place WHERE NAME=?";
		return getList(sql,Word.class,new Object[]{name});
	}
	
	/**
	 * 
	 * @Description 
	 * @param name
	 * @return
	 * @Date 2015-11-17 上午11:23:12
	 */
	public List<PlaceName> getWordById(PlaceName name) {
		String sql = "SELECT * FROM people_and_place WHERE id=?";
		return getList(sql,PlaceName.class,new Object[]{name.getId()});
	}

	/**
	 * 
	 * @Description 得到语文的教材体系
	 * @return
	 * @Date 2015-12-7 下午5:14:39
	 */
	public List<SubjectProperty> getJctxOfYw() {
		StringBuffer sql = new StringBuffer();
		sql.append("select id,name from subject_property where parent_id in(");
		sql.append("SELECT id FROM subject_property WHERE name='教材体系' and PARENT_ID=");
		sql.append("(SELECT ID FROM SUBJECT_PROPERTY WHERE SUBJECT_ID=");
		sql.append("(SELECT ID FROM SUBJECT WHERE NAME='高中英语') and name='题目'))");
		return this.getList(sql.toString(), SubjectProperty.class);
	}
	
	/**
	 * 
	 * @Description 得到字词的教材体系
	 * @param word
	 * @return
	 * @Date 2015-12-7 下午5:17:24
	 */
	public List<Word> getWordJctx(PlaceName name) {
		StringBuffer sql = new StringBuffer();
		sql.append("with tmp(id,parent_id,name) as(");
		sql.append("select id,parent_id,cast(name as varchar(max)) from subject_property where id ");
		sql.append("in (select jctx_id from textbook_word where word_id=? and type =4)");
		sql.append("union all select child.id,child.parent_id,cast((parent.name+'-'+child.name) as varchar(max)) ");
		sql.append("from subject_property child,tmp parent where child.parent_id=parent.id)");
		sql.append("select sp.id,sp.name,tp.name type  ");
		sql.append("from textbook_word tw join people_and_place w on(tw.word_id=w.id and tw.type=4) ");
		sql.append("join subject_property sp on tw.jctx_id=sp.id ");
		sql.append("join tmp tp on tw.textbook_id=tp.id where w.id=?");
		return this.getList(sql.toString(), Word.class,new Object[]{name.getId(),name.getId()});
	}
}

package com.fhsoft.word.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import com.fhsoft.base.bean.Page;
import com.fhsoft.base.dao.BaseDao;
import com.fhsoft.model.SubjectProperty;
import com.fhsoft.model.Word;

/**
 * 
 * @Classname com.fhsoft.word.dao.ModenWordDao
 * @Description 
 *
 * @author lw
 * @Date 2015-11-5 下午3:22:17
 *
 */
@Repository
public class ModenWordDao extends BaseDao {
	/**
	 * 
	 * @Description 
	 * @param pageNo
	 * @param pageSize
	 * @param values
	 * @return
	 * @Date 2015-11-5 下午3:22:11
	 */
	public Page list(int pageNo, int pageSize,Word word){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT NAME,SOUNDMARK,COMPONENT,CBHS,BHS,ID,CREATED,TYPE,BUILDINGMETHOD, ");
		sql.append("STUFF((SELECT ',{\"ID\":\"'+CAST(ID AS VARCHAR)+'\",\"EXPLAIN\":\"' +ISNULL(EXPLAIN,'')+");
		sql.append("'\",\"PROPERTY\":\"' +ISNULL(PROPERTY,'')+'\",\"SORT\":\"'+CAST(SORT AS VARCHAR)+'\"}' ");
		sql.append("FROM word_additional WHERE wid=w.id ORDER BY SORT ");
		sql.append("FOR XML PATH('')),1,1,'') EXPLAIN ");
		sql.append("FROM WORD  W WHERE 1=1 ");
		List<String> params = new ArrayList<String>();
		if(StringUtils.isNotBlank(word.getName())) {
			sql.append("AND NAME=? ");
			params.add(word.getName());
		}
		if(StringUtils.isNotBlank(word.getComponent())) {
			sql.append("AND COMPONENT=? ");
			params.add(word.getComponent());
		}
		if(StringUtils.isNotBlank(word.getSoundmark())) {
			sql.append("AND SOUNDMARK=? ");
			params.add(word.getSoundmark());
		}
		return pageQuery(pageNo, pageSize, sql.toString(),"select count(1) FROM(SELECT NAME "+sql.substring(sql.lastIndexOf("FROM"))+")A",Word.class,params.toArray());
	}

	/**
	 * 
	 * @Description 
	 * @param word
	 * @Date 2015-11-5 下午3:22:07
	 */
	public int addWord(final Word word) {
		final String sql = "INSERT INTO WORD(NAME,TYPE,SOUNDMARK,COMPONENT,BUILDINGMETHOD,CBHS,BHS,CREATED,LASTMODIFIED)VALUES(?,?,?,?,?,?,?,(select CONVERT(varchar, getdate(), 120 ) ),(select CONVERT(varchar, getdate(), 120 ) ))";
//		jdbcTemplate.update(sql, new Object[]{word.getName(),word.getType(),word.getSoundmark(),
//				word.getComponent(),word.getBuildingMethod()});
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con)throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				ps.setObject(1, word.getName());
				ps.setObject(2, word.getType());
				ps.setObject(3, word.getSoundmark());
				ps.setObject(4, word.getComponent());
				ps.setObject(5, word.getBuildingMethod());
				ps.setObject(6, word.getCbhs());
				ps.setObject(7, word.getBhs());
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}
	
	/**
	 * 添加字词附加信息
	 * @param word
	 * @param wid 字词ID
	 */
	public void addWordAdditional(Word word,String wid) {
		String sql = "INSERT INTO word_additional(PROPERTY,EXPLAIN,SXC,WID,SORT)VALUES(?,?,?,?,ISNULL((SELECT COUNT(1) FROM word_additional WHERE WID=?) + 1,1))";
		jdbcTemplate.update(sql, new Object[]{word.getProperty(),word.getExplain(),word.getSxc(),wid,wid});
	}

	public void updateWord(Word word) {
		String sql = "UPDATE WORD SET TYPE=?,SOUNDMARK=?,COMPONENT=?,BUILDINGMETHOD=?,CBHS=?, BHS=?, LASTMODIFIED=(select CONVERT(varchar, getdate(), 120 )) WHERE ID=?";
		jdbcTemplate.update(sql,new Object[]{word.getType(),word.getSoundmark(),word.getComponent(),word.getBuildingMethod(),word.getCbhs(),word.getBhs(),word.getId()});
	}

	/**
	 * 
	 * @Description 
	 * @param word
	 * @Date 2015-11-6 上午9:58:38
	 */
	public void delWord(Word word) {
		String sql = "DELETE WORD WHERE ID =?";
		String sql2 = "DELETE word_example where wid in(select id from word_additional where wid=?) ";
		String sql3 = "DELETE FROM WORD_ADDITIONAL WHERE WID=?";
		String sql4 = "DELETE FROM textbook_word WHERE word_id=? and type=1";
		jdbcTemplate.update(sql,new Object[]{word.getId()});
		jdbcTemplate.update(sql2,new Object[]{word.getId()});
		jdbcTemplate.update(sql3,new Object[]{word.getId()});
		jdbcTemplate.update(sql4,new Object[]{word.getId()});
	}

	/**
	 * 
	 * @Description 
	 * @param list
	 * @Date 2015-11-16 上午9:23:18
	 */
	public void save(List<Word> list) {
		String sql = "INSERT INTO WORD(NAME,TYPE,SOUNDMARK,COMPONENT,BUILDINGMETHOD,PROPERTY,EXPLAIN,SORT,CBHS,BHS,CREATED)"
				+ "VALUES(?,?,?,?,?,?,?,ISNULL((select max(sort) + 1 from WORD where name=? and soundmark=?),1),?,?,(SELECT CONVERT(char(19), getdate(), 120)))";
		List<Object[]> args = new ArrayList<Object[]>();
		List<String> wordInfo = new ArrayList<String>();
		int[] types = new int[11];
		for(int i = 0; i < types.length; i++) {
			types[i] = Types.VARCHAR;
		}
		for(Word word : list) {
			wordInfo.clear();
			wordInfo.add(word.getName());
			wordInfo.add(word.getType());
			wordInfo.add(word.getSoundmark());
			wordInfo.add(word.getComponent());
			wordInfo.add(word.getBuildingMethod());
			wordInfo.add(word.getProperty());
			wordInfo.add(word.getExplain());
			wordInfo.add(word.getName());
			wordInfo.add(word.getSoundmark());
			wordInfo.add(word.getCbhs());
			wordInfo.add(word.getBhs());
			args.add(wordInfo.toArray());
		}
		jdbcTemplate.batchUpdate(sql, args, types);
	}

	public List<Word> getWordByName(String name) {
		String sql = "SELECT * FROM WORD WHERE NAME=?";
		return getList(sql,Word.class,new Object[]{name});
	}

	/**
	 * 
	 * @Description 
	 * @param word
	 * @return
	 * @Date 2015-12-4 上午11:27:50
	 */
	public List<Word> getWordInfo(Word word) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ID,NAME,SOUNDMARK,COMPONENT,CBHS,BHS,BUILDINGMETHOD,CREATED,CREATOR,LASTMODIFIED,");
		sql.append("STUFF((SELECT ',{\"ID\":\"' + CAST(ID AS VARCHAR) + '\",\"PROPERTY\":\"' +ISNULL(PROPERTY,'')+'\",\"EXPLAIN\":\"'+ISNULL(EXPLAIN,'')+'\"");
		sql.append(",\"SXC\":\"' +ISNULL(SXC,'') + '\",\"SORT\":\"' + CAST(SORT AS VARCHAR) + '\",\"EXAMPLES\":['");
		sql.append("+ ISNULL(STUFF((SELECT ',{\"ID\":\"' + CAST(ID AS VARCHAR) + '\",\"TEXT\":\"' + ISNULL(TEXT,'') + ");
		sql.append("'\",\"EXAMPLE\":\"' +ISNULL(EXAMPLE,'') + '\"}' FROM word_example WHERE WID=WA.ID FOR XML PATH('')),1,1,''),'') + ']}'");
		sql.append("FROM word_additional wa WHERE WID=W.ID FOR XML PATH('')),1,1,'') EXAMPLES FROM WORD W ");
		sql.append("WHERE ID =? ");
		return getList(sql.toString(),Word.class,new Object[]{word.getId()});
	}
	
	/**
	 * 查看数据库中是否已经存在相同信息的字词，此三个字段确定唯一一个字词
	 * @param name
	 * @param soundmark
	 * @param meaning
	 * @return
	 */
	public int getWordIsSingle(String name,String soundmark,String meaning) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT count(1) FROM WORD W JOIN word_additional WA ON W.ID=WA.WID WHERE w.name=? and wa.explain=? AND w.SOUNDMARK=?");
		return this.jdbcTemplate.queryForObject(sql.toString(), new String[]{name,meaning,soundmark},Integer.class);
	}
	
	/**
	 * 昨到基础字词
	 * @param name
	 * @param soundmark
	 * @return
	 */
	public Word getBasicWord(String name,String soundmark) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM WORD WHERE name=? AND SOUNDMARK=?");
		List<Word> ws = this.getList(sql.toString(), Word.class, new Object[]{name,soundmark});
		if(ws.size() > 0 ) {
			return ws.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @Description 
	 * @param word
	 * @Date 2015-12-7 上午10:17:41
	 */
	public void updateAdditionalWord(Word word) {
		String sql = "UPDATE word_additional SET SXC=?, PROPERTY=?,EXPLAIN=? WHERE ID=? ";
		jdbcTemplate.update(sql,new Object[]{word.getSxc(),word.getProperty(),word.getExplain(),word.getId()});
	}

	/**
	 * 
	 * @Description 
	 * @param word
	 * @Date 2015-12-7 上午11:39:22
	 */
	public int addExample(final Word word) {
		final String sql = "INSERT INTO word_example(TEXT,EXAMPLE,WID) VALUES(?,?,?)";
		//jdbcTemplate.update(sql,new Object[]{word.getExplain(),word.getExample(),word.getId()});
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con)throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				ps.setObject(1, word.getExplain());
				ps.setObject(2, word.getExample());
				ps.setObject(3, word.getId());
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}

	/**
	 * 
	 * @Description 
	 * @param word
	 * @return
	 * @Date 2015-12-7 下午2:44:14
	 */
	public Word getExampleById(Word word) {
		String sql = "SELECT TEXT EXPLAIN,EXAMPLE,ID FROM word_example WHERE ID=? ";
		return this.jdbcTemplate.queryForObject(sql, new Object[]{word.getId()},new BeanPropertyRowMapper<Word>(Word.class));
	}

	/**
	 * 
	 * @Description 
	 * @param word
	 * @Date 2015-12-7 下午2:57:59
	 */
	public void updExampleWord(Word word) {
		String sql = "UPDATE word_example SET TEXT=?,EXAMPLE=? WHERE ID=?";
		jdbcTemplate.update(sql,new Object[]{word.getExplain(),word.getExample(),word.getId()});
	}

	/**
	 * 
	 * @Description 
	 * @param word
	 * @Date 2015-12-7 下午3:12:32
	 */
	public void delExample(Word word) {
		String sql = "DELETE WORD_EXAMPLE WHERE ID=? ";
		jdbcTemplate.update(sql,new Object[]{word.getId()});
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
		sql.append("(SELECT ID FROM SUBJECT WHERE NAME='高中语文') and name='题目'))");
		return this.getList(sql.toString(), SubjectProperty.class);
	}
	
	/**
	 * 
	 * @Description 得到字词的教材体系
	 * @param word
	 * @return
	 * @Date 2015-12-7 下午5:17:24
	 */
	public List<Word> getWordJctx(Word word) {
		StringBuffer sql = new StringBuffer();
		sql.append("with tmp(id,parent_id,name) as(");
		sql.append("select id,parent_id,cast(name as varchar(max)) from subject_property where id ");
		sql.append("in (select jctx_id from textbook_word where word_id=? and type =1)");
		sql.append("union all select child.id,child.parent_id,cast((parent.name+'-'+child.name) as varchar(max)) ");
		sql.append("from subject_property child,tmp parent where child.parent_id=parent.id)");
		sql.append("select sp.id,sp.name,tp.name type  ");
		sql.append("from textbook_word tw join word w on(tw.word_id=w.id and tw.type=1) ");
		sql.append("join subject_property sp on tw.jctx_id=sp.id ");
		sql.append("join tmp tp on tw.textbook_id=tp.id where w.id=?");
		return this.getList(sql.toString(), Word.class,new Object[]{word.getId(),word.getId()});
	}
}

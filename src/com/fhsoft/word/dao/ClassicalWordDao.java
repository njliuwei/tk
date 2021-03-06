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
import com.fhsoft.model.Users;
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
public class ClassicalWordDao extends BaseDao {
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
		sql.append("SELECT NAME,COMPONENT,CBHS,BHS,ID,CREATED,TYPE,BUILDINGMETHOD, ");
		sql.append("STUFF((SELECT ',{\"ID\":\"'+CAST(ID AS VARCHAR)+'\",\"PROPERTY\":\"' +ISNULL(PROPERTY,'')+'\"");
		sql.append(",\"MEANING\":\"' +ISNULL(MEANING,'')+'\",\"SORT\":\"'+CAST(SORT AS VARCHAR)+'\",\"SOUNDMARK\":\"'+ISNULL(SOUNDMARK,'')+'\"}' ");
		sql.append("FROM classicalword_additional WHERE wid=w.id ORDER BY SORT ");
		sql.append("FOR XML PATH('')),1,1,'') EXPLAIN ");
		sql.append("FROM classical_word  W WHERE 1=1 ");
		List<String> params = new ArrayList<String>();
		if(StringUtils.isNotBlank(word.getName())) {
			sql.append("AND NAME=? ");
			params.add(word.getName());
		}
		return pageQuery(pageNo, pageSize, sql.toString(),"select count(1) FROM(SELECT NAME "+sql.substring(sql.lastIndexOf("FROM"))+")A",Word.class,params.toArray());
	}

	/**
	 * 
	 * @Description 
	 * @param word
	 * @param u 
	 * @Date 2015-11-5 下午3:22:07
	 */
	public int addWord(final Word word, final Users u) {
		final String sql = "INSERT INTO classical_word(NAME,TYPE,COMPONENT,BUILDINGMETHOD,CBHS,BHS,CREATED,LASTMODIFIED,CREATOR,CREATOR_ID," +
				"LASTMODIFIER,LASTMODIFIER_ID)VALUES(?,?,?,?,?,?,(select CONVERT(varchar, getdate(), 120 ) ),(select CONVERT(varchar, getdate(), 120 ) ),?,?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con)throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				ps.setObject(1, word.getName());
				ps.setObject(2, word.getType());
				ps.setObject(3, word.getComponent());
				ps.setObject(4, word.getBuildingMethod());
				ps.setObject(5, word.getCbhs());
				ps.setObject(6, word.getBhs());
				ps.setObject(7, u.getName());
				ps.setObject(8, u.getId());
				ps.setObject(9, u.getName());
				ps.setObject(10, u.getId());
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
	public int addWordAdditional(final Word word,final String wid) {
		final String sql = "INSERT INTO classicalword_additional(SOUNDMARK,PROPERTY,MEANING,SXC,MEANINGINFO,WID,SORT)VALUES(?,?,?,?,?,?,ISNULL((SELECT COUNT(1) FROM classicalword_additional WHERE WID=?) + 1,1))";
		//jdbcTemplate.update(sql, new Object[]{word.getSoundmark(),word.getProperty(),word.getExplain(),word.getSxc(),word.getMeaningInfo(),wid,wid});
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con)throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				ps.setObject(1, word.getSoundmark());
				ps.setObject(2, word.getProperty());
				ps.setObject(3, word.getMeaning());
				ps.setObject(4, word.getSxc());
				ps.setObject(5, word.getMeaningInfo());
				ps.setObject(6, wid);
				ps.setObject(7, wid);
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}

	public void updateWord(Word word) {
		String sql = "UPDATE classical_word SET TYPE=?,COMPONENT=?,BUILDINGMETHOD=?,CBHS=?, BHS=?" +
				",LASTMODIFIER_ID=?,LASTMODIFIER=?,LASTMODIFIED=(select CONVERT(varchar, getdate(), 120 )) WHERE ID=?";
		jdbcTemplate.update(sql,new Object[]{word.getType(),word.getComponent(),word.getBuildingMethod(),word.getCbhs(),word.getBhs(),
				word.getLastModifierId(),word.getLastModifier(),word.getId()});
	}

	/**
	 * 
	 * @Description 
	 * @param word
	 * @Date 2015-11-6 上午9:58:38
	 */
	public void delWord(Word word) {
		String sql = "DELETE classical_word WHERE ID =?";
		String sql2 = "DELETE classicalword_example where wid in(select id from classicalword_additional where wid=?) ";
		String sql3 = "DELETE FROM classicalword_additional WHERE WID=?";
		String sql4 = "DELETE FROM textbook_word WHERE word_id=? and type=2";
		jdbcTemplate.update(sql,new Object[]{word.getId()});
		jdbcTemplate.update(sql2,new Object[]{word.getId()});
		jdbcTemplate.update(sql3,new Object[]{word.getId()});
		jdbcTemplate.update(sql4,new Object[]{word.getId()});
	}

	public Word getWordByName(String name) {
		String sql = "SELECT * FROM classical_word WHERE NAME=?";
		List<Word> ws = getList(sql,Word.class,new Object[]{name});
		if(ws.size() > 0) {
			return ws.get(0);
		}
		return null;
	}

	public void save(List<Word> list) {
		String sql = "INSERT INTO classical_word(NAME,TYPE,SOUNDMARK,PROPERTY,SORT,MEANING,EXAMPLE)VALUES(?,?,?,?,ISNULL((select max(sort) + 1 from classical_word where name=?),1),?,?)";
		List<Object[]> args = new ArrayList<Object[]>();
		List<String> wordInfo = new ArrayList<String>();
		int[] types = new int[7];
		for(int i = 0; i < types.length; i++) {
			types[i] = Types.VARCHAR;
		}
		for(Word word : list) {
			wordInfo.clear();
			wordInfo.add(word.getName());
			wordInfo.add(word.getType());
			wordInfo.add(word.getSoundmark());
			wordInfo.add(word.getProperty());
			wordInfo.add(word.getName());
			wordInfo.add(word.getMeaning());
			wordInfo.add(word.getExample());
			args.add(wordInfo.toArray());
		}
		jdbcTemplate.batchUpdate(sql, args, types);
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
		sql.append("SELECT ID,NAME,COMPONENT,CBHS,BHS,BUILDINGMETHOD,CREATED,CREATOR,LASTMODIFIED,");
		sql.append("STUFF((SELECT ',{\"ID\":\"' + CAST(ID AS VARCHAR) + '\",\"PROPERTY\":\"' +ISNULL(PROPERTY,'')+'\",\"MEANING\":\"'+ISNULL(MEANING,'')+'\"");
		sql.append(",\"SOUNDMARK\":\"' +ISNULL(SOUNDMARK,'')+'\",\"MEANINGINFO\":\"'+ISNULL(MEANINGINFO,'')+'\"");
		sql.append(",\"SXC\":\"' +ISNULL(SXC,'') + '\",\"SORT\":\"' + CAST(SORT AS VARCHAR) + '\",\"EXAMPLES\":['");
		sql.append("+ ISNULL(STUFF((SELECT ',{\"ID\":\"' + CAST(ID AS VARCHAR) + '\",\"TEXT\":\"' + ISNULL(TEXT,'') + ");
		sql.append("'\",\"EXAMPLE\":\"' +ISNULL(EXAMPLE,'') + '\"}' FROM classicalword_example WHERE WID=WA.ID FOR XML PATH('')),1,1,''),'') + ']}'");
		sql.append("FROM classicalword_additional wa WHERE WID=W.ID FOR XML PATH('')),1,1,'') EXAMPLES FROM classical_word W ");
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
		sql.append("SELECT count(1) FROM classical_word W JOIN classicalword_additional WA ON W.ID=WA.WID WHERE w.name=? and wa.meaning=? AND WA.SOUNDMARK=?");
		return this.jdbcTemplate.queryForObject(sql.toString(), new String[]{name,meaning,soundmark},Integer.class);
	}
	
	/**
	 * 
	 * @Description 
	 * @param word
	 * @Date 2015-12-7 上午10:17:41
	 */
	public void updateAdditionalWord(Word word) {
		String sql = "UPDATE classicalword_additional SET SXC=?, PROPERTY=?,MEANING=?,MEANINGINFO=?,SOUNDMARK=? WHERE ID=? ";
		jdbcTemplate.update(sql,new Object[]{word.getSxc(),word.getProperty(),word.getMeaning(),word.getMeaningInfo(),word.getSoundmark(),word.getId()});
	}

	/**
	 * 
	 * @Description 
	 * @param word
	 * @Date 2015-12-7 上午11:39:22
	 */
	public int addExample(final Word word) {
		final String sql = "INSERT INTO classicalword_example(TEXT,EXAMPLE,WID) VALUES(?,?,?)";
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
		String sql = "SELECT TEXT EXPLAIN,EXAMPLE,ID FROM classicalword_example WHERE ID=? ";
		return this.jdbcTemplate.queryForObject(sql, new Object[]{word.getId()},new BeanPropertyRowMapper<Word>(Word.class));
	}

	/**
	 * 
	 * @Description 
	 * @param word
	 * @Date 2015-12-7 下午2:57:59
	 */
	public void updExampleWord(Word word) {
		String sql = "UPDATE classicalword_example SET TEXT=?,EXAMPLE=? WHERE ID=?";
		jdbcTemplate.update(sql,new Object[]{word.getExplain(),word.getExample(),word.getId()});
	}

	/**
	 * 
	 * @Description 
	 * @param word
	 * @Date 2015-12-7 下午3:12:32
	 */
	public void delExample(Word word) {
		String sql = "DELETE classicalword_example WHERE ID=? ";
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
		sql.append("in (select jctx_id from textbook_word where word_id=? and type =2)");
		sql.append("union all select child.id,child.parent_id,cast((parent.name+'-'+child.name) as varchar(max)) ");
		sql.append("from subject_property child,tmp parent where child.parent_id=parent.id)");
		sql.append("select sp.id,sp.name,tp.name type  ");
		sql.append("from textbook_word tw join classical_word w on(tw.word_id=w.id and tw.type=2) ");
		sql.append("join subject_property sp on tw.jctx_id=sp.id ");
		sql.append("join tmp tp on tw.textbook_id=tp.id where w.id=?");
		return this.getList(sql.toString(), Word.class,new Object[]{word.getId(),word.getId()});
	}

	public Word getClassicalWordBasic(Word word) {
		String sql = "SELECT * FROM classical_word WHERE ID=? ";
		return this.jdbcTemplate.queryForObject(sql, new Object[]{word.getId()},new BeanPropertyRowMapper<Word>(Word.class));
	}
	
	public Word getClassicalWordAdditional(Word word) {
		String sql = "SELECT * FROM classicalword_additional WHERE ID=? ";
		return this.jdbcTemplate.queryForObject(sql, new Object[]{word.getId()},new BeanPropertyRowMapper<Word>(Word.class));
	}
}

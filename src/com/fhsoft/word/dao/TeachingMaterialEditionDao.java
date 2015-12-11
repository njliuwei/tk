package com.fhsoft.word.dao;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import com.fhsoft.base.bean.Page;
import com.fhsoft.base.dao.BaseDao;
import com.fhsoft.model.TextBookWord;
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
public class TeachingMaterialEditionDao extends BaseDao {
	/**
	 * 
	 * @Description 
	 * @param pageNo
	 * @param pageSize
	 * @param word
	 * @param type
	 * @return
	 * @Date 2015-11-18 下午3:48:31
	 */
	public Page list(int pageNo, int pageSize,TextBookWord word){
		StringBuffer withSql = new StringBuffer();
		StringBuffer sql = new StringBuffer();
		List<String> params = new ArrayList<String>();
		params.add(word.getTextbookId());
		withSql.append("WITH TMP(ID,PARENT_ID,ALLNAME)AS(SELECT ID,PARENT_ID,");
		withSql.append("CAST(NAME AS VARCHAR(1000)) FROM SUBJECT_PROPERTY WHERE ID=? ");
		withSql.append("UNION ALL SELECT CHILD.ID,CHILD.PARENT_ID,");
		withSql.append("CAST((PARENT.ALLNAME+'-'+CHILD.NAME) AS VARCHAR(1000)) ");
		withSql.append("FROM SUBJECT_PROPERTY CHILD,TMP PARENT WHERE CHILD.PARENT_ID=PARENT.ID) ");
		sql.append("SELECT * FROM(");
		sql.append("SELECT W.NAME WORDNAME,T.ALLNAME TEXTBOOKNAME,TW.ID,TW.TYPE ");
		sql.append("FROM TEXTBOOK_WORD TW JOIN TMP T ON TW.TEXTBOOK_ID=T.ID ");
		sql.append("JOIN WORD W ON TW.WORD_ID=W.ID WHERE TW.TYPE='1' ");
		if(StringUtils.isNotBlank(word.getWordName())) {
			sql.append("AND W.NAME=? ");
			params.add(word.getWordName());
		}
		sql.append("UNION ALL SELECT W.NAME WORDNAME,T.ALLNAME TEXTBOOKNAME,TW.ID,TW.TYPE ");
		sql.append("FROM TEXTBOOK_WORD TW JOIN TMP T ON TW.TEXTBOOK_ID=T.ID ");
		sql.append("JOIN CLASSICAL_WORD W ON TW.WORD_ID=W.ID WHERE TW.TYPE='2' ");
		if(StringUtils.isNotBlank(word.getWordName())) {
			sql.append("AND W.NAME=? ");
			params.add(word.getWordName());
		}
		sql.append("UNION ALL SELECT W.NAME WORDNAME,T.ALLNAME TEXTBOOKNAME,TW.ID,TW.TYPE ");
		sql.append("FROM TEXTBOOK_WORD TW JOIN TMP T ON TW.TEXTBOOK_ID=T.ID ");
		sql.append("JOIN ENGLISH_WORD W ON TW.WORD_ID=W.ID WHERE TW.TYPE='3' ");
		if(StringUtils.isNotBlank(word.getWordName())) {
			sql.append("AND W.NAME=? ");
			params.add(word.getWordName());
		}
		sql.append("UNION ALL SELECT W.NAME WORDNAME,T.ALLNAME TEXTBOOKNAME,TW.ID,TW.TYPE ");
		sql.append("FROM TEXTBOOK_WORD TW JOIN TMP T ON TW.TEXTBOOK_ID=T.ID ");
		sql.append("JOIN PEOPLE_AND_PLACE W ON TW.WORD_ID=W.ID WHERE TW.TYPE='4' ");
		if(StringUtils.isNotBlank(word.getWordName())) {
			sql.append("AND W.NAME=? ");
			params.add(word.getWordName());
		}
		sql.append(")A ");
		if(StringUtils.isNotBlank(word.getType())) {
			sql.append("WHERE TYPE=? ");
			params.add(word.getType());
		}
		return pageQueryContainsWithSql(pageNo, pageSize, 1, withSql.toString(), sql.toString(),withSql.toString() + sql.toString().replace("*", "COUNT(1)"),TextBookWord.class,params.toArray());
	}

	/**
	 * 
	 * @Description 
	 * @param word
	 * @Date 2015-11-18 下午3:48:39
	 */
	public void addWord(TextBookWord word) {
		String sql = "INSERT INTO textbook_word(textbook_id,word_id,type,jctx_id)VALUES(?,?,?,?)";
		jdbcTemplate.update(sql, new Object[]{word.getTextbookId(),word.getWordId(),word.getType(),word.getJctxId()});
	}

	/**
	 * 
	 * @Description 
	 * @param word
	 * @Date 2015-11-6 上午9:58:38
	 */
	public void delWord(Word word) {
		String sql = "DELETE textbook_word WHERE ID=?";
		jdbcTemplate.update(sql,new Object[]{word.getId()});
	}

	public Page wordList(int pageNo, int pageSize, String table,String name,String jctxId) {
		StringBuffer sql = new StringBuffer();
		List<String> params = new ArrayList<String>();
		String type = "4";
		if("word".equals(table)) {
			type = "1";
		} else if("classical_word".equals(table)) {
			type = "2";
		} else if("english_word".equals(table)) {
			type = "3";
		}
		sql.append("SELECT WORD.NAME,WORD.ID FROM ").append(table).append(" WORD ");
		sql.append("LEFT JOIN textbook_word TW ON (WORD.ID=TW.WORD_ID AND TW.JCTX_ID=? AND TW.TYPE=?) ");
		sql.append("WHERE TW.WORD_ID IS NULL ");
		params.add(jctxId);
		params.add(type);
		if(StringUtils.isNotBlank(name)) {
			sql.append("AND WORD.NAME=? ");
			params.add(name);
		}
		return pageQuery(pageNo, pageSize, sql.toString(), Word.class, params.toArray());
	}

	/**
	 * 
	 * @Description 查询对应的字词在对应的教材体系中是否已存在 
	 * @param word
	 * @return
	 * @Date 2015-11-20 下午2:44:17
	 */
	public boolean isExists(TextBookWord word) {
		return false;
	}
}

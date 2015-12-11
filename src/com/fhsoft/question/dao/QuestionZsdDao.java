package com.fhsoft.question.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.fhsoft.base.dao.BaseDao;
import com.fhsoft.model.Zsd;

/**
 * 
 * @Classname com.fhsoft.question.dao.QuestionZsdDao
 * @Description 
 *
 * @author lw
 * @Date 2015-10-22 下午5:13:48
 *
 */
@Repository
public class QuestionZsdDao extends BaseDao {
	
	/**
	 * 
	 * @Description 
	 * @param values
	 * @return
	 * @Date 2015-10-22 下午5:09:53
	 */
	public List<Zsd> list(final Object... values){
		String sql = "SELECT ID,PARENT_ID,NAME,SUBJECT,CODE,is_leaf FROM KNOWLEDGE WHERE 1=1 ";
		if(values.length > 0 && values[0] != null) {
			sql += "AND TYPE=? ";
		}
		if(values.length > 1 && values[1] != null) {
			sql += "AND SUBJECT=? ";
		}
		return getList(sql,Zsd.class,values);
	}

}

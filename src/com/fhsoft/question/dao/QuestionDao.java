package com.fhsoft.question.dao;


import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.fhsoft.base.bean.Page;
import com.fhsoft.base.bean.TreeNodeBean;
import com.fhsoft.base.dao.BaseDao;
import com.fhsoft.model.QstModel;
import com.fhsoft.model.Question;
import com.fhsoft.model.QuestionAnalysis;
import com.fhsoft.model.SubjectProperty;
import com.fhsoft.model.ZsdJctxRelations;

@Repository
public class QuestionDao extends BaseDao {
	@Resource
	private Map<String,String> subjectMap;
	
	public int count(){
		String sql = "SELECT COUNT(1) FROM TKCZYW.dbo.CZYW";
		int count = jdbcTemplate.queryForObject(sql, Integer.class);
		return count;
	}
	
	/**
	 * 
	 * @Description 根据知识点或教材体系等（col）查询试题 
	 * @param pageNo 
	 * @param pageSize
	 * @param subject 学科ID
	 * @param col 做为菜单查询的列
	 * @param values 当前节点下的所有子节点
	 * @return
	 * @Date 2015-11-10 上午10:30:07
	 */
	public Page list(int pageNo, int pageSize,String subject,String col,List<String> values,Question question){
		StringBuffer sql = new StringBuffer();
		List<String> params = new ArrayList<String>();
		params.addAll(values);
		sql.append("SELECT iid id,xk,t,zsd,da,xmlda,xmltm,");
		sql.append("isnull((select stuff((select ','+name from subject_property where cast(id as varchar) in (select col from dbo.f_splitSTR(zsd,',')) for xml path('')),1,1,'')),zsd) zsdmc,");
		sql.append("isnull((select stuff((select ','+name from subject_property where cast(id as varchar) in (select col from dbo.f_splitSTR(jctx,',')) for xml path('')),1,1,'')),jctx) jctx,");
		sql.append("isnull((select stuff((select ','+name from subject_property where cast(id as varchar) in (select col from dbo.f_splitSTR(tx,',')) for xml path('')),1,1,'')),tx) tx,");
		sql.append("isnull((select stuff((select ','+name from subject_property where cast(id as varchar) in (select col from dbo.f_splitSTR(nd,',')) for xml path('')),1,1,'')),nd) nd,");
		sql.append("isnull((select stuff((select ','+name from subject_property where cast(id as varchar) in (select col from dbo.f_splitSTR(ly,',')) for xml path('')),1,1,'')),ly) ly,");
		
		sql.append("isnull((select stuff((select ','+name from subject_property where cast(id as varchar) in (select col from dbo.f_splitSTR(property1,',')) for xml path('')),1,1,'')),property1)property1,");
		sql.append("isnull((select stuff((select ','+name from subject_property where cast(id as varchar) in (select col from dbo.f_splitSTR(property2,',')) for xml path('')),1,1,'')),property2)property2,");
		sql.append("isnull((select stuff((select ','+name from subject_property where cast(id as varchar) in (select col from dbo.f_splitSTR(property3,',')) for xml path('')),1,1,'')),property3)property3,");
		sql.append("isnull((select stuff((select ','+name from subject_property where cast(id as varchar) in (select col from dbo.f_splitSTR(property4,',')) for xml path('')),1,1,'')),property4)property4,");
		sql.append("isnull((select stuff((select ','+name from subject_property where cast(id as varchar) in (select col from dbo.f_splitSTR(property5,',')) for xml path('')),1,1,'')),property5)property5,");
		sql.append("isnull((select stuff((select ','+name from subject_property where cast(id as varchar) in (select col from dbo.f_splitSTR(property6,',')) for xml path('')),1,1,'')),property6)property6,");
		sql.append("isnull((select stuff((select ','+name from subject_property where cast(id as varchar) in (select col from dbo.f_splitSTR(property7,',')) for xml path('')),1,1,'')),property7)property7,");
		sql.append("isnull((select stuff((select ','+name from subject_property where cast(id as varchar) in (select col from dbo.f_splitSTR(property8,',')) for xml path('')),1,1,'')),property8)property8,");
		sql.append("isnull((select stuff((select ','+name from subject_property where cast(id as varchar) in (select col from dbo.f_splitSTR(property9,',')) for xml path('')),1,1,'')),property9)property9,");
		sql.append("isnull((select stuff((select ','+name from subject_property where cast(id as varchar) in (select col from dbo.f_splitSTR(property10,',')) for xml path('')),1,1,'')),property10)property10 ");
		sql.append("FROM ").append(subjectMap.get(subject)).append(" ");
		sql.append(" WHERE 1=1 ");
		sql.append("AND (EXISTS (select 1 from (select col from dbo.f_splitSTR(").append(col);
//		sql.append("zsd,','))a join (select code from knowledge where code_all like ");
//		sql.append("(select code_all from knowledge where code=?) + '%' )b on a.col=b.code )");
		sql.append(",','))a  where a.col in (");
		for(int i = 0; i < values.size() - 1; i++) {
			sql.append("?,");
		}
		sql.append("?)) ");
		if("jctx".equals(col)) {
			params.addAll(values);
			sql.append(" OR EXISTS(SELECT 1 FROM ZSDJCTX WHERE JCTXID IN( ");
			for(int i = 0; i < values.size() - 1; i++) {
				sql.append("?,");
			}
			sql.append("?) ");
			sql.append("AND ZSDID IN(SELECT COL FROM DBO.F_SPLITSTR(ZSD,','))) ");
		}
		sql.append(")");
		
		if(StringUtils.isNotBlank(question.getTx())) {
			sql.append("AND TX=? ");
			params.add(question.getTx());
		}
		if(StringUtils.isNotBlank(question.getNd())) {
			sql.append("AND ND=? ");
			params.add(question.getNd());
		}
		StringBuffer countSql = new StringBuffer();
		countSql.append("SELECT COUNT(1) FROM ").append(subjectMap.get(subject)).append(" ");
		countSql.append(" WHERE (EXISTS (select 1 from (select col from dbo.f_splitSTR(").append(col);
		countSql.append(",','))a  where a.col in (");
		for(int i = 0; i < values.size() - 1; i++) {
			countSql.append("?,");
		}
		countSql.append("?))");
		if("jctx".equals(col)) {
			countSql.append(" OR EXISTS(SELECT 1 FROM ZSDJCTX WHERE JCTXID IN( ");
			for(int i = 0; i < values.size() - 1; i++) {
				countSql.append("?,");
			}
			countSql.append("?) ");
			countSql.append("AND ZSDID IN(SELECT COL FROM DBO.F_SPLITSTR(ZSD,','))) ");
		}
		countSql.append(")");
		
		if(StringUtils.isNotBlank(question.getTx())) {
			countSql.append("AND TX=? ");
		}
		if(StringUtils.isNotBlank(question.getNd())) {
			countSql.append("AND ND=? ");
		}
		return pageQuery(pageNo, pageSize, sql.toString(),countSql.toString(),Question.class,params.toArray());
	}

	/**
	 * 
	 * @Description 查询试题标引属性
	 * @param id
	 * @param subject
	 * @param name 题目或解析
	 * @return
	 * @Date 2015-10-27 上午11:01:53
	 */
	public List<SubjectProperty> getQuestionIndexesInfo(String id,String subject,String name) {
		String sql = "SELECT ID,NAME,TYPE,STATUS,LEVEL,COL FROM SUBJECT_PROPERTY WHERE 1=1 ";
		sql += "AND PARENT_ID=(SELECT ID FROM SUBJECT_PROPERTY WHERE SUBJECT_ID=? AND NAME=?) ORDER BY SORT ";
		Object[] values = {subject,name};
		return this.getList(sql, SubjectProperty.class, values);
	}

	/**
	 * 
	 * @Description 
	 * @param id
	 * @param subject
	 * @return
	 * @Date 2015-11-12 下午2:19:09
	 */
	public Question getQuestionById(String id,String subject) {
		String sql = "SELECT * FROM " + subjectMap.get(subject) + " WHERE IID=? ";
		return this.jdbcTemplate.queryForObject(sql, new Object[]{ id }, new BeanPropertyRowMapper<Question>(Question.class));
	}
	/**
	 * 
	 * @Description 
	 * @param id
	 * @return
	 * @Date 2015-10-27 下午4:10:59
	 */
	public List<TreeNodeBean> getQuestionTreeValue(String id) {
		StringBuffer sql = new StringBuffer();
		sql.append("WITH TMP(ID,PARENT_ID,NAME,SORT,LEVEL)AS(SELECT ID,PARENT_ID,NAME,SORT,LEVEL FROM subject_property WHERE ID=? ");
		sql.append("UNION ALL SELECT CHILD.ID,CHILD.PARENT_ID,CHILD.NAME,CHILD.SORT,CHILD.LEVEL FROM subject_property CHILD,");
		sql.append("TMP PARENT WHERE CHILD.PARENT_ID=PARENT.ID)");
		sql.append("SELECT ID,PARENT_ID PID,NAME TEXT,CASE (select count(1) from subject_property where parent_id=tmp.id) ");
		sql.append("WHEN 0 THEN 'open' else 'closed' END STATE FROM TMP ");
		Object[] values = {id};
		return this.getList(sql.toString(), TreeNodeBean.class, values);
	}

	/**
	 * 
	 * @Description 
	 * @param id
	 * @param subject
	 * @return
	 * @Date 2015-10-28 上午10:40:19
	 */
	public List<QuestionAnalysis> getQuestionAnalysesInfo(String id,
			String subject) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ID,ST_ID,NR,XMLNR,LX,FA,property1,property2,property3,property4,property5 ");
		sql.append("FROM ").append(subjectMap.get(subject)).append("STJX WHERE ST_ID=? ");
		Object[] values = {id};
		return this.getList(sql.toString(), QuestionAnalysis.class, values);
	}

	/**
	 * 
	 * @Description 
	 * @param question
	 * @Date 2015-10-28 下午2:30:45
	 */
	public void updateQuestion(Question question) {
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE ").append(subjectMap.get(question.getXk())).append(" SET ");
		List<String> params = new ArrayList<String>();
		if(StringUtils.isNotBlank(question.getNd())) {
			sql.append("ND=?,");
			params.add(question.getNd());
		}
		if(StringUtils.isNotBlank(question.getMemo())) {
			sql.append("MEMO=?,");
			params.add(question.getMemo());
		}
		if(StringUtils.isNotBlank(question.getTx())) {
			sql.append("TX=?,");
			params.add(question.getTx());
		}
		if(StringUtils.isNotBlank(question.getZc())) {
			sql.append("ZC=?,");
			params.add(question.getZc());
		}
		if(StringUtils.isNotBlank(question.getZsd())) {
			sql.append("ZSD=?,");
			params.add(question.getZsd());
		}
		if(StringUtils.isNotBlank(question.getZsdmc())) {
			sql.append("ZSDMC=?,");
			params.add(question.getZsdmc());
		}
		if(StringUtils.isNotBlank(question.getZcids())) {
			sql.append("ZCIDS=?,");
			params.add(question.getZcids());
		}
		if(StringUtils.isNotBlank(question.getDqs())) {
			sql.append("DQS=?,");
			params.add(question.getDqs());
		}
		if(StringUtils.isNotBlank(question.getSjs())) {
			sql.append("SJS=?,");
			params.add(question.getSjs());
		}
		if(StringUtils.isNotBlank(question.getTjxxs())) {
			sql.append("TJXXS=?,");
			params.add(question.getTjxxs());
		}
		if(StringUtils.isNotBlank(question.getSycs())) {
			sql.append("SYCS=?,");
			params.add(question.getSycs());
		}
		if(StringUtils.isNotBlank(question.getNds())) {
			sql.append("NDS=?,");
			params.add(question.getNds());
		}
		if(StringUtils.isNotBlank(question.getLy())) {
			sql.append("LY=?,");
			params.add(question.getLy());
		}
		if(StringUtils.isNotBlank(question.getDp())) {
			sql.append("DP=?,");
			params.add(question.getDp());
		}
		if(StringUtils.isNotBlank(question.getProperty1())) {
			sql.append("PROPERTY1=?,");
			params.add(question.getProperty1());
		}
		if(StringUtils.isNotBlank(question.getProperty2())) {
			sql.append("PROPERTY2=?,");
			params.add(question.getProperty2());
		}
		if(StringUtils.isNotBlank(question.getProperty3())) {
			sql.append("PROPERTY3=?,");
			params.add(question.getProperty3());
		}
		if(StringUtils.isNotBlank(question.getProperty4())) {
			sql.append("PROPERTY4=?,");
			params.add(question.getProperty4());
		}
		if(StringUtils.isNotBlank(question.getProperty5())) {
			sql.append("PROPERTY5=?,");
			params.add(question.getProperty5());
		}
		if(StringUtils.isNotBlank(question.getProperty6())) {
			sql.append("PROPERTY6=?,");
			params.add(question.getProperty6());
		}
		if(StringUtils.isNotBlank(question.getProperty7())) {
			sql.append("PROPERTY7=?,");
			params.add(question.getProperty7());
		}
		if(StringUtils.isNotBlank(question.getProperty8())) {
			sql.append("PROPERTY8=?,");
			params.add(question.getProperty8());
		}
		if(StringUtils.isNotBlank(question.getProperty9())) {
			sql.append("PROPERTY9=?,");
			params.add(question.getProperty9());
		}
		if(StringUtils.isNotBlank(question.getProperty10())) {
			sql.append("PROPERTY10=?,");
			params.add(question.getProperty10());
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(" WHERE IID=? ");
		params.add(question.getId());
		this.jdbcTemplate.update(sql.toString(), params.toArray());
	}

	/**
	 * 
	 * @Description 
	 * @param type
	 * @param subject
	 * @return
	 * @Date 2015-10-29 下午2:22:09
	 */
	public List<TreeNodeBean> getFixedTreeValue(String type, String subject) {
		StringBuffer sql = new StringBuffer();
		sql.append("with tmp(id, pid,text,state)as(");
		sql.append("select id,parent_id ,name,case is_leaf when 1 then 'open' else 'closed' end ");
		sql.append("from knowledge where parent_id=0 and subject=? and type=? ");
		sql.append("union all select child.id,child.parent_id,child.name,");
		sql.append("case child.is_leaf when 1 then 'open' else 'closed' end ");
		sql.append("from knowledge child,tmp parent where child.parent_id=parent.id)");
		sql.append("select id,pid,text,state from tmp ");
		Object[] values = {subject,type};
		return this.getList(sql.toString(), TreeNodeBean.class, values);
	}

	/**
	 * 
	 * @Description 
	 * @param index
	 * @Date 2015-11-2 上午10:32:34
	 */
	public void saveAnalysis(QuestionAnalysis analysis) {
		StringBuffer sql = new StringBuffer();
		List<String> params = new ArrayList<String>();
		sql.append("UPDATE ").append(subjectMap.get(analysis.getSubject())).append("STJX SET ");
		if(StringUtils.isNotBlank(analysis.getFa())) {
			sql.append("fa=?,");
			params.add(analysis.getFa());
		}
		if(StringUtils.isNotBlank(analysis.getLx())) {
			sql.append("lx=?,");
			params.add(analysis.getLx());
		}
		if(StringUtils.isNotBlank(analysis.getProperty1())) {
			sql.append("PROPERTY1=?,");
			params.add(analysis.getProperty1());
		}
		if(StringUtils.isNotBlank(analysis.getProperty2())) {
			sql.append("PROPERTY2=?,");
			params.add(analysis.getProperty2());
		}
		if(StringUtils.isNotBlank(analysis.getProperty3())) {
			sql.append("PROPERTY3=?,");
			params.add(analysis.getProperty3());
		}
		if(StringUtils.isNotBlank(analysis.getProperty4())) {
			sql.append("PROPERTY4=?,");
			params.add(analysis.getProperty4());
		}
		if(StringUtils.isNotBlank(analysis.getProperty5())) {
			sql.append("PROPERTY5=?,");
			params.add(analysis.getProperty5());
		}
		
		sql.deleteCharAt(sql.length() - 1);
		sql.append(" WHERE ID=? ");
		params.add(analysis.getId());
		this.jdbcTemplate.update(sql.toString(), params.toArray());
	}

	/**
	 * 
	 * @Description 
	 * @param id
	 * @param subject
	 * @param string
	 * @return
	 * @Date 2015-11-2 下午3:52:30
	 */
	public List<SubjectProperty> getQuestionSelValue(String id, String subject,String name) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT a.COL,b.name,b.id ");
		sql.append("FROM subject_property a join subject_property b on a.id=b.parent_id ");
		sql.append("WHERE a.PARENT_ID=(SELECT ID FROM subject_property WHERE SUBJECT_ID=? AND NAME=?) ");
		sql.append("AND a.TYPE in('下拉','单选','复选') and a.status='启用' order by b.sort  ");
		Object[] values = {subject,name};
		return this.getList(sql.toString(), SubjectProperty.class, values);
	}

	/**
	 * @Description:根据学科和知识点获得题目列表
	 * @param subject
	 * @param zsd
	 * @param nd 
	 * @param tx 
	 * @return
	 * @Date:2015年11月12日上午10:37:22
	 * 
	 */
	public List<Integer> getBySubjectAndZsd(String subject, String zsd, String tx, String nd) {
		StringBuffer sql = new StringBuffer();
		sql.append("select iid from ");
		sql.append(subjectMap.get(subject));
		sql.append(" a where exists (select b.col from dbo.f_splitSTR(?,',') b where exists(select col from dbo.f_splitSTR(a.zsd,',') where col = b.col)) ");
		if(null != tx && !"".equals(tx)){
			sql.append(" and exists (select col from dbo.f_splitSTR('");
			sql.append(tx);
			sql.append("',',') where col = a.tx) ");
		}
		if(null != nd && !"".equals(nd)){
			sql.append(" and exists (select col from dbo.f_splitSTR('");
			sql.append(nd);
			sql.append("',',') where col = a.nd) ");
		}
		return jdbcTemplate.queryForList(sql.toString(), new Object[]{ zsd }, Integer.class);
	}

	/**
	 * @Description:根据产品ID查询题目列表
	 * @param productId
	 * @param subject 
	 * @return
	 * @Date:2015年11月12日下午3:26:27
	 * 
	 */
	public List<Question> getByProductId(String productId, String subject) {
		String sql = "select a.* from " +  subjectMap.get(subject) + " a where exists (select 1 from product_question where product_id = ? and qst_id = a.iid)";
		List<Question> list = jdbcTemplate.query(sql, new Object[]{ productId }, new BeanPropertyRowMapper<Question>(Question.class));
		return list;
	}
	
	/**
	 * @Description:根据产品ID和知识点查询题目列表
	 * @param productId
	 * @param subject
	 * @param zsd
	 * @param tx 
	 * @return
	 * @Date:2015年11月20日下午3:36:05
	 *
	 */
	public Page getByProductAndZsd(int pageNo, int pageSize, String productId, String subject, String zsd, String tx) {
		StringBuffer sql = new StringBuffer();
		sql.append("select a.iid id, a.* from ");
		sql.append(subjectMap.get(subject));
		sql.append(" a right join product_question b on a.iid = b.qst_id where b.product_id = ? ");
		if(null != zsd && !"".equals(zsd)){
			sql.append(" and exists (select col from dbo.f_splitSTR(a.zsd,',') where col = '");
			sql.append(zsd);
			sql.append("')");
		}
		if(null != tx && !"全部".equals(tx)){
			sql.append(" and a.tx = '");
			sql.append(tx);
			sql.append("'");
		}
		Page page = super.pageQuery1(pageNo, pageSize, sql.toString(), Question.class, productId);
		return page;
	}
	
	/**
	 * @Description:根据zsd查询题目列表
	 * @param pageNo
	 * @param pageSize
	 * @param subject
	 * @param zsd
	 * @return
	 * @Date:2015年11月27日下午4:41:50
	 * 
	 */
	public Page getByZsd(int pageNo, int pageSize, String subject, String zsd) {
		String sql = "select a.iid id, a.* from " +  subjectMap.get(subject) + " a  where exists (select col from dbo.f_splitSTR(a.zsd,',') where col = ?)";
		Page page = super.pageQuery1(pageNo, pageSize, sql, Question.class, zsd);
		return page;
	}

	/**
	 * @Description:根据ids获得试题列表
	 * @param qstIds
	 * @param subject 
	 * @return
	 * @Date:2015年11月25日上午11:28:48
	 * 
	 */
	public List<Question> getByIds(String qstIds, String subject) {
		String sql = "select a.iid id, a.* from " +  subjectMap.get(subject) + " a   where exists (select col from dbo.f_splitSTR(?,',') where col = a.iid)";
		List<Question> list = jdbcTemplate.query(sql, new Object[]{ qstIds }, new BeanPropertyRowMapper<Question>(Question.class));
		return list;
	}
	

	/**
	 * @Description:获得产品添加试题的列表
	 * @param pageNo
	 * @param pageSize
	 * @param subject
	 * @param productId
	 * @param zsd
	 * @param tx 
	 * @param tx2 
	 * @return
	 * @Date:2015年12月16日上午11:41:00
	 * 
	 */
	public Page getProductAddQstByPage(int pageNo, int pageSize, String subject, String productId, String productZsd, String zsd, String tx) {
		StringBuffer sql = new StringBuffer();
		sql.append("select a.iid id, a.* from ");
		sql.append(subjectMap.get(subject));
		sql.append(" a where not exists (select qst_id from product_question where qst_id = a.iid and product_id = ?) ");
		sql.append(" and exists (select a.col from (select col from dbo.f_splitSTR(a.zsd,',')) a, ");
		sql.append(" (select col from dbo.f_splitSTR('");
		sql.append(productZsd);
		sql.append("',',')) b");
		sql.append(" where a.col = b.col ");
		if(null != zsd && !"".equals(zsd)){
			sql.append("and a.col = '");
			sql.append(zsd);
			sql.append("'");
		}
		sql.append(")");
		if(null != tx && !"全部".equals(tx)){
			sql.append(" and a.tx = '");
			sql.append(tx);
			sql.append("'");
		}
		Page page = super.pageQuery1(pageNo, pageSize, sql.toString(), Question.class, productId);
		return page;
	}
	
	/**
	 * @Description:
	 * @param qstId
	 * @return
	 * @Date:2015年12月1日下午4:41:52
	 * 
	 */
	public QstModel getModelById(int qstId, String subject) {
		String sql = "select iid id,t content,tx type from " + subjectMap.get(subject) + " where iid = ? ";
		QstModel qstModel = jdbcTemplate.queryForObject(sql, new Object[]{ qstId }, new BeanPropertyRowMapper<QstModel>(QstModel.class)); 
		return qstModel;
	}
	
	/**
	 * 
	 * @Description 
	 * @param id
	 * @param subject
	 * @param string
	 * @return
	 * @Date 2015-11-2 下午3:52:30
	 */
	public List<SubjectProperty> getQuestionTreeValue(String id, String subject,String name) {
		StringBuffer sql = new StringBuffer();
		sql.append("WITH TMP(COL,NAME,ID,PARENTID) AS(");
		sql.append("SELECT a.COL,b.name,b.id,B.PARENT_ID ");
		sql.append("FROM subject_property a join subject_property b on a.id=b.parent_id ");
		sql.append("WHERE a.PARENT_ID=(SELECT ID FROM subject_property WHERE SUBJECT_ID=? AND NAME=?) ");
		sql.append("AND a.TYPE ='树形' and a.status='启用' UNION ALL ");
		sql.append("SELECT PARENT.COL,CHILD.NAME,CHILD.ID,CHILD.PARENT_ID FROM SUBJECT_PROPERTY CHILD,TMP PARENT ");
		sql.append("WHERE CHILD.PARENT_ID = PARENT.ID) SELECT * FROM TMP ");
		Object[] values = {subject,name};
		return this.getList(sql.toString(), SubjectProperty.class, values);
	}

	/**
	 * 
	 * @Description 
	 * @param type
	 * @param subject
	 * @return
	 * @Date 2015-11-12 上午10:18:40
	 */
	public List<SubjectProperty> getOptions(String type, String subject) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT b.name,b.id col ");
		sql.append("FROM subject_property a join subject_property b on a.id=b.parent_id ");
		sql.append("WHERE a.PARENT_ID=(SELECT ID FROM subject_property WHERE SUBJECT_ID=? AND NAME='题目') ");
		sql.append("and a.status='启用' AND A.COL=? order by b.sort  ");
		Object[] values = {subject,type};
		return this.getList(sql.toString(), SubjectProperty.class, values);
	}

	/**
	 * 
	 * @Description 得到知识点/教材体系对应的教材体系/知识点
	 * @param page
	 * @param pageRows
	 * @param type zsd/jctx
	 * @param code zsd/jsct id
	 * @return
	 * @Date 2015-12-17 上午10:17:50
	 */
	public Page zsdJctxList(int page, int pageRows, String type, String code) {
		List<String> params = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		StringBuffer withSql = new StringBuffer();
		withSql.append("WITH TMP(ID,PARENT_ID,PATH)AS(SELECT ID,PARENT_ID,CAST(NAME AS VARCHAR(MAX))");
		withSql.append("FROM SUBJECT_PROPERTY WHERE LEVEL = 2 UNION ALL ");
		withSql.append("SELECT CHILD.ID,CHILD.PARENT_ID,CAST((PARENT.PATH+'-'+CHILD.NAME) AS VARCHAR(MAX)) FROM ");
		withSql.append("TMP PARENT,SUBJECT_PROPERTY CHILD WHERE PARENT.ID=CHILD.PARENT_ID)");
		sql.append("SELECT ZJ.ID,ZSD.PATH ZSDNAME,ZSDID,JCTX.PATH JCTXNAME,JCTXID ");
		sql.append("FROM ZSDJCTX ZJ JOIN TMP ZSD ON ZJ.ZSDID=ZSD.ID ");
		sql.append("JOIN TMP JCTX ON ZJ.JCTXID=JCTX.ID WHERE 1=1 ");
		if(StringUtils.isNotBlank(code)) {
			params.add(code);
			if("jctx".equals(type)) {
				sql.append("AND JCTXID=? ");
			} else {
				sql.append("AND ZSDID=? ");
			}
		} else {
			sql.append("AND 1=2 ");
		}
		return pageQueryContainsWithSql(page, pageRows,0, withSql.toString(),sql.toString(),withSql.toString()+sql.replace(sql.lastIndexOf(")")+1, sql.lastIndexOf("FROM"), "select count(1) ").toString(),ZsdJctxRelations.class,params.toArray());
	}

	/**
	 * 
	 * @Description 添加知识点、教材体系关系 
	 * @param type 知识点/教材体系
	 * @param code ID
	 * @param values IDS
	 * @Date 2015-12-17 上午11:23:08
	 */
	public void saveZsdJctx(String type, String code, String[] values) {
		String sql = "IF NOT EXISTS(SELECT 1 FROM ZSDJCTX WHERE ZSDID=? AND JCTXID=?)INSERT INTO ZSDJCTX(ZSDID,JCTXID)VALUES(?,?)";
		List<Object[]> args = new ArrayList<Object[]>();
		List<String> info = new ArrayList<String>();
		int[] types = new int[4];
		for(int i = 0; i < types.length; i++) {
			types[i] = Types.VARCHAR;
		}
		for(String id : values) {
			info.clear();
			if("zsd".equals(type)) {
				info.add(code);
				info.add(id);
				info.add(code);
				info.add(id);
			} else {
				info.add(id);
				info.add(code);
				info.add(id);
				info.add(code);
			}
			args.add(info.toArray());
		}
		jdbcTemplate.batchUpdate(sql, args, types);
	}

	public void delZsdJctx(String[] ids) {
		StringBuffer sql = new StringBuffer();
		List<String> params = new ArrayList<String>();
		sql.append("DELETE ZSDJCTX WHERE ID IN(");
		for(String id : ids) {
			sql.append("?,");
			params.add(id);
		}
		sql.delete(sql.length() - 1, sql.length());
		sql.append(")");
		this.jdbcTemplate.update(sql.toString(), params.toArray());
	}


}

package com.fhsoft.question.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhsoft.base.bean.Page;
import com.fhsoft.base.bean.TreeNodeAttribute;
import com.fhsoft.base.bean.TreeNodeBean;
import com.fhsoft.model.Question;
import com.fhsoft.model.QuestionAnalysis;
import com.fhsoft.model.SubjectProperty;
import com.fhsoft.model.Zsd;
import com.fhsoft.question.dao.QuestionDao;
import com.fhsoft.question.dao.QuestionZsdDao;
import com.fhsoft.subject.dao.SubjectPropertyDao;

@Service("czywService")
@Transactional
public class QuestionService {
	@Autowired
	private QuestionDao questionDao;
	
	@Autowired
	private QuestionZsdDao zsdDao;
	
	@Autowired
	private SubjectPropertyDao spDao;
	
	/**
	 * 
	 * @Description 
	 * @return
	 * @Date 2015-10-27 上午10:15:54
	 */
	public int count(){
		return questionDao.count();
	}
	
	/**
	 * 
	 * @Description 
	 * @param pageNo
	 * @param pageSize
	 * @param subject 学科ID
	 * @param code 知识点ID
	 * @param col 做为查询菜单的列
	 * @param question
	 * @return
	 * @Date 2015-10-27 上午10:16:14
	 */
	public Page list(int pageNo, int pageSize,String subject,String code,String col,Question question){
		List<SubjectProperty> sps = spDao.getChildrenById(Integer.parseInt(code));
		List<String> spids = new ArrayList<String>();
		for(SubjectProperty sp : sps) {
			spids.add(sp.getId()+"");
		}
		return questionDao.list(pageNo, pageSize,subject,col,spids,question);
	}

	/**
	 * 
	 * @Description 得到试题标引信息
	 * @param id
	 * @param subject
	 * @param name 试题或解析
	 * @return
	 * @Date 2015-10-27 上午10:16:21
	 */
	public List<SubjectProperty> getQuestionIndexesInfo(String id,String subject,String name) {
		List<SubjectProperty> props = questionDao.getQuestionIndexesInfo(id,subject,name);
		return props;
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
		return questionDao.getQuestionById(id, subject);
	}
	
	/**
	 * 
	 * @Description 得到试题解析信息
	 * @param id
	 * @param subject
	 * @return
	 * @Date 2015-10-27 上午10:16:29
	 */
	public List<QuestionAnalysis> getQuestionAnalysesInfo(String id,String subject) {
		return questionDao.getQuestionAnalysesInfo(id,subject);
	}

	/**
	 * 
	 * @Description 
	 * @param id
	 * @return
	 * @Date 2015-10-27 下午4:10:24
	 */
	public List<TreeNodeBean> getQuestionTreeValue(String id,String values) {
		List<TreeNodeBean> nodes = questionDao.getQuestionTreeValue(id);
		if(StringUtils.isNotBlank(values)) {
			String[] arr = values.split(",");
			for(TreeNodeBean node : nodes) {
				for(int i = 0; i < arr.length; i++) {
					if(arr[i].equals(node.getId()+"")) {
						node.setChecked(true);
					}
				}
			}
		}
		return nodes;
	}

	/**
	 * 
	 * @Description 
	 * @param question
	 * @Date 2015-10-28 下午2:46:49
	 */
	public void updateQuestion(Question question) {
		questionDao.updateQuestion(question);
		
	}

	
	/**
	 * 
	 * @Description 
	 * @param type
	 * @param subject
	 * @return
	 * @Date 2015-10-29 下午2:21:33
	 */
	public List<TreeNodeBean> listZsd(String type, String subject,String codes) {
		Object[] values = {type,subject};
		List<TreeNodeBean> nodes = new ArrayList<TreeNodeBean>();
		List<Zsd> zsds = zsdDao.list(values);
		String[] codeArray = null;
		for(Zsd zsd : zsds) {
			TreeNodeBean node = new TreeNodeBean();
			node.setId(zsd.getId());
			node.setText(zsd.getName());
			node.setPid(zsd.getParent_id());
			if(StringUtils.isNotBlank(codes)) {
				codeArray = codes.split(",");
				for(int i = 0; i < codeArray.length; i++) {
					if(codeArray[i].equals(zsd.getCode())) {
						node.setChecked(true);
					}
				}
			}
			//根据level添加url具体内容
			if("1".equals(zsd.getIs_leaf())){
				node.setAttributes(new TreeNodeAttribute(zsd.getCode(),zsd.getSubject()));
				node.setState("open");
			}else{
				node.setAttributes(new TreeNodeAttribute(zsd.getCode(),zsd.getSubject()));
				node.setState("closed");
			}
			nodes.add(node);
		}
		return nodes;
	}

	/**
	 * 
	 * @Description 
	 * @param index
	 * @Date 2015-11-2 上午10:30:31
	 */
	public void saveAnalysis(QuestionAnalysis analysis) {
		questionDao.saveAnalysis(analysis);
	}

	/**
	 * 
	 * @Description 
	 * @param id
	 * @param subject
	 * @param string
	 * @return
	 * @Date 2015-11-2 下午3:51:31
	 */
	public List<SubjectProperty> getQuestionSelValue(String id, String subject,
			String string) {
		List<SubjectProperty> sels = questionDao.getQuestionSelValue(id,subject,string);
		List<SubjectProperty> nodes = questionDao.getQuestionTreeValue(id,subject,string);
		sels.addAll(nodes);
		return sels;
	}

	/**
	 * 
	 * @Description 
	 * @param type
	 * @param subject
	 * @return
	 * @Date 2015-11-12 上午10:18:07
	 */
	public List<SubjectProperty> getOptions(String type, String subject) {
		return questionDao.getOptions(type,subject);
	}
}

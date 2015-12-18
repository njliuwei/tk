package com.fhsoft.product.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fhsoft.base.bean.Page;
import com.fhsoft.base.bean.TreeNodeBean;
import com.fhsoft.model.Paper;
import com.fhsoft.model.PaperModel;
import com.fhsoft.model.QstModel;
import com.fhsoft.model.QstTypeModel;
import com.fhsoft.product.dao.PaperDao;
import com.fhsoft.question.dao.QuestionDao;
import com.fhsoft.subject.dao.SubjectPropertyValueDao;

/**
 * @ClassName:com.fhsoft.product.service.PaperService
 * @Description:试卷管理的service处理
 *
 * @Author:liyi
 * @Date:2015年11月17日下午2:12:45
 *
 */
@Service("paperService")
@Transactional
public class PaperService {
	
	@Autowired
	private PaperDao paperDao;
	@Autowired
	private QuestionDao questionDao;
	@Autowired
	private SubjectPropertyValueDao subjectPropertyValueDao;
	@Resource
	private Map<String,String> typeIndexMap; 

	/**
	 * @Description:获得试卷列表
	 * @param pageNo
	 * @param pageSize
	 * @param subject
	 * @param searchPaperName
	 * @return
	 * @Date:2015年11月17日下午2:46:16
	 * 
	 */
	public Page getByPage(int pageNo, int pageSize, String subject, String searchPaperName) {
		Page page = paperDao.getByPage(pageNo,pageSize,subject,searchPaperName);
		return page;
	}

	/**
	 * @Description:根据id删除试卷
	 * @param id
	 * @Date:2015年11月19日上午9:42:05
	 * 
	 */
	public void delete(int id) {
		paperDao.delete(id);
	}

	/**
	 * @Description:获得试卷的导出路径
	 * @param id
	 * @param request
	 * @return
	 * @Date:2015年11月19日上午11:19:55
	 * 
	 */
	public String export(String id, HttpServletRequest request) {
		String path;
		Paper paper = paperDao.getById(id);
		path = request.getSession().getServletContext().getRealPath("/") + "download/paper/" + paper.getExportPath1();
		return path;
	}

	/**
	 * @Description:根据subject获得对应的知识点树
	 * @param subject
	 * @return
	 * @Date:2015年11月27日下午3:13:05
	 * 
	 */
	public List<TreeNodeBean> getPaperZsdTree(String subject) {
		return subjectPropertyValueDao.getZsdTree(subject,"题目");
	}

	/**
	 * @Description:根据zsd获得试题列表
	 * @param pageNo
	 * @param pageSize
	 * @param subject
	 * @param zsd
	 * @return
	 * @Date:2015年11月27日下午4:40:27
	 * 
	 */
	public Page paperZsdQuestion(int pageNo, int pageSize, String subject, String zsd) {
		Page page = questionDao.getByZsd(pageNo, pageSize, subject, zsd);
		return page;
	}

	/**
	 * @Description:获得试卷的内容
	 * @param paperInfo
	 * @return
	 * @Date:2015年12月1日上午11:02:43
	 * 
	 */
	public PaperModel getPaperFullContent(String paperInfo) {
		int typeIndex = 1;
		int qstIndex = 1;
		PaperModel paper = new PaperModel();
		
		//获得试卷的信息
		JSONObject paperObject = JSON.parseObject(paperInfo);
		paper.setPaperTitle(paperObject.getString("paperTitle"));
		paper.setPaperNote(paperObject.getString("paperNote"));
		paper.setPaperAuthor(paperObject.getString("paperAuthor"));
		paper.setParperAnswerTime(paperObject.getString("parperAnswerTime"));
		paper.setProductId(paperObject.getIntValue("productId"));
		int subject = paperObject.getIntValue("subject");
		paper.setSubject(subject);
		
		//试卷的题型列表
		List<QstTypeModel> types = new ArrayList<QstTypeModel>();
		JSONArray typeJsonArray = paperObject.getJSONArray("typeList");
		for (Object typeObject : typeJsonArray) {
			JSONObject object = (JSONObject) typeObject;
			if (!"".equals(object.get("id"))) {
				QstTypeModel type = new QstTypeModel();
				type.setIndex(typeIndexMap.get(String.valueOf(typeIndex++)));
				type.setId(object.getIntValue("id"));
				type.setName(object.getString("name"));
				type.setComment(object.getString("comment"));
				
				//试题列表
				List<QstModel> qsts = new ArrayList<QstModel>();
				JSONArray qstIds = object.getJSONArray("qstList");
				for (Object qst : qstIds) {
					JSONObject qstObject = (JSONObject) qst;
					int qstId = qstObject.getIntValue("id");
					QstModel question = questionDao.getModelById(qstId, String.valueOf(subject));
					question.setIndex(qstIndex++);
					qsts.add(question);
				}
				type.setQstList(qsts);
				types.add(type);
			}
		}
		paper.setTypeList(types);
		
		return paper;
	}

	/**
	 * @Description:保存试卷
	 * @param paper
	 * @Date:2015年12月2日上午10:22:07
	 * 
	 */
	public void savePaper(Paper paper) {
		paperDao.save(paper);
	}

	/**
	 * @Description:根据id获得试卷信息
	 * @param id
	 * @return
	 * @Date:2015年12月3日上午10:55:58
	 * 
	 */
	public Paper getById(String id) {
		return paperDao.getById(id);
	}

	/**
	 * @Description:修改试卷
	 * @param paper
	 * @Date:2015年12月11日下午4:29:58
	 * 
	 */
	public void update(Paper paper) {
		paperDao.update(paper);
	}

}

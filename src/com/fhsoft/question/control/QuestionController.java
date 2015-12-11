package com.fhsoft.question.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.fhsoft.base.bean.Page;
import com.fhsoft.base.bean.TreeNodeBean;
import com.fhsoft.model.Question;
import com.fhsoft.model.QuestionAnalysis;
import com.fhsoft.model.SubjectProperty;
import com.fhsoft.question.service.QuestionService;

/**
 * 
 * @Classname com.fhsoft.question.control.QuestionController
 * @Description 
 *
 * @author lw
 * @Date 2015-10-23 上午9:40:13
 *
 */
@Controller
public class QuestionController {
	
	@Autowired
	private QuestionService questionService;
	
	 private Logger logger = Logger.getLogger(QuestionController.class);
	 
	 @RequestMapping("toQuestionList")
	 public String czyw(){
		 return "question/czyw";
	 }
	 
	 @RequestMapping("czywList")
	 @ResponseBody
	 public Object czywList(HttpServletRequest request, HttpServletResponse response,Question question){
		int pageRows=20;
		int page=1;
		String subject = request.getParameter("subject");
		String code = request.getParameter("code");
		if(null!=request.getParameter("rows")){
			pageRows=Integer.parseInt(request.getParameter("rows").toString());
		}
		if(null!=request.getParameter("page")){
			page=Integer.parseInt(request.getParameter("page").toString());
		}
		Page pageInfo = null;
		try {
			pageInfo = questionService.list(page,pageRows,subject,code,"zsd",question);
		} catch(Exception e) {
			e.printStackTrace();
			logger.error("得到试题信息出错", e);
		}
		return pageInfo;
	 }
	 
	 /**
	  * @Description 得到试题附加信息，如解析、标引等 
	  * @param request
	  * @param response
	  * @return
	  * @Date 2015-10-27 上午10:00:49
	  */
	 @RequestMapping("getQuestionAdditionInfo")
	 @ResponseBody
	 public Object getQuestionAdditionInfo(HttpServletRequest request, HttpServletResponse response){
		String id = request.getParameter("id");
		String subject = request.getParameter("subject");
		Map<String,Object> data = new HashMap<String, Object>();
		try{
			Question question = questionService.getQuestionById(id, subject);
			List<SubjectProperty> props = questionService.getQuestionIndexesInfo(id, subject,"题目");
			List<SubjectProperty> propSels = questionService.getQuestionSelValue(id, subject,"题目");
			List<SubjectProperty> analysesIndex = questionService.getQuestionIndexesInfo(id, subject,"解析");
			List<SubjectProperty> analysisSels = questionService.getQuestionSelValue(id, subject,"解析");
			List<QuestionAnalysis> analyses = questionService.getQuestionAnalysesInfo(id, subject);
			data.put("props",props);
			data.put("propSels", propSels);
			data.put("analysesIndex", analysesIndex);
			data.put("analysisSels", analysisSels);
			data.put("analyses", analyses);
			data.put("question", question);
		} catch(Exception e) {
			e.printStackTrace();
			logger.error("得到试题附加信息出错", e);
		 }
		return data;
	 }
	 
	 /**
	  * @Description 得到试题tree类型标引属性的值
	  * @param request
	  * @param response
	  * @return
	  * @Date 2015-10-27 上午10:00:49
	  */
	 @RequestMapping("getQuestionTreeValue")
	 @ResponseBody
	 public Object getQuestionTreeValue(HttpServletRequest request, HttpServletResponse response){
		String values = request.getParameter("values");
		String id = request.getParameter("id");
		List<TreeNodeBean> nodes = new ArrayList<TreeNodeBean>();
		try {
			nodes = questionService.getQuestionTreeValue(id,values);
		} catch(Exception e) {
			e.printStackTrace();
			 logger.error("获取树节点信息出错", e);
		 }
		return nodes;
	 }
	 
	 /**
	  * @Description 得到试题tree类型标引属性的值
	  * @param request
	  * @param response
	  * @return
	  * @Date 2015-10-27 上午10:00:49
	  */
	 @RequestMapping("saveQuestionIndex")
	 @ResponseBody
	 public Object saveQuestionIndex(@RequestBody Question question,HttpServletRequest request){
		 Map<String,Object> data = new HashMap<String, Object>();
		 try{
			 questionService.updateQuestion(question);
			 data.put("result", "success");
			 data.put("msg", "保存成功");
		 } catch(Exception e) {
			 data.put("result", "error");
			 data.put("msg", "保存失败");
		 }
		 return data;
	 }

	 /**
	  * @Description 得到试题tree类型标引属性的值
	  * @param request
	  * @param response
	  * @return
	  * @Date 2015-10-27 上午10:00:49
	  */
	 @RequestMapping("getFixedTreeValue")
	 @ResponseBody
	 public Object getFixedTreeValue(HttpServletRequest request, HttpServletResponse response){
		 String type = request.getParameter("type");
		 String subject = request.getParameter("subject");
		 String codes = request.getParameter("codes");
		 List<TreeNodeBean> nodes = new ArrayList<TreeNodeBean>();
		 try{
			 nodes = questionService.listZsd(type,subject,codes);
		 } catch(Exception e) {
			 e.printStackTrace();
			 logger.error("获取树节点信息出错", e);
		 }
		 return nodes;
	 }
	 
	 /**
	  * @Description 保存试题解析标引信息
	  * @param analysis
	  * @return
	  * @Date 2015-10-27 上午10:00:49
	  */
	 @RequestMapping("saveAnalysisIndex")
	 @ResponseBody
	 public Object saveAnalysisIndex(QuestionAnalysis analysis){
		 Map<String,Object> data = new HashMap<String, Object>();
		 try{
			 questionService.saveAnalysis(analysis);
			 data.put("result", "success");
			 data.put("msg", "保存成功");
		 } catch(Exception e) {
			 e.printStackTrace();
			 data.put("result", "error");
			 data.put("msg", "解析标引失败");
		 }
		 return JSON.toJSONString(data);
	 }
	 
	 /**
	  * @Description 保存试题解析标引信息
	  * @param analysis
	  * @return
	  * @Date 2015-10-27 上午10:00:49
	  */
	 @RequestMapping("getOptions.do")
	 @ResponseBody
	 public Object getOptions(HttpServletRequest request, HttpServletResponse response){
		 List<SubjectProperty> nodes = new ArrayList<SubjectProperty>();
		 SubjectProperty node = new SubjectProperty();
		 node.setName("--请选择--");
		 node.setCol("");
		 nodes.add(node);
		 String type = request.getParameter("type");
		 String subject = request.getParameter("subject");
		 try{
			 nodes.addAll(questionService.getOptions(type,subject));
		 } catch(Exception e) {
			 e.printStackTrace();
		 }
		 return nodes;
	 }

}

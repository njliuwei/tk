package com.fhsoft.word.control;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.fhsoft.base.bean.JsonResult;
import com.fhsoft.base.bean.Page;
import com.fhsoft.model.TextBookWord;
import com.fhsoft.model.Word;
import com.fhsoft.word.service.TeachingMaterialEditionService;

/**
 * 
 * @Classname com.fhsoft.word.control.TeachingMaterialEditionCtrol
 * @Description 
 *
 * @author lw
 * @Date 2015-11-18 上午10:26:10
 *
 */
@Controller
public class TeachingMaterialEditionCtrol {
	
	@Autowired
	private TeachingMaterialEditionService edtionService;
	
	 private Logger logger = Logger.getLogger(TeachingMaterialEditionCtrol.class);
	 
	 /**
	  * 
	  * @Description 
	  * @return
	  * @Date 2015-11-18 上午10:39:07
	  */
	 @RequestMapping("toEditionWord.do")
	 public String czyw(){
		 return "word/editionWordList";
	 }
	 
	 /**
	  * 
	  * @Description 
	  * @param request
	  * @param response
	  * @param word
	  * @return
	  * @Date 2015-11-18 上午10:39:02
	  */
	 @RequestMapping("addEditionWord")
	 @ResponseBody
	 public Object addEditionWord(HttpServletRequest request, HttpServletResponse response,TextBookWord word){
		JsonResult result = new JsonResult();
		try {
			edtionService.addWord(word);
			result.setSuccess(true);
			result.setMsg("添加成功！");
		} catch (Exception e) {
			result.setMsg("添加失败！");
			logger.error("教材版本词库添加失败！", e);
		}
		return JSON.toJSONString(result);
	 }
	 
	 /**
	  * 
	  * @Description 
	  * @param request
	  * @param response
	  * @param word
	  * @return
	  * @Date 2015-11-18 上午10:38:56
	  */
	 @RequestMapping("deleteEditionWord")
	 @ResponseBody
	 public Object deleteEditionWord(HttpServletRequest request, HttpServletResponse response,Word word){
		JsonResult result = new JsonResult();
		try {
			edtionService.delWord(word);
			result.setSuccess(true);
			result.setMsg("删除成功！");
		} catch (Exception e) {
			result.setMsg("删除失败！");
			logger.error("教材版本词库删除失败！", e);
		}
		return JSON.toJSONString(result);
	 }
	 
	 /**
	  * 
	  * @Description 
	  * @param request
	  * @param response
	  * @param word
	  * @return
	  * @Date 2015-11-18 上午10:38:50
	  */
	 @RequestMapping("editionWordList")
	 @ResponseBody
	 public Object editionWordList(HttpServletRequest request, HttpServletResponse response,TextBookWord word){
		int pageRows=20;
		int page=1;
		if(null!=request.getParameter("rows")){
			pageRows=Integer.parseInt(request.getParameter("rows").toString());
		}
		if(null!=request.getParameter("page")){
			page=Integer.parseInt(request.getParameter("page").toString());
		}
		Page pageInfo = null;
		try {
			pageInfo = edtionService.list(page,pageRows,word);
		} catch(Exception e) {
			e.printStackTrace();
			logger.error("得到教材版本词库信息出错", e);
		}
		return pageInfo;
	 }
	 
	 /**
	  * 
	  * @Description 
	  * @param request
	  * @param response
	  * @param word
	  * @return
	  * @Date 2015-11-18 上午10:38:50
	  */
	 @RequestMapping("wordList")
	 @ResponseBody
	 public Object wordList(HttpServletRequest request, HttpServletResponse response){
		 int pageRows=20;
		 int page=1;
		 String table = request.getParameter("wordlibtype");
		 String name = request.getParameter("name");
		 String jctxId = request.getParameter("jctxId");
		 if(null!=request.getParameter("rows")){
			 pageRows=Integer.parseInt(request.getParameter("rows").toString());
		 }
		 if(null!=request.getParameter("page")){
			 page=Integer.parseInt(request.getParameter("page").toString());
		 }
		 Page pageInfo = null;
		 try {
			 pageInfo = edtionService.wordList(page,pageRows,table,name,jctxId);
		 } catch(Exception e) {
			 e.printStackTrace();
			 logger.error("得到教材版本词库信息出错", e);
		 }
		 return pageInfo;
	 }
}

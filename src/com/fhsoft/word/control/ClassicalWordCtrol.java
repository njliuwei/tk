package com.fhsoft.word.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.fhsoft.base.bean.JsonResult;
import com.fhsoft.base.bean.Page;
import com.fhsoft.model.SubjectProperty;
import com.fhsoft.model.Users;
import com.fhsoft.model.Word;
import com.fhsoft.util.ExcelContentParser;
import com.fhsoft.word.service.ClassicalWordService;

/**
 * 
 * @Classname com.fhsoft.word.control.ModenWordCtrol
 * @Description 
 *
 * @author lw
 * @Date 2015-11-5 上午9:56:27
 *
 */
@Controller
public class ClassicalWordCtrol {
	
	@Autowired
	private ClassicalWordService wordService;
	
	 private Logger logger = Logger.getLogger(ClassicalWordCtrol.class);
	 
	 @RequestMapping("toClassicalWordList.do")
	 public String czyw(){
		 return "word/classicalWordList";
	 }
	 
	 @RequestMapping("addClassicalWord")
	 @ResponseBody
	 public Object addWord(HttpServletRequest request, HttpServletResponse response,HttpSession session,Word word){
		Users u = (Users) session.getAttribute("user_info");
		word.setCreator_id(u.getId()+"");
		word.setLastModifierId(u.getId()+"");
		word.setLastModifier(u.getName());
		word.setCreator(u.getName());
		JsonResult result = new JsonResult();
		try {
			wordService.addWord(word);
			result.setSuccess(true);
			result.setMsg("添加成功！");
		} catch (Exception e) {
			result.setMsg("添加失败！");
			logger.error("文言文字词添加失败！", e);
		}
		return JSON.toJSONString(result);
	 }
	 
	 @RequestMapping("updateClassicalWord")
	 @ResponseBody
	 public Object updateWord(HttpServletRequest request, HttpServletResponse response,HttpSession session,Word word){
		JsonResult result = new JsonResult();
		try {
			Users u = (Users) session.getAttribute("user_info");
			word.setLastModifierId(u.getId()+"");
			word.setLastModifier(u.getName());
			wordService.updateWord(word);
			result.setSuccess(true);
			result.setMsg("修改成功！");
		} catch (Exception e) {
			result.setMsg("修改失败！");
			logger.error("文言文字词修改失败！", e);
		}
		return JSON.toJSONString(result);
	 }
	 
	 @RequestMapping("delClassicalWord")
	 @ResponseBody
	 public Object delWord(HttpServletRequest request, HttpServletResponse response,Word word){
		JsonResult result = new JsonResult();
		try {
			wordService.delWord(word);
			result.setSuccess(true);
			result.setMsg("删除成功！");
		} catch (Exception e) {
			result.setMsg("删除失败！");
			logger.error("文言文字词删除失败！", e);
		}
		return JSON.toJSONString(result);
	 }
	 
	 @RequestMapping("classicalWordList")
	 @ResponseBody
	 public Object classicalWordList(HttpServletRequest request, HttpServletResponse response,Word word){
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
			pageInfo = wordService.list(page,pageRows,word);
		} catch(Exception e) {
			e.printStackTrace();
			logger.error("得到文言文字词信息出错", e);
		}
		return pageInfo;
	 }
	 
	 @RequestMapping("uploadClassicalWord")
	 @ResponseBody
	 public Object uploadClassicalWord(MultipartFile file,HttpServletRequest request, HttpServletResponse response,HttpSession session){
		JsonResult result = new JsonResult();
		try {
			ExcelContentParser<Word> parser = new ExcelContentParser<Word>();
			Users u = (Users) session.getAttribute("user_info");
			List<Word> list = new ArrayList<Word>();
			String[] propertyNames = {"name","soundmark","type","property","meaning","example"};
			parser.parseExcel(list, Word.class, file.getInputStream(), propertyNames, file.getOriginalFilename(), 1);
			String msg = wordService.save(list,u);
			if("success".equals(msg)) {
				result.setSuccess(true);
				result.setMsg("上传成功");
			} else {
				result.setSuccess(false);
				result.setMsg(msg);
			}
		} catch (Exception e) {
			result.setMsg("导入失败！");
			logger.error("文言文字词导入失败！", e);
		}
		return JSON.toJSONString(result);
	 }
	 
	 @RequestMapping("addExampleClassicalWord")
	 @ResponseBody
	 public Object addExampleWord(HttpServletRequest request, HttpServletResponse response,Word word){
		 JsonResult result = new JsonResult();
		 try {
			 int id = wordService.addExample(word);
			 result.setSuccess(true);
			 result.setObj(id);
			 result.setMsg("添加成功！");
		 } catch (Exception e) {
			 result.setMsg("添加失败！");
			 logger.error("文言文字词添加失败！", e);
		 }
		 return JSON.toJSONString(result);
	 }
	 
	 @RequestMapping("updateAdditionalClassicalWord")
	 @ResponseBody
	 public Object updateAdditionalWord(HttpServletRequest request, HttpServletResponse response,Word word){
		 JsonResult result = new JsonResult();
		 try {
			 wordService.updateAdditionalWord(word);
			 result.setSuccess(true);
			 result.setMsg("修改成功！");
		 } catch (Exception e) {
			 result.setMsg("修改失败！");
			 logger.error("文言文字词附加信息修改失败！", e);
		 }
		 return JSON.toJSONString(result);
	 }
	 
	 @RequestMapping("updExampleClassicalWord")
	 @ResponseBody
	 public Object updExampleWord(HttpServletRequest request, HttpServletResponse response,Word word){
		 JsonResult result = new JsonResult();
		 try {
			 wordService.updExampleWord(word);
			 result.setSuccess(true);
			 result.setMsg("修改成功！");
		 } catch (Exception e) {
			 result.setMsg("修改失败！");
			 logger.error("文言文字词例句修改失败！", e);
		 }
		 return JSON.toJSONString(result);
	 }
	 
	 @RequestMapping("delClassicalExample")
	 @ResponseBody
	 public Object delExample(HttpServletRequest request, HttpServletResponse response,Word word){
		 JsonResult result = new JsonResult();
		 try {
			 wordService.delExample(word);
			 result.setSuccess(true);
			 result.setMsg("删除成功！");
		 } catch (Exception e) {
			 result.setMsg("删除失败！");
			 logger.error("文言文字词删除失败！", e);
		 }
		 return JSON.toJSONString(result);
	 }
	 
	 @RequestMapping("getClassicalWordInfo")
	 @ResponseBody
	 public Object getWordInfo(HttpServletRequest request, HttpServletResponse response, Word word){
		 JsonResult result = new JsonResult();
		 try {
			 List<Word> list = wordService.getWordInfo(word);
			 List<SubjectProperty> sps = wordService.getJctxOfYw();
			 List<Word> ws = wordService.getWordJctx(word);
			 Map<String,Object> obj = new HashMap<String, Object>();
			 obj.put("list", list);
			 obj.put("sps", sps);
			 obj.put("ws", ws);
			 result.setObj(obj);
			 result.setSuccess(true);
		 } catch (Exception e) {
			 result.setSuccess(false);
			 result.setMsg("查询字词信息失败！");
			 logger.error("查询文言文字词信息失败！", e);
		 }
		 return JSON.toJSONString(result);
	 }
	 
	 @RequestMapping("getClassicalExampleById")
	 @ResponseBody
	 public Object getExampleById(HttpServletRequest request, HttpServletResponse response, Word word){
		 try {
			 word = wordService.getExampleById(word);
		 } catch (Exception e) {
			 logger.error("查询字词例句信息失败！", e);
		 }
		 return word;
	 }
	 
	 @RequestMapping("getClassicalWordBasic")
	 @ResponseBody
	 public Object getClassicalWordBasic(HttpServletRequest request, HttpServletResponse response, Word word){
		 try {
			 word = wordService.getClassicalWordBasic(word);
		 } catch (Exception e) {
			 logger.error("查询字词基本信息失败！", e);
		 }
		 return word;
	 }
	 
	 @RequestMapping("getClassicalWordAdditional")
	 @ResponseBody
	 public Object classicalword_additional(HttpServletRequest request, HttpServletResponse response, Word word){
		 try {
			 word = wordService.getClassicalWordAdditional(word);
		 } catch (Exception e) {
			 logger.error("查询字词附加信息失败！", e);
		 }
		 return word;
	 }
}

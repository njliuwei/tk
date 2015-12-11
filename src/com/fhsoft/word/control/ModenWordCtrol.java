package com.fhsoft.word.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.fhsoft.model.Word;
import com.fhsoft.util.ExcelContentParser;
import com.fhsoft.word.service.ModenWordService;

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
public class ModenWordCtrol {
	
	@Autowired
	private ModenWordService wordService;
	
	 private Logger logger = Logger.getLogger(ModenWordCtrol.class);
	 
	 @RequestMapping("toWordList.do")
	 public String czyw(){
		 return "word/modenWordList";
	 }
	 
	 @RequestMapping("addWord")
	 @ResponseBody
	 public Object addWord(HttpServletRequest request, HttpServletResponse response,Word word){
		JsonResult result = new JsonResult();
		try {
			wordService.addWord(word);
			result.setSuccess(true);
			result.setMsg("添加成功！");
		} catch (Exception e) {
			result.setMsg("添加失败！");
			logger.error("现代字词添加失败！", e);
		}
		return JSON.toJSONString(result);
	 }
	 
	 @RequestMapping("addExampleWord")
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
			 logger.error("现代字词添加失败！", e);
		 }
		 return JSON.toJSONString(result);
	 }
	 
	 @RequestMapping(value="updateWord")
	 @ResponseBody
	 public Object updateWord(HttpServletRequest request, HttpServletResponse response,Word word){
		JsonResult result = new JsonResult();
		try {
			wordService.updateWord(word);
			result.setSuccess(true);
			result.setMsg("修改成功！");
		} catch (Exception e) {
			result.setMsg("修改失败！");
			logger.error("现代字词修改失败！", e);
		}
		return JSON.toJSONString(result);
	 }
	 
	 @RequestMapping("updateAdditionalWord")
	 @ResponseBody
	 public Object updateAdditionalWord(HttpServletRequest request, HttpServletResponse response,Word word){
		 JsonResult result = new JsonResult();
		 try {
			 wordService.updateAdditionalWord(word);
			 result.setSuccess(true);
			 result.setMsg("修改成功！");
		 } catch (Exception e) {
			 result.setMsg("修改失败！");
			 logger.error("现代字词附加信息修改失败！", e);
		 }
		 return JSON.toJSONString(result);
	 }
	 
	 @RequestMapping("updExampleWord")
	 @ResponseBody
	 public Object updExampleWord(HttpServletRequest request, HttpServletResponse response,Word word){
		 JsonResult result = new JsonResult();
		 try {
			 wordService.updExampleWord(word);
			 result.setSuccess(true);
			 result.setMsg("修改成功！");
		 } catch (Exception e) {
			 result.setMsg("修改失败！");
			 logger.error("现代字词例句修改失败！", e);
		 }
		 return JSON.toJSONString(result);
	 }
	 
	 @RequestMapping("delWord")
	 @ResponseBody
	 public Object delWord(HttpServletRequest request, HttpServletResponse response,Word word){
		JsonResult result = new JsonResult();
		try {
			wordService.delWord(word);
			result.setSuccess(true);
			result.setMsg("删除成功！");
		} catch (Exception e) {
			result.setMsg("删除失败！");
			logger.error("现代字词删除失败！", e);
		}
		return JSON.toJSONString(result);
	 }
	 
	 @RequestMapping("delExample")
	 @ResponseBody
	 public Object delExample(HttpServletRequest request, HttpServletResponse response,Word word){
		 JsonResult result = new JsonResult();
		 try {
			 wordService.delExample(word);
			 result.setSuccess(true);
			 result.setMsg("删除成功！");
		 } catch (Exception e) {
			 result.setMsg("删除失败！");
			 logger.error("现代字词例句删除失败！", e);
		 }
		 return JSON.toJSONString(result);
	 }
	 
	 @RequestMapping("modenWordList")
	 @ResponseBody
	 public Object modenWordList(HttpServletRequest request, HttpServletResponse response,Word word){
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
			logger.error("得到现代字词信息出错", e);
		}
		return pageInfo;
	 }
	 
	 @RequestMapping("uploadWord")
	 @ResponseBody
	 public Object uploadWord(MultipartFile file,HttpServletRequest request, HttpServletResponse response){
		JsonResult result = new JsonResult();
		try {
			ExcelContentParser<Word> parser = new ExcelContentParser<Word>();
			List<Word> list = new ArrayList<Word>();
			String[] propertyNames = {"name","type","sxc","soundmark","component","cbhs","bhs","buildingMethod","property","explain"};
			parser.parseExcel(list, Word.class, file.getInputStream(), propertyNames, file.getOriginalFilename(), 1);
			String msg = wordService.save(list);
			if("success".equals(msg)) {
				result.setSuccess(true);
				result.setMsg("导入成功");
			} else {
				result.setSuccess(false);
				result.setMsg(msg);
			}
		} catch (Exception e) {
			result.setMsg("导入失败！");
			logger.error("现代字词导入失败！", e);
		}
		return JSON.toJSONString(result);
	 }
	 
	 @RequestMapping("getWordInfo")
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
			 logger.error("查询现代字词信息失败！", e);
		 }
		 return JSON.toJSONString(result);
	 }
	 
	 @RequestMapping("getExampleById")
	 @ResponseBody
	 public Object getExampleById(HttpServletRequest request, HttpServletResponse response, Word word){
		 try {
			 word = wordService.getExampleById(word);
		 } catch (Exception e) {
			 logger.error("查询现代字词例句信息失败！", e);
		 }
		 return word;
	 }
}

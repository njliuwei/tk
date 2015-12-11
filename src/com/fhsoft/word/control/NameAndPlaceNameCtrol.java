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
import com.fhsoft.model.PlaceName;
import com.fhsoft.model.SubjectProperty;
import com.fhsoft.model.Word;
import com.fhsoft.util.ExcelContentParser;
import com.fhsoft.word.service.NameAndPlaceNameService;

/**
 * 
 * @Classname com.fhsoft.word.control.NameAndPlaceNameCtrol
 * @Description 
 *
 * @author lw
 * @Date 2015-11-17 上午10:44:18
 *
 */
@Controller
public class NameAndPlaceNameCtrol {
	
	@Autowired
	private NameAndPlaceNameService nameService;
	
	 private Logger logger = Logger.getLogger(NameAndPlaceNameCtrol.class);
	 
	 @RequestMapping("toPlaceName.do")
	 public String czyw(){
		 return "word/placeNameList";
	 }
	 
	 @RequestMapping("addPlaceName")
	 @ResponseBody
	 public Object addWord(HttpServletRequest request, HttpServletResponse response,PlaceName name){
		JsonResult result = new JsonResult();
		try {
			nameService.addName(name);
			result.setSuccess(true);
			result.setMsg("添加成功！");
		} catch (Exception e) {
			result.setMsg("添加失败！");
			logger.error("英文人名地名信息添加失败！", e);
		}
		return JSON.toJSONString(result);
	 }
	 
	 @RequestMapping("updatePlaceName")
	 @ResponseBody
	 public Object updateWord(HttpServletRequest request, HttpServletResponse response,PlaceName name){
		JsonResult result = new JsonResult();
		try {
			nameService.updateName(name);
			result.setSuccess(true);
			result.setMsg("修改成功！");
		} catch (Exception e) {
			result.setMsg("修改失败！");
			logger.error("英文人名地名信息修改失败！", e);
		}
		return JSON.toJSONString(result);
	 }
	 
	 @RequestMapping("delPlaceName")
	 @ResponseBody
	 public Object delWord(HttpServletRequest request, HttpServletResponse response,PlaceName name){
		JsonResult result = new JsonResult();
		try {
			nameService.delName(name);
			result.setSuccess(true);
			result.setMsg("删除成功！");
		} catch (Exception e) {
			result.setMsg("删除失败！");
			logger.error("英文人名地名信息删除失败！", e);
		}
		return JSON.toJSONString(result);
	 }
	 
	 @RequestMapping("placeNameList")
	 @ResponseBody
	 public Object placeNameList(HttpServletRequest request, HttpServletResponse response,PlaceName name){
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
			pageInfo = nameService.list(page,pageRows,name);
		} catch(Exception e) {
			e.printStackTrace();
			logger.error("得到英文人名地名信息信息出错", e);
		}
		return pageInfo;
	 }
	 
	 @RequestMapping("uploadPlaceName")
	 @ResponseBody
	 public Object uploadEnglishWord(MultipartFile file,HttpServletRequest request, HttpServletResponse response){
		JsonResult result = new JsonResult();
		try {
			ExcelContentParser<PlaceName> parser = new ExcelContentParser<PlaceName>();
			List<PlaceName> list = new ArrayList<PlaceName>();
			String[] propertyNames = {"name","cname","type"};
			parser.parseExcel(list, PlaceName.class, file.getInputStream(), propertyNames, file.getOriginalFilename(), 1);
			String msg = nameService.save(list);
			if("success".equals(msg)) {
				result.setSuccess(true);
				result.setMsg("上传成功");
			} else {
				result.setSuccess(false);
				result.setMsg(msg);
			}
		} catch (Exception e) {
			result.setMsg("删除失败！");
			logger.error("英文人名地名信息删除失败！", e);
		}
		return JSON.toJSONString(result);
	 }
	 
	 @RequestMapping("getPlaceNameInfo")
	 @ResponseBody
	 public Object getPlaceNameInfo(HttpServletRequest request, HttpServletResponse response,PlaceName name){
		 JsonResult result = new JsonResult();
		 try {
			 List<PlaceName> list = nameService.getWordInfo(name);
			 List<SubjectProperty> sps = nameService.getJctxOfYw();
			 List<Word> ws = nameService.getWordJctx(name);
			 Map<String,Object> obj = new HashMap<String, Object>();
			 obj.put("list", list);
			 obj.put("sps", sps);
			 obj.put("ws", ws);
			 result.setObj(obj);
			 result.setSuccess(true);
		 } catch (Exception e) {
			 result.setSuccess(false);
			 result.setMsg("查询英文人名地名信息信息失败！");
			 logger.error("查询英文人名地名信息信息失败！", e);
		 }
		 return JSON.toJSONString(result);
	 }
	 @RequestMapping("getPlaceNameBasic")
	 @ResponseBody
	 public Object getPlaceNameBasic(HttpServletRequest request, HttpServletResponse response,PlaceName name){
		 try {
			 List<PlaceName> list = nameService.getWordInfo(name);
			 return list.get(0);
		 } catch (Exception e) {
			 logger.error("查询英文人名地名信息失败！", e);
		 }
		 return null;
	 }
}

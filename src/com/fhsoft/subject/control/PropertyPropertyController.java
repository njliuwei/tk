package com.fhsoft.subject.control;

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
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.fhsoft.base.bean.JsonResult;
import com.fhsoft.base.bean.TreeNodeBean1;
import com.fhsoft.model.ComboxModel;
import com.fhsoft.model.PropertyProperty;
import com.fhsoft.subject.service.PropertyPropertyService;

/**
 * @ClassName:com.fhsoft.subject.control.PropertyPropertyController
 * @Description:学科属性的属性control处理
 *
 * @Author:liyi
 * @Date:2015年10月30日下午3:39:22
 *
 */
@Controller
public class PropertyPropertyController {
	
	@Autowired
	private PropertyPropertyService propertyPropertyService;
	
	private Logger logger = Logger.getLogger(PropertyPropertyController.class);
	
	/**
	 * @Description:进入学科属性的属性列表界面
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @Date:2015年11月2日上午11:17:21
	 *
	 */
	@RequestMapping("propertyProperty")
	public ModelAndView propertyProperty(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		String id = request.getParameter("id");
		map.put("id", id);
		return new ModelAndView("subject/propertyproperty",map);
	}
	
	/**
	 * @Description:学科属性的属性列表
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @Date:2015年11月2日下午4:07:08
	 *
	 */
	@RequestMapping("propertyPropertyTreeGrid")
	@ResponseBody
	public Object treegrid(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		List<TreeNodeBean1> beans = new ArrayList<TreeNodeBean1>();
		try {
			int pid = Integer.parseInt(request.getParameter("pid"));
			beans = propertyPropertyService.getTreeGrid(pid);
			map.put("total", beans.size());
			map.put("rows", beans);
		} catch (Exception e) {
			logger.error("获取TreeGrid树形学科属性的属性列表出错！", e);
		}
		return map;
	}
	
	
	/**
	 * @Description:添加学科属性的属性
	 * @param request
	 * @param response
	 * @param session
	 * @param propertyProperty
	 * @return
	 * @throws Exception
	 * @Date:2015年11月3日上午10:19:02
	 *
	 */
	@RequestMapping("propertyPropertyAdd")
	@ResponseBody
	public Object add(HttpServletRequest request,
			HttpServletResponse response,HttpSession session,PropertyProperty propertyProperty) throws Exception {
		JsonResult result = new JsonResult();
		try {
			propertyPropertyService.add(propertyProperty);
			result.setSuccess(true);
			result.setMsg("添加成功！");
		} catch (Exception e) {
			result.setMsg("添加失败！");
			logger.error("学科属性的属性添加失败！", e);
		}
		return JSON.toJSONString(result);
	}
	
	/**
	 * @Description:获取学科属性的属性修改数据
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @Date:2015年11月3日上午10:58:51
	 *
	 */
	@RequestMapping("propertyPropertyEdit")
	@ResponseBody
	public Object edit(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception {
		PropertyProperty editPropertyProperty = null;
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			editPropertyProperty = propertyPropertyService.getById(id);
		} catch (Exception e) {
			logger.error("获取学科属性的属性修改数据出错！", e);
		}
		return editPropertyProperty;
	}
	
	/**
	 * @Description:修改学科属性的属性
	 * @param request
	 * @param response
	 * @param session
	 * @param propertyProperty
	 * @return
	 * @throws Exception
	 * @Date:2015年11月3日上午11:06:34
	 *
	 */
	@RequestMapping("propertyPropertyUpdate")
	@ResponseBody
	public Object update(HttpServletRequest request,
			HttpServletResponse response,HttpSession session,PropertyProperty propertyProperty) throws Exception {
		JsonResult result = new JsonResult();
		try {
			propertyPropertyService.update(propertyProperty);
			result.setSuccess(true);
			result.setMsg("修改成功！");
		} catch (Exception e) {
			result.setMsg("修改失败！");
			logger.error("学科属性的属性修改失败！", e);
		}
		return JSON.toJSONString(result);
	}
	
	/**
	 * @Description:删除学科属性的属性
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @Date:2015年11月3日上午11:29:02
	 *
	 */
	@RequestMapping("propertyPropertyTreeDelete")
	@ResponseBody
	public Object treeDelete(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception {
		JsonResult result = new JsonResult();
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			propertyPropertyService.deleteTreeById(id);
			result.setSuccess(true);
			result.setMsg("删除成功！");
		} catch (Exception e) {
			result.setMsg("删除失败！");
			logger.error("学科属性的属性删除失败！", e);
		}
		return JSON.toJSONString(result);
	}
	
	@RequestMapping("getPropertyPropertyList")
	@ResponseBody
	public Object list(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception{
		List<ComboxModel> models = new ArrayList<ComboxModel>();
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			ComboxModel model = new ComboxModel(0, "请选择...");
			models.add(model);
			List<ComboxModel> list = propertyPropertyService.getPropertyByParentId(id);
			models.addAll(list);
		} catch (Exception e) {
			logger.error("根据ParentId获取学科属性的属性数据出错！", e);
		}
		return models;
	}

}

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
import com.fhsoft.base.bean.Page;
import com.fhsoft.base.bean.TreeNodeBean;
import com.fhsoft.base.bean.TreeNodeBean1;
import com.fhsoft.model.SubjectProperty;
import com.fhsoft.subject.service.SubjectPropertyValueService;

/**
 * @ClassName:com.fhsoft.subject.control.SubjectPropertyValueController
 * @Description:学科属性值的control处理
 *
 * @Author:liyi
 * @Date:2015年10月27日上午10:57:06
 *
 */
@Controller
public class SubjectPropertyValueController {
	
	@Autowired
	private SubjectPropertyValueService subjectPropertyValueService;
	
	private Logger logger = Logger.getLogger(SubjectPropertyValueController.class);
	
	/**
	 * @Description:进入添加学科属性值页面
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @Date:2015年10月27日上午11:20:49
	 *
	 */
	@RequestMapping("subjectPropertyValue")
	public ModelAndView subjectPropertyValue(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		String id = request.getParameter("id");
		String type = request.getParameter("type");
		map.put("id", id);
		if("树形".equals(type)){
			return new ModelAndView("subject/subjectpropertyvalue_tree",map);
		}else{
			return new ModelAndView("subject/subjectpropertyvalue_list",map);
		}
	}
	
	/**
	 * @Description:学科属性值列表
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @Date:2015年10月27日上午11:24:55
	 *
	 */
	@RequestMapping("subjectPropertyValueList")
	@ResponseBody
	public Object list(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception {
		Page page = null;
		try {
			int pageNo = Integer.valueOf(request.getParameter("page"));
			int pageSize = Integer.valueOf(request.getParameter("rows"));
			String id = request.getParameter("id");
			page = subjectPropertyValueService.getByPage(pageNo, pageSize, id);
		} catch (Exception e) {
			logger.error("获取学科属性值列表出错！",e);
		}
		return page;
	}
	
	/**
	 * @Description:添加列表型的学科属性值
	 * @param request
	 * @param response
	 * @param session
	 * @param subjectProperty
	 * @return
	 * @throws Exception
	 * @Date:2015年10月27日下午2:32:06
	 *
	 */
	@RequestMapping("subjectPropertyValueAdd")
	@ResponseBody
	public Object add(HttpServletRequest request,
			HttpServletResponse response,HttpSession session,SubjectProperty subjectProperty) throws Exception {
		JsonResult result = new JsonResult();
		try {
			subjectPropertyValueService.add(subjectProperty);
			result.setSuccess(true);
			result.setMsg("添加成功！");
		} catch (Exception e) {
			result.setMsg("添加失败！");
			logger.error("学科属性值添加失败！", e);
		}
		return JSON.toJSONString(result);
	}
	
	/**
	 * @Description:获得学科属性值的修改数据
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @Date:2015年10月27日下午2:44:17
	 *
	 */
	@RequestMapping("subjectPropertyValueEdit")
	@ResponseBody
	public Object edit(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception {
		SubjectProperty editSubjectProperty = null;
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			editSubjectProperty = subjectPropertyValueService.getById(id);
		} catch (Exception e) {
			logger.error("获取学科属性值的修改数据出错！", e);
		}
		return editSubjectProperty;
	}
	
	/**
	 * @Description:修改学科属性值
	 * @param request
	 * @param response
	 * @param session
	 * @param subjectProperty
	 * @return
	 * @throws Exception
	 * @Date:2015年10月27日下午2:45:13
	 *
	 */
	@RequestMapping("subjectPropertyValueUpdate")
	@ResponseBody
	public Object update(HttpServletRequest request,
			HttpServletResponse response,HttpSession session,SubjectProperty subjectProperty) throws Exception {
		JsonResult result = new JsonResult();
		try {
			subjectPropertyValueService.update(subjectProperty);
			result.setSuccess(true);
			result.setMsg("修改成功！");
		} catch (Exception e) {
			result.setMsg("修改失败！");
			logger.error("学科属性值修改失败！", e);
		}
		return JSON.toJSONString(result);
	}
	
	/**
	 * @Description:删除学科属性值
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @Date:2015年10月27日下午3:27:45
	 *
	 */
	@RequestMapping("subjectPropertyValueDelete")
	@ResponseBody
	public Object delete(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception {
		JsonResult result = new JsonResult();
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			subjectPropertyValueService.deleteById(id);
			result.setSuccess(true);
			result.setMsg("删除成功！");
		} catch (Exception e) {
			result.setMsg("删除失败！");
			logger.error("学科属性值删除失败！", e);
		}
		return JSON.toJSONString(result);
	}
	
	/**
	 * @Description:获得treegrid的树形表格数据
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @Date:2015年10月28日下午3:13:28
	 *
	 */
	@RequestMapping("subjectPropertyValueTreeGrid")
	@ResponseBody
	public Object treegrid(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		List<TreeNodeBean1> beans = new ArrayList<TreeNodeBean1>();
		try {
			int id = Integer.parseInt(request.getParameter("pid"));
			beans = subjectPropertyValueService.getTreeGrid(id);
			map.put("total", beans.size());
			map.put("rows", beans);
		} catch (Exception e) {
			logger.error("获取TreeGrid树形学科属性值出错！", e);
		}
		return map;
	}
	
	/**
	 * @Description:获取ComboTree树形学科属性值
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @Date:2015年10月29日上午11:16:36
	 *
	 */
	@RequestMapping("subjectPropertyValueComboTree")
	@ResponseBody
	public Object getMenuTree(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception{
		List<TreeNodeBean> beans = new ArrayList<TreeNodeBean>();
		try {
			int pid = Integer.parseInt(request.getParameter("pid"));
			String id = request.getParameter("id");
			beans = subjectPropertyValueService.getComboTree(pid,id);
		} catch (Exception e) {
			logger.error("获取ComboTree树形学科属性值出错！", e);
		}
		return beans;
	}
	
	/**
	 * @Description:删除树形学科属性值
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @Date:2015年10月27日下午3:27:45
	 *
	 */
	@RequestMapping("subjectPropertyValueTreeDelete")
	@ResponseBody
	public Object treeDelete(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception {
		JsonResult result = new JsonResult();
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			subjectPropertyValueService.deleteTreeById(id);
			result.setSuccess(true);
			result.setMsg("删除成功！");
		} catch (Exception e) {
			result.setMsg("删除失败！");
			logger.error("树形学科属性值删除失败！", e);
		}
		return JSON.toJSONString(result);
	}

}

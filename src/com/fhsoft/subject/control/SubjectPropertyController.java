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
import com.fhsoft.model.SubjectProperty;
import com.fhsoft.subject.service.SubjectPropertyService;

/**
 * @ClassName:com.fhsoft.subject.control.SubjectPropertyController
 * @Description:学科属性的control处理
 *
 * @Author:liyi
 * @Date:2015年10月16日上午11:16:51
 *
 */
@Controller
public class SubjectPropertyController {
	
	@Autowired
	private SubjectPropertyService subjectPropertyService;
	
	private Logger logger = Logger.getLogger(SubjectPropertyController.class);
	
	/**
	 * @Description:进入学科属性列表界面
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @Date:2015年10月22日上午9:44:48
	 *
	 */
	@RequestMapping("subjectProperty")
	public ModelAndView subjectProperty(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		String id = request.getParameter("id");
		map.put("id", id);
		return new ModelAndView("subject/subjectproperty",map);
	}
	
	/**
	 * @Description:学科属性列表
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @Date:2015年10月22日下午2:52:36
	 *
	 */
	@RequestMapping("subjectPropertyList")
	@ResponseBody
	public Object list(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception {
		Page page = null;
		try {
			int pageNo = Integer.valueOf(request.getParameter("page"));
			int pageSize = Integer.valueOf(request.getParameter("rows"));
			String id = request.getParameter("id");
			String status = request.getParameter("status");
			page = subjectPropertyService.getByPage(pageNo, pageSize, id, status);
		} catch (Exception e) {
			logger.error("获取学科属性列表出错！",e);
		}
		return page;
	}
	
	/**
	 * @Description:添加学科属性
	 * @param request
	 * @param response
	 * @param session
	 * @param subjectProperty
	 * @return
	 * @throws Exception
	 * @Date:2015年10月22日下午3:45:38
	 *
	 */
	@RequestMapping("subjectPropertyAdd")
	@ResponseBody
	public Object add(HttpServletRequest request,
			HttpServletResponse response,HttpSession session,SubjectProperty subjectProperty) throws Exception {
		JsonResult result = new JsonResult();
		try {
			subjectPropertyService.add(subjectProperty);
			result.setSuccess(true);
			result.setMsg("添加成功！");
		} catch (Exception e) {
			result.setMsg("添加失败！");
			logger.error("学科属性添加失败！", e);
		}
		return JSON.toJSONString(result);
	}
	
	/**
	 * @Description:获得需修改的学科属性信息
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @Date:2015年10月26日上午9:23:14
	 *
	 */
	@RequestMapping("subjectPropertyEdit")
	@ResponseBody
	public Object edit(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception {
		SubjectProperty editSubjectProperty = null;
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			editSubjectProperty = subjectPropertyService.getById(id);
		} catch (Exception e) {
			logger.error("获取学科属性的修改数据出错！", e);
		}
		return editSubjectProperty;
	}
	
	/**
	 * @Description:修改学科属性
	 * @param request
	 * @param response
	 * @param session
	 * @param subjectProperty
	 * @return
	 * @throws Exception
	 * @Date:2015年10月26日上午9:26:42
	 *
	 */
	@RequestMapping("subjectPropertyUpdate")
	@ResponseBody
	public Object update(HttpServletRequest request,
			HttpServletResponse response,HttpSession session,SubjectProperty subjectProperty) throws Exception {
		JsonResult result = new JsonResult();
		try {
			subjectPropertyService.update(subjectProperty);
			result.setSuccess(true);
			result.setMsg("修改成功！");
		} catch (Exception e) {
			result.setMsg("修改失败！");
			logger.error("学科属性修改失败！", e);
		}
		return JSON.toJSONString(result);
	}
	
	/**
	 * @Description:获得复制属性的节点树
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @Date:2015年10月26日上午11:26:43
	 *
	 */
	@RequestMapping("subjectPropertyCopyToTree")
	@ResponseBody
	public Object copyToTree(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception {
		List<TreeNodeBean> beans = new ArrayList<TreeNodeBean>();
		try {
			beans = subjectPropertyService.getCopyToTree();
		} catch (Exception e) {
			logger.error("获取复制到节点树出错！", e);
		}
		return beans;
	}
	
	/**
	 * @Description:复制属性到某节点下
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @Date:2015年10月26日上午11:27:50
	 *
	 */
	@RequestMapping("subjectPropertyCopy")
	@ResponseBody
	public Object copy(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception {
		JsonResult result = new JsonResult();
		try {
			int coptToId = Integer.parseInt(request.getParameter("copyToId"));
			int parentId = Integer.parseInt(request.getParameter("parentId"));
			int subjectPropertyId = Integer.parseInt(request.getParameter("subjectPropertyId"));
			subjectPropertyService.copy(subjectPropertyId,parentId,coptToId);
			result.setSuccess(true);
			result.setMsg("复制成功！");
		} catch (Exception e) {
			result.setMsg("复制失败！");
			logger.error("学科属性复制失败！", e);
		}
		return JSON.toJSONString(result);
	}
	
	/**
	 * @Description:学科属性名称是否存在
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @Date:2015年11月4日上午11:47:15
	 *
	 */
	@RequestMapping("isExistSubjectPropertyName")
	@ResponseBody
	public Object exist(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception {
		JsonResult result = new JsonResult();
		try {
			String name = request.getParameter("name");
			String pid = request.getParameter("pid");
			String id = request.getParameter("id");
			boolean flag = subjectPropertyService.isExistByNameAndPid(name, pid, id);
			result.setSuccess(flag);
		} catch (Exception e) {
			logger.error("查询学科属性名称是否存在失败！", e);
		}
		return JSON.toJSONString(result);
	}
	
	

}

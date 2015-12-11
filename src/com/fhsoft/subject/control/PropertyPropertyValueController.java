package com.fhsoft.subject.control;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.fhsoft.base.bean.JsonResult;
import com.fhsoft.model.PropertyProperty;
import com.fhsoft.subject.service.PropertyPropertyValueService;

/**
 * @ClassName:com.fhsoft.subject.control.PropertyPropertyValueController
 * @Description:学科属性的属性值的control操作
 *
 * @Author:liyi
 * @Date:2015年11月3日下午3:31:14
 *
 */
@Controller
public class PropertyPropertyValueController {
	
	@Autowired
	private PropertyPropertyValueService propertyPropertyValueService;
	
	private Logger logger = Logger.getLogger(PropertyPropertyValueController.class);
	
	/**
	 * @Description:添加学科属性的属性值
	 * @param request
	 * @param response
	 * @param session
	 * @param propertyProperty
	 * @return
	 * @throws Exception
	 * @Date:2015年11月3日下午3:41:54
	 *
	 */
	@RequestMapping("propertyPropertyValueAdd")
	@ResponseBody
	public Object add(HttpServletRequest request,
			HttpServletResponse response,HttpSession session,PropertyProperty propertyProperty) throws Exception {
		JsonResult result = new JsonResult();
		try {
			propertyPropertyValueService.add(propertyProperty);
			result.setSuccess(true);
			result.setMsg("添加成功！");
		} catch (Exception e) {
			result.setMsg("添加失败！");
			logger.error("学科属性的属性值添加失败！", e);
		}
		return JSON.toJSONString(result);
	}
	
	/**
	 * @Description:获取学科属性的属性值修改数据
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @Date:2015年11月3日下午4:15:52
	 *
	 */
	@RequestMapping("propertyPropertyValueEdit")
	@ResponseBody
	public Object edit(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception {
		PropertyProperty editPropertyProperty = null;
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			editPropertyProperty = propertyPropertyValueService.getById(id);
		} catch (Exception e) {
			logger.error("获取学科属性的属性值修改数据出错！", e);
		}
		return editPropertyProperty;
	}
	
	/**
	 * @Description:修改学科属性的属性值
	 * @param request
	 * @param response
	 * @param session
	 * @param propertyProperty
	 * @return
	 * @throws Exception
	 * @Date:2015年11月3日下午4:16:03
	 *
	 */
	@RequestMapping("propertyPropertyValueUpdate")
	@ResponseBody
	public Object update(HttpServletRequest request,
			HttpServletResponse response,HttpSession session,PropertyProperty propertyProperty) throws Exception {
		JsonResult result = new JsonResult();
		try {
			propertyPropertyValueService.update(propertyProperty);
			result.setSuccess(true);
			result.setMsg("修改成功！");
		} catch (Exception e) {
			result.setMsg("修改失败！");
			logger.error("学科属性的属性值修改失败！", e);
		}
		return JSON.toJSONString(result);
	}

}

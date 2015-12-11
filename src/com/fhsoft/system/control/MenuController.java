package com.fhsoft.system.control;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhsoft.base.bean.TreeNodeBean;
import com.fhsoft.system.service.MenuService;

/**
 * @ClassName:com.fhsoft.system.control.MenuController
 * @Description:系统菜单的control处理
 *
 * @Author:liyi
 * @Date:2015年10月20日上午11:35:53
 *
 */
@Controller
public class MenuController {
	
	@Autowired
	private MenuService menuService;
	
	private Logger logger = Logger.getLogger(MenuController.class);
	
	/**
	 * @Description:将系统菜单的TreeNodeBean对象返回为JSON数据
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @Date:2015年10月20日上午11:40:52
	 *
	 */
	@RequestMapping("menuTree")
	@ResponseBody
	public Object getMenuTree(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception{
		List<TreeNodeBean> beans = new ArrayList<TreeNodeBean>();
		try {
			int type = Integer.parseInt(request.getParameter("type"));
			beans = menuService.getMenuTree(type);
		} catch (Exception e) {
			logger.error("获取系统菜单出错！", e);
		}
		return beans;
	}

}

package com.fhsoft.system.control;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fhsoft.base.bean.JsonResult;
import com.fhsoft.base.bean.Page;
import com.fhsoft.model.Role;
import com.fhsoft.model.TreeNodeBean;
import com.fhsoft.model.Users;
import com.fhsoft.system.service.UserRoleService;
import com.fhsoft.util.DateUtil;
import com.fhsoft.word.control.ModenWordCtrol;

/**
 * 
 * @author hjb
 * @date 2015-12-9 下午3:41:39
 */
@Controller
public class UserRoleControl {
	
	@Autowired
	protected UserRoleService userRoleService;
	
	private Logger logger = Logger.getLogger(ModenWordCtrol.class);
	
	/**
	 * 跳转用户管理页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/user.do")
    public String user() throws Exception
    {	
		 return"system/user";
    }
	/**
	 * 跳转角色管理页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/role.do")
    public String role() throws Exception
    {	
		 return"system/role";
    }
	
	/**
	 * 查询所有用户（或者带入查询条件查询）
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/userList")
	@ResponseBody
    public Object userList(HttpServletRequest request, HttpServletResponse response) throws Exception
    {	
		int pageRows=20;
		int page=1;
		String name=request.getParameter("name");
		String created=request.getParameter("created");
		if(null!=request.getParameter("rows")){
			pageRows=Integer.parseInt(request.getParameter("rows").toString());
		}
		if(null!=request.getParameter("page")){
			page=Integer.parseInt(request.getParameter("page").toString());
		}
		Page pageInfo = null;
		try{
			pageInfo = userRoleService.userList(page,pageRows,name,created);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("得到用户信息出错", e);
		}
        return pageInfo;
    }
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/userAdd.do")
	@ResponseBody
	public Object add(HttpServletRequest request,
			HttpServletResponse response,HttpSession session,Users user) throws Exception {
		JsonResult result = new JsonResult();
		try {
			user.setCreated(DateUtil.getTime());
			user.setLastmodified(DateUtil.getTime());
			userRoleService.saveUser(user);
			result.setSuccess(true);
			result.setMsg("添加成功！");
		} catch (Exception e) {
			e.printStackTrace();
			result.setMsg("添加失败！");
		}
		return result;
	}
	
	/**
	 * @Description:修改用户
	 * @param request
	 * @param response
	 * @param session
	 * @param user
	 * @return
	 * @throws Exception
	 *
	 */
	@RequestMapping(value = "/userUpdate.do")
	@ResponseBody
	public Object update(HttpServletRequest request,
			HttpServletResponse response,HttpSession session,Users user) throws Exception {
		JsonResult result = new JsonResult();
		try {
			user.setLastmodified(DateUtil.getTime());
			userRoleService.userUpdate(user);
			result.setSuccess(true);
			result.setMsg("修改成功！");
		} catch (Exception e) {
			e.printStackTrace();
			result.setMsg("修改失败！");
		}
		return result;
	}
	
	/**
	 * @Description:删除用户
	 * @param request
	 * @param response
	 * @param session
	 * @param user
	 * @return
	 * @throws Exception
	 *
	 */
	@RequestMapping(value = "/userDelete.do")
	@ResponseBody
	public Object delete(HttpServletRequest request,
			HttpServletResponse response,HttpSession session,Users user) throws Exception {
		JsonResult result = new JsonResult();
		try {
			userRoleService.deleteUser(user);
			result.setSuccess(true);
			result.setMsg("删除成功！");
		} catch (Exception e) {
			e.printStackTrace();
			result.setMsg("删除失败！");
		}
		return result;
	}
	
	/**
	 * 查询所有角色
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/roleList")
	@ResponseBody
    public Object roleList(HttpServletRequest request, HttpServletResponse response) throws Exception
    {	
		int pageRows=20;
		int page=1;
		if(null!=request.getParameter("rows")){
			pageRows=Integer.parseInt(request.getParameter("rows").toString());
		}
		if(null!=request.getParameter("page")){
			page=Integer.parseInt(request.getParameter("page").toString());
		}
		Page pageInfo = null;
		try{
			pageInfo = userRoleService.roleList(page,pageRows);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("得到角色信息出错", e);
		}
        return pageInfo;
    }
	
	/**
	 * @Description:获得修改所需要的角色数据
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 *
	 */
	@RequestMapping(value = "/roleEdit.do")
	@ResponseBody
	public Object edit(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception {
		int id = Integer.parseInt(request.getParameter("id"));
		Role editRole = userRoleService.getRoleInfoById(id);
		return editRole;
	}
	
	/**
	 * @Description:保存或修改角色
	 * @param request
	 * @param response
	 * @param session
	 * @param role
	 * @return
	 * @throws Exception
	 *
	 */
	@RequestMapping(value = "/roleSave.do")
	@ResponseBody
	public Object save(HttpServletRequest request,
			HttpServletResponse response,HttpSession session,Role role) throws Exception {
		JsonResult result = new JsonResult();
		try {
			role.setCreated(DateUtil.getTime());
			userRoleService.saveRole(role);
			result.setSuccess(true);
			result.setMsg("保存成功！");
		} catch (Exception e) {
			e.printStackTrace();
			result.setMsg("保存失败！");
		}
		return result;
	}
	
	/**
	 * @Description:删除角色
	 * @param request
	 * @param response
	 * @param session
	 * @param role
	 * @return
	 * @throws Exception
	 * @Date:2015年4月8日下午3:00:33
	 *
	 */
	@RequestMapping(value = "/roleDelete.do")
	@ResponseBody
	public Object delete(HttpServletRequest request,
			HttpServletResponse response,HttpSession session,Role role) throws Exception {
		JsonResult result = new JsonResult();
		try {
			userRoleService.deleteRole(role);
			result.setSuccess(true);
			result.setMsg("删除成功！");
		} catch (Exception e) {
			e.printStackTrace();
			result.setMsg("删除失败！");
		}
		return result;
	}
	
	/**
	 * @Description:获得用户分配角色所需的角色信息
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 *
	 */
	@RequestMapping(value = "/userGetRole.do")
	@ResponseBody
	public Object getRole(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception {
		int userId = Integer.valueOf(request.getParameter("userId"));
		//userId=1000;
		List<TreeNodeBean> roles = userRoleService.getRoles(userId);
		return roles;
	}
	
	/**
	 * @Description:保存分配角色信息
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @Date:2015年4月9日上午10:21:59
	 *
	 */
	@RequestMapping(value = "/userGrant.do")
	@ResponseBody
	public Object grant(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception {
		JsonResult result = new JsonResult();
		try {
			int userId = Integer.parseInt(request.getParameter("userId"));
			String roleIds = request.getParameter("roleIds");
			userRoleService.saveUserRole(userId,roleIds);
			result.setSuccess(true);
			result.setMsg("授权成功！");
		} catch (Exception e) {
			e.printStackTrace();
			result.setMsg("授权失败！");
		}
		return result;
	}
	
	/**
	 * @Description:获得授权所需的菜单树
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 *
	 */
	@RequestMapping(value = "/roleGetTree.do")
	@ResponseBody
	public Object getMenuTree(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception {
		int roleId = Integer.parseInt(request.getParameter("id"));
		List<TreeNodeBean> nodes = userRoleService.getRoleMenuTree(roleId,1);
		return nodes;
	}
	
	/**
	 * @Description:保存角色授权信息
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 *
	 */
	@RequestMapping(value = "/roleGrant.do")
	@ResponseBody
	public Object roleGrant(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception {
		JsonResult result = new JsonResult();
		try {
			String roleId = request.getParameter("roleId");
			String menuIds = request.getParameter("menuIds");
			userRoleService.saveRoleMenu(roleId,menuIds);
			result.setSuccess(true);
			result.setMsg("授权成功！");
		} catch (Exception e) {
			e.printStackTrace();
			result.setMsg("授权失败！");
		}
		return result;
	}
}

package com.fhsoft.system.control;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fhsoft.model.User;
import com.fhsoft.model.Users;
import com.fhsoft.system.service.SysService;

/**
 * @ClassName:com.fhsoft.sys.control.LoginControl
 * @Description:用户登录的control处理
 *
 * @Author:liyi
 * @Date:2015年4月7日下午2:16:06
 *
 */
@Controller
public class SysControl {
	
	@Autowired
	protected SysService sysService;
	protected Logger log = LoggerFactory.getLogger(getClass());
	
	/**
	 * @Description:用户登录
	 * @param request
	 * @param response
	 * @param attr
	 * @param session
	 * @return
	 * @throws Exception
	 * @Date:2015年4月7日下午2:30:01
	 *
	 */
	@RequestMapping(value="/login.do",method=RequestMethod.POST)
	public ModelAndView login(HttpServletRequest request,
			HttpServletResponse response,RedirectAttributes attr,HttpSession session) throws Exception {
		//TODO 用户校验
		String name=request.getParameter("name");
		String password=request.getParameter("password");
		Users u = sysService.login(name, password);
		if(u == null){
			attr.addFlashAttribute("error", "用户不存在，请重新输入！");
			return new ModelAndView("redirect:relogin.do");
		}else{
			if(password.trim().equals(u.getPassword().trim())){
				//根据userId查询出用户拥有的角色ID集合
				//List<Integer> roleIds = userService.getRoleIds(user.getId());
				//根据userId查询出用户拥有的菜单ID集合
				//List<Integer> menuIds = userService.getMenuIds(user.getId());
				session.setAttribute("menuIds", "");
				session.setAttribute("roleIds", "");
				session.setAttribute("user_info", u);
			}else{
				attr.addFlashAttribute("error", "用户名或密码不正确！");
				return new ModelAndView("redirect:relogin.do");
			}
		}
		return new ModelAndView("redirect:index.do");
	}
	
	/**
	 * @Description:重新登录
	 * @return
	 * @Date:2015年4月7日下午2:30:40
	 *
	 */
	@RequestMapping(value="relogin.do")
	public String relogin(){
		return "../../login";
	}
	
	/**
	 * @Description:跳转到主页面
	 * @return
	 * @Date:2015年4月7日下午2:33:10
	 *
	 */
	@RequestMapping(value="index.do")
	public String index(){
		return "index";
	}
	
	@RequestMapping(value="header.do")
	public ModelAndView header(){
		//TODO 用户处理逻辑
		User user = new User();  
	    Map<String,Object> data = new HashMap<String,Object>();  
	    user.setUsername("test");
	    data.put("user",user);  
	    return new ModelAndView("header",data);  
	}
	
	
	/**
	 * @Description:注销登录
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @Date:2015年4月7日下午2:34:00
	 *
	 */
	@RequestMapping(value="logout.do")
	public String logout(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (request.getSession() != null) {
			request.getSession().invalidate();
		}
		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		return "../../login";
	}
	
	/**
	 * 
	 * @Description 资源下载公用ACTION，fileName从WebRoot开始算，如WebRoot/resources/template/文言文字词导入模板.xls
	 * 的文件名为resources/template/文言文字词导入模板.xls
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @Date 2015-11-12 下午3:32:29
	 */
	@RequestMapping(value="download.do")
	public void download(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String path = this.getClass().getClassLoader().getResource("/../../").getPath();//项目主路径
		String fileName = URLDecoder.decode(path + request.getParameter("filename"),"utf8");
		File file = new File(fileName);
		String filename = file.getName();
		InputStream fis = new BufferedInputStream(new FileInputStream(fileName));
		byte[] buffer = new byte[fis.available()];
		fis.read(buffer);
		fis.close();
		response.reset();
		response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("utf-8"), "iso8859-1"));
		response.addHeader("Content-Length", "" + file.length());
		OutputStream os = new BufferedOutputStream(response.getOutputStream());
		response.setContentType("application/octet-stream");
		os.write(buffer);
		os.flush();
		os.close();
	}
}

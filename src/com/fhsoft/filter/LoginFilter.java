package com.fhsoft.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;  
        HttpServletResponse res = (HttpServletResponse)response;  
        //基于http协议的servlet  
          
        //如果没有登录.  
        String requestURI = req.getRequestURI().substring(req.getRequestURI().indexOf("/",1),req.getRequestURI().length());  
      
        //如果第一次请求不为登录页面,则进行检查用session内容,如果为登录页面就不去检查.  
        if(!("/login.jsp".equals(requestURI)||"/login.do".equals(requestURI)))  
        {  
            //取得session. 如果没有session则自动会创建一个, 我们用false表示没有取得到session则设置为session为空.  
            HttpSession session = req.getSession(false);  
            //如果session中没有任何东西.  
            if(session == null ||session.getAttribute("user_info")==null)  
            {  
                res.sendRedirect(req.getContextPath() + "/login.jsp");  
                //返回  
                return;  
            }  
              
        }  
        //session中的内容等于登录页面, 则可以继续访问其他区资源.  
        chain.doFilter(req, res);  
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}

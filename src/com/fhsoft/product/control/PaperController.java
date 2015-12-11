package com.fhsoft.product.control;

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
import com.alibaba.fastjson.JSONObject;
import com.fhsoft.base.bean.JsonResult;
import com.fhsoft.base.bean.Page;
import com.fhsoft.base.bean.TreeNodeBean;
import com.fhsoft.model.Paper;
import com.fhsoft.model.PaperModel;
import com.fhsoft.model.SubjectProperty;
import com.fhsoft.product.service.PaperService;
import com.fhsoft.subject.service.SubjectPropertyValueService;
import com.fhsoft.util.DownloadUtil;

/**
 * @ClassName:com.fhsoft.product.control.PaperController
 * @Description:试卷管理的control处理
 *
 * @Author:liyi
 * @Date:2015年11月17日下午2:05:35
 *
 */
@Controller
public class PaperController {
	
	private Logger logger = Logger.getLogger(PaperController.class);

	@Autowired
	private PaperService paperService;
	@Autowired
	private SubjectPropertyValueService subjectPropertyValueService;
	
	/**
	 * @Description:进入试卷库页面
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @Date:2015年11月17日下午2:39:58
	 *
	 */
	@RequestMapping("paperLib")
	public ModelAndView paperLib(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		String subject = request.getParameter("subject");
		map.put("subject", subject);
		return new ModelAndView("product/paper",map);
	}
	
	/**
	 * @Description:获得试卷列表
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @Date:2015年11月17日下午3:21:23
	 *
	 */
	@RequestMapping("paperList")
	@ResponseBody
	public Object list(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception {
		Page page = null;
		try {
			int pageNo = Integer.valueOf(request.getParameter("page"));
			int pageSize = Integer.valueOf(request.getParameter("rows"));
			String subject = request.getParameter("subject");
			String searchPaperName = request.getParameter("searchPaperName");
			page = paperService.getByPage(pageNo, pageSize, subject, searchPaperName);
		} catch (Exception e) {
			logger.error("获取试卷列表出错！",e);
		}
		return page;
	}
	
	/**
	 * @Description:进入添加试卷页面
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @Date:2015年11月27日下午3:08:44
	 *
	 */
	@RequestMapping("paperAdd")
	public ModelAndView paperAdd(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			String productId = request.getParameter("productId");
			String subject = request.getParameter("subject");
			//获得对应学科下的题型
			List<SubjectProperty> qtypeList = subjectPropertyValueService.getChildBySubjectCol(subject, "tx");
			String qtypeJson = JSON.toJSONString(qtypeList);
			map.put("qtypeJson", qtypeJson);
			map.put("qtypeList", qtypeList);
			map.put("productId", productId);
			map.put("subject", subject);
			map.put("zsdGenerateUrl", "paperGenerateByZsd1?subject="+subject+"&productId="+productId);
		} catch (Exception e) {
			logger.error("进入新增试卷页面出错！", e);
		}
		return new ModelAndView("product/paper_generate",map);
	}
	
	/**
	 * @Description:获得新增试卷的知识点树
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @Date:2015年11月20日上午11:48:59
	 *
	 */
	@RequestMapping("getPaperZsdTree")
	@ResponseBody
	public Object getPaperZsdTree(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception{
		List<TreeNodeBean> beans = new ArrayList<TreeNodeBean>();
		try {
			String subject = request.getParameter("subject");
			beans = paperService.getPaperZsdTree(subject);
		} catch (Exception e) {
			logger.error("获取新增试卷的知识点树出错！", e);
		}
		return beans;
	}
	
	/**
	 * @Description:根据知识点进入试题列表页面
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @Date:2015年11月27日下午4:39:07
	 *
	 */
	@RequestMapping("paperGenerateByZsd1")
	public ModelAndView paperGenerateByZsd1(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		String productId = request.getParameter("productId");
		String subject = request.getParameter("subject");
		map.put("productId", productId);
		map.put("subject", subject);
		map.put("zsdTreeUrl", "getPaperZsdTree?subject=" + subject);
		map.put("qstUrl", "paperZsdQuestionList?productId=" + productId + "&subject=" + subject);
		return new ModelAndView("product/paper_generate_zsd",map);
	}
	
	/**
	 * @Description:根据知识点获得试题列表
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @Date:2015年11月27日下午4:39:26
	 *
	 */
	@RequestMapping("paperZsdQuestionList")
	@ResponseBody
	public Object paperZsdQuestionList(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception {
		Page page = null;
		try {
			int pageNo = Integer.valueOf(request.getParameter("page"));
			int pageSize = Integer.valueOf(request.getParameter("rows"));
			String subject = request.getParameter("subject");
			String zsd = request.getParameter("zsd");
			page = paperService.paperZsdQuestion(pageNo,pageSize,subject,zsd);
		} catch (Exception e) {
			logger.error("获取新增试卷的试题列表出错！",e);
		}
		return page;
	}
	
	/**
	 * @Description:保存试卷
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @Date:2015年12月2日上午10:00:33
	 *
	 */
	@RequestMapping("paperSave")
	@ResponseBody
	public Object paperSave(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception {
		JsonResult result = new JsonResult();
		try {
			String paperName = request.getParameter("paperName");
			String paperInfo = request.getParameter("paperInfo");
			JSONObject paperObject = JSON.parseObject(paperInfo);
			int subject = paperObject.getIntValue("subject");
			int productId = paperObject.getIntValue("productId");
			Paper paper = new Paper();
			paper.setName(paperName);
			paper.setSubject(subject);
			paper.setProduct(productId);
			paper.setPaperContent(paperInfo);
			paperService.savePaper(paper);
			result.setSuccess(true);
			result.setMsg("保存成功！");
		} catch (Exception e) {
			result.setMsg("保存失败！");
			logger.error("试卷保存失败！", e);
		}
		return JSON.toJSONString(result);
	}
	
	/**
	 * @Description:浏览试卷
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @Date:2015年12月3日上午10:53:44
	 *
	 */
	@RequestMapping("paperView")
	public ModelAndView paperView(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			String id = request.getParameter("id");
			Paper paper = paperService.getById(id);
			PaperModel paperModel = paperService.getPaperFullContent(paper.getPaperContent());
			map.put("paperModel", paperModel);
		} catch (Exception e) {
			logger.error("浏览试卷出错！", e);
		}
		return new ModelAndView("product/paper_view",map);
	}
	
	/**
	 * @Description:删除试卷
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @Date:2015年11月19日上午9:39:56
	 *
	 */
	@RequestMapping("paperDelete")
	@ResponseBody
	public Object delete(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception {
		JsonResult result = new JsonResult();
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			paperService.delete(id);
			result.setSuccess(true);
			result.setMsg("删除成功！");
		} catch (Exception e) {
			result.setMsg("删除失败！");
			logger.error("试卷删除失败！", e);
		}
		return JSON.toJSONString(result);
	}
	
	/**
	 * @Description:导出试卷
	 * @param request
	 * @param response
	 * @param session
	 * @param authorize
	 * @return
	 * @throws Exception
	 * @Date:2015年11月19日上午10:52:01
	 *
	 */
	@RequestMapping("exportPaper")
	@ResponseBody
	public Object export(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception {
		JsonResult result = new JsonResult();
		try {
			String id = request.getParameter("id");
			String path = paperService.export(id,request);
			result.setSuccess(true);
			result.setObj(path);
			result.setMsg("导出成功！");
		} catch (Exception e) {
			result.setMsg("导出失败！");
			logger.error("试卷导出失败！", e);
		}
		return JSON.toJSONString(result);
	}
	
	/**
	 * @Description:下载试卷
	 * @param request
	 * @param response
	 * @param session
	 * @throws Exception
	 * @Date:2015年11月19日上午11:32:14
	 *
	 */
	@RequestMapping("paperDownload")
	public void download(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception {
		try {
			String path = request.getParameter("filepath");
			DownloadUtil.downLoad(path, response);
		} catch (Exception e) {
			logger.error("下载试卷出错！", e);
		}
	}
	
}

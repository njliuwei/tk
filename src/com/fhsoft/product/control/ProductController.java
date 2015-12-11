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
import com.fhsoft.base.bean.JsonResult;
import com.fhsoft.base.bean.Page;
import com.fhsoft.base.bean.TreeNodeBean;
import com.fhsoft.model.Authorize;
import com.fhsoft.model.PaperModel;
import com.fhsoft.model.Product;
import com.fhsoft.model.SubjectProperty;
import com.fhsoft.product.service.PaperService;
import com.fhsoft.product.service.ProductService;
import com.fhsoft.subject.service.SubjectPropertyValueService;
import com.fhsoft.util.DownloadUtil;


/**
 * @ClassName:com.fhsoft.product.control.ProductController
 * @Description:产品库管理的control处理
 *
 * @Author:liyi
 * @Date:2015年11月6日上午11:45:52
 *
 */
@Controller
public class ProductController {

	private Logger logger = Logger.getLogger(ProductController.class);
	
	@Autowired
	private ProductService productService;
	@Autowired
	private SubjectPropertyValueService subjectPropertyValueService;
	@Autowired
	private PaperService paperService;
	
	/**
	 * @Description:进入产品库页面
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @Date:2015年11月6日下午3:05:45
	 *
	 */
	@RequestMapping("productLib")
	public ModelAndView productLib(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		String subject = request.getParameter("subject");
		map.put("subject", subject);
		return new ModelAndView("product/product",map);
	}
	
	/**
	 * @Description:获得产品库列表
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @Date:2015年11月6日下午4:36:58
	 *
	 */
	@RequestMapping("productList")
	@ResponseBody
	public Object list(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception {
		Page page = null;
		try {
			int pageNo = Integer.valueOf(request.getParameter("page"));
			int pageSize = Integer.valueOf(request.getParameter("rows"));
			String subject = request.getParameter("subject");
			String status = request.getParameter("status");
			page = productService.getByPage(pageNo, pageSize, subject, status);
		} catch (Exception e) {
			logger.error("获取产品列表出错！",e);
		}
		return page;
	}
	
	/**
	 * @Description:添加产品
	 * @param request
	 * @param response
	 * @param session
	 * @param subjectProperty
	 * @return
	 * @throws Exception
	 * @Date:2015年11月9日上午10:24:00
	 *
	 */
	@RequestMapping(value="productAdd")
	@ResponseBody
	public Object add(HttpServletRequest request,
			HttpServletResponse response,HttpSession session,Product product) throws Exception {
		JsonResult result = new JsonResult();
		try {
			productService.add(product);
			result.setSuccess(true);
			result.setMsg("添加成功！");
		} catch (Exception e) {
			result.setMsg("添加失败！");
			logger.error("产品添加失败！", e);
		}
		return JSON.toJSONString(result);
	}
	
	/**
	 * @Description:根据subject和类型获得知识点树
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @Date:2015年11月9日下午3:11:11
	 *
	 */
	@RequestMapping("zsdTree")
	@ResponseBody
	public Object getZsdTree(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception{
		List<TreeNodeBean> beans = new ArrayList<TreeNodeBean>();
		try {
			String subject = request.getParameter("subject");
			beans = subjectPropertyValueService.getZsdTree(subject,"题目");
		} catch (Exception e) {
			logger.error("根据学科获取知识点树出错！", e);
		}
		return beans;
	}
	
	/**
	 * @Description:获得产品的修改数据
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @Date:2015年11月11日下午3:36:43
	 *
	 */
	@RequestMapping("productEdit")
	@ResponseBody
	public Object edit(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception {
		Product editProduct = null;
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			editProduct = productService.getById(id);
		} catch (Exception e) {
			logger.error("获取产品的修改数据出错！", e);
		}
		return editProduct;
	}
	
	/**
	 * @Description:修改产品
	 * @param request
	 * @param response
	 * @param session
	 * @param product
	 * @return
	 * @throws Exception
	 * @Date:2015年11月11日下午3:55:43
	 *
	 */
	@RequestMapping("productUpdate")
	@ResponseBody
	public Object update(HttpServletRequest request,
			HttpServletResponse response,HttpSession session,Product product) throws Exception {
		JsonResult result = new JsonResult();
		try {
			productService.update(product);
			result.setSuccess(true);
			result.setMsg("修改成功！");
		} catch (Exception e) {
			result.setMsg("修改失败！");
			logger.error("产品修改失败！", e);
		}
		return JSON.toJSONString(result);
	}
	
	/**
	 * @Description:浏览产品
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @Date:2015年11月12日下午3:19:04
	 *
	 */
	@RequestMapping("productView")
	public ModelAndView productView(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		String productId = request.getParameter("productId");
		String subject = request.getParameter("subject");
		map.put("productId", productId);
		map.put("subject", subject);
		return new ModelAndView("product/product_view",map);
	}
	
	/**
	 * @Description:产品浏览中的试题删除
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @Date:2015年12月7日上午10:13:07
	 *
	 */
	@RequestMapping("productViewQstDelete")
	@ResponseBody
	public Object productViewQstDelete(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception {
		JsonResult result = new JsonResult();
		try {
			String qstId = request.getParameter("qstId");
			String productId = request.getParameter("productId");
			productService.deleteViewQst(qstId,productId);
			result.setSuccess(true);
			result.setMsg("删除成功！");
		} catch (Exception e) {
			result.setMsg("删除失败！");
			logger.error("产品浏览试题删除失败！", e);
		}
		return JSON.toJSONString(result);
	}
	
	/**
	 * @Description:删除产品
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @Date:2015年11月12日上午9:58:49
	 *
	 */
	@RequestMapping("productDelete")
	@ResponseBody
	public Object delete(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception {
		JsonResult result = new JsonResult();
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			productService.delete(id);
			result.setSuccess(true);
			result.setMsg("删除成功！");
		} catch (Exception e) {
			result.setMsg("删除失败！");
			logger.error("产品删除失败！", e);
		}
		return JSON.toJSONString(result);
	}
	
	/**
	 * @Description:进入组卷页面
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @Date:2015年11月19日下午4:07:15
	 *
	 */
	@RequestMapping("paperGenerate")
	public ModelAndView paperGenerate(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			String productId = request.getParameter("productId");
			String productName = request.getParameter("productName");
			String subject = request.getParameter("subject");
			//获得对应学科下的题型
			List<SubjectProperty> qtypeList = subjectPropertyValueService.getChildBySubjectCol(subject, "tx");
			String qtypeJson = JSON.toJSONString(qtypeList);
			map.put("qtypeJson", qtypeJson);
			map.put("productId", productId);
			map.put("productName", productName);
			map.put("subject", subject);
			map.put("qtypeList", qtypeList);
			map.put("zsdGenerateUrl", "paperGenerateByZsd?subject="+subject+"&productId="+productId);
		} catch (Exception e) {
			logger.error("进入组卷页面出错！", e);
		}
		return new ModelAndView("product/paper_generate",map);
	}
	
	/**
	 * @Description:根据知识点浏览选题
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @Date:2015年11月30日下午5:11:05
	 *
	 */
	@RequestMapping("paperGenerateByZsd")
	public ModelAndView paperGenerateRight(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		String productId = request.getParameter("productId");
		String subject = request.getParameter("subject");
		map.put("productId", productId);
		map.put("subject", subject);
		map.put("zsdTreeUrl", "getProductZsdTree?productId=" + productId);
		map.put("qstUrl", "productZsdQuestionList?productId="+productId+"&subject="+subject);
		return new ModelAndView("product/paper_generate_zsd",map);
	}
	
	/**
	 * @Description:获得产品组卷的知识点树
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @Date:2015年11月20日上午11:48:59
	 *
	 */
	@RequestMapping("getProductZsdTree")
	@ResponseBody
	public Object getProductZsdTree(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception{
		List<TreeNodeBean> beans = new ArrayList<TreeNodeBean>();
		try {
			String productId = request.getParameter("productId");
			beans = productService.getProductZsdTree(productId);
		} catch (Exception e) {
			logger.error("获取产品组卷的知识点树出错！", e);
		}
		return beans;
	}
	
	/**
	 * @Description:根据知识点获得试题列表
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @Date:2015年11月25日上午11:15:29
	 *
	 */
	@RequestMapping("productZsdQuestionList")
	@ResponseBody
	public Object productZsdQuestionList(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception {
		Page page = null;
		try {
			int pageNo = Integer.valueOf(request.getParameter("page"));
			int pageSize = Integer.valueOf(request.getParameter("rows"));
			String productId = request.getParameter("productId");
			String subject = request.getParameter("subject");
			String zsd = request.getParameter("zsd");
			page = productService.productZsdQuestion(pageNo,pageSize,productId,subject,zsd);
		} catch (Exception e) {
			logger.error("获取产品组卷的试题列表出错！",e);
		}
		return page;
	}
	
	/**
	 * @Description:进入组卷中心
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @Date:2015年11月25日上午11:16:41
	 *
	 */
	@RequestMapping("paperPreview")
	public String paperPreview() {
		return "product/paper_preview";
	}
	
	/**
	 * @Description:试卷预览内容
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @Date:2015年12月1日上午10:52:55
	 *
	 */
	@RequestMapping("paperPreviewContent")
	@ResponseBody
	public Object paperPreviewContent(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String paperInfo = request.getParameter("paperInfo");
		PaperModel paperModel = paperService.getPaperFullContent(paperInfo);
		return paperModel;
	}
	
	
	/**
	 * @Description:授权
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @Date:2015年11月16日下午4:57:54
	 *
	 */
	@RequestMapping("keyGenerate")
	@ResponseBody
	public Object generate(HttpServletRequest request,
			HttpServletResponse response,HttpSession session,Authorize authorize) throws Exception {
		JsonResult result = new JsonResult();
		try {
			String path = productService.generateKey(authorize,request);
			result.setSuccess(true);
			result.setObj(path);
			result.setMsg("授权成功！");
		} catch (Exception e) {
			result.setMsg("授权失败！");
			logger.error("产品授权失败！", e);
		}
		return JSON.toJSONString(result);
	}
	
	/**
	 * @Description:下载txt文件
	 * @param request
	 * @param response
	 * @param session
	 * @throws Exception
	 * @Date:2015年11月17日上午10:53:53
	 *
	 */
	@RequestMapping("productTxtDownload")
	public void download(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception {
		try {
			String path = request.getParameter("filepath");
			DownloadUtil.downLoad(path, response);
		} catch (Exception e) {
			logger.error("下载授权文件出错！", e);
		}
	}
	
	/**
	 * @Description:查询产品名称是否存在
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 * @Date:2015年12月9日上午9:39:30
	 *
	 */
	@RequestMapping("isExistProductName")
	@ResponseBody
	public Object exist(HttpServletRequest request,
			HttpServletResponse response,HttpSession session) throws Exception {
		JsonResult result = new JsonResult();
		try {
			String name = request.getParameter("name");
			String id = request.getParameter("id");
			boolean flag = productService.isExistByName(name, id);
			result.setSuccess(flag);
		} catch (Exception e) {
			logger.error("查询产品名称是否存在失败！", e);
		}
		return JSON.toJSONString(result);
	}
	
}

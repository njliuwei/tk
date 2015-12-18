package com.fhsoft.product.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhsoft.base.bean.Page;
import com.fhsoft.base.bean.TreeNodeBean;
import com.fhsoft.model.Authorize;
import com.fhsoft.model.Product;
import com.fhsoft.model.Question;
import com.fhsoft.product.dao.AuthorizeDao;
import com.fhsoft.product.dao.ProductDao;
import com.fhsoft.product.dao.ProductQuestionDao;
import com.fhsoft.question.dao.QuestionDao;
import com.fhsoft.subject.dao.SubjectPropertyValueDao;
import com.fhsoft.util.DateUtil;
import com.fhsoft.util.FileUtil;

/**
 * @ClassName:com.fhsoft.product.service.ProductService
 * @Description:产品库管理的service操作
 *
 * @Author:liyi
 * @Date:2015年11月6日下午2:04:32
 *
 */
@Service("productService")
@Transactional
public class ProductService {
	
	@Autowired
	private ProductDao productDao;
	@Autowired
	private QuestionDao questionDao;
	@Autowired
	private ProductQuestionDao productQuestionDao;
	@Autowired
	private AuthorizeDao authorizeDao;
	@Autowired
	private SubjectPropertyValueDao subjectPropertyValueDao;

	/**
	 * @Description:获得产品列表
	 * @param pageNo
	 * @param pageSize
	 * @param subjectId
	 * @param status
	 * @return
	 * @Date:2015年11月6日下午3:10:55
	 * 
	 */
	public Page getByPage(int pageNo, int pageSize, String subjectId, String status) {
		Page page = productDao.getByPage(pageNo,pageSize,subjectId,status);
		return page;
	}

	/**
	 * @Description:添加产品
	 * @param product
	 * @Date:2015年11月9日上午10:33:05
	 * 
	 */
	public void add(Product product) {
		//获得知识点IDs
		String zsdIds = product.getZsd();
		//获得题型
		String tx = product.getTx();
		//获得难度
		String nd = product.getNd();
		
		List<Integer> qstIds = questionDao.getBySubjectAndZsd(String.valueOf(product.getSubject()), zsdIds, tx, nd);
	
		product.setQstCount(qstIds.size());
		//添加产品数据
		int productId = productDao.add(product);
		//添加产品和试题的关联数据
		productQuestionDao.batchAdd(productId,qstIds);
	}

	/**
	 * @Description:根据id获得Product对象
	 * @param id
	 * @return
	 * @Date:2015年11月11日下午3:37:04
	 * 
	 */
	public Product getById(int id) {
		return productDao.getById(id);
	}

	/**
	 * @Description:修改Product数据
	 * @param product
	 * @Date:2015年11月11日下午4:00:08
	 * 
	 */
	public void update(Product product) {
		//先删除产品和试题的关联
		productQuestionDao.deleteByProductId(product.getId());
		
		//获得知识点IDs
		String zsdIds = product.getZsd();
		//获得题型
		String tx = product.getTx();
		//获得难度
		String nd = product.getNd();
				
		List<Integer> qstIds = questionDao.getBySubjectAndZsd(String.valueOf(product.getSubject()), zsdIds, tx, nd);
		
		product.setQstCount(qstIds.size());
		//更新产品
		productDao.update(product);
		//添加产品和试题的关联数据
		productQuestionDao.batchAdd(product.getId(), qstIds);
	}

	/**
	 * @Description:删除产品数据
	 * @param id
	 * @Date:2015年11月12日上午10:15:08
	 * 
	 */
	public void delete(int id) {
		//删除产品表数据
		productDao.delete(id);
		//删除产品和试题的关联
		productQuestionDao.deleteByProductId(id);
	}

	/**
	 * @Description:浏览产品
	 * @param id
	 * @param subject 
	 * @return
	 * @Date:2015年11月12日下午3:24:32
	 * 
	 */
	public List<Question> view(String id, String subject) {
		List<Question> list = questionDao.getByProductId(id,subject);
		return list;
	}

	/**
	 * @Description:授权产品
	 * @param authorize
	 * @param filePath 
	 * @Date:2015年11月16日下午5:02:49
	 * 
	 */
	public String generateKey(Authorize authorize, HttpServletRequest request) throws Exception{
		//生成授权码
		String code = "12345456";
		//添加数据库记录
		authorize.setCode(code);
		authorizeDao.add(authorize);
		//生成txt文件
		StringBuffer str = new StringBuffer();
		str.append("产品名称：" + authorize.getProductName() + "\r\n");
		str.append("使用者：" + authorize.getUserName() + "\r\n");
		str.append("使用IP：" + authorize.getUserIP() + "\r\n");
		str.append("授权码：" + code + "\r\n");
		str.append("失效日期：" + authorize.getDueDate() + "\r\n");
		str.append("是否会员：" + authorize.getIsMember());
		String filePath = request.getSession().getServletContext().getRealPath("/") + "download/code_txt/";
		String fileName =filePath + DateUtil.getCurrentTime() + ".txt";
		FileUtil.writeTxtFile(str.toString(),fileName);
		//将产品状态置为已发布
		productDao.updateStatus(authorize.getProductId());
		return fileName;
	}

	/**
	 * @Description:根据productId获得对应的知识点树
	 * @param id
	 * @return
	 * @Date:2015年11月19日下午4:18:26
	 * 
	 */
	public List<TreeNodeBean> getProductZsdTree(String id) {
		Product product = productDao.getById(Integer.valueOf(id));
		String zsd = product.getZsd();
		List<TreeNodeBean> beans = subjectPropertyValueDao.getZsdTreeByIds(zsd);
		return beans;
	}

	/**
	 * @Description:根据productId和zsd获得试题列表
	 * @param id
	 * @param subject
	 * @param zsd
	 * @param tx 
	 * @return
	 * @Date:2015年11月20日下午4:03:55
	 * 
	 */
	public Page productZsdQuestion(int pageNo, int pageSize, String id, String subject, String zsd, String tx) {
		Page page = questionDao.getByProductAndZsd(pageNo, pageSize, id, subject, zsd, tx);
		return page;
	}

	/**
	 * @Description:根据ids获得试题列表
	 * @param qstIds
	 * @param subject 
	 * @return
	 * @Date:2015年11月25日上午11:25:11
	 * 
	 */
	public List<Question> getQstsByIds(String qstIds, String subject) {
		List<Question> list = questionDao.getByIds(qstIds,subject);
		return list;
	}

	/**
	 * @Description:产品浏览中的试题删除
	 * @param qstId
	 * @param productId
	 * @Date:2015年12月7日上午10:18:26
	 * 
	 */
	public void deleteViewQst(String qstId, String productId) {
		//删除试题
		productQuestionDao.deleteByProductAndQst(productId,qstId);
		//修改产品的试题数
		productDao.updateQstCount(productId);
	}
	
	/**
	 * @Description:
	 * @param qstId
	 * @param productId
	 * @Date:2015年12月16日下午2:27:56
	 * 
	 */
	public void addViewQst(String qstId, String productId) {
		//删除试题
		productQuestionDao.addByProductAndQst(productId,qstId);
		//修改产品的试题数
		productDao.updateQstCount(productId);
	}

	/**
	 * @Description:产品名称是否存在
	 * @param name
	 * @param id
	 * @return
	 * @Date:2015年12月9日上午9:41:12
	 * 
	 */
	public boolean isExistByName(String name, String id) {
		List<Product> list = productDao.getByName(name,id);
		if(list != null && list.size() > 0){
			return false;
		}
		return true;
	}

	/**
	 * @Description:获得产品添加试题的列表
	 * @param pageNo
	 * @param pageSize
	 * @param subject
	 * @param productId
	 * @param zsd
	 * @param tx 
	 * @param tx2 
	 * @return
	 * @Date:2015年12月16日上午11:39:34
	 * 
	 */
	public Page getAddQstByPage(int pageNo, int pageSize, String subject, String productId,String productZsd, String zsd, String tx) {
		Page page = questionDao.getProductAddQstByPage(pageNo, pageSize, subject, productId,productZsd, zsd, tx);
		return page;
	}

}

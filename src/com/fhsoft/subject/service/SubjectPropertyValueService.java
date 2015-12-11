package com.fhsoft.subject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhsoft.base.bean.Page;
import com.fhsoft.base.bean.TreeNodeBean;
import com.fhsoft.base.bean.TreeNodeBean1;
import com.fhsoft.model.SubjectProperty;
import com.fhsoft.subject.dao.SubjectPropertyValueDao;

/**
 * @ClassName:com.fhsoft.subject.service.SubjectPropertyValueService
 * @Description:学科属性值的service处理
 *
 * @Author:liyi
 * @Date:2015年10月27日上午11:26:45
 *
 */
@Service("subjectPropertyValueService")
@Transactional
public class SubjectPropertyValueService {
	
	@Autowired
	private SubjectPropertyValueDao subjectPropertyValueDao;

	/**
	 * @Description:学科属性值列表
	 * @param pageNo
	 * @param pageSize
	 * @param id
	 * @return
	 * @Date:2015年10月27日上午11:29:33
	 * 
	 */
	public Page getByPage(int pageNo, int pageSize, String id) {
		Page page = subjectPropertyValueDao.getByPage(pageNo,pageSize,id);
		return page;
	}

	/**
	 * @Description:添加学科属性值
	 * @param subjectProperty
	 * @Date:2015年10月27日下午2:12:08
	 * 
	 */
	public void add(SubjectProperty subjectProperty) {
		subjectPropertyValueDao.add(subjectProperty);
	}

	/**
	 * @Description:根据id获得SubjectProperty对象
	 * @param id
	 * @return
	 * @Date:2015年10月27日下午2:39:36
	 * 
	 */
	public SubjectProperty getById(int id) {
		return subjectPropertyValueDao.getById(id);
	}

	/**
	 * @Description:修改学科属性值
	 * @param subjectProperty
	 * @Date:2015年10月27日下午2:45:47
	 * 
	 */
	public void update(SubjectProperty subjectProperty) {
		subjectPropertyValueDao.update(subjectProperty);
	}

	/**
	 * @Description:根据id删除学科属性值
	 * @param id
	 * @Date:2015年10月27日下午3:05:52
	 * 
	 */
	public void deleteById(int id) {
		subjectPropertyValueDao.deleteById(id);
	}

	/**
	 * @Description:获取树形学科属性值
	 * @param id
	 * @return
	 * @Date:2015年10月27日下午3:30:53
	 * 
	 */
	public List<TreeNodeBean1> getTreeGrid(int id) {
		return subjectPropertyValueDao.getTreeGridByRoot(id);
	}

	/**
	 * @param id 
	 * @Description:获取树形学科属性值
	 * @param id
	 * @return
	 * @Date:2015年10月28日下午3:16:34
	 * 
	 */
	public List<TreeNodeBean> getComboTree(int pid, String id) {
		return subjectPropertyValueDao.getComboTree(pid,id);
	}

	/**
	 * @Description:根据id删除树形学科属性值
	 * @param id
	 * @Date:2015年10月29日上午11:18:06
	 * 
	 */
	public void deleteTreeById(int id) {
		subjectPropertyValueDao.deleteTreeById(id);
	}

	/**
	 * @Description:获得知识点树
	 * @param subjectId 学科Id
	 * @param type "题目"或"解析"
	 * @return
	 * @Date:2015年11月9日下午3:14:23
	 * 
	 */
	public List<TreeNodeBean> getZsdTree(String subjectId, String type) {
		return subjectPropertyValueDao.getZsdTree(subjectId,type);
	}

	/**
	 * @Description:根据学科和类型，列名获得对应的属性值
	 * @param subject
	 * @param col
	 * @return
	 * @Date:2015年12月2日下午3:07:24
	 *
	 */
	public List<SubjectProperty> getChildBySubjectCol(String subject, String col) {
		return subjectPropertyValueDao.getChildBySubjectCol(subject,col);
	}

}

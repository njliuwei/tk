package com.fhsoft.subject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhsoft.base.bean.TreeNodeBean1;
import com.fhsoft.model.ComboxModel;
import com.fhsoft.model.PropertyProperty;
import com.fhsoft.subject.dao.PropertyPropertyDao;

/**
 * @ClassName:com.fhsoft.subject.service.PropertyPropertyService
 * @Description:学科属性的属性service处理
 *
 * @Author:liyi
 * @Date:2015年10月30日下午3:52:03
 *
 */
@Service("propertyPropertyService")
public class PropertyPropertyService {
	
	@Autowired
	private PropertyPropertyDao propertyPropertyDao;

	/**
	 * @Description:获得学科属性的属性列表
	 * @param pid
	 * @return
	 * @Date:2015年11月2日下午4:10:22
	 * 
	 */
	public List<TreeNodeBean1> getTreeGrid(int pid) {
		return propertyPropertyDao.getTreeGrid(pid);
	}

	/**
	 * @Description:添加学科属性的属性
	 * @param propertyProperty
	 * @Date:2015年11月3日上午10:19:27
	 * 
	 */
	public void add(PropertyProperty propertyProperty) {
		propertyPropertyDao.add(propertyProperty);
	}

	/**
	 * @Description:根据id获得PropertyProperty对象
	 * @param id
	 * @return
	 * @Date:2015年11月3日上午10:59:02
	 * 
	 */
	public PropertyProperty getById(int id) {
		return propertyPropertyDao.getById(id);
	}

	/**
	 * @Description:根据id修改PropertyProperty
	 * @param propertyProperty
	 * @Date:2015年11月3日上午11:06:53
	 * 
	 */
	public void update(PropertyProperty propertyProperty) {
		propertyPropertyDao.update(propertyProperty);
	}

	/**
	 * @Description:删除学科属性的属性及属性值
	 * @param id
	 * @Date:2015年11月3日上午11:29:18
	 * 
	 */
	public void deleteTreeById(int id) {
		propertyPropertyDao.deleteTreeById(id);
	}

	/**
	 * @Description:根据ParentId获取学科属性的属性
	 * @param id
	 * @return
	 * @Date:2015年11月3日下午2:53:17
	 * 
	 */
	public List<ComboxModel> getPropertyByParentId(int id) {
		return propertyPropertyDao.getPropertyByParentId(id);
	}

}

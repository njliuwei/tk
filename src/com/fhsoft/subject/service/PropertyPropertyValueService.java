package com.fhsoft.subject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhsoft.model.PropertyProperty;
import com.fhsoft.subject.dao.PropertyPropertyValueDao;

/**
 * @ClassName:com.fhsoft.subject.service.PropertyPropertyValueService
 * @Description:学科属性的属性值的service操作
 *
 * @Author:liyi
 * @Date:2015年11月3日下午3:33:46
 *
 */
@Service("propertyPropertyValueService")
public class PropertyPropertyValueService {
	
	@Autowired
	private PropertyPropertyValueDao propertyPropertyValueDao;

	/**
	 * @Description:添加学科属性的属性值
	 * @param propertyProperty
	 * @Date:2015年11月3日下午3:37:00
	 * 
	 */
	public void add(PropertyProperty propertyProperty) {
		propertyPropertyValueDao.add(propertyProperty);
	}

	/**
	 * @Description:根据id获得PropertyProperty对象
	 * @param id
	 * @return
	 * @Date:2015年11月3日下午4:06:20
	 * 
	 */
	public PropertyProperty getById(int id) {
		return propertyPropertyValueDao.getById(id);
	}

	/**
	 * @Description:修改
	 * @param propertyProperty
	 * @Date:2015年11月3日下午4:08:26
	 * 
	 */
	public void update(PropertyProperty propertyProperty) {
		propertyPropertyValueDao.update(propertyProperty);
	}

}

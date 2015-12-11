package com.fhsoft.subject.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhsoft.base.bean.Page;
import com.fhsoft.base.bean.TreeNodeAttribute;
import com.fhsoft.base.bean.TreeNodeBean;
import com.fhsoft.model.Menu;
import com.fhsoft.model.SubjectProperty;
import com.fhsoft.subject.dao.SubjectPropertyDao;
import com.fhsoft.system.dao.MenuDao;

/**
 * @ClassName:com.fhsoft.subject.service.SubjectPropertyService
 * @Description:学科属性的service处理
 *
 * @Author:liyi
 * @Date:2015年10月16日上午11:13:25
 *
 */
@Service("subjectPropertyService")
@Transactional
public class SubjectPropertyService {
	
	@Autowired
	private SubjectPropertyDao subjectPropertyDao;
	@Autowired
	private MenuDao menuDao;

	/**
	 * @Description:学科属性列表
	 * @param pageNo
	 * @param pageSize
	 * @param id
	 * @param status 
	 * @return
	 * @Date:2015年10月22日上午10:56:26
	 * 
	 */
	public Page getByPage(int pageNo, int pageSize, String id, String status) {
		Page page = subjectPropertyDao.getByPage(pageNo,pageSize,id,status);
		return page;
	}

	/**
	 * @Description:添加
	 * @param subjectProperty
	 * @Date:2015年10月22日下午3:48:13
	 * 
	 */
	public void add(SubjectProperty subjectProperty) {
		//获得父节点下子节点的数目
		int index = subjectPropertyDao.getChildrenCountByParentId(subjectProperty.getParentId()) + 1;
		//设置该属性对应的列名
		subjectProperty.setCol("property" + index);
		subjectPropertyDao.add(subjectProperty);
	}

	/**
	 * @Description:根据id获得SubjectProperty对象
	 * @param id
	 * @return
	 * @Date:2015年10月23日下午3:20:28
	 * 
	 */
	public SubjectProperty getById(int id) {
		return subjectPropertyDao.getById(id);
	}

	/**
	 * @Description:更新SubjectProperty对象
	 * @param subjectProperty
	 * @Date:2015年10月26日上午9:27:05
	 * 
	 */
	public void update(SubjectProperty subjectProperty) {
		subjectPropertyDao.update(subjectProperty);
		
	}

	/**
	 * @Description:获得复制节点树
	 * @return
	 * @Date:2015年10月26日上午10:28:31
	 * 
	 */
	public List<TreeNodeBean> getCopyToTree() {
		List<TreeNodeBean> nodes = new ArrayList<TreeNodeBean>();
		List<Menu> menus = menuDao.getMenusByType(3);
		for(Menu menu : menus){
			TreeNodeBean node = new TreeNodeBean();
			node.setId(menu.getId());
			node.setText(menu.getName());
			node.setPid(menu.getParentId());
			node.setAttributes(new TreeNodeAttribute(menu.getUrl()));
			node.setState("open");
			
			nodes.add(node);
		}
		List<SubjectProperty> props = subjectPropertyDao.getBySubjectType1();
		for(SubjectProperty prop : props){
			TreeNodeBean node = new TreeNodeBean();
			node.setId(prop.getId());
			node.setText(prop.getName());
			node.setPid(prop.getParentId());
			node.setState("open");
			
			nodes.add(node);
		}
		return nodes;
	}

	/**
	 * @Description:复制属性到某节点下
	 * @param subjectPropertyId
	 * @param parentId
	 * @param coptToId 
	 * @Date:2015年10月26日上午11:33:09
	 * 
	 */
	public void copy(int subjectPropertyId, int parentId, int coptToId) {
		//map中存放的是复制前id和复制后id的对应关系
		Map<Integer,Integer> map = new HashMap<Integer, Integer>();
		map.put(parentId, coptToId);
		List<SubjectProperty> props = subjectPropertyDao.getChildrenById(subjectPropertyId);
		for(SubjectProperty prop : props){
			prop.setParentId(map.get(prop.getParentId()));
			int id = subjectPropertyDao.insert(prop);
			map.put(prop.getId(), id);
		}
		//获得父节点下子节点的数目
		int index = subjectPropertyDao.getChildrenCountByParentId(coptToId);
		String col = "property" + index;
		//更新学科属性的col列数据
		subjectPropertyDao.updateCol(col, map.get(subjectPropertyId));
	}

	/**
	 * @Description:学科属性名称是否存在
	 * @param name
	 * @param pid
	 * @param id 
	 * @return
	 * @Date:2015年11月4日下午2:36:11
	 * 
	 */
	public boolean isExistByNameAndPid(String name, String pid, String id) {
		List<SubjectProperty> list = subjectPropertyDao.getByNameAndPid(name,pid,id);
		if(list != null && list.size() > 0){
			return false;
		}
		return true;
	}
	

}

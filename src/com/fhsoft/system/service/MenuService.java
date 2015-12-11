package com.fhsoft.system.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhsoft.base.bean.TreeNodeAttribute;
import com.fhsoft.base.bean.TreeNodeBean;
import com.fhsoft.model.Menu;
import com.fhsoft.model.SubjectProperty;
import com.fhsoft.subject.dao.SubjectPropertyDao;
import com.fhsoft.system.dao.MenuDao;

/**
 * @ClassName:com.fhsoft.system.service.MenuService
 * @Description:系统菜单的service处理
 *
 * @Author:liyi
 * @Date:2015年10月20日上午11:36:42
 *
 */
@Service("menuService")
public class MenuService {
	
	@Autowired
	private MenuDao menuDao;
	@Autowired
	private SubjectPropertyDao subjectPropertyDao;

	/**
	 * @Description:根据type获得菜单的TreeNodeBean列表
	 * @param type 1:试题管理菜单  2:产品管理菜单  3:学科管理菜单  4:试卷管理菜单 5:系统管理菜单 6:字词管理菜单
	 * @return
	 * @Date:2015年10月20日上午11:42:30
	 * 
	 */
	public List<TreeNodeBean> getMenuTree(int type) {
		List<TreeNodeBean> nodes = new ArrayList<TreeNodeBean>();
		List<Menu> menus = menuDao.getMenusByType(type);
		for(Menu menu : menus){
			TreeNodeBean node = new TreeNodeBean();
			node.setId(menu.getId());
			node.setText(menu.getName());
			node.setPid(menu.getParentId());
			node.setAttributes(new TreeNodeAttribute(menu.getUrl()));
			if(type != 3){
				node.setState("open");
			}
			nodes.add(node);
		}
		//试题管理
		if(type == 1) {
			List<SubjectProperty> props = subjectPropertyDao.getMenuTreeByName("知识点");
			for(SubjectProperty prop : props){
				TreeNodeBean node = new TreeNodeBean();
				node.setId(prop.getId());
				node.setText(prop.getName());
				node.setPid(prop.getParentId());
				node.setAttributes(new TreeNodeAttribute(prop.getName(),prop.getSubjectId()+""));
				if(prop.getLevel() == 0){
					node.setState("open");
				}
				nodes.add(node);
			}
		}
		//字词管理
		if(type == 6) {
			List<SubjectProperty> props = subjectPropertyDao.getMenuTreeForJcbb();
			for(SubjectProperty prop : props){
				TreeNodeBean node = new TreeNodeBean();
				node.setId(prop.getId());
				node.setText(prop.getName());
				node.setPid(prop.getParentId());
				node.setAttributes(new TreeNodeAttribute(null,prop.getSubjectName()));
				if(prop.getLevel() == 0){
					node.setState("open");
				}
				nodes.add(node);
			}
		}
		//学科管理菜单中增加子菜单
		if(type == 3){
			List<SubjectProperty> props = subjectPropertyDao.getBySubjectType();
			for(SubjectProperty prop : props){
				TreeNodeBean node = new TreeNodeBean();
				node.setId(prop.getId());
				node.setText(prop.getName());
				node.setPid(prop.getParentId());
				//根据level添加url具体内容
				if(prop.getLevel() == 1){
					node.setAttributes(new TreeNodeAttribute("subjectProperty?id=" + prop.getId(),prop.getSubjectName()));
					node.setState("open");
				}else{
					node.setAttributes(new TreeNodeAttribute("subjectPropertyValue?id=" + prop.getId(),prop.getSubjectName() + "_" + prop.getParentName(), prop.getType()));
					node.setState("open");
				}
				nodes.add(node);
			}
		}
		return nodes;
	}

}

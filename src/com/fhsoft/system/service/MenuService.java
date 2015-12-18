package com.fhsoft.system.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhsoft.base.bean.TreeNodeAttribute;
import com.fhsoft.base.bean.TreeNodeBean;
import com.fhsoft.model.Menu;
import com.fhsoft.model.SubjectProperty;
import com.fhsoft.model.Users;
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
	public List<TreeNodeBean> getMenuTree(int type,Users user) {
		List<TreeNodeBean> nodes = new ArrayList<TreeNodeBean>();
		List<Menu> menus = menuDao.getMenusByType(type,user);
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
			Set<Integer> pids = new HashSet<Integer>();
			for(TreeNodeBean node : nodes) {
				pids.add(node.getId());
			}
			Set<Integer> removedPids = new HashSet<Integer>();
			List<SubjectProperty> props = subjectPropertyDao.getMenuTreeByName("知识点",null);
			for(SubjectProperty prop : props){
				if(!pids.contains(prop.getParentId()) && prop.getLevel() == 2) { 
					removedPids.add(prop.getId());
					continue;
				}
				if(prop.getLevel() > 2 && removedPids.contains(prop.getParentId())) {
					removedPids.add(prop.getId());
					continue;
				}
//				if(prop.getLevel() == 2) {
//					for(Menu menu : menus) {
//						if("知识点与教材体系关系设置".equals(menu.getName()) && menu.getParentId() == prop.getParentId()) {
//							TreeNodeBean node = new TreeNodeBean();
//							node.setId(prop.getId());
//							node.setText(prop.getName());
//							node.setPid(menu.getId());
//							node.setAttributes(new TreeNodeAttribute("zsd",prop.getSubjectId()+""));
//							if(prop.getIsSystem() == 0){
//								node.setState("open");
//							}
//							nodes.add(node);
//						}
//					}
//				}
				TreeNodeBean node = new TreeNodeBean();
				node.setId(prop.getId());
				node.setText(prop.getName());
				node.setPid(prop.getParentId());
				node.setAttributes(new TreeNodeAttribute("zsd",prop.getSubjectId()+""));
				if(prop.getIsSystem() == 0){
					node.setState("open");
				}
				nodes.add(node);
			}
			
			props = subjectPropertyDao.getMenuTreeByName("教材体系",null);
			removedPids = new HashSet<Integer>();
			for(SubjectProperty prop : props){
				if(!pids.contains(prop.getParentId()) && prop.getLevel() == 2) {
					removedPids.add(prop.getId());
					continue;
				}
				if(prop.getLevel() > 2 && removedPids.contains(prop.getParentId())) {
					removedPids.add(prop.getId());
					continue;
				}
				TreeNodeBean node = new TreeNodeBean();
				node.setId(prop.getId());
				node.setText(prop.getName());
				node.setPid(prop.getParentId());
				node.setAttributes(new TreeNodeAttribute("jctx",prop.getSubjectId()+""));
				if(prop.getIsSystem() == 0){
					node.setState("open");
				}
				nodes.add(node);
			}
		}
		//字词管理
		if(type == 6) {
			Set<Integer> pids = new HashSet<Integer>();
			for(TreeNodeBean node : nodes) {
				pids.add(node.getId());
			}
			Set<Integer> removedPids = new HashSet<Integer>();
			List<SubjectProperty> props = subjectPropertyDao.getMenuTreeForJcbb();
			for(SubjectProperty prop : props){
				if(!pids.contains(prop.getParentId()) && prop.getLevel() == 2) {
					removedPids.add(prop.getId());
					continue;
				}
				if(prop.getLevel() > 2 && removedPids.contains(prop.getParentId())) {
					removedPids.add(prop.getId());
					continue;
				}
				TreeNodeBean node = new TreeNodeBean();
				node.setId(prop.getId());
				node.setText(prop.getName());
				node.setPid(prop.getParentId());
				node.setAttributes(new TreeNodeAttribute(null,prop.getSubjectName()));
				if(prop.getIsSystem() == 0){
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

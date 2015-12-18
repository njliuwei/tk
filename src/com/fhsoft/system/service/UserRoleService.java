package com.fhsoft.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fhsoft.base.bean.Page;
import com.fhsoft.model.Role;
import com.fhsoft.model.TreeNodeBean;
import com.fhsoft.model.Users;
import com.fhsoft.system.dao.UserRoleDao;

@Service("userRoleService")
public class UserRoleService {

	@Autowired
	private UserRoleDao userRoleDao;
	
	public Page userList(int page,int pageRows,String name,String created){
		return userRoleDao.userList(page, pageRows, name, created);
	}
	
	public void saveUser(Users user) {
		userRoleDao.saveUser(user);
	}
	
	public void userUpdate(Users user) {
		userRoleDao.userUpdate(user);
	}
	
	public void deleteUser(Users user) {
		userRoleDao.deleteUser(user);
	}
	
	public Page roleList(int page,int pageRows){
		return userRoleDao.roleList(page, pageRows);
	}
	
	public Role getRoleInfoById(Integer id){
		return userRoleDao.getRoleInfoById(id);
	}
	
	public void saveRole(Role role) {
		userRoleDao.saveRole(role);
	}
	
	public void deleteRole(Role role) {
		userRoleDao.deleteRole(role);
	}
	
	public List<TreeNodeBean> getRoles(int userId) {
		return userRoleDao.getRoles(userId);
	}
	
	public void saveUserRole(int userId, String roleIds){
		userRoleDao.saveUserRole(userId, roleIds);
	}
	
	public List<TreeNodeBean> getRoleMenuTree(int roleId,int level) {
		return userRoleDao.getRoleMenuTree(roleId,level);
	}
	
	public void saveRoleMenu(String roleId,String menuIds){
		userRoleDao.saveUserRole(roleId, menuIds);
	}
}

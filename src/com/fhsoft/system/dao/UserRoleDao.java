package com.fhsoft.system.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.fhsoft.base.bean.Page;
import com.fhsoft.base.dao.BaseDao;
import com.fhsoft.model.Menu;
import com.fhsoft.model.Role;
import com.fhsoft.model.RoleMenu;
import com.fhsoft.model.TreeNodeBean;
import com.fhsoft.model.UserRole;
import com.fhsoft.model.Users;

@Repository
public class UserRoleDao extends BaseDao{

	public Page userList(int pageNo, int pageSize,String name,String created){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM users where 1=1 ");
		List<String> params = new ArrayList<String>();
		if(StringUtils.isNotBlank(name)){
			sql.append("AND NAME=? ");
			params.add(name);
		}
		if(StringUtils.isNotBlank(created)){
			sql.append("AND created>? ");
			params.add(created);
		}
		return pageQuery(pageNo, pageSize, sql.toString(),Users.class,params.toArray());
	}
	
	public void saveUser(Users user) {
		String sql="insert into users (name,password,created,lastmodified,username) values(?,?,?,?,?)";
		jdbcTemplate.update(sql, user.getName(),user.getPassword(),user.getCreated(),user.getLastmodified(),user.getUsername());
	}
	
	public void userUpdate(Users user) {
		String sql="update users set username=?,name=?,password=?,lastmodified=? where id=?";
		jdbcTemplate.update(sql,user.getUsername(),user.getName(),user.getPassword(),user.getLastmodified(),user.getId());
	}
	
	public void deleteUser(Users user) {
		String sql="delete from users where id=?";
		jdbcTemplate.update(sql,user.getId());
	}
	
	public Page roleList(int pageNo, int pageSize){
		String sql = "SELECT * FROM ROLE ";
		return pageQuery(pageNo, pageSize, sql.toString(),Role.class);
	}
	
	public Role getRoleInfoById(Integer id){
		String sql = "SELECT * FROM ROLE where id=? ";
		return jdbcTemplate.queryForObject(sql, new Object[] { id }, new BeanPropertyRowMapper<Role>(Role.class));
	}
	
	public void saveRole(Role role) {
		if(role.getId()==null){
			String sql="insert into ROLE (name,description,created) values(?,?,?)";
			jdbcTemplate.update(sql, role.getName(),role.getDescription(),role.getCreated());
		}else{
			String sql="update ROLE set name=?,description=? where id=?";
			jdbcTemplate.update(sql,role.getName(),role.getDescription(),role.getId());
		}
	}
	
	public void deleteRole(Role role) {
		String sql="delete from ROLE where id=?";
		jdbcTemplate.update(sql,role.getId());
	}
	
	public List<TreeNodeBean> getRoles(int userId) {
		List<TreeNodeBean> nodes = new ArrayList<TreeNodeBean>();
		String sql="select * from role";
		List<Role> roles = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Role>(Role.class));
		String sql1="select a.* from role a left join user_role b on a.id=b.roleId where b.userId = ?";
		List<Role> selectRoles = jdbcTemplate.query(sql1, new BeanPropertyRowMapper<Role>(Role.class),userId);
		/*将PRole对象转换成easyui需要的节点对象*/
		for(Role role : roles){
			TreeNodeBean node = new TreeNodeBean();
			node.setId(String.valueOf(role.getId()));
			node.setText(role.getName());
			for(Role srole : selectRoles){
				if(srole.getId()==role.getId()){
					node.setChecked(true);
				}
			}
//			if(selectRoles.contains(role)){
//				node.setChecked(true);
//			}
			node.setState("open");
			nodes.add(node);
		}
		return nodes;
	}
	
	public void saveUserRole(int userId, String roleIds) {
		//删除原先的用户授权
		String delsql=" delete from user_role where userId=? ";
		jdbcTemplate.update(delsql,userId);
		//用户授权
		if(roleIds !=null && !"".equals(roleIds)){
			String[] ids = roleIds.split(",");
			for(String roleId :ids){
				UserRole t = new UserRole();
				t.setRoleId(Integer.parseInt(roleId));
				t.setUserId(userId);
				this.saveUserRole(t);
			}
		}
	}
	
	public void saveUserRole(UserRole t) {
		String sql="insert into USER_ROLE (userId,roleId) values(?,?)";
		jdbcTemplate.update(sql, t.getUserId(),t.getRoleId());
	}
	
	public List<TreeNodeBean> getRoleMenuTree(int roleId, int level) {
		List<TreeNodeBean> nodes = new ArrayList<TreeNodeBean>();
		//List<Menu> menus =  menuDao.findByHql(" from PMenu ");
		String sql="select id,name,url,type,parent_Id as parentId  from menu ";
		List<Menu> menus =  jdbcTemplate.query(sql, new BeanPropertyRowMapper<Menu>(Menu.class));
		//List<PMenu> selectMenus = menuDao.findBySql(PMenu.class,"select a.* from p_menu a left join p_role_menu b on a.menuid=b.menuId where b.roleId = ?0",roleId);
		String sql1 = "select a.* from menu a left join role_menu b on a.id=b.menuId where b.roleId = ?";
		List<Menu> selectMenus = jdbcTemplate.query(sql1, new BeanPropertyRowMapper<Menu>(Menu.class),roleId);
		/*将PMenu对象转换成easyui需要的节点对象*/
		for(Menu menu : menus){
			TreeNodeBean node = new TreeNodeBean();
			node.setId(String.valueOf(menu.getId()));
			node.setText(menu.getName());
			node.setPid(String.valueOf(menu.getParentId()));
//			if(selectMenus.contains(menu)){
//				node.setChecked(true);
//			}
			for (Menu smenu : selectMenus) {
				if(menu.getId()==smenu.getId()){
					node.setChecked(true);
				}
			}
			if(menu.getParentId() != null ){
				node.setState("open");
			}
			nodes.add(node);
		}
		return nodes;
	}
	
	public void saveUserRole(String roleId, String menuIds) {
		//删除原先的角色授权
		String delsql="delete from Role_Menu where roleid = ?";
		jdbcTemplate.update(delsql,roleId);
		//角色授权
		if(menuIds !=null && !"".equals(menuIds)){
		String[] ids = menuIds.split(",");
		for(String menuId :ids){
			RoleMenu t = new RoleMenu();
			t.setRoleId(Integer.parseInt(roleId));
			t.setMenuId(menuId);
			this.save(t);
			}
		}
	}
	
	public void save(RoleMenu rm){
		String sql="insert into role_menu (roleId,menuId) values(?,?)";
		jdbcTemplate.update(sql, rm.getRoleId(),rm.getMenuId());
	}
}

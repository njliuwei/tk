package com.fhsoft.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhsoft.model.Users;
import com.fhsoft.system.dao.SysDao;

/**
 * 
 * @author hjb
 * @date 2015-11-19 下午3:44:09
 * 
 */

@Service("sysService")
public class SysService {

	@Autowired
	private SysDao sysDao;
	
	public Users login (String name , String password){
		Users u=sysDao.login(name, password);
		return u;
	}
}

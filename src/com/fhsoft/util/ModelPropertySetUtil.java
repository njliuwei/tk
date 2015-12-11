package com.fhsoft.util;

import java.lang.reflect.Method;

import com.fhsoft.model.User;

/**
 * @ClassName:com.fhsoft.util.ModelPropertySetUtil
 * @Description:对对象的属性值进行赋值
 *
 * @Author:liyi
 * @Date:2015年7月1日下午2:03:58
 *
 */
public class ModelPropertySetUtil {

	public static void addInfo(Object obj,User user){
		setter(obj,"Created",DateUtil.getTime(),String.class); 
		setter(obj,"Creatorid",user.getId(),int.class); 
		setter(obj,"Creator",user.getUsername(),String.class); 
		setter(obj,"Lastmodified",DateUtil.getTime(),String.class); 
		setter(obj,"Lastmodifierid",user.getId(),int.class); 
		setter(obj,"Lastmodifier",user.getUsername(),String.class); 
	}
	
	public static void updateInfo(Object obj,User user){
		setter(obj,"Lastmodified",DateUtil.getTime(),String.class); 
		setter(obj,"Lastmodifierid",user.getId(),int.class); 
		setter(obj,"Lastmodifier",user.getUsername(),String.class); 
	}
	
    protected static void setter(Object obj, String att, Object value, Class<?> type) { 
        try { 
            Method method = obj.getClass().getMethod("set" + att, type); 
            method.invoke(obj, value); 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
    } 

	
}

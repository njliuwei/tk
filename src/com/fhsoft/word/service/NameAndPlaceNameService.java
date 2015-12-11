package com.fhsoft.word.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhsoft.base.bean.Page;
import com.fhsoft.model.PlaceName;
import com.fhsoft.model.SubjectProperty;
import com.fhsoft.model.Word;
import com.fhsoft.word.dao.NameAndPlaceNameDao;

@Service("nameService")
@Transactional
public class NameAndPlaceNameService {
	@Autowired
	private NameAndPlaceNameDao nameDao;
	
	@Resource
	private Set<String> nameTypeSet;
	
	/**
	 * 
	 * @Description 
	 * @param pageNo
	 * @param pageSize
	 * @param subject
	 * @param code
	 * @param values
	 * @return
	 * @Date 2015-11-5 上午10:02:00
	 */
	public Page list(int pageNo, int pageSize,PlaceName name){
		return nameDao.list(pageNo, pageSize,name);
	}

	/**
	 * 
	 * @Description 
	 * @param word
	 * @Date 2015-11-5 下午3:21:46
	 */
	public void addName(PlaceName name) {
		nameDao.addName(name);
	}

	/**
	 * 
	 * @Description 
	 * @param word
	 * @Date 2015-11-6 上午9:44:53
	 */
	public void updateName(PlaceName name) {
		nameDao.updateName(name);
	}

	/**
	 * 
	 * @Description 
	 * @param word
	 * @Date 2015-11-6 上午9:58:19
	 */
	public void delName(PlaceName name) {
		nameDao.delName(name);
	}

	/**
	 * 
	 * @Description 
	 * @param list
	 * @return
	 * @Date 2015-11-16 下午4:22:34
	 */
	public String save(List<PlaceName> list) {
		StringBuffer msg = new StringBuffer();
		Map<String,Integer> map = new HashMap<String,Integer>();
		for(int i = 0; i < list.size(); i++) {
			PlaceName name = list.get(i);
			
			if(!nameTypeSet.contains(name.getType())) {
				msg.append("第" + (i + 1) + "行类型错误，只能选择：人名或地名;\r\n");
			}
			if(name.getName() == null || name.getName().trim().length() == 0) {
				msg.append("第" + (i + 1) + "行英文名称必填;\r\n");
			}
			if(name.getCname() == null || name.getCname().trim().length() == 0) {
				msg.append("第" + (i + 1) + "行中文名称必填;\r\n");
			}
			if(name.getName() != null && name.getName().trim().length() > 0) {
				if(nameDao.getWordByName(name.getName()).size() > 0) {
					msg.append("第" + (i + 1) + "行字词已存在;\r\n");
				} else {
					if(map.containsKey(name.getName())) {
						msg.append("第" + (i + 1) + "行与"+map.get(name.getName())+"行重复;\r\n");
					} else {
						map.put(name.getName(), i + 1);
					}
				}
			}
			if(name.getName() != null && name.getName().length() > 500) {
				msg.append("第" + (i + 1) + "行英文名称，最多只能输入200字符;\r\n");
			}
			if(name.getCname() != null && name.getCname().length() > 500) {
				msg.append("第" + (i + 1) + "行中文名称，最多只能输入200字符;\r\n");
			}
		}
		if(msg.length() > 0) {
			return msg.toString();
		}
		nameDao.save(list);
		return "success";
	}
	
	public List<SubjectProperty> getJctxOfYw() {
		return nameDao.getJctxOfYw();
	}
	
	/**
	 * 
	 * @Description 得到字词的教材体系
	 * @param word
	 * @return
	 * @Date 2015-12-7 下午5:17:24
	 */
	public List<Word> getWordJctx(PlaceName name) {
		return nameDao.getWordJctx(name);
	}

	public List<PlaceName> getWordInfo(PlaceName name) {
		return nameDao.getWordById(name);
	}
}

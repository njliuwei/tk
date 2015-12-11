package com.fhsoft.word.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fhsoft.base.bean.Page;
import com.fhsoft.model.SubjectProperty;
import com.fhsoft.model.Word;
import com.fhsoft.word.dao.ModenWordDao;

@Service("wordService")
@Transactional
public class ModenWordService {
	@Autowired
	private ModenWordDao wordDao;
	
	@Resource
	private Set<String> wordTypeSet;
	
	@Resource
	private Set<String> wordPropertySet;
	
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
	public Page list(int pageNo, int pageSize,Word word){
		return wordDao.list(pageNo, pageSize,word);
	}

	/**
	 * 
	 * @Description 
	 * @param word
	 * @Date 2015-11-5 下午3:21:46
	 */
	@Transactional
	public void addWord(Word word) {
		wordDao.addWord(word);
	}

	/**
	 * 
	 * @Description 
	 * @param word
	 * @Date 2015-11-6 上午9:44:53
	 */
	@Transactional
	public void updateWord(Word word) {
		wordDao.updateWord(word);
	}

	/**
	 * 
	 * @Description 
	 * @param word
	 * @Date 2015-11-6 上午9:58:19
	 */
	@Transactional
	public void delWord(Word word) {
		wordDao.delWord(word);
	}

	/**
	 * 
	 * @Description 
	 * @param list
	 * @Date 2015-11-16 上午9:22:33
	 */
	@Transactional
	public String save(List<Word> list) {
		StringBuffer msg = new StringBuffer();
		//检测导入数据中是否有重复数据
		Map<String,Integer> map = new HashMap<String,Integer>();
		boolean flag = false;
		String key = null;
		for(int i = 0; i < list.size(); i++) {
			Word word = list.get(i);
			
			if(!wordTypeSet.contains(word.getType())) {
				msg.append("第" + (i + 1) + "行字词类型错误，只能选择：字或词;\r\n");
			}
			if(!wordPropertySet.contains(word.getProperty())) {
				msg.append("第" + (i + 1) + "行字词词性填写错误，请使用下载模板填写;\r\n");
			}
			if(word.getName() == null || word.getName().trim().length() == 0) {
				msg.append("第" + (i + 1) + "行字词必填;\r\n");
			}
			if(word.getExplain() == null || word.getExplain().trim().length() == 0) {
				msg.append("第" + (i + 1) + "行字词义项必填;\r\n");
			}
			if(StringUtils.isBlank(word.getSoundmark())) {
				msg.append("第" + (i + 1) + "行字词拼音必填;\r\n");
			}
//			if(word.getName() != null && word.getName().trim().length() > 0) {
//				if(wordDao.getWordByName(word.getName()).size() > 0) {
//					msg.append("第" + (i + 1) + "行字词已存在;\r\n");
//				} else if(map.containsKey(word.getName())) {
//					msg.append("第" + (i + 1) + "行与"+map.get(word.getName())+"行重复;\r\n");
//				} else {
//					map.put(word.getName(), i + 1);
//				}
//			}
			if(wordDao.getWordIsSingle(word.getName(), word.getSoundmark(), word.getExplain()) >0) {
				msg.append("第" + (i + 1) + "行字词已存在;\r\n");
			} else {
				key = word.getName() + "-" + word.getSoundmark() + "-" + word.getExplain();
				if(map.containsKey(key)) {
					msg.append("第" + (i + 1) + "行与"+map.get(key)+"行重复;\r\n");
				} else {
					map.put(key, i + 1);
					flag = true;
				}
			}
			
			if(StringUtils.isNotBlank(word.getBhs()) && !StringUtils.isNumeric(word.getBhs())) {
				msg.append("第" + (i + 1) + "行笔画数只能是数字;\r\n");
			}
			if(StringUtils.isNotBlank(word.getCbhs()) && !StringUtils.isNumeric(word.getCbhs())) {
				msg.append("第" + (i + 1) + "行查笔画数只能是数字;\r\n");
			}
			if(word.getComponent() != null && word.getComponent().length() > 1) {
				msg.append("第" + (i + 1) + "行偏旁部首，只能是一个中文字符;\r\n");
			}
			if(word.getName() != null && word.getName().length() > 100) {
				msg.append("第" + (i + 1) + "行字词，最多只能输入100字符;\r\n");
			}
			if(word.getSoundmark() != null && word.getSoundmark().length() > 100) {
				msg.append("第" + (i + 1) + "行拼音，最多只能输入100字符;\r\n");
			}
		}
		
		if(msg.length() > 0) {
			return msg.toString();
		}
		for(int i = 0; i < list.size(); i++) {
			Word word = list.get(i);
			Word basicWord = wordDao.getBasicWord(word.getName(), word.getSoundmark());
			//不存在基础字词
			if(basicWord == null) {
				int id = wordDao.addWord(word);
				wordDao.addWordAdditional(word, id+"");
			} else if(flag) {//此字词附加信息不重复，需要添加字词附加信息
				wordDao.addWordAdditional(word,basicWord.getId());
			}
		}
		
		return "success";
	}

	/**
	 * 
	 * @Description 
	 * @param word
	 * @return
	 * @Date 2015-12-4 上午11:27:16
	 */
	public List<Word> getWordInfo(Word word) {
		return wordDao.getWordInfo(word);
	}

	/**
	 * 
	 * @Description 
	 * @param word
	 * @Date 2015-12-7 上午10:17:20
	 */
	public void updateAdditionalWord(Word word) {
		wordDao.updateAdditionalWord(word);
	}

	/**
	 * 
	 * @Description 
	 * @param word
	 * @Date 2015-12-7 上午11:33:03
	 */
	public int addExample(Word word) {
		return wordDao.addExample(word);
	}

	/**
	 * 
	 * @Description 
	 * @param word
	 * @return
	 * @Date 2015-12-7 下午2:43:47
	 */
	public Word getExampleById(Word word) {
		return wordDao.getExampleById(word);
	}

	/**
	 * 
	 * @Description 
	 * @param word
	 * @Date 2015-12-7 下午2:57:33
	 */
	public void updExampleWord(Word word) {
		wordDao.updExampleWord(word);
	}

	/**
	 * 
	 * @Description 
	 * @param word
	 * @Date 2015-12-7 下午3:12:14
	 */
	public void delExample(Word word) {
		wordDao.delExample(word);
	}
	
	public List<SubjectProperty> getJctxOfYw() {
		return wordDao.getJctxOfYw();
	}
	
	/**
	 * 
	 * @Description 得到字词的教材体系
	 * @param word
	 * @return
	 * @Date 2015-12-7 下午5:17:24
	 */
	public List<Word> getWordJctx(Word word) {
		return wordDao.getWordJctx(word);
	}
}

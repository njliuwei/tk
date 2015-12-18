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
import com.fhsoft.model.Users;
import com.fhsoft.model.Word;
import com.fhsoft.word.dao.EnglishWordDao;

@Service("englishWordService")
@Transactional
public class EnglishWordService {
	@Autowired
	private EnglishWordDao wordDao;
	
	@Resource
	private Set<String> wordTypeSet;
	
	@Resource
	private Set<String> englishWordPropertySet;
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
	public void addWord(Word word) {
		wordDao.addWord(word,null);
	}

	/**
	 * 
	 * @Description 
	 * @param word
	 * @Date 2015-11-6 上午9:44:53
	 */
	public void updateWord(Word word) {
		wordDao.updateWord(word);
	}

	/**
	 * 
	 * @Description 
	 * @param word
	 * @Date 2015-11-6 上午9:58:19
	 */
	public void delWord(Word word) {
		wordDao.delWord(word);
	}

	/**
	 * 
	 * @Description 
	 * @param list
	 * @param u 
	 * @return
	 * @Date 2015-11-16 下午4:22:34
	 */
	public String save(List<Word> list, Users u) {
		StringBuffer msg = new StringBuffer();
		Map<String,Integer> map = new HashMap<String,Integer>();
		boolean flag = false;
		String key = null;
		for(int i = 0; i < list.size(); i++) {
			Word word = list.get(i);
			
			if(!wordTypeSet.contains(word.getType())) {
				msg.append("第" + (i + 2) + "行字词类型错误，只能选择：字或词;\r\n");
			}
			if(!englishWordPropertySet.contains(word.getProperty())) {
				msg.append("第" + (i + 2) + "行字词词性填写错误，请使用下载模板填写;\r\n");
			}
			if(StringUtils.isBlank(word.getName())) {
				msg.append("第" + (i + 2) + "行字词必填;\r\n");
			}
			if(StringUtils.isBlank(word.getSoundmark())) {
				msg.append("第" + (i + 2) + "行音标必填;\r\n");
			}
			if(StringUtils.isBlank(word.getMeaning())) {
				msg.append("第" + (i + 2) + "行释义必填;\r\n");
			}
			if(word.getName() != null && word.getName().length() > 500) {
				msg.append("第" + (i + 2) + "行字词，最多只能输入500字符;\r\n");
			}
			if(word.getSoundmark() != null && word.getSoundmark().length() > 500) {
				msg.append("第" + (i + 2) + "行音标，最多只能输入500字符;\r\n");
			}
			if(word.getVing() != null && word.getVing().length() > 500) {
				msg.append("第" + (i + 2) + "行现在分词，最多只能输入500字符;\r\n");
			}
			if(word.getAdv() != null && word.getAdv().length() > 500) {
				msg.append("第" + (i + 2) + "行副词，最多只能输入500字符;\r\n");
			}
			if(word.getComparison() != null && word.getComparison().length() > 500) {
				msg.append("第" + (i + 2) + "行比较级，最多只能输入500字符;\r\n");
			}
			if(word.getSuperlative() != null && word.getSuperlative().length() > 500) {
				msg.append("第" + (i + 2) + "行最高级，最多只能输入500字符;\r\n");
			}
			if(word.getSynonym() != null && word.getSynonym().length() > 500) {
				msg.append("第" + (i + 2) + "行近义词，最多只能输入500字符;\r\n");
			}
			if(word.getPreterite() != null && word.getPreterite().length() > 500) {
				msg.append("第" + (i + 2) + "行过去式，最多只能输入500字符;\r\n");
			}
			if(word.getEd() != null && word.getEd().length() > 500) {
				msg.append("第" + (i + 2) + "行过去分词，最多只能输入500字符;\r\n");
			}
			if(word.getSingular() != null && word.getSingular().length() > 500) {
				msg.append("第" + (i + 2) + "行第三人称单数，最多只能输入500字符;\r\n");
			}
			if(word.getStandard1() != null && word.getStandard1().length() > 500) {
				msg.append("第" + (i + 2) + "行标准1，最多只能输入500字符;\r\n");
			}
			if(word.getStandard2() != null && word.getStandard2().length() > 500) {
				msg.append("第" + (i + 2) + "行标准2，最多只能输入500字符;\r\n");
			}
			if(word.getProvenance() != null && word.getProvenance().length() > 500) {
				msg.append("第" + (i + 2) + "行出处，最多只能输入500字符;\r\n");
			}
			if(word.getPlural() != null && word.getPlural().length() > 500) {
				msg.append("第" + (i + 2) + "行名词-复数形式，最多只能输入500字符;\r\n");
			}
			if(word.getSubject() != null && word.getSubject().length() > 500) {
				msg.append("第" + (i + 2) + "行代词-主格，最多只能输入500字符;\r\n");
			}
			if(word.getObject() != null && word.getObject().length() > 500) {
				msg.append("第" + (i + 2) + "行代词-宾格，最多只能输入500字符;\r\n");
			}
			if(word.getNpp() != null && word.getNpp().length() > 500) {
				msg.append("第" + (i + 2) + "行名词性物主代词，最多只能输入500字符;\r\n");
			}
			if(word.getApp() != null && word.getApp().length() > 500) {
				msg.append("第" + (i + 2) + "行形容词性物主代词，最多只能输入500字符;\r\n");
			}
			if(word.getReflexive() != null && word.getReflexive().length() > 500) {
				msg.append("第" + (i + 2) + "行反身代词，最多只能输入500字符;\r\n");
			}
			if(word.getCardinalNum() != null && word.getCardinalNum().length() > 500) {
				msg.append("第" + (i + 2) + "行基数词，最多只能输入500字符;\r\n");
			}
			if(word.getOrdinalNum() != null && word.getOrdinalNum().length() > 500) {
				msg.append("第" + (i + 2) + "行序数词，最多只能输入500字符;\r\n");
			}
			
			if(wordDao.getWordIsSingle(word.getName(), word.getSoundmark(), word.getMeaning()) >0) {
				msg.append("第" + (i + 2) + "行字词已存在;\r\n");
			} else {
				key = word.getName() + "-" + word.getSoundmark() + "-" + word.getMeaning();
				if(map.containsKey(key)) {
					msg.append("第" + (i + 2) + "行与"+map.get(key)+"行重复;\r\n");
				} else {
					map.put(key, i + 2);
					flag = true;
				}
			}
		}
		if(msg.length() > 0) {
			return msg.toString();
		}
		Word word = null;
		Word basicWord = null;
		for(int i = 0; i < list.size(); i++) {
			word = list.get(i);
			basicWord = wordDao.getWordByName(word.getName());
			//不存在基础字词
			if(basicWord == null) {
				int id = wordDao.addWord(word,u);
				int aid = wordDao.addWordAdditional(word, id+"");
				word.setId(aid + "");
				wordDao.addExample(word);
			} else if(flag) {//此字词附加信息不重复，需要添加字词附加信息
				int aid = wordDao.addWordAdditional(word,basicWord.getId());
				word.setId(aid + "");
				wordDao.addExample(word);
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

	public Word getClassicalWordBasic(Word word) {
		return wordDao.getEnglishwordBasic(word);
	}

	public Word getClassicalWordAdditional(Word word) {
		return wordDao.getEnglishwordAdditional(word);
	}

}

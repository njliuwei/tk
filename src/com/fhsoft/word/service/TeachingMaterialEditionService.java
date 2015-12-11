package com.fhsoft.word.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fhsoft.base.bean.Page;
import com.fhsoft.model.TextBookWord;
import com.fhsoft.model.Word;
import com.fhsoft.word.dao.TeachingMaterialEditionDao;

@Service("editionService")
@Transactional
public class TeachingMaterialEditionService {
	@Autowired
	private TeachingMaterialEditionDao wordDao;
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
	public Page list(int pageNo, int pageSize,TextBookWord word){
		return wordDao.list(pageNo, pageSize,word);
	}

	/**
	 * 
	 * @Description 
	 * @param word
	 * @Date 2015-11-5 下午3:21:46
	 */
	public void addWord(TextBookWord word) {
		wordDao.addWord(word);
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

	public Page wordList(int page, int pageRows, String table,String name,String jctxId) {
		return wordDao.wordList(page,pageRows,table,name,jctxId);
	}

}

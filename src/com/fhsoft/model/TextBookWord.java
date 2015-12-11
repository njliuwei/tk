package com.fhsoft.model;

/**
 * 教材版本词库
 * @Classname com.fhsoft.model.TextBookWord
 * @Description 
 *
 * @author lw
 * @Date 2015-11-18 上午11:06:14
 *
 */
public class TextBookWord {
	private String id;
	
	private String jctxId;
	
	private String textbookId;
	
	private String wordId;
	
	private String type;
	
	private String textbookName;
	
	private String wordName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getJctxId() {
		return jctxId;
	}

	public void setJctxId(String jctxId) {
		this.jctxId = jctxId;
	}

	public String getTextbookId() {
		return textbookId;
	}

	public void setTextbookId(String textbookId) {
		this.textbookId = textbookId;
	}

	public String getWordId() {
		return wordId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setWordId(String wordId) {
		this.wordId = wordId;
	}

	public String getTextbookName() {
		return textbookName;
	}

	public void setTextbookName(String textbookName) {
		this.textbookName = textbookName;
	}

	public String getWordName() {
		return wordName;
	}

	public void setWordName(String wordName) {
		this.wordName = wordName;
	}
	
	
}

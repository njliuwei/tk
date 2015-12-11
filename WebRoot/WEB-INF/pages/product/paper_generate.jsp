<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML>
<html>
<head>
	<title>组卷</title>
	<script type="text/javascript" src="resources/js/jquery-easyui-1.3.6/jquery.min.js"></script>
	<script type="text/javascript" src="resources/js/jquery-easyui-1.3.6/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="resources/js/jquery-easyui-1.3.6/locale/easyui-lang-zh_CN.js"></script>
	<link rel="stylesheet" type="text/css" href="resources/js/jquery-easyui-1.3.6/themes/gray/easyui.css">
	<link rel="stylesheet" type="text/css" href="resources/js/jquery-easyui-1.3.6/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="resources/js/jquery-easyui-1.3.6/extEasyUIIcon.css">
	<link rel="stylesheet" type="text/css" href="resources/css/style.css">
	<link rel="stylesheet" type="text/css" href="resources/css/product/paper_generate.css">
	<link type="text/css" rel="stylesheet" href="resources/js/bootstrap2.3.1/css/bootstrap.min.css"/>
	<script type="text/javascript" src="resources/js/jquery-easyui-1.3.6/lyUtils.js"></script>
	<script type="text/javascript" src="resources/js/underscore-min.js"></script>
</head>

<script type="text/javascript">

	var qtypeJson = ${qtypeJson};
	var productId = ${productId};
	var subject = ${subject};
	
	/* 
	*  方法:Array.remove(dx) 通过遍历,重构数组 
	*  功能:删除数组元素. 
	*  参数:dx删除元素的下标. 
	*/ 
	Array.prototype.remove=function(dx) 
	{ 
	    if(isNaN(dx)||dx>this.length){return false;} 
	    for(var i=0,n=0;i<this.length;i++) 
	    { 
	        if(this[i]!=this[dx]) 
	        { 
	            this[n++]=this[i]; 
	        } 
	    } 
	    this.length-=1;
	};

	/**
	 * 试卷
	 */
	function Paper(){
		var paperId;
		var paperTitle ="请输入试卷标题";
		var paperNote;
		var paperAuthor ;	//命题人
		var parperAnswerTime = 120;	//答题时间
		this.productId = productId;
		this.subject= subject; 
		this.typeList = new Array();
	}
	
	/**
	 * 题型
	 */
	function Type(id,name,sort,comment){   
	    this.id  =  id;
	    this.name = name;
	    this.comment = comment;
	    this.sort = sort;
	    //题型中的试题数量
	    this.qstList = new Array();
	}

	/**
	 * 试题
	 */
	function Question(id,score){   
	    this.id  =  id;
	    this.score = score;
	}

	var paper = new Paper();
	
	/**
	 * 返回试卷中试题的数量
	 * @return 
	 */
	Paper.prototype.getQstCount = function(){
		var count = 0;
		for(i in this.typeList){
			count += this.getTypeQstCount(this.typeList[i].id);
		}
		return count;
	};

	/**
	 * 返回试卷中试题的数量
	 * @return 
	 */
	Paper.prototype.getSelectedQstIds = function(){
		var qstids = "";
		for(var i=0;i< this.typeList.length;i++){
			for(var j=0;j<this.typeList[i].qstList.length;j++){
				qstids += "," +  this.typeList[i].qstList[j].id;
			}
		}
		if(qstids.length>0){
			qstids = qstids.substr(1); 
		}
		return qstids;
	};

	/**
	 * 返回试卷中某个题型的试题数量
	 * @return 
	 */
	Paper.prototype.getTypeQstCount = function(typeId){
		var type = this.getType(typeId);
		if(type && type.qstList){
			return type.qstList.length;
		}
		return 0;
	};

	/**
	 * 判断试卷中是否有该题型
	 * @param typeId 题型id
	 * @return 包含返回true 不包含返回false
	 */
	Paper.prototype.containsType = function(typeId){
		for(i in this.typeList){
			if(typeId == this.typeList[i].id){
				return true;
			}
		}
		return false;
	};
	
	/**
	 * 判断试卷中是否有该试题
	 * @param qstId 试题id
	 * @return 包含返回true 不包含返回false
	 */
	Paper.prototype.containsQst = function(qstId){
		for(i in this.typeList){
			for(j in this.typeList[i].qstList){
				if(qstId == this.typeList[i].qstList[j].id){
					return true;
				}
			}
		}
		return false;
	};
	
	/**
	 * 获得某个题型
	 * @param typeId 题型id
	 */
	Paper.prototype.getType = function(typeId){
		for(i in this.typeList){
			if(typeId == this.typeList[i].id){
				return this.typeList[i];
			}
		}
		return null;
	};
	
	/**
	 * 设置某个题型的分数，需要更新该题型下的试题的分数
	 * @param typeId 题型id
	 */
	Paper.prototype.setTypeScore = function(typeId,score){
		var type = this.getType(typeId);
		type.qstScore = score;
		for(i in type.qstList){
			if(score != type.qstList[i].score){
				type.qstList[i].score = score;
			}
		}
		return null;
	};
	
	/**
	 * 获得某个试题
	 * @param qstId 题型id
	 */
	Paper.prototype.getQst = function(qstId){
		for(j in this.typeList){
			var type = this.typeList[j];
			for(i in type.qstList){
				if(qstId == type.qstList[i].id){
					return type.qstList[i];
				}
			}
		}
		return null;
	};
	
	/**
	 * 上移某个题型
	 * @param typeId 题型id
	 */
	Paper.prototype.moveUpType = function(typeId){
		for(i in this.typeList){
			if(typeId == this.typeList[i].id){
				if(i!=0){//如果不是第一个
					var tmp = this.typeList[i];
					this.typeList[i] = this.typeList[i-1];
					this.typeList[i-1] = tmp;
					return true;
				}
			}
		}
		return false;
	};
	
	/**
	 * 下移某个题型
	 * @param typeId 题型id
	 */
	Paper.prototype.moveDownType = function(typeId){
		for(i in this.typeList){
			if(typeId == this.typeList[i].id){
				if(i != this.typeList.length-1){
					var tmp = this.typeList[i];
					this.typeList[i] = this.typeList[parseInt(i)+1];
					this.typeList[parseInt(i)+1] = tmp;
					return true;
				}
			}
		}
		return false;
	};
	
	/**
	 * 上移某个试题
	 * @param typeId 题型id
	 */
	Paper.prototype.moveUpQst = function(qstId,typeId){
		var type = this.getType(typeId);
		for(i in type.qstList){
			if(qstId == type.qstList[i].id){
				if(i!=0){//如果不是第一个
					var tmp = type.qstList[i];
		            type.qstList[i] = type.qstList[i-1];
					type.qstList[i-1] = tmp;
					return true;
				}
			}
		}
		return false;
	};
	
	/**
	 * 下移某个试题
	 * @param typeId 题型id
	 */
	Paper.prototype.moveDownQst = function(qstId,typeId){
		var type = this.getType(typeId);
		for(i in type.qstList){
			if(qstId == type.qstList[i].id){
				if(i != type.qstList.length-1){
					var tmp = type.qstList[i];
		            type.qstList[i] = type.qstList[parseInt(i)+1];
					type.qstList[parseInt(i)+1] = tmp;
					return true;
				}
			}
		}
		return false;
	};

	/**
	 * 清空试卷中的数据
	 * @return 包含返回true 不包含返回false
	 */
	Paper.prototype.clear = function(){
		this.qstList = new Array();
		this.typeList = new Array();
	};

	/**
	 * 清空试卷中某个题型的数据
	 * @return 
	 */
	Paper.prototype.clearType = function(typeId){
		for(var i=0;i< this.typeList.length;i++){
			if(typeId == this.typeList[i].id){
				this.typeList.remove(i);
			}
		}
	};

	/**
	 * 向试卷中添加试题
	 * 1、首先检查题型是否存在，如果不存在，先添加题型
	 * 2、题型如果存在向该题型中添加试题
	 * @param qstid 试题id
	 * @param typeId 题型id
	 * @return
	 */
	Paper.prototype.add = function(qstid,typeId){
		//如果包含试题,则不需要添加
		if(this.containsQst(qstid)){
			return;
		}
		//初始化取题型的默认分数
		var score = $("#qtype_"+typeId).attr("qstScore");
		//题型
		var name = $("#qtype_"+typeId).attr("name");
		//排序
		var sort = $("#qtype_"+typeId).attr("sort");
		//备注
		var comment = $("#qtype_"+typeId).attr("comment");
		//如果试卷中已经包含了该题型，则把试题添加到该题型下
		if(this.containsType(typeId)){
			var qtype = this.getType(typeId);
			if(qtype != null){
				qtype.qstList.push(new Question(qstid,score));
			}
		}else{
			var qtype = new Type(typeId,name,sort,comment);
			qtype.qstList.push(new Question(qstid,score));
			this.typeList.push(qtype);
			this.typeList.sort(function(a,b){
				return a.sort - b.sort;
			});
		}
	};
	
	/**
	 * 从试卷中删除试题
	 * 如果题型中只有一个试题，则清空题型
	 * @param qstid 试题id
	 * @param typeId 题型id
	 * @return
	 */
	Paper.prototype.remove = function(qstid,typeId){
		//删除题型中的试题
		var type = paper.getType(typeId);

		//如果题型中只有一个试题
		if(type.qstList.length == 1){
			paper.clearType(typeId);
		}
		
		for(i in type.qstList){
			if(qstid == type.qstList[i].id){
				type.qstList.remove(i);
				break;
			}
		}
	};

	/**
	 * 替换试题，使用新的试题ID替换旧的试题ID
	 * @return
	 */
	Paper.prototype.replace = function(oldId,newId){
		
		for(j in paper.typeList){
			type = paper.typeList[j];
			for(i in type.qstList){
				if(oldId == type.qstList[i].id){
		            type.qstList[i].id = newId;
		            return;
				}
			}
		}
	};
	
	/**
	 * 清空试题蓝中的试题
	 * @return
	 */
	function emptyBasket(){
		paper.clear();
		
		refreshBasket();
		refreshRight();
	}

	/**
	 * 清空某个题型中的试题
	 * @param typeId
	 * @return
	 */
	function emptyTypeQst(typeId){
		paper.clearType(typeId);
		
		refreshBasket();
		refreshRight();
	}

	function refreshRight(){
		$("#iframe").attr("src",$("#iframe").attr("src"));
	}

	/**
	 * 刷新试题篮
	 * @return
	 */
	function refreshBasket(){
		$('#quescount').text(paper.getQstCount());
		 _.each( qtypeJson, function( type ){
			$('#typecount_'+type.id).text(paper.getTypeQstCount(type.id));
	    });
	}

	/**
	 * 向试题篮中加入试题
	 * @param qstid 试题id
	 * @param qtype 题型id
	 * @return
	 */
	function addQuestion(qstid,qtype){
		paper.add(qstid, qtype);
		refreshBasket();
	}
	
	/**
	 * 从试题篮删除某个试题
	 * @param qstid 试题ID
	 * @return
	 */
	function removeQuestion(qstid,qtype){
		paper.remove(qstid, qtype);
		refreshBasket();
	}
	
	/**
	 * 从试题篮替换某个试题
	 * @param 
	 * @return
	 */
	function replaceQuestion(oldId,newId){
		paper.replace(oldId,newId);
	}
	
	//刷新右侧iframe内容
	function goPage(src){
		$("#iframe").attr("src",src);
	}
	
	//进入组卷中心
	function goPreviewPaper(src){
		if(paper.getQstCount()>0){
			goPage(src);
		}else{
			$.messager.alert('警告', '试卷中还没有选择试题，请先选择！', 'warning');
		}
	}

</script>

<body class="easyui-layout">
<div data-options="region:'west'" style="width:200px;">
	<div id="content">
		<div id="left">
	        <table border="0" cellpadding="0" cellspacing="0" width="100%" style="table-layout:auto;height:100%;">
	            <tbody>
	                <tr>
	                <td width="98%" valign="top">
	                    <div>
	                        <div id="leftContent" style="max-width: 230px; padding: 8px 0;">
	                        	<div align="center">
		                            <div id="curbank"><span>当前产品：<a id="curproductname" role="button">${productName}</a> <i class="icon-hand-left"></i></span></div>
		                        </div>
	                        	<div id="basket">
		                            <div>
		                            <strong style="font-size:14px;"><i class="icon-shopping-cart"></i>试题篮</strong> (共 <span id="quescount">0</span> 题) <a id="emptybasket" href="javascript:emptyBasket();" title="清空该题型中的试题">清空</a>
		                            </div>
		                            <div style="margin-bottom:10px;">
		                                <table style='margin:auto;'>
		                                	<tbody id="quescountdetail">
		                                		<c:forEach var="type" items="${qtypeList}">
			                                		<tr>
														<td align='right' id="qtype_${type.id}" name="${type.name}" comment="${type.comment}" sort="${type.sort}">${type.name}：</td><td> <span id='typecount_${type.id}' >0</span> 题 <a href='javascript:emptyTypeQst(${type.id})'><i class='icon-trash'></i></a></td>
													</tr>
		                                		</c:forEach>
		                                	</tbody>
		                                </table>
		                            </div>
		                            <div>&nbsp;&nbsp;<a class="btn" href="javascript:goPreviewPaper('paperPreview')">进入组卷中心<i class="icon-arrow-right"></i></a></div>
		                        </div>
		                        
		                        <div style="border-bottom:1px solid #DEDEDE;"></div>
	                        
					            <ul class="nav nav-list">
					                <li class="nav-header">选题组卷</li>
					                <li><a href="javascript:goPage('${zsdGenerateUrl}')">按知识点浏览选题</a></li>
					                <li><a href="javascript:goPage('${basePath }/app/papersel/zujuan.do?productId=${product.SYS_DOCUMENTID }')">按教材体系浏览选题</a></li>
					                 <li><a href="javascript:goPage('${basePath }/app/papersel/zujuan.do?productId=${product.SYS_DOCUMENTID }')">按试卷浏览选题</a></li>
					            </ul>
					            <div style="margin-top:10px;border-bottom:1px solid #DEDEDE;"></div>
				            </div>
	                    </div>
	                </td>
	                </tr>
	            </tbody>
	        </table>
	    </div>
	</div>
</div>
<div data-options="region:'center',border:false">
	<iframe id="iframe" scrolling="yes" frameborder="0"  src="" style="width:100%;height:99%;"></iframe>
</div>

<!-- 保存试卷对话框start -->
<div id="saveDialog">
	<form id="addForm" method="post">
		<table style="margin-top: 10px;">
			<tr>
				<td>试卷名称</td>
				<td>
					<input type="hidden" id="paperInfo" name="paperInfo"/>
					<input class="easyui-validatebox" name="paperName" data-options="required:true" style="width: 200px;"/>
				</td>
			</tr>
		</table>
	</form>
</div>
<!-- 保存试卷对话框end -->

</body>
</html>

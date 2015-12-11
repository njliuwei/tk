<%@ page language="java" contentType="text/html; charset=utf-8"  pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>试卷浏览</title>
	<script type="text/javascript" src="resources/js/jquery-easyui-1.3.6/jquery.min.js"></script>
	<link rel="stylesheet" type="text/css" href="resources/css/product/paper_view.css">
	<link type="text/css" rel="stylesheet" href="resources/js/bootstrap2.3.1/css/bootstrap.min.css"/>
	<link rel="stylesheet" type="text/css" href="resources/css/style.css">
	
	<script type="text/javascript">
	/**
	 * 点击试卷结构试题，跳转到试题内容
	 */
	function move2anchor(aid){
	    $('#right').animate({scrollTop: $("#right").scrollTop()+$("#"+ aid).offset().top-40},'fast');
	    $('#'+aid).animate({}, 500 );
	}

	</script>
</head>
<body>
	<div id="content">
	  	<div id="left">
		  	<div class="left">
				<div id="paper_struct">
					<ul>
						<li onclick="move2anchor('paper_title')"><i class="icon-flag"></i> 试卷主标题</li>
					</ul>
					<div id="type_struct">
						<c:forEach var="type_struct" items="${paperModel.typeList}">
							<ul id="type_struct_ul_${type_struct.id}">
								<li id="type_struct_${type_struct.id}" onclick="move2anchor('type_${type_struct.id}')">&nbsp;&nbsp;<i class="icon-leaf"></i> <span id="typeStructIndex_${type_struct.id}">${type_struct.index}</span>、${type_struct.name}</li>
								<c:forEach var="qst_struct" items="${type_struct.qstList}">
									<li id="qst_struct_${qst_struct.id}" onclick="move2anchor('qstBlock_${qst_struct.id}')" >&nbsp;&nbsp;&nbsp;&nbsp;<span id="qstStructIndex_${qst_struct.id}">${qst_struct.index}</span> . <span id="qstStructContent_${qst_struct.id}">${qst_struct.summary}</span>...</li>
								</c:forEach>
							</ul>
						</c:forEach>
					</div>
				</div>
			</div>
	  	</div>
	  	
	  	
	  	<div id="right">
	  		<div id="paper_wrapper" >
	  			<div style="float: right;"><a class="btn" href="javascript:downloadPaper();"><i class="icon-download-alt"></i> 下载试卷</a>&nbsp;</div>
	  			<div id="paper_top">
		  			<div id="paper_title"><h1 id="paper_title_content">${paperModel.paperTitle}</h1></div>
		  			<div>考试时间：<span id="answerTime">${paperModel.parperAnswerTime}</span>分钟 ； 命题人：<span id="paperUserName">${paperModel.paperAuthor}</span></div>
		  			<div></div>
	  			</div>
	  			<div id="paper_note">${paperModel.paperNote}</div>
	  			<div id="contentWrap">
	  				<c:forEach var="type" items="${paperModel.typeList}">
						<div class="qstType" id="type_${type.id}">
							<div class="typeTitleDiv">
								<!-- 首先输出题标的信息 -->
								<div class="typeTitleContent" >
									<span class="typeLeft" id="typeIndex_${type.id}">${type.index}</span>、
									<span class="typeMemo">${type.name}
									<c:if test="${type.comment != ''}">(${type.comment})</c:if>
									</span>
								</div>
							</div>
							<!-- 存放该题型下的试题 -->
							<div id="typeQstDiv_${type.id}">
								<c:forEach var="qst" items="${type.qstList}">
								<div class="qstBlock" id="qstBlock_${qst.id}">
									<div class="qstContent">
										<!-- 输出试题题干 -->
										<div class="qstTitleBox">
											<span class="qstOrderNumber" id="qstIndex_${qst.id}">${qst.index}.</span>
										</div>
										<div style="margin-left:10px;margin-right:10px;overflow: hidden; word-break: break-all; word-wrap: break-word;">
											${qst.content}
										</div>
									</div>
								</div>
								</c:forEach>
							</div>
						</div>
	  				</c:forEach>
	  			</div>
	  		</div>
	  	</div>
	 </div>
  
</body>
</html>
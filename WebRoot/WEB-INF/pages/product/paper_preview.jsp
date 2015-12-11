<%@ page language="java" contentType="text/html; charset=utf-8"  pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>试卷预览</title>
	<script type="text/javascript" src="resources/js/jquery-easyui-1.3.6/jquery.min.js"></script>
	<link type="text/css" rel="stylesheet" href="resources/js/bootstrap2.3.1/css/bootstrap.min.css"/>
	<link type="text/css" rel="stylesheet" href="resources/css/style.css">
	<link type="text/css" rel="stylesheet" href="resources/css/product/paper_preview.css"/>
	<script type="text/javascript" src="resources/js/underscore-min.js"></script>
	
	<script type="text/javascript">
	var paper = window.parent.paper;
	
	var paperContent;
	var oldId;
	var typeIndex_zh = ["一","二","三","四","五","六","七","八","九","十","十一","十二","十三","十四","十五","十六","十七","十八","十九","二十"];

	_.templateSettings = {
			interpolate: /\<\@\=(.+?)\@\>/gim,
			evaluate: /\<\@([\s\S]+?)\@\>/gim,
			escape: /\<\@\-(.+?)\@\>/gim
	};
	
	$(function(){
		//如果此时试卷中的标题或注意事项为空，则初始化
		if($.isEmptyObject(paper.paperTitle)){
			paper.paperTitle = $("#paper_title_content").text();
		}
		if($.isEmptyObject(paper.paperNote)){
			paper.paperNote = $("#paper_note").html();
		}
		//当改变了标题或注意事项后更新
		$("#paper_title_content").focusout(function() {
			paper.paperTitle = $(this).text();
		});
		$("#paper_note").focusout(function() {
			paper.paperNote = $(this).html();
		});
		
		//获得试卷预览的内容
		$.post('paperPreviewContent', {
			'paperInfo' : JSON.stringify(paper)
		}, function(paperJson) {
			paperContent = paperJson;
			var type_struct_template = _.template(
				$( "#type_struct_template" ).html()
			);
			var qst_struct_template = _.template(
				$( "#qst_struct_template" ).html()
			);
			var type_template = _.template(
			    $( "#type_template" ).html()
			);
			var question_template = _.template(
			    $( "#question_template" ).html()
			);
			
			_.each( paperContent.typeList, function( type ){
				$( "#contentWrap" ).append(
					type_template( {'type':type})
		        );
				$( "#type_struct" ).append(
					type_struct_template( {'type':type} )
				);
				_.each( type.qstList, function( question ){
					$( "#typeQstDiv_"+type.id ).append(
						question_template( {'question':question} )
			        );
					$( "#type_struct_ul_"+type.id ).append(
						qst_struct_template( {'question':question} )
					);
				});
			});
			//操作工具条
			$(".qstBlock").hover(function(){
				$(".qst_tool_bar").hide();
				$(this).find(".qst_tool_bar").show();
			});
			$(".qstBlock").mouseleave(function(){
				$(".qst_tool_bar").hide();
			});
			$(".typeTitleDiv").mouseover(function(){
				$(this).find(".type_tool_bar").show();
			});
			$(".typeTitleDiv").mouseout(function(){
				$(this).find(".type_tool_bar").hide();
			});
			resetState();
		}, 'JSON');
	});
	
	
	/**
	 * 删除某个题型
	 */
	function deleteType(typeId){
		window.parent.emptyTypeQst(typeId);
	}
	/**
	 * 上移题型
	 */
	function moveUpType(typeId){
		if(paper.moveUpType(typeId)){
			$("#type_"+typeId).after($("#type_"+typeId).prev());
			$("#type_struct_ul_"+typeId).after($("#type_struct_ul_"+typeId).prev());
			resetState();
		}
	}
	/**
	 * 下移题型
	 */
	function moveDownType(typeId){
		if(paper.moveDownType(typeId)){
			$("#type_"+typeId).before($("#type_"+typeId).next());
			$("#type_struct_ul_"+typeId).before($("#type_struct_ul_"+typeId).next());
			resetState();
		}
	}
	
	/**
	 * 上移试题
	 */
	function moveUpQst(qid,typeId){
		if(paper.moveUpQst(qid,typeId)){
			$("#qstBlock_"+qid).after($("#qstBlock_"+qid).prev());
			$("#qst_struct_"+qid).after($("#qst_struct_"+qid).prev());
			resetState();
		}
	}
	/**
	 * 下移试题
	 */
	function moveDownQst(qid,typeId){
		if(paper.moveDownQst(qid,typeId)){
			$("#qstBlock_"+qid).before($("#qstBlock_"+qid).next());
			$("#qst_struct_"+qid).before($("#qst_struct_"+qid).next());
			resetState();
		}
	}
	
	/**
	 * 删除某个试题
	 */
	function deleteQst(qid,typeId){
		$("#qstBlock_"+qid).remove();
		$("#qst_struct_"+qid).remove();
		window.parent.removeQuestion(qid,typeId);
		resetState();
	}
	
	/**
	 * 当试卷结构变化时，重新设置一些属性
	 * 内排序
	 * 1、上移下移按钮
	 * 2、题型、试题序号
	 */
	function resetState(){
		var typeList = paper.typeList;
		//设置第一个和最后一个上移下移是否可用
		for(var i = 0;i < typeList.length;i++){
			$("#type_up_"+typeList[i].id).removeAttr("disabled");
			$("#type_down_"+typeList[i].id).removeAttr("disabled");
			
			$("#type_up_"+typeList[0].id).attr("disabled","disabled");
			$("#type_down_"+typeList[typeList.length-1].id).attr("disabled","disabled");
			var qstList = typeList[i].qstList;
			for(var j = 0;j<qstList.length;j++){
				$("#qst_up_"+qstList[j].id).removeAttr("disabled");
				$("#qst_down_"+qstList[j].id).removeAttr("disabled");
			}
			$("#qst_up_"+qstList[0].id).attr("disabled","disabled");
			$("#qst_down_"+qstList[qstList.length-1].id).attr("disabled","disabled");
		}
		
		//重排题型的序号
		var typeIndex = typeIndex_zh; 
		var index = 1;
		for(var i = 0;i < typeList.length;i++){
			$("#typeIndex_"+typeList[i].id).text(typeIndex[i]);
			$("#typeStructIndex_"+typeList[i].id).text(typeIndex[i]);
			var qstList = typeList[i].qstList;
			for(var j =0 ;j<qstList.length;j++){
				$("#qstIndex_"+qstList[j].id).text(index);
				$("#qstStructIndex_"+qstList[j].id).text(index);
				index++;
			}
		}
		
	}
	
	//保存试卷
	function savePaper(){
		if(paper.getQstCount() <= 0){
			window.parent.$.messager.alert('警告', '试卷中还没有选择试题，请先选择！', 'warning');
			return;
		}
		window.parent.$('#saveDialog').dialog({
			title : '保存试卷',
			modal : true,
			inline : true,
			width : 300,
			height : 135,
			buttons : [ {
				text : '保存',
				handler : function() {
					window.parent.$.messager.progress({
						title : '提示',
						text : '数据处理中，请稍候....'
					}); // 显示进度条
					window.parent.$('#addForm').form('submit', {
						url : 'paperSave',
						onSubmit : function() {
							var isValid = window.parent.$('#addForm').form('validate');
							if (!isValid) {
								window.parent.$.messager.progress('close'); // 如果表单是无效的则隐藏进度条
							}
							return isValid; // 返回false终止表单提交
						},
						success : function(result) {
							window.parent.$.messager.progress('close'); // 如果提交成功则隐藏进度条
							window.parent.$('#saveDialog').dialog('close');
							var r = $.parseJSON(result);
							window.parent.$.messager.show({
								title : '提示',
								msg : r.msg
							});
						}
					});
				}
			} ],
			onOpen : function() {
				window.parent.$('#addForm').form('reset');
				window.parent.$('#paperInfo').val(JSON.stringify(paper));
			}
		});
	}
	
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
	<div id="top">
	  <div style="float: left;">
	  	<h1>试卷预览</h1>
	  </div>
	  <div style="float: right;">
		  <a class="btn" href="javascript:downloadPaper();"><i class="icon-download-alt"></i> 下载试卷</a>&nbsp;&nbsp;
		  <a class="btn" href="javascript:savePaper();"><i class="icon-ok"></i> 保存试卷</a>
	  </div>
	  <div style="clear:both;"></div>
	</div>
  <div id="content">
  	<div id="left">
	  	<div class="left">
			<div id="paper_struct">
				<ul>
					<li onclick="move2anchor('paper_title')"><i class="icon-flag"></i> 试卷主标题</li>
				</ul>
				<div id="type_struct">
				</div>
			</div>
		</div>
  	</div>
  	<div id="right">
  		<div id="paper_wrapper" >
  			<div id="paper_top">
	  			<div id="paper_title" title="点击修改标题内容"><h1 contentEditable="true" id="paper_title_content">请输入试卷标题</h1></div>
	  			<div>考试时间：<span id="answerTime" contentEditable="true">120</span>分钟 ； 命题人：<span id="paperUserName" contentEditable="true"></span></div>
	  			<div></div>
  			</div>
  			<div id="paper_note" contentEditable="true" title="点击修改注意事项的内容">
  			注意事项：<br/>
  			1．答题前填写好自己的姓名、班级、考号等信息<br/>
			2．请将答案正确填写在答题卡上
  			</div>
  			<div id="contentWrap">
  			</div>
  		</div>
  	</div>
  </div>
 
 <!-- 左侧题型结构 -->
<script type="text/template" id="type_struct_template">
	<ul id="type_struct_ul_<@= type.id @>">
		<li id="type_struct_<@= type.id @>" onclick="move2anchor('type_<@= type.id @>')">&nbsp;&nbsp;<i class="icon-leaf"></i> <span id="typeStructIndex_<@= type.id @>"><@= type.index @></span>、<@= type.name @></li>
	</ul>
</script>
<!-- 左侧试题结构 -->
<script type="text/template" id="qst_struct_template">
	<li id="qst_struct_<@= question.id @>" onclick="move2anchor('qstBlock_<@= question.id @>')" >&nbsp;&nbsp;&nbsp;&nbsp;<span id="qstStructIndex_<@= question.id @>"><@= question.index @></span> . <span id="qstStructContent_<@= question.id @>"><@= question.summary @></span>...</li>
</script>
<!-- 右侧题型结构 -->
<script type="text/template" id="type_template">
	<div class="qstType" id="type_<@= type.id @>">
	<div class="typeTitleDiv">
		<div class="pull-right type_tool_bar" style="float:right;" >
			<div class="btn-group">
				<a class="btn btn-small" href="javascript:configType(<@= type.id @>)"><i class="icon-cog"></i> 设置</a>
				<a class="btn btn-small" href="javascript:deleteType(<@= type.id @>)"><i class="icon-remove"></i> 删除</a>
				<a id="type_up_<@= type.id @>" class="btn btn-small" href="javascript:moveUpType(<@= type.id @>)"><i class="icon-arrow-up"></i> 上移</a>
				<a id="type_down_<@= type.id @>" class="btn btn-small" href="javascript:moveDownType(<@= type.id @>)"><i class="icon-arrow-down"></i> 下移</a>
			</div>
		</div>
		<div style="clear:both"></div>
		<!-- 首先输出题标的信息 -->
		<div class="typeTitleContent" >
			<span class="typeLeft" id="typeIndex_<@= type.id @>"><@= type.index @></span>、
			<span class="typeMemo"><@= type.name @>
			<@ if (type.comment != "" ){@> 
				(<@= type.comment @>)
			<@}@>
			</span>
		</div>
	</div>
	<!-- 存放该题型下的试题 -->
	<div id="typeQstDiv_<@= type.id @>"></div>
	<div>
</script>
<!-- 右侧试题 -->
<script type="text/template" id="question_template">
	<div class="qstBlock" id="qstBlock_<@= question.id @>">
		<div class="qst_tool_bar">
			<div class="btn-group">
				<a class="btn btn-small" href="javascript:configQst(<@= question.id @>)"><i class="icon-cog"></i> 设置</a>
				<a class="btn btn-small" href="javascript:showDetail(<@= question.id @>)"><i class="icon-align-justify"></i> 详细</a>
				<a class="btn btn-small" href="javascript:replaceQst(<@= question.id @>)"><i class="icon-refresh"></i> 替换</a>
				<a class="btn btn-small" href="javascript:deleteQst(<@= question.id @>,<@= question.type @>)"><i class="icon-remove"></i> 删除</a>
				<a id="qst_up_<@= question.id @>" class="btn btn-small" href="javascript:moveUpQst(<@= question.id @>,<@= question.type @>)"><i class="icon-arrow-up"></i> 上移</a>
				<a id="qst_down_<@= question.id @>" class="btn btn-small" href="javascript:moveDownQst(<@= question.id @>,<@= question.type @>)"><i class="icon-arrow-down"></i> 下移</a>
			</div>
		</div>

		<div class="qstContent">
			<!-- 输出试题题干 -->
			<div class="qstTitleBox">
				<span class="qstOrderNumber" id="qstIndex_<@= question.id @>"><@= question.index @>.</span>
			</div>
			<div style="margin-left:10px;margin-right:10px;overflow: hidden; word-break: break-all; word-wrap: break-word;">
				<@= question.content @>
			</div>
		</div>
	</div>
</script>


<script type="text/template" id="question_replace_template">

<div class="tabbable"> <!-- Only required for left/right tabs -->
  <ul class="nav nav-tabs" id="replaceTab">
<@ _.each( qstList, function( question ){ @>
    <li><a href="#tab<@=question.id @>" data-toggle="tab"><@=question.id @></a></li>
<@ });@>
  </ul>
  <div class="tab-content">
<@ _.each( qstList, function( question ){ @>
<div class="tab-pane" id="tab<@=question.id @>">
 <@ if ( question.parentId != "0" ){ @> 
	<div id="subQstReplace">
 <@ }else{ @>
	<div id="qstReplace_<@=question.id@>" class="qstReplace">
 	<div class="qst_title">
		<table class="pull-left">
		    <tr><td>【题号】：<@= question.id @></td><td>【题型】：<@= question.type @></td>
				<td>【难度】：<@= question.diff @></td>
			</tr>
			<tr><td colspan="4">【知识点】：<@= question.key @></td></tr>
		</table>
		<div class="pull-right qst_toolbar" id="qst_toolbar_<@= question.id @>">
			<a class="btn btn-primary btn-small" href="javascript:doReplace(<@= question.id @>)">确定替换 </a>
		</div>
		<div style="clear:both;"></div>		
	</div>
 <@ } @>
		<div class="qstContent">
			<!-- 输出试题题干 -->
			<div class="qstTitleBox">
				<span class="qstOrderNumber"><@= question.indexOrder @><@ if ( question.parentId != "0" ){ @> ) <@ } @> .
				</span>
			</div>
			<div style="margin-left:10px;margin-right:10px;overflow: hidden; word-break: break-all; word-wrap: break-word;">
				<@= question.content @>
			</div>
			<!-- 输出试题选项 -->
			<div class="qstChoiceBox">
				<@ _.each( question.options, function( value,key ){ @>
					<div style="width:600; border=1px;">
						<table><tr>
							<td class="choiceNumber"><@= key @>.</td>
							<td width="95%"><@= value @></td>
						</tr></table>
					</div>
				<@ }); @>
				<div style="clear:both;"></div>
			</div>
			<!-- 试题选项结束 -->

			<!-- 子题目 -->
			<div id="replace_sub_<@= question.id@>"></div>
			<!-- 子题目结束 -->
			
			<div>【答案】：<@= question.answer @></div>
			<div>【解析】：
				<@ _.each( question.analysis, function( item ){ @>
					<@= item.xHTML @>
				<@ }); @>
			</div>

		</div>
	</div>
</div>
<@ }); @>
  </div>
</div>
</script>

<script type="text/template" id="question_detail_template">
 <@ if ( question.parentId != "0" ){ @> 
	<div id="subQstDetail">
 <@ }else{ @>
	<div id="qstDetail">
 		<div class="qst_title">
		    	<table>
		    		<tr><td>【题号】：<@= question.id @></td><td>【题型】：<@= question.type @></td>
						<td>【难度】：<@= question.diff @></td>
					</tr>
					<tr><td colspan="4">【知识点】：<@= question.key @></td></tr>
		    	</table>
		</div>
 <@ } @>
		<div class="qstContent">
			<!-- 输出试题题干 -->
			<div class="qstTitleBox">
				<span class="qstOrderNumber"><@= question.indexOrder @><@ if ( question.parentId != "0" ){ @> ) <@ } @> .
				</span>
			</div>
			<div style="margin-left:10px;margin-right:10px;overflow: hidden; word-break: break-all; word-wrap: break-word;">
				<@= question.content @>
			</div>
			<!-- 输出试题选项 -->
			<div class="qstChoiceBox">
				<@ _.each( question.options, function( value,key ){ @>
					<div style="width:600; border=1px;">
						<table><tr>
							<td class="choiceNumber"><@= key @>.</td>
							<td width="95%"><@= value @></td>
						</tr></table>
					</div>
				<@ }); @>
				<div style="clear:both;"></div>
			</div>
			<!-- 试题选项结束 -->

			<!-- 子题目 -->
			<div id="detail_sub"></div>
			<!-- 子题目结束 -->
			<!-- 输出试题答案-->
			<@ if (question.answer){ @>
				<div class="qstAnswer" id="Answer_<@= question.doc_QSTID @>">
					【答案】<@= question.answer @>
				</div>
			<@ }@>
			<!-- 输出试题解析 -->
			<@_.map(question.analysis, function(analysis){@>
				<div class="qstAnalysis" id="Analysis_<@= analysis.doc_ID @>">
					【解析】<@= analysis.xHTML @>
				</div>
			<@ }); @>

		</div>
	</div>
</script>

     <!-- 试卷导出时提示用户选择试卷模式和纸大小 -->
	<div style="display: none;" id="downloadDiv">
		<div class="form-horizontal">
			<div class="control-group">
				<label class="control-label" >试卷名称</label>
				<div class="controls">
					<input style="width:200px;" type='text' id='downloadPaperName' checked />
				</div>
			</div>
			<hr/>
			<div class="control-group">
				<label class="control-label" >试卷格式</label>
				<div class="controls">
					<label> <input type="radio"   style="width:20px;border:0;" id="fileType" name="fileType" checked value="DOCX"/>Word 2007/2010，文件扩展名为docx </label>
					<label> <input type="radio"   style="width:20px;border:0;"  id="fileType" name="fileType" value="DOC"/> Word 2000/2003，文件扩展名为doc</label>
					<label> <input type="radio"   style="width:20px;border:0;" id="fileType" name="fileType" value="PDF"/>Pdf文档格式</label>
				</div>
			</div>
			<hr/>
			<div class="control-group">
				<label class="control-label" >纸张大小</label>
				<div class="controls">
					<label><input type="radio"   style="width:20px;border:0;"  id="paperSize" name="paperSize" checked  value="A4"/>A4</label>
					<label><input type="radio"   style="width:20px;border:0;" id="paperSize" name="paperSize" value="A3"/>A3双栏</label>
					<label><input type="radio"   style="width:20px;border:0;" id="paperSize" name="paperSize" value="16K"/>16K</label>
				</div>
			</div>
			<hr/>
			<div class="control-group">
				<label class="control-label">试卷类型</label>
				<div class="controls" >
					<label><input type="radio"   style="width:20px;border:0;" id="config" name="config" checked value="1"/>普通用劵（答案集中在卷尾）</label>
					<label><input type="radio"   style="width:20px;border:0;"  id="config" name="config" value="0"/>教师用劵（每题后面跟答案）</label>
					<label><input type="radio"   style="width:20px;border:0;" id="config" name="config" value="2"/>学生用劵（没答案）</label>
				</div>
			</div>
		</div>
		
		
	</div>
</body>
</html>
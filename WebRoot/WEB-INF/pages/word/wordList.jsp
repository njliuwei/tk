<%@ page language="java" pageEncoding="UTF-8"%>
<jsp:include page="../common/header.jsp"/>
<script type="text/javascript">
	$(function() {
		var czsxQstDatagrid;
		var wordtype = '<%=request.getParameter("wordtype")%>';
		var soundmarkTitle = '拼音';
		var componentHidden = true;
		var meaningHidden = false;
		var buildingmethodHidden = true;
		var vingHidden = true;
		var advHidden = true;
		var comparisonHidden = true;
		var superlativeHidden = true;
		var synonymHidden = true;
		var meaningHidden = false;
		var explainHidden = true;
		if(wordtype == 'ywzc') {
			soundmarkTitle = '音标';
			vingHidden = false;
			advHidden = false;
			comparisonHidden = false;
			superlativeHidden = false;
			synonymHidden = false;
		}
		if(wordtype == 'xdzc') {
			componentHidden = false;
			buildingmethodHidden = false;
			explainHidden = false;
			meaningHidden = true;
		}
		wordgrid = $('#wordgrid').datagrid({
			title : '题目列表',
			url : 'wordList.do?wordtype=<%=request.getParameter("wordtype")%>',
			columns : [[
				{
					field : 'id',
					title : '编号',
					width : 100
				},{
					field : 'name',
					title : '字词',
					width : 100
				},{
					field : 'type',
					title : '字词类型',
					width : 100
				},{
					field : 'property',
					title : '词性',
					width : 100
				},{
					field : 'soundmark',
					title : soundmarkTitle,
					width : 100
				},{
					field : 'component',
					title : '偏旁部首',
					width : 100,
					hidden : componentHidden
				},{
					field : 'buildingmethod',
					title : '造字法',
					width : '100',
					hidden : buildingmethodHidden
				},{
					field : 'ving',
					title : '现在分词',
					width : 100,
					hidden : vingHidden
				},{
					field : 'adv',
					title : '形变副',
					width : 100,
					hidden : advHidden
				},{
					field : 'comparison',
					title : '比较级',
					width : 100,
					hidden : comparisonHidden
				},{
					field : 'superlative',
					title : '最高级',
					width : 100,
					hidden : superlativeHidden
				},{
					field : 'synonym',
					title : '近义词',
					width : 100,
					hidden : synonymHidden
				},{
					field : 'meaning',
					title : '词义',
					width : 100,
					hidden : meaningHidden
				},{
					field : 'explain',
					title : '解释',
					width : 100,
					hidden : explainHidden
				},]
			],
			toolbar : [ {
				text : '新增',
				iconCls : 'icon-add',
				handler : function() {
					addWord(wordtype);
				}
			},'-', {
				text : '修改',
				iconCls : 'icon-edit',
				handler : function() {
					updateWord(wordtype);
				}
			}, '-', {
				text : '删除',
				iconCls : 'icon-remove',
				handler : function() {
					delWord(wordtype);
				}
			}],
			//加载数据成功后触发的事件
			onLoadSuccess : function(data) {
				if (data.rows.length <= 0) {
					var body = $(this).data().datagrid.dc.body2;
					body.find('table tbody').append('<tr><td width="' + body.width() + '" style="height: 25px; text-align: center;">没有数据！</td></tr>');
				}
			},
			fitColumns : true,
			striped : true,
			idField : 'id',
			pagination : true,
			pageSize : 50,
			pageList : [50,100,150,200,250,300],
			rownumbers : true,
			rowspan : false,
			singleSelect : true,
			border : false,
			nowrap : false,
			fit : true,
			
		});
		
	});
	
	
	/**
	 * 将表单对象转为json对象
	 * @param formValues
	 * @returns
	 */
	function convertToJson(formValues) {
	    var result = {};
	    for(var formValue,j=0;j<formValues.length;j++) {
		    formValue = formValues[j];
		    var name = formValue.name;
		    var value = formValue.value;
		    if (name.indexOf('.') < 0) {
		    	if(result[name]) {
		    		result[name] = result[name] + "," + value;
		    	} else {
			    	result[name] = value;
			    }
			    continue;
		    } else {
			    var simpleNames = name.split('.');
			    // 构建命名空间
			    var obj = result;
			    for ( var i = 0; i < simpleNames.length - 1; i++) {
			    	var simpleName = simpleNames[i];
				    if (simpleName.indexOf('[') < 0) {
					    if (obj[simpleName] == null) {
					    	obj[simpleName] = {};
					    }
				    	obj = obj[simpleName];
				    } else { // 数组
					    // 分隔
					    var arrNames = simpleName.split('[');
					    var arrName = arrNames[0];
					    var arrIndex = parseInt(arrNames[1]);
					    if (obj[arrName] == null) {
					    	obj[arrName] = []; // new Array();
					    }
					    obj = obj[arrName];
					    multiChooseArray = result[arrName];
					    if (obj[arrIndex] == null) {
					    	obj[arrIndex] = {}; // new Object();
					    }
				    	obj = obj[arrIndex];
				    }
			    }
			 
			    if(obj[simpleNames[simpleNames.length - 1]] ) {
				    var temp = obj[simpleNames[simpleNames.length - 1]];
				    obj[simpleNames[simpleNames.length - 1]] = temp;
			    }else {
			    	obj[simpleNames[simpleNames.length - 1]] = value;
			    }
		 
		    }
	    }
	    return result;
	}
	
	
</script>

<div class="easyui-layout" data-options="fit:true">
<div data-options="region:'north'" class="datagrid-toolbar">
		<fieldset>
			<legend>查询条件</legend>
			<form id="pdfSearchForm" method="post">
				题型
				<select id="picIsFPSelect">
					<option selected="selected">全部</option>
					<option value="名著导读1">名著导读1</option>
					<option value="默写">默写</option>
					<option value="其他">其他</option>
					<option value="诗歌鉴赏">诗歌鉴赏</option>
					<option value="书写">书写</option>
					<option value="文言文阅读">文言文阅读</option>
					<option value="现代文阅读">现代文阅读</option>
					<option value="选择题">选择题</option>
					<option value="语言表达">语言表达</option>
					<option value="综合性学习">综合性学习</option>
					<option value="作文">作文</option>
				</select>
				<input type="hidden" id="id" name="id"/>
				<input type="hidden" id="tx" name="tx"/>
			</form>
		</fieldset>
	</div>
	<div data-options="region:'center'">
		<table id="wordgrid"></table>
	</div>
</div>

<div id="questionViewWin" title="试题标引" style="width:900px;height:700px" data-options="modal:true">
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'center'">
			<div class="easyui-layout" data-options="fit:true">
				<div id="questionInfo" data-options="region:'north'" style="height:400px">
					
				</div>
				<div id="questionAnalyses" data-options="region:'center'">
					
				</div>
			</div>
		</div>
		<div id="QuestionIndexes" data-options="region:'east'" style="width:320px;padding:10px;background-color:Azure">
			<form id='questionIndexForm' action="saveQuestionIndex.do" method='post'>
				<div style='margin:5px'>
					<label style='display:inline-block;width:80px;text-align:right;' for='zsd'>知识点：</label>
					<input type='text' id='zsdmc' name='zsdmc' style='width:130px;' readonly='readonly'></input>
					<input type='hidden' name='zsd' id='zsd' readonly='readonly'></input>
					<input type='button' onclick='openFixedTree("zsd","知识点")' value='选择'/>
				</div>
				<div style='margin:5px'>
					<label style='display:inline-block;width:80px;text-align:right;' for='jctx'>教材体系：</label>
					<input type='text' name='jctx' style='width:130px;' readonly='readonly'></input>
					<input type='button' onclick='openFixedTree("jctx","教材体系")' value='选择'/>
				</div>
				<div style='margin:5px'>
					<label style='display:inline-block;width:80px;text-align:right;' for='tx'>题型：</label>
					<select name='tx' style='width:130px;'>
						<option selected="selected">--请选择--</option>
						<option value="填空题">填空题</option>
						<option value="判断题">判断题</option>
						<option value="计算题">计算题</option>
						<option value="简答题">简答题</option>
						<option value="选择题">选择题</option>
						<option value="解答题">解答题</option>
						<option value="证明题">证明题</option>
						<option value="作图题">作图题</option>
					</select>
				</div>
				<div style='margin:5px'>
					<label style='display:inline-block;width:80px;text-align:right;' for='nd'>难度：</label>
					<select name='nd' style='width:130px;'>
						<option selected="selected">--请选择--</option>
						<option value="易">易</option>
						<option value="中">中</option>
						<option value="难">难</option>
					</select>
				</div>
				<div style='margin:5px'>
					<label style='display:inline-block;width:80px;text-align:right;' for='ly'>来源：</label>
					<select name='ly' style='width:130px;'>
						<option selected="selected">--请选择--</option>
						<option value="真题">真题</option>
						<option value="原创">原创</option>
						<option value="模拟">模拟</option>
						<option value="网络">网络</option>
						<option value="教材">教材</option>
					</select>
				</div>
			</form>
		</div>
	</div>
</div>

<div id="analysisViewWin" title="解析标引" style="width:350px;height:500px;padding-left:10px" data-options="modal:true">
	<form id='analysisIndexForm' action="saveAnalysisIndex.do" method='post'>
		<div style='margin:5px'>
			<label style='display:inline-block;width:80px;text-align:right;' for='fa'>解题方法：</label>
			<input type='text' name='fa' style='width:130px;'></input>
			<input type='hidden' name='st_id'/>
			<input type='hidden' name='id'/>
			<input type='hidden' name='subject' value='<%=request.getParameter("subject")%>'/>
		</div>
		<div style='margin:5px'>
			<label style='display:inline-block;width:80px;text-align:right;' for='lx'>解题类型：</label>
			<input type='text' name='lx' style='width:130px;'></input>
		</div>
	</form>
</div>

<div id="treeDialog">
	<div>
		<input type="button" style='position: absolute; right: 5px;' value="保存" onclick="chooseTreeNode()"/>
		<input type="hidden" id="reverse"/>
		<input type="hidden" id="which"/>
	</div>
	<ul id="questionTreeValue" class="easyui-tree"></ul>
</div>
<jsp:include page="../common/footer.jsp"/>

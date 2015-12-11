<%@ page language="java" pageEncoding="UTF-8"%>
<jsp:include page="../common/header.jsp"/>
<style>
	input{width:200px}
	select{width:206px}
	a{
		color : blue;
	}
	a:link{
		text-decoration:none;
	}
	a:hover{
		text-decoration:underline;
	} 
	.soundmark{
		margin-left:10px;
		color : #66B3FF;
	}
	.property{
		margin-left : 10px;
	}
	.explain{
		margin-left : 10px;
	}
	.createdtime{
		margin-left : 10px;
	}
	.container{
		margin-bottom : 10px;
		margin-top : 10px;
		background-color : #F8F8FF;
		padding-bottom : 10px;
	}
	.datagrid-row-selected{
		color : #000;
		background : #fff;
	}
	.datagrid-row-over{
		color : #000;
		background : #fff;
	}
	.update_view{
		padding-left:10px;
	}
</style>
<script type="text/javascript">
	$(function() {
		var wordgrid;
		var quanma={1:'①',2:'②',3:'③',4:'④',5:'⑤',6:'⑥',7:'⑦',8:'⑧',9:'⑨',10:'⑩'};
		wordgrid = $('#wordgrid').datagrid({
			title : '字词列表',
			url : 'englishWordList.do',
			columns : [[
			/*
				{
					field : 'id',
					title : '编号',
					width : 100,
					hidden : true,
				},{
					field : 'name',
					title : '字词',
					width : 100
				},{
					field : 'soundmark',
					title : '音标',
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
					field : 'ving',
					title : '现在分词',
					width : 100
				},{
					field : 'adv',
					title : '副词',
					width : '100'
				},{
					field : 'comparison',
					title : '比较级',
					width : '100'
				},{
					field : 'superlative',
					title : '最高级',
					width : '100'
				},{
					field : 'meaning',
					title : '词义',
					width : 100
				}*/{
					field : 'explain',
					title : '字词',
					width : 100,
					formatter : function(value, row, index) {
						var result = '<div class="container"><br/><span style="margin-left:10px;margin-right:10px;font-size:30px;">' + row.name + '</span><a href="#" onclick="delWord('+row.id + ')">删除</a><br/>';
						if(row.explain) {
							var yxs = $.parseJSON('[' + row.explain + ']');
							$(yxs).each(function(index,element) {
								result += '<span class="soundmark">[' + element.SOUNDMARK + ']</span> <span class="property">' + element.PROPERTY + '</span>';
								result += '<span class="explain">' + (quanma[element.SORT] ? quanma[element.SORT] : element.SORT) + ': ' + element.MEANING + '</span><br/>';
							});
						}
						result += '<span class="createdtime">创建时间:'+row.created +'</span><br/></div>';
						return result;
					},
					styler: function(value,row,index){
						return  'border-bottom-color:transparent';
					}
				},]
			],
			toolbar : [ /*{
				text : '新增',
				iconCls : 'icon-add',
				handler : function() {
					addWord();
				}
			},'-', {
				text : '修改',
				iconCls : 'icon-edit',
				handler : function() {
					updateWord();
				}
			}, '-', {
				text : '删除',
				iconCls : 'icon-remove',
				handler : function() {
					delWord();
				}
			}, '-', */{
				text : '导入',
				iconCls : 'icon-import',
				handler : function() {
					uploadWord();
				}
			}, '-', {
				text : '下载模板',
				iconCls : 'icon-download',
				handler : function() {
    				var filename = encodeURI(encodeURI("resources/template/英文字词导入模板.xls"));
					window.open("download.do?filename=" + filename);
				}
			}],
			//加载数据成功后触发的事件
			onLoadSuccess : function(data) {
				if (data.rows.length <= 0) {
					var body = $(this).data().datagrid.dc.body2;
					body.find('table tbody').append('<tr><td width="' + body.width() + '" style="height: 25px; text-align: center;">没有数据！</td></tr>');
				}
			},
			onDblClickRow : function(index,data){
				updateWord(data.id);
			},
			
			fitColumns : true,
			striped : false,
			idField : 'id',
			pagination : true,
			pageSize : 50,
			pageList : [50,100,150,200,250,300],
			rownumbers : false,
			rowspan : false,
			singleSelect : true,
			border : false,
			nowrap : false,
			fit : true,
			
		});
		
	});
	
	function addWord() {
		$('#addDialog').dialog({
			title : '添加字词',
			modal : true,
			inline : true,
			width : 380,
			height : 440,
			buttons : [ {
				text : '添加',
				handler : function() {
					var _btn = this;
					$(_btn).linkbutton("disable");
					$.messager.progress({
						title : '提示',
						text : '数据处理中，请稍候....'
					}); // 显示进度条
					$('#addForm').form('submit', {
						url : 'addEnglishWord',
						onSubmit : function() {
							var isValid = $(this).form('validate');
							if (!isValid) {
								$.messager.progress('close'); // 如果表单是无效的则隐藏进度条
							}
							return isValid; // 返回false终止表单提交
						},
						success : function(result) {
							$.messager.progress('close'); // 如果提交成功则隐藏进度条
							$(_btn).linkbutton("enable");
							var r = $.parseJSON(result);
							if (r.success) {
								$('#addDialog').dialog('close');
								$('#wordgrid').datagrid('reload', ly.serializeObjct($('#queryForm')));
							}
							$.messager.show({
								title : '提示',
								msg : r.msg
							});
						}
					});
				}
			} ],
			onOpen : function() {
				$('#addForm').form('reset');
			}
		});
	};
		
		
	/*字词更新界面*/
	function updateWord(id) {
		var selectedData = $('#wordgrid').datagrid('getSelected');
		if (!selectedData) {
			$.messager.alert('警告', '请选择需要操作的数据！', 'warning');
			return;
		}
		$('#updateDialog').dialog({
			title : '修改属性',
			modal : true,
			inline : true,
			width : 800,
			height : 500,
			onOpen : function() {
				var yxs = $.parseJSON('[' + selectedData.explain + ']');
				var props = {};
				$(yxs).each(function(index,element) {
					props[element.PROPERTY] = element.PROPERTY;
				});
				if(!props.n) {
					$("#basiccontents #n").hide();
					$("#basicForm #n").hide();
				} else {
					$("#basiccontents #n").show();
					$("#basicForm #n").show();
				}
				if(!props.v) {
					$("#basiccontents #v").hide();
					$("#basicForm #v").hide();
				} else {
					$("#basiccontents #v").show();
					$("#basicForm #v").show();
				}
				
				if(!props.num) {
					$("#basiccontents #num").hide();
					$("#basicForm #num").hide();
				} else {
					$("#basiccontents #num").show();
					$("#basicForm #num").show();
				}
				
				if(!props.adj && !props.adv) {
					$("#basiccontents #adj").hide();
					$("#basicForm #adj").hide();
				} else {
					$("#basiccontents #adj").show();
					$("#basicForm #adj").show();
				}
				
				if(!props.pron) {
					$("#basiccontents #pron").hide();
					$("#basicForm #pron").hide();
				} else {
					$("#basiccontents #pron").show();
					$("#basicForm #pron").show();
				}
				$.post('getEnglishWordInfo.do', {
					id : id
				}, function(result) {
					if (result.success) {
						var obj = result.obj.list[0];
						$("#updateDialog #lname").html(obj.name);
						$("#updateDialog #rname").html(obj.name);
						$("#updateDialog #creator").html(obj.creator);
						$("#updateDialog #created").html(obj.created);
						$("#updateDialog #lastmodified").html(obj.lastModified);
						$("#updateDialog #type").html(obj.type);
						$("#updateDialog #ed").html(obj.ed);
						$("#updateDialog #ving").html(obj.ving);
						$("#updateDialog #preterite").html(obj.preterite);
						$("#updateDialog #subject").html(obj.subject);
						$("#updateDialog #object").html(obj.object);
						$("#updateDialog #npp").html(obj.npp);
						$("#updateDialog #app").html(obj.app);
						$("#updateDialog #reflexive").html(obj.reflexive);
						$("#updateDialog #plural").html(obj.plural);
						$("#updateDialog #singular").html(obj.singular);
						$("#updateDialog #adv").html(obj.adv);
						$("#updateDialog #comparison").html(obj.comparison);
						$("#updateDialog #superlative").html(obj.superlative);
						$("#updateDialog #cardinalNum").html(obj.cardinalNum);
						$("#updateDialog #ordinalNum").html(obj.ordinalNum);
						$("#updateDialog #synonym").html(obj.synonym);
						$("#updateDialog #standard1").html(obj.standard1);
						$("#updateDialog #standard2").html(obj.standard2);
						$("#updateDialog #provenance").html(obj.provenance);
						$("#yxcontents").html('');
						$("#jctxContents").html('');
						$.each(result.obj.list,function(index,element) {
							// alert($.parseJSON("[" + element.examples + "]")[0].PROPERTY);
							var yxs = $.parseJSON("[" + element.examples + "]");
							for(var i = 0; i < yxs.length; i++) {
								var yxdiv = '';
								yxdiv = '<div style="background-color:#F8F8FF;height:30px;">';
								yxdiv += '<span style="display:inline-block;padding-top:5px;">' + yxs[i].PROPERTY + '&nbsp;&nbsp;'+ yxs[i].SOUNDMARK;
								yxdiv += '</span><span style="display:inline-block;float:right;">';
								yxdiv += '<a id="btn" href="#" class="easyui-linkbutton" onclick="updateAdditional('+ yxs[i].ID + ')" data-options="iconCls:\'icon-edit\'">编辑</a></span></div>';
								yxdiv += '<div id="additional' + yxs[i].ID + '"><label>释义:</label><span name="explain">' + yxs[i].MEANING + '</span><br/><div id="examples' + yxs[i].ID + '">';
								if(yxs[i].EXAMPLES) {
									var examples = yxs[i].EXAMPLES;
									for(var j = 0; j < examples.length; j++) {
										var example = examples[j];
										yxdiv += '<ul style="margin-bottom:10px;" id="example' + example.ID + '"><li>' + example.TEXT;
										yxdiv += '<a href="#" onclick="updExample(' + example.ID + ')">&nbsp;&nbsp;编辑例句</a>';
										yxdiv += '<a href="#" onclick="delExample(' + example.ID + ')">&nbsp;&nbsp;删除例句</a></li>';
										yxdiv += '<li style="list-style-type:none;color:#808080">' + example.EXAMPLE + '</li></ul>';
									}
								}
								yxdiv += '</div><br/><span><a id="btn" href="#" class="easyui-linkbutton" onclick="addExample(' + yxs[i].ID + ')" data-options="iconCls:\'icon-add\'">添加例句</a></span>'; 
								yxdiv = $(yxdiv).appendTo("#yxcontents");
								$.parser.parse(yxdiv);
							}
						});
						
						$.each(result.obj.sps,function(index,ele) {
							$("#jctxContents").append('<label style="color:#808080">' + ele.name + ':</label>');
							$.each(result.obj.ws,function(index,element) {
								if(ele.name == element.name) {
									$("#jctxContents").append('<span>' + element.type + '</span>;');
								}
							});
							$("#jctxContents").append('<br/>');
						});
						
					}
				}, 'JSON');  
			}
		});
	};
	
	function delWord(id) {
		$.messager.confirm('确认','您确认想要删除记录吗？',function(r){    
			if (r){    
	        	$.post('delEnglishWord.do', {
					id : id
				}, function(result) {
					if (result.success) {
						$('#wordgrid').datagrid('reload',ly.serializeObjct($('#queryForm')));
						$('#wordgrid').datagrid('clearSelections');
					}
					$.messager.show({
						title : '提示',
						msg : result.msg
					});
				}, 'JSON');    
		    }    
		});  
	}
	
	function queryWord() {
		$('#wordgrid').datagrid('reload',ly.serializeObjct($('#queryForm')));
	}
	
	function uploadWord() {
		$('#uploadDialog').dialog({
			title : '上传文件',
			modal : true,
			inline : true,
			width : 380,
			height : 120,
			buttons : [ {
				text : '确定',
				handler : function() {
					var _btn = this;
					$(_btn).linkbutton("disable");
					$.messager.progress({
						title : '提示',
						text : '数据处理中，请稍候....'
					}); // 显示进度条
					$('#uploadForm').form('submit', {
						url : 'uploadEnglishWord',
						onSubmit : function() {
							var filename = $('#uploadForm input[type="file"]').val();
							var isValid = /\.(xls|xlsx|XLS|XLSX)$/.test(filename);
							if (!isValid) {
								$.messager.progress('close'); // 如果表单是无效的则隐藏进度条
								$.messager.alert('警告', '请选择excel文件！', 'warning');
							}
							return isValid; // 返回false终止表单提交
						},
						success : function(result) {
							$.messager.progress('close'); // 如果提交成功则隐藏进度条
							$(_btn).linkbutton("enable");
							var r = $.parseJSON(result);
							if (r.success) {
								$('#uploadDialog').dialog('close');
								$('#wordgrid').datagrid('reload',ly.serializeObjct($('#queryForm')));
							}
							$.messager.show({
								title : '提示',
								msg : r.msg
							});
						}
					});
				}
			} ],
			onOpen : function() {
				$('#uploadForm').form('clear');
			}
		});
	}
	
			
	function updateBasic() {
		$('#updBasicDialog').dialog({
			title : '字词基本信息修改',
			modal : true,
			inline : true,
			width : 380,
			height : 310,
			buttons : [ {
				text : '确定',
				handler : function() {
					var _btn = this;
					$(_btn).linkbutton("disable");
					$.messager.progress({
						title : '提示',
						text : '数据处理中，请稍候....'
					}); // 显示进度条
					$('#basicForm').form('submit', {
						url : 'updateEnglishWord',
						onSubmit : function() {
							
						},
						success : function(result) {
							$.messager.progress('close'); // 如果提交成功则隐藏进度条
							$(_btn).linkbutton("enable");
							var r = $.parseJSON(result);
							if (r.success) {
								$('#updBasicDialog').dialog('close');
								$('#wordgrid').datagrid('reload',ly.serializeObjct($('#queryForm')));
								$('#basiccontents #component').html($("#basicForm input[name='component']").val());
								$('#basiccontents #cbhs').html($("#basicForm input[name='cbhs']").val());
								$('#basiccontents #bhs').html($("#basicForm input[name='bhs']").val());
								$('#basiccontents #buildingmethod').html($("#basicForm").find("select[name='buildingMethod']").val());
								
								$("#updateDialog #type").html($("#basicForm input[name='type']").val());
								$("#updateDialog #ed").html($("#basicForm input[name='ed']").val());
								$("#updateDialog #ving").html($("#basicForm input[name='ving']").val());
								$("#updateDialog #preterite").html($("#basicForm input[name='preterite']").val());
								$("#updateDialog #subject").html($("#basicForm input[name='subject']").val());
								$("#updateDialog #object").html($("#basicForm input[name='object']").val());
								$("#updateDialog #npp").html($("#basicForm input[name='npp']").val());
								$("#updateDialog #app").html($("#basicForm input[name='app']").val());
								$("#updateDialog #reflexive").html($("#basicForm input[name='object']").val());
								$("#updateDialog #plural").html($("#basicForm input[name='plural']").val());
								$("#updateDialog #singular").html($("#basicForm input[name='singular']").val());
								$("#updateDialog #adv").html($("#basicForm input[name='adv']").val());
								$("#updateDialog #comparison").html($("#basicForm input[name='comparison']").val());
								$("#updateDialog #superlative").html($("#basicForm input[name='superlative']").val());
								$("#updateDialog #cardinalNum").html($("#basicForm input[name='cardinalNum']").val());
								$("#updateDialog #ordinalNum").html($("#basicForm input[name='ordinalNum']").val());
								$("#updateDialog #synonym").html($("#basicForm input[name='synonym']").val());
								$("#updateDialog #standard1").html($("#basicForm input[name='standard1']").val());
								$("#updateDialog #standard2").html($("#basicForm input[name='standard2']").val());
								$("#updateDialog #provenance").html($("#basicForm input[name='provenance']").val());
							}
							$.messager.show({
								title : '提示',
								msg : r.msg
							});
						}
					});
				}
			} ],
			onOpen : function() {
				$('#basicForm').form('clear');
				var selectedData = $('#wordgrid').datagrid('getSelected');
				$('#basicForm').form('load','getEnglishWordBasic?id=' + selectedData.id);
			}
		});
	}
	
	function updateAdditional(id) {
		$('#updAdditionalDialog').dialog({
			title : '字词附加信息修改',
			modal : true,
			inline : true,
			width : 380,
			height : 200,
			buttons : [ {
				text : '确定',
				handler : function() {
					var _btn = this;
					$(_btn).linkbutton("disable");
					$.messager.progress({
						title : '提示',
						text : '数据处理中，请稍候....'
					}); // 显示进度条
					$('#additionalForm').form('submit', {
						url : 'updateAdditionalEnglishWord',
						onSubmit : function() {
							
						},
						success : function(result) {
							$.messager.progress('close'); // 如果提交成功则隐藏进度条
							$(_btn).linkbutton("enable");
							var r = $.parseJSON(result);
							if (r.success) {
								$('#updAdditionalDialog').dialog('close');
								$('#wordgrid').datagrid('reload',ly.serializeObjct($('#queryForm')));
								$('#additional' + id + ' span[name="explain"]').html($("#additionalForm textarea[name='explain']").val());
							}
							$.messager.show({
								title : '提示',
								msg : r.msg
							});
						}
					});
				}
			} ],
			onOpen : function() {
				$('#additionalForm').form('clear');
				$('#additionalForm').form('load','getEnglishWordAdditional?id=' + id);
			}
		});
	}
	
	function addExample(id) {
		$('#addExampleDialog').dialog({
			title : '添加例句',
			modal : true,
			inline : true,
			width : 380,
			height : 180,
			buttons : [ {
				text : '确定',
				handler : function() {
					var _btn = this;
					$(_btn).linkbutton("disable");
					$.messager.progress({
						title : '提示',
						text : '数据处理中，请稍候....'
					}); // 显示进度条
					$('#exampleForm').form('submit', {
						url : 'addExampleEnglishWord?id=' + id,
						onSubmit : function() {
							
						},
						success : function(result) {
							$.messager.progress('close'); // 如果提交成功则隐藏进度条
							$(_btn).linkbutton("enable");
							var r = $.parseJSON(result);
							if (r.success) {
								$('#addExampleDialog').dialog('close');
								var examplediv = '';
								examplediv += '<ul style="margin-bottom:10px;" id="example' + r.obj + '"><li>' + $("#exampleForm input[name='explain']").val();
								examplediv += '<a href="#" onclick="updExample(' + r.obj + ')">&nbsp;&nbsp;编辑例句</a>';
								examplediv += '<a href="#" onclick="delExample(' + r.obj + ')">&nbsp;&nbsp;删除例句</a></li>';
								examplediv += '<li style="list-style-type:none;color:#808080">' + $("#exampleForm input[name='example']").val() + '</li></ul>';
								$("#examples"+id).append(examplediv);
							}
							$.messager.show({
								title : '提示',
								msg : r.msg
							});
						}
					});
				}
			} ],
			onOpen : function() {
				$('#exampleForm').form('clear');
			}
		});
	}
	
	function updExample(id) {
		$('#updExampleDialog').dialog({
			title : '添加例句',
			modal : true,
			inline : true,
			width : 380,
			height : 180,
			buttons : [ {
				text : '确定',
				handler : function() {
					var _btn = this;
					$(_btn).linkbutton("disable");
					$.messager.progress({
						title : '提示',
						text : '数据处理中，请稍候....'
					}); // 显示进度条
					$('#updExampleForm').form('submit', {
						url : 'updExampleEnglishWord',
						onSubmit : function() {
							
						},
						success : function(result) {
							$.messager.progress('close'); // 如果提交成功则隐藏进度条
							$(_btn).linkbutton("enable");
							var r = $.parseJSON(result);
							if (r.success) {
								$('#updExampleDialog').dialog('close');
								$('#example' + id + ' li').first().html($("#updExampleForm input[name='explain']" ).val() + '<a href="#" onclick="updExample(' + id + ')">&nbsp;&nbsp;编辑例句</a><a href="#" onclick="delExample(' + id + ')">&nbsp;&nbsp;删除例句</a>');
								$('#example' + id + ' li').last().html($("#updExampleForm input[name='example']").val());
							}
							$.messager.show({
								title : '提示',
								msg : r.msg
							});
						}
					});
				}
			} ],
			onOpen : function() {
				$('#updExampleForm').form('load','getEnglishExampleById?id=' + id);
			}
		});
	}
	
	function delExample(id) {
		$.messager.confirm('确认','您确认想要删除记录吗？',function(r){    
			if (r){    
	        	$.post('delEnglishExample.do', {
					id : id
				}, function(result) {
					if (result.success) {
						$("#example" + id).remove();
					}
					$.messager.show({
						title : '提示',
						msg : result.msg
					});
				}, 'JSON');    
		    }    
		});  
	}
	
	
</script>

<div class="easyui-layout" data-options="fit:true">
<div data-options="region:'north'" class="datagrid-toolbar">
		<fieldset>
			<legend>查询条件</legend>
			<form id="queryForm" method="post">
				<label for='name'>字词：</label><input type='text' name='name'/>
				<label for='soundmark' style='margin-left:20px'>拼音：</label><input type='text' name='soundmark'/>
				<label for='component' style='margin-left:20px'>偏旁：</label><input type='text' name='component'/>
				<input type='button' style='margin-left:20px;width:50px' value='查询' onclick='queryWord()'/>
			</form>
		</fieldset>
	</div>
	<div data-options="region:'center'">
		<table id="wordgrid"></table>
	</div>
</div>

<div id="addDialog">
	<fieldset style="margin-top: 5px;padding-left:40px">
		<legend>
			<b><i>字词基本信息</i></b>
		</legend>
		<form id="addForm" method="post">
			<table>
				<tr>
					<td>字词</td>
					<td>
						<input class="easyui-validatebox" name="name" data-options="required:true,validType:'length[1,200]'"/>
					</td>
				</tr>
				<tr>
					<td>字词类型</td>
					<td>
						<select name="type" class="easyui-validatebox" data-options="required:true">
							<option selected="selected" value=''>--请选择--</option>
							<option>字</option>
							<option>词</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>音标</td>
					<td>
						<input type="text" name="soundmark" class="easyui-validatebox" data-options="validType:'length[1,200]'"/>
					</td>
				</tr>
				<tr>
					<td>词性</td>
					<td>
						<select name="property" class="easyui-validatebox">
							<option selected="selected" value=''>--请选择--</option>
							<option>n</option>
							<option>pron</option>
							<option>adj</option>
							<option>num</option>
							<option>v</option>
							<option>adv</option>
							<option>art</option>
							<option>prep</option>
							<option>conj</option>
							<option>int</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>现在分词</td>
					<td>
						<input type="text" name="ving" class="easyui-validatebox" data-options="validType:'length[1,200]'"/>
					</td>
				</tr>
				<tr>
					<td>副词</td>
					<td>
						<input type="text" name="adv" class="easyui-validatebox" data-options="validType:'length[1,200]'"/>
					</td>
				</tr>
				<tr>
					<td>比较级</td>
					<td>
						<input type="text" name="comparison" class="easyui-validatebox" data-options="validType:'length[1,200]'"/>
					</td>
				</tr>
				<tr>
					<td>最高级</td>
					<td>
						<input type="text" name="superlative" class="easyui-validatebox" data-options="validType:'length[1,200]'"/>
					</td>
				</tr>
				<tr>
					<td>近义词</td>
					<td>
						<input type="text" name="synonym" class="easyui-validatebox" data-options="validType:'length[1,200]'"/>
					</td>
				</tr>
				<tr>
					<td>词义</td>
					<td>
						<textarea rows="2" cols="21" name="meaning" data-options="required:true,validType:'length[1,5000]'"></textarea>
					</td>
				</tr>
			</table>
		</form>
	</fieldset>
</div>

<div id="updateDialog">
	<div class="easyui-layout" data-options="fit:true">   
    	<div data-options="region:'west'" style="width:300px">
    		<div class="easyui-layout" data-options="fit:true">   
	            <div data-options="region:'north'" style="height:180px;line-height:30px;">
	            	<label class='update_view'></label><span style='font-size:30px;' id='lname'></span><br/>
	            	<label class='update_view'>入库人：</label><span id='creator'></span><br/>
	            	<label class='update_view'>创建时间：</label><span id='created'></span><br/>
	            	<label class='update_view'>最后修改时间：</label><span id='lastmodified'></span><br/>
	            </div>   
	            <div data-options="region:'center'" id="jctxContents">
				</div>   
	        </div>   
    	</div>   
	    <div data-options="region:'center'">   
	        <div class="easyui-layout" data-options="fit:true">   
	            <div id='basiccontents' data-options="region:'north'" style="height:180px;line-height:20px;">
	            	<div style="background-color:#F8F8FF;height:30px;">
	            		<span style="display:inline-block;padding-top:5px;">基本属性</span>
	            		<span style="display:inline-block;float:right;"><a id="btn" href="#" class="easyui-linkbutton" onclick="updateBasic()" data-options="iconCls:'icon-edit'">编辑</a></span>  
	            	</div>
	            	<label class='update_view'>字词：</label><span id='rname'></span><br/>
	            	
	            	<!--   adj  -->
	            	<div id='adj'>
		            	<label class='update_view'>比较级：</label><span id='comparison'></span><br/>
		            	<label class='update_view'>最高级：</label><span id='superlative'></span><br/>
	            	</div>
	            	<!--   pron  -->
	            	<div id='pron'>
		            	<label class='update_view'>主格：</label><span id='subject'></span><br/>
		            	<label class='update_view'>宾格：</label><span id='object'></span><br/>
		            	<label class='update_view'>形容词性物主代词：</label><span id='app'></span><br/>
		            	<label class='update_view'>名词性物主代词：</label><span id='npp'></span><br/>
		            	<label class='update_view'>反身代词：</label><span id='reflexive'></span><br/>
	            	</div>
	            	<!--   num  -->
	            	<div id='num'>
		            	<label class='update_view'>基数词：</label><span id='cardinalNum'></span><br/>
		            	<label class='update_view'>序数词：</label><span id='ordinalNum'></span><br/>
	            	</div>
	            	
	            	<!--   n  -->
	            	<div id='n'>
	            		<label class='update_view'>复数：</label><span id='plural'></span><br/>
	            	</div>
	            	
	            	<!--   v  -->
	            	<div id='v'>
		            	<label class='update_view'>现在分词：</label><span id='ving'></span><br/>
		            	<label class='update_view'>过去分词：</label><span id='ed'></span><br/>
		            	<label class='update_view'>过去式：</label><span id='preterite'></span><br/>
	            	</div>
	            	
	            	<label class='update_view'>近义词：</label><span id='synonym'></span><br/>
	            	<label class='update_view'>标准一：</label><span id='standard1'></span><br/>
	            	<label class='update_view'>标准二：</label><span id='standard2'></span><br/>
	            	<label class='update_view'>出处：</label><span id='provenance'></span><br/>
	            </div>   
	            <div id='yxcontents' data-options="region:'center'">
					
				</div>   
	        </div>  
	    </div>   
	</div>  
</div>


<div id="updBasicDialog">
	<fieldset style="margin-top: 5px;padding-left:40px">
		<legend>
			<b><i>字词基本信息</i></b>
		</legend>
		<form id="basicForm" method="post">
			<table>
				<tr>
					<td>字词</td>
					<td>
						<input class="easyui-validatebox" disabled='disabled' name="name" data-options="required:true,validType:['length[1,100]','Hanzi']"/>
						<input type='hidden' name='id'/>
					</td>
				</tr>
				<tr>
					<td>字词类型</td>
					<td>
						<select name="type" class="easyui-validatebox" data-options="required:true">
							<option selected="selected" value=''>--请选择--</option>
							<option>字</option>
							<option>词</option>
						</select>
					</td>
				</tr>
				<tbody id='adj'>
				<tr>
					<td>比较级</td>
					<td>
						<input type="text" id='comparison' name="comparison" class="easyui-validatebox" data-options="validType:['length[1,500]']"/>
					</td>
				</tr>
				<tr>
					<td>最高级</td>
					<td>
						<input type="text" id='superlative' name="superlative" class="easyui-validatebox" data-options="validType:['length[1,500]']"/>
					</td>
				</tr>
				</tbody>
				<tbody id='pron'>
				<tr>
					<td>主格</td>
					<td>
						<input type="text" id='subject' name="subject" class="easyui-validatebox" data-options="validType:['length[1,500]']"/>
					</td>
				</tr>
				<tr>
					<td>宾格</td>
					<td>
						<input type="text" id='object' name="object" class="easyui-validatebox" data-options="validType:['length[1,500]']"/>
					</td>
				</tr>
				<tr>
					<td>形容词性物主代词</td>
					<td>
						<input type="text" id='app' name="app" class="easyui-validatebox" data-options="validType:['length[1,500]']"/>
					</td>
				</tr>
				<tr>
					<td>名词性物主代词</td>
					<td>
						<input type="text" id='npp' name="npp" class="easyui-validatebox" data-options="validType:['length[1,500]']"/>
					</td>
				</tr>
				<tr>
					<td>反身代词</td>
					<td>
						<input type="text" id='reflexive' name="reflexive" class="easyui-validatebox" data-options="validType:['length[1,500]']"/>
					</td>
				</tr>
				</tbody>
				<tbody id='num'>
				<tr>
					<td>基数词</td>
					<td>
						<input type="text" id='cardinalNum' name="cardinalNum" class="easyui-validatebox" data-options="validType:['length[1,500]']"/>
					</td>
				</tr>
				<tr>
					<td>序数词</td>
					<td>
						<input type="text" id='ordinalNum' name="ordinalNum" class="easyui-validatebox" data-options="validType:['length[1,500]']"/>
					</td>
				</tr>
				</tbody>
				<tbody id='n'>
				<tr>
					<td>复数</td>
					<td>
						<input type="text" id='plural' name="plural" class="easyui-validatebox" data-options="validType:['length[1,500]']"/>
					</td>
				</tr>
				</tbody>
				<tbody id='v'>
				<tr>
					<td>现在分词</td>
					<td>
						<input type="text" id='ving' name="ving" class="easyui-validatebox" data-options="validType:['length[1,500]']"/>
					</td>
				</tr>
				<tr>
					<td>过去分词</td>
					<td>
						<input type="text" id='ed' name="ed" class="easyui-validatebox" data-options="validType:['length[1,500]']"/>
					</td>
				</tr>
				<tr>
					<td>过去式</td>
					<td>
						<input type="text" id='preterite' name="preterite" class="easyui-validatebox" data-options="validType:['length[1,500]']"/>
					</td>
				</tr>
				</tbody>
				<tr>
					<td>近义词</td>
					<td>
						<input type="text" id='synonym' name="synonym" class="easyui-validatebox" data-options="validType:['length[1,500]']"/>
					</td>
				</tr>
				<tr>
					<td>标准一</td>
					<td>
						<input type="text" id='standard1' name="standard1" class="easyui-validatebox" data-options="validType:['length[1,500]']"/>
					</td>
				</tr>
				<tr>
					<td>标准二</td>
					<td>
						<input type="text" id='standard2' name="standard2" class="easyui-validatebox" data-options="validType:['length[1,500]']"/>
					</td>
				</tr>
				<tr>
					<td>出处</td>
					<td>
						<input type="text" id='provenance' name="provenance" class="easyui-validatebox" data-options="validType:['length[1,500]']"/>
					</td>
				</tr>
			</table>
		</form>
	</fieldset>
</div>

<div id="updAdditionalDialog">
	<fieldset style="margin-top: 5px;padding-left:40px">
		<legend>
			<b><i>字词附加信息</i></b>
		</legend>
		<form id="additionalForm" method="post">
			<table>
				<tr>
					<td>音标</td>
					<td>
						<input type='hidden' name='id'/>
						<input type="text" name="soundmark" class="easyui-validatebox" data-options="validType:'length[1,100]'"/>
					</td>
				</tr>
				<tr>
					<td>词性</td>
					<td>
						<select name="property" class="easyui-validatebox">
							<option selected="selected" value=''>--请选择--</option>
							<option>n</option>
							<option>pron</option>
							<option>adj</option>
							<option>num</option>
							<option>v</option>
							<option>adv</option>
							<option>art</option>
							<option>prep</option>
							<option>conj</option>
							<option>int</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>释义</td>
					<td>
						<input type="text" name="meaning" data-options="required:true,validType:'length[1,1000]'"></textarea>
					</td>
				</tr>
			</table>
		</form>
	</fieldset>
</div>

<div id="addExampleDialog">
	<fieldset style="margin-top: 5px;padding-left:40px">
		<legend>
			<b><i>字词基本信息</i></b>
		</legend>
		<form id="exampleForm" method="post">
			<table>
				<tr>
					<td>例句</td>
					<td>
						<input class="easyui-validatebox" name="explain" data-options="required:true,validType:'length[1,1000]'"/>
					</td>
				</tr>
				<tr>
					<td>上下文含义</td>
					<td>
						<input class="easyui-validatebox" name="example" data-options="required:true,validType:'length[1,500]'"/>
					</td>
				</tr>
			</table>
		</form>
	</fieldset>
</div>

<div id="updExampleDialog">
	<fieldset style="margin-top: 5px;padding-left:40px">
		<legend>
			<b><i>例句信息</i></b>
		</legend>
		<form id="updExampleForm" method="post">
			<table>
				<tr>
					<td>例句</td>
					<td>
						<input class="easyui-validatebox" name="explain" data-options="required:true,validType:'length[1,1000]'"/>
						<input name="id" type='hidden'/>
					</td>
				</tr>
				<tr>
					<td>上下文含义</td>
					<td>
						<input class="easyui-validatebox" name="example" data-options="required:true,validType:'length[1,500]'"/>
					</td>
				</tr>
			</table>
		</form>
	</fieldset>
</div>

<div id="uploadDialog">
	<form id="uploadForm" method="post" style="position: relative;padding:15px"enctype="multipart/form-data">
	   <label>选择文件：</label>
       <input type="text" readOnly="readOnly" name="txt" style="position:absolute;width:170px;background-color:Silver"/>
       <button style="position: absolute;top:15px;left:256px;width:52px;height:26px;">浏览</button>
       <input type="file" name="file" style='opacity:0;background:green;width:230px' onchange="txt.value=this.value" data-options="required:true,validType:'length[1,5000]'"/>
	</form>
</div>
<jsp:include page="../common/footer.jsp"/>

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
		//{1:'①',2:'②',3:'③',4:'④',5:'⑤',6:'⑥',7:'⑦',8:'⑧',9:'⑨',10:'⑩'}
		var quanma={1:'①',2:'②',3:'③',4:'④',5:'⑤',6:'⑥',7:'⑦',8:'⑧',9:'⑨',10:'⑩'};
		wordgrid = $('#wordgrid').datagrid({
			title : '字词列表',
			url : 'modenWordList.do',
			columns : [[/*
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
					title : '拼音',
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
					field : 'component',
					title : '偏旁部首',
					width : 100
				},{
					field : 'buildingMethod',
					title : '造字法',
					width : '100'
				},*/{
					field : 'explain',
					title : '解释',
					width : 100,
					formatter : function(value, row, index) {
						var result = '<div class="container"><br/><span style="margin-left:10px;margin-right:10px;font-size:30px;">' + row.name + '</span><a href="#" onclick="delWord('+row.id + ')">删除</a><br/>';
						if(row.explain) {
							var yxs = $.parseJSON('[' + row.explain + ']');
							$(yxs).each(function(index,element) {
								result += '<span class="soundmark">' + row.soundmark + '</span> <span class="property">' + element.PROPERTY + '</span>';
								result += '<span class="explain">' + (quanma[element.SORT] ? quanma[element.SORT] : element.SORT) + ': ' + element.EXPLAIN + '</span><br/>';
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
			toolbar : [/* {
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
			}, '-',*/ {
				text : '导入',
				iconCls : 'icon-import',
				handler : function() {
					uploadWord();
				}
			}, '-', {
				text : '下载模板',
				iconCls : 'icon-download',
				handler : function() {
    				var filename = encodeURI(encodeURI("resources/template/现代字词导入模板.xls"));
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
			height : 360,
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
						url : 'addWord',
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
				//$('#updateForm').form('clear');
				//$('#updateForm').form('load',selectedData);
				$.post('getWordInfo.do', {
					id : id
				}, function(result) {
					if (result.success) {
						var obj = result.obj.list[0];
						$("#updateDialog #lname").html(obj.name);
						$("#updateDialog #rname").html(obj.name);
						$("#updateDialog #creator").html(obj.creator);
						$("#updateDialog #created").html(obj.created);
						$("#updateDialog #lastmodified").html(obj.lastModified);
						$("#updateDialog #component").html(obj.component);
						$("#updateDialog #cbhs").html(obj.cbhs);
						$("#updateDialog #bhs").html(obj.bhs);
						$("#updateDialog #buildingmethod").html(obj.buildingMethod);
						$("#yxcontents").html('');
						$("#jctxContents").html('');
						$.each(result.obj.list,function(index,element) {
							// alert($.parseJSON("[" + element.examples + "]")[0].PROPERTY);
							var yxs = $.parseJSON("[" + element.examples + "]");
							for(var i = 0; i < yxs.length; i++) {
								var yxdiv = '';
								yxdiv = '<div style="background-color:#F8F8FF;height:30px;">';
								yxdiv += '<span style="display:inline-block;padding-top:5px;">' + yxs[i].PROPERTY + '&nbsp;&nbsp;'+ element.soundmark;
								yxdiv += '</span><span style="display:inline-block;float:right;">';
								var yxStr = JSON.stringify(yxs[i]).replace(/"/g,'$');
								yxdiv += '<a id="btn" href="#" class="easyui-linkbutton" onclick="updateAdditional(\''+ yxStr + '\')" data-options="iconCls:\'icon-edit\'">编辑</a></span></div>';
								yxdiv += '<div id="additional' + yxs[i].ID + '"><label>义项:</label><span name="explain">' + yxs[i].EXPLAIN + '</span><br/><div id="examples' + yxs[i].ID + '">';
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
	        	$.post('delWord.do', {
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
						url : 'uploadWord',
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
						url : 'updateWord',
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
				$('#basicForm').form('load',selectedData);
			}
		});
	}
	
	function updateAdditional(yx) {
		yx = yx.replace(/[$]/g,'"').toLowerCase();
		yx = $.parseJSON(yx);
		$('#updAdditionalDialog').dialog({
			title : '字词附加信息修改',
			modal : true,
			inline : true,
			width : 380,
			height : 250,
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
						url : 'updateAdditionalWord',
						onSubmit : function() {
							
						},
						success : function(result) {
							$.messager.progress('close'); // 如果提交成功则隐藏进度条
							$(_btn).linkbutton("enable");
							var r = $.parseJSON(result);
							if (r.success) {
								$('#updAdditionalDialog').dialog('close');
								$('#wordgrid').datagrid('reload',ly.serializeObjct($('#queryForm')));
								$('#additional' + yx.id + ' span[name="explain"]').html($("#additionalForm textarea[name='explain']").val());
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
				$('#additionalForm').form('load',yx);
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
						url : 'addExampleWord?id=' + id,
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
						url : 'updExampleWord',
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
				$('#updExampleForm').form('load','getExampleById?id=' + id);
			}
		});
	}
	
	function delExample(id) {
		$.messager.confirm('确认','您确认想要删除记录吗？',function(r){    
			if (r){    
	        	$.post('delExample.do', {
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
						<input class="easyui-validatebox" name="name" data-options="required:true,validType:['length[1,100]','Hanzi']"/>
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
					<td>拼音</td>
					<td>
						<input type="text" name="soundmark" class="easyui-validatebox" data-options="validType:'length[1,100]'"/>
					</td>
				</tr>
				<tr>
					<td>偏旁</td>
					<td>
						<input type="text" name="component" class="easyui-validatebox" data-options="validType:['length[1,1]','Hanzi']"/>
					</td>
				</tr>
				<tr>
					<td>造字法</td>
					<td>
						<select name="buildingMethod" class="easyui-validatebox">
							<option selected="selected" value=''>--请选择--</option>
							<option>象形</option>
							<option>形声</option>
							<option>指事</option>
							<option>会意</option>
							<option>转注</option>
							<option>假借</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>词性</td>
					<td>
						<select name="property" class="easyui-validatebox">
							<option selected="selected" value=''>--请选择--</option>
							<option>名词</option>
							<option>动词</option>
							<option>形容词</option>
							<option>数词</option>
							<option>量词</option>
							<option>代词</option>
							<option>副词</option>
							<option>介词</option>
							<option>连词</option>
							<option>助词</option>
							<option>叹词</option>
							<option>拟声词</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>解释</td>
					<td>
						<textarea rows="2" cols="21" name="explain" data-options="required:true,validType:'length[1,5000]'"></textarea>
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
	            	<label class='update_view'>部首：</label><span id='component'></span><br/>
	            	<label class='update_view'>查笔画数：</label><span id='cbhs'></span><br/>
	            	<label class='update_view'>笔画：</label><span id='bhs'></span><br/>
	            	<label class='update_view'>造字法：</label><span id='buildingmethod'></span><br/>
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
				<tr>
					<td>拼音</td>
					<td>
						<input type="text" name="soundmark" class="easyui-validatebox" data-options="validType:'length[1,100]'"/>
					</td>
				</tr>
				<tr>
					<td>偏旁</td>
					<td>
						<input type="text" id='cid' name="component" class="easyui-validatebox" data-options="validType:['length[1,1]','Hanzi']"/>
					</td>
				</tr>
				<tr>
					<td>查笔画数</td>
					<td>
						<input type="text" name="cbhs" class="easyui-numberbox" min="1" max="200"/>
					</td>
				</tr>
				<tr>
					<td>笔画数</td>
					<td>
						<input type="text" name="bhs" class="easyui-numberbox" min="1" max="200"/>
					</td>
				</tr>
				<tr>
					<td>造字法</td>
					<td>
						<select name="buildingMethod" class="easyui-validatebox">
							<option selected="selected" value=''>--请选择--</option>
							<option>象形</option>
							<option>形声</option>
							<option>指事</option>
							<option>会意</option>
							<option>转注</option>
							<option>假借</option>
						</select>
					</td>
				</tr>
			</table>
		</form>
	</fieldset>
</div>

<div id="updAdditionalDialog">
	<fieldset style="margin-top: 5px;padding-left:40px">
		<legend>
			<b><i>字词基本信息</i></b>
		</legend>
		<form id="additionalForm" method="post">
			<table>
				<tr>
					<td>实词/虚词</td>
					<td>
						<select name="sxc" class="easyui-validatebox" data-options="required:true">
							<option selected="selected" value=''>--请选择--</option>
							<option>实词</option>
							<option>虚词</option>
						</select>
						<input type='hidden' name='id'/>
					</td>
				</tr>
				<tr>
					<td>词性</td>
					<td>
						<select name="property" class="easyui-validatebox">
							<option selected="selected" value=''>--请选择--</option>
							<option>名词</option>
							<option>动词</option>
							<option>形容词</option>
							<option>数词</option>
							<option>量词</option>
							<option>代词</option>
							<option>副词</option>
							<option>介词</option>
							<option>连词</option>
							<option>助词</option>
							<option>叹词</option>
							<option>拟声词</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>义项</td>
					<td>
						<textarea rows="2" cols="21" name="explain" data-options="required:true,validType:'length[1,5000]'"></textarea>
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
			<b><i>字词基本信息</i></b>
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
